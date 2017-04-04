package a40i6_capstone.tabs;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static a40i6_capstone.tabs.R.id.container;
import static a40i6_capstone.tabs.R.id.start;
import static a40i6_capstone.tabs.R.id.tab3text;
import static a40i6_capstone.tabs.R.layout.tab3contents;

/**
 * Created by grace_000 on 2016-11-19.
 */

public class Tab3 extends Fragment {
    int error_message = -6;
    boolean startGraph;
    public static final Random RANDOM = new Random();
    public LineGraphSeries<DataPoint> series;
    public int lastX = 0;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final UUID my_uuid =UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothDevice Device;
    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;
    public static double values;
    public static double[] doublevalue = new double[161];
    View rootView;
    MainActivity mActivity = new MainActivity();
    int emergency_flag=0;

    int i = 0;

    //Creating start/stop button to control data transfer

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(tab3contents, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.tab3text);
        // we get graph view instance
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        //Code to get the button from layout file
        Button btn = (Button) rootView.findViewById(R.id.buttonGraph);
        final TextView startstopmsg = (TextView) rootView.findViewById(R.id.tab3text);
        btn.setTag(0);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Implement the code to run on button click here
                final int status = (Integer) view.getTag();
                switch (status) {
                    case 0:
                        ((Button) view).setText("Stop");
                        startstopmsg.setText("Device is now ON");
                        view.setTag(1); //pause
                        startGraph = true;

                        break;
                    case 1:
                        ((Button) view).setText("Start");
                        startstopmsg.setText("Device is now OFF");
                        view.setTag(0); //pause
                        startGraph = false;
                      //  sendMessage();
                        break;
                }

            }
        });

        // data
        readFromFile();
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(800);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(100);
        viewport.setScrollable(true);
        viewport.setScalable(true);

        BluetoothAdapter my_bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (my_bluetooth == null) {
            // Device does not support Bluetooth
        }
        if (!my_bluetooth.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = my_bluetooth.getBondedDevices();
        Log.d(getClass().getSimpleName(), "Test1");

        for (BluetoothDevice devices: pairedDevices) {
            textView.append("\nDevices: " + devices.getName());
        }

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-06"))    // Change to match RN42 - node name
                {
                    Device = device;
                    textView.append("\nDevice =: " + device.getName());
                    break;
                }
            }
        }
        //Log.d(getClass().getSimpleName(), "Test");


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                    for (int i = 0; i < 100; i++) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //addEntry();
                                //try {

                                if(startGraph == true) {
                                    myThreadConnectBTdevice = new ThreadConnectBTdevice(Device);
                                    myThreadConnectBTdevice.start();
                                    Log.d(getClass().getSimpleName(), "Test");
                                    myThreadConnectBTdevice.cancel();
                                }
                                //} catch (IOException ex) { ex.printStackTrace();}

                            /*try {
                                closeBT();
                            }
                            catch (IOException ex) { ex.printStackTrace(); }*/

                            }
                        });

                        // sleep to slow down the add of entries
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            // manage error ...


                        }
                    }
                }
            }).start();
    }

    //add random data to graph
   /*public void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 50);
    }
    void startBT () throws IOException {

        Socket = Device.createRfcommSocketToServiceRecord(my_uuid);
        Socket.connect();
        Log.d(getClass().getSimpleName(), "Test3");
        OutputStream = Socket.getOutputStream();
        InputStream = Socket.getInputStream();

        //ListenforData();

    }

    void closeBT() throws IOException
    {
        stopWorker = true;
        OutputStream.close();
        InputStream.close();
        Socket.close();

    }*/
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(myThreadConnectBTdevice!=null){
            myThreadConnectBTdevice.cancel();
        }
    }

    //Called in ThreadConnectBTdevice once connect succeeds
    //to start ThreadConnected
    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }
    /*
    ThreadConnectBTdevice:
    Background Thread to handle BlueTooth connecting
    */
    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;


        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;
            //my_uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            try {

                //Log.d(getClass().getSimpleName(), "UUID: " + my_uuid);
                bluetoothSocket = device.createRfcommSocketToServiceRecord(my_uuid);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            if (bluetoothSocket.isConnected() == true) {
                try{
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    Log.d(getClass().getSimpleName(), "Socket cannot be closed!");
                }
            }
            try {
                //Log.d(getClass().getSimpleName(), "Device: " + Device);
                bluetoothSocket.connect();
                success = true;
                //Log.d(getClass().getSimpleName(), "Status of success: " + success);
            } catch (IOException e) {
                try {
                    bluetoothSocket = (BluetoothSocket) Device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(Device, 1);
                    bluetoothSocket.connect();
                    success = true;
                    //Log.d(getClass().getSimpleName(), "Status of success: " + success);
                } catch (Exception e2) {
                    try {
                        bluetoothSocket.close();
                    } catch (IOException closeException) {
                        Log.d(getClass().getSimpleName(), "Couldn't establish connection!");

                    }

                }

/*                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }*/
                if (success) {
                    //connect successful
                    final String msgconnected = "connect successful:\n"
                            + "BluetoothSocket: " + bluetoothSocket + "\n"
                            + "BluetoothDevice: " + bluetoothDevice;

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Log.d(getClass().getSimpleName(), "Status : " + msgconnected);

                            //listViewPairedDevice.setVisibility(View.GONE);
                            //inputPane.setVisibility(View.VISIBLE);
                        }
                    });
                    Log.d(getClass().getSimpleName(), "startGraph: "+ startGraph);

                    if(startGraph == true) {
                        startThreadConnected(bluetoothSocket);
                    }
                } else {
                    //fail
                    try {
                        bluetoothSocket.close();
                    } catch (IOException closeException) {
                        Log.d(getClass().getSimpleName(), "Failed connection!");
                    }
                }
            }
        }

        public void cancel() {


            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    /*
ThreadConnected:
Background Thread to handle Bluetooth data communication
after connected
 */
    public class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
                Log.d(getClass().getSimpleName(), "In out stream ran, socket: "+ socket);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException closeException) {Log.d(getClass().getSimpleName(), "Failed connection2!"); }
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            char signal;
            int graphTrack;
            int current_state = -1;
            String trimString="0";
            String newString;
            int m =0;

            while (true) {


                    if (startGraph) {
                        graphTrack = 1;
                        signal = 'A';
                        if (current_state != -1) {
                            sendMessage(signal);
                            current_state = -1;
                            Log.d(getClass().getSimpleName(), "Transmission success start: " + signal);

                        }
                    } else {
                        graphTrack = 2;
                        signal = 'B';
                        if (current_state != -5) {
                            sendMessage(signal);
                            current_state = -5;
                            Log.d(getClass().getSimpleName(), "Transmission success end: " + signal);

                        }
                    }
                    switch (graphTrack) {
                        case 1:
                            try {

                                buffer = new byte[1024];
                                bytes = connectedInputStream.read(buffer);
                                Log.d(getClass().getSimpleName(), "connectedInputStream: " + connectedInputStream);
                                String strReceived = new String(buffer, 0, bytes);
                                buffer = null;
                                final String msgReceived = String.valueOf(bytes) +
                                        " bytes received:\n"
                                        + strReceived;
                                Log.d(getClass().getSimpleName(), "msgReceived: " + msgReceived);
                                newString = strReceived;
                                int count = strReceived.length() - strReceived.replace("\r\n", "").length();
                                //Log.d(getClass().getSimpleName(), "count: "+ count);
                                for (int i = 0; i < count; i++) {
                                    int endOfLineIndex = newString.indexOf("\r\n");
                                    //Log.d(getClass().getSimpleName(), "endofLineindex: "+ endOfLineIndex);
                                    if (endOfLineIndex > 0) {
                                        trimString = newString.substring(0, endOfLineIndex).trim();
                                        Log.d(getClass().getSimpleName(), "trimString: " + trimString);
                                        try {

                                            double intvalue = Double.parseDouble(trimString);
                                            values = intvalue;

                                            series.appendData(new DataPoint(lastX++, intvalue), true, 50);
                                            //}
                                            Log.d(getClass().getSimpleName(), "I am working");
                                            try {
                                                Thread.sleep(50);
                                            } catch (InterruptedException e) {
                                                // manage error ...
                                            }
                                        } catch (NumberFormatException nfe) {
                                            //your string not in numeric format
                                            Log.d(getClass().getSimpleName(), "String not numeric");
                                            nfe.printStackTrace();
                                        }
                                        newString = newString.substring(endOfLineIndex + 1);
                                        //Log.d(getClass().getSimpleName(), "length of newString: "+ newString.length());
                                        //beginOfLineIndex = endOfLineIndex+1;
                                    } else if (endOfLineIndex == -1) {
                                        break;
                                    }
                                            /*if (startGraph == false) {
                                            break;
                                            }*/

                                }


                                //int beginOfLineIndex =0;
                                /*newString = strReceived;
                                int count = strReceived.length() - strReceived.replace("\r\n", "").length();
                                //Log.d(getClass().getSimpleName(), "count: "+ count);

                                for (int i = 0; i < count; i++) {

                                    int endOfLineIndex = newString.indexOf("\r\n");
                                    //Log.d(getClass().getSimpleName(), "endofLineindex: "+ endOfLineIndex);
                                    if (endOfLineIndex > 0) {

                                        trimString = newString.substring(0, endOfLineIndex).trim();
                                        Log.d(getClass().getSimpleName(), "trimString: " + trimString);
                                        try {
                                            double intvalue = Double.parseDouble(trimString);
                                            values = intvalue;
                                            if (intvalue != (double) -10) {
                                                series.appendData(new DataPoint(lastX++, intvalue), true, 50);
                                                inputarray[m] = intvalue;
                                                m++;
                                            } else if (intvalue == (double) -10) {
                                                //do nothing
                                            }

                                            try {
                                                Thread.sleep(75);
                                            } catch (InterruptedException e) {
                                                // manage error ...
                                            }
                                        } catch (NumberFormatException nfe) {
                                            //your string not in numeric format
                                            Log.d(getClass().getSimpleName(), "String not numeric");
                                            nfe.printStackTrace();
                                        }
                                        newString = newString.substring(endOfLineIndex + 1);
                                        if (m == 160) {
                                            sendArray.DecisionMaking(inputarray);
                                            m = 0;
                                        }
                                    } else if (endOfLineIndex == -1) {
                                        break;
                                    }

                        *//*if (startGraph == false) {
                            break;
                        }*//*
                                }*/
                                //connectedInputStream = null;
                                //connectedOutputStream =null;







/*                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            Log.d(getClass().getSimpleName(), "reading bytes");
                            String strIncom = new String(msgReceived, 0, 5);
                            Log.d(getClass().getSimpleName(), "strIncom: "+strIncom);
                            double intvalue = Double.parseDouble(strIncom);
                            series.appendData(new DataPoint(lastX++,intvalue),true,50);



                            //series.appendData(new DataPoint(msgReceived), true, 50);

                        }});*/
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                //e.printStackTrace();

                                final String msgConnectionLost = "Connection lost:\n"
                                        + e.getMessage();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(getClass().getSimpleName(), msgConnectionLost);
                                    }
                                });
                                onDestroy();
                                break;
                            }
                            break;
                        case 2:
                            break;
                    }

                    }
                 }
        public void sendMessage(char status_code){
            try{
                connectedOutputStream.write(status_code);
                } catch (IOException e) {
                e.printStackTrace();
                }
            }


        }
    public ArrayList<String> readFromFile() {


        ArrayList<String> stringArray = new ArrayList<String>();
        DataPath var = new DataPath();


        try {

            //InputStream inputStream = getContext().openFileInput("text2abnormal.txt");
            InputStream inputStream = this.getResources().openRawResource(R.raw.test5);

            int i = 0;

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                //StringBuilder stringBuilder = new StringBuilder();


                while ((receiveString = bufferedReader.readLine()) != null) {
                    Log.d(getClass().getSimpleName(), "Received String: "+ receiveString+" "+i);
                    //while ((receiveString = bufferedReader.readLine())!= "-10.0" && (receiveString = bufferedReader.readLine()) !="-15.0" && i<160) {

                        //stringBuilder.append(receiveString);

                        stringArray.add(i, receiveString);
                        doublevalue[i] = Double.parseDouble(stringArray.get(i));
                        Log.d(getClass().getSimpleName(), "Input: "+ doublevalue[i]+" "+ i);

                        i++;

                        if (i == 161) {
                            var.DecisionMaking(doublevalue);
                            i=0;
                        }
                    //}

                }


                inputStream.close();
                //ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }



        return stringArray;
    }
    }



