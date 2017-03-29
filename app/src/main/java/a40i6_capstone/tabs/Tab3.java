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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
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
    //BluetoothAdapter blueAdaptor = null;
    private static final UUID my_uuid =UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private static UUID my_uuid;
    BluetoothDevice Device;
    //BluetoothSocket Socket = null;
    java.io.OutputStream OutputStream;
    InputStream InputStream;
    //Thread workerThread;
    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;

    //Creating start/stop button to control data transfer

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(tab3contents, container, false);
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
                        sendMessage();
                        break;
                    case 1:
                        ((Button) view).setText("Start");
                        startstopmsg.setText("Device is now OFF");
                        view.setTag(0); //pause
                        startGraph = false;
                        sendMessage();
                        break;
                }

            }
        });

        // data
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
    private class ThreadConnected extends Thread {
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
            String trimString="0";
            String newString;
            while (true) {
                if (startGraph == true){
                try {

                    bytes = connectedInputStream.read(buffer);
                    Log.d(getClass().getSimpleName(), "connectedInputStream: "+ connectedInputStream);
                    String strReceived = new String(buffer, 0, bytes);

                    final String msgReceived = String.valueOf(bytes) +
                            " bytes received:\n"
                            + strReceived;
                    Log.d(getClass().getSimpleName(), "msgReceived: "+ msgReceived);

                    //int beginOfLineIndex =0;
                    newString = strReceived;
                    int count = strReceived.length() - strReceived.replace("\r\n", "").length();
                    //Log.d(getClass().getSimpleName(), "count: "+ count);
                    for(int i=0; i<count; i++) {

                        int endOfLineIndex = newString.indexOf("\r\n");
                        //Log.d(getClass().getSimpleName(), "endofLineindex: "+ endOfLineIndex);
                        if (endOfLineIndex >0) {

                            trimString = newString.substring(0,endOfLineIndex).trim();
                            Log.d(getClass().getSimpleName(), "trimString: "+ trimString);
                            try {
                                double intvalue = Double.parseDouble(trimString);

                                series.appendData(new DataPoint(lastX++, intvalue), true, 50);

                                try {
                                    Thread.sleep(25);
                                } catch (InterruptedException e) {
                                    // manage error ...
                                }
                            }
                            catch (NumberFormatException nfe)
                            {
                                //your string not in numeric format
                                Log.d(getClass().getSimpleName(), "String not numeric");
                                nfe.printStackTrace();
                            }
                            newString = newString.substring(endOfLineIndex+1);
                            //Log.d(getClass().getSimpleName(), "length of newString: "+ newString.length());
                            //beginOfLineIndex = endOfLineIndex+1;
                        }
                        else if (endOfLineIndex == -1) {
                            break;
                        }

                        /*if (startGraph == false) {
                            break;
                        }*/
                    }
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
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            Log.d(getClass().getSimpleName(), msgConnectionLost);

                        }});
                    onDestroy();
                    break;
                }
            }}
        }
        
        public void sendMessage(){
         try{
             connectedOutputStream.write(error_message);
        } catch (IOException e) {
          e.printStackTrace();   
         }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

/*    void ListenforData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {

                        int bytesAvailable = InputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packet = new byte[bytesAvailable];
                            InputStream.read(packet);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packet[i];
                                if(b != delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    //final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * 10d), true, 50);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }*/



