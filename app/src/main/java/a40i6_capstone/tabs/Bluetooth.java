package a40i6_capstone.tabs;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.series.DataPoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static a40i6_capstone.tabs.Tab3.RANDOM;
import static android.content.ContentValues.TAG;

/**
 * Created by grace_000 on 2017-03-04.
 */

public class Bluetooth extends Tab3 {
    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter blueAdaptor;
    private UUID my_uuid;
    BluetoothSocket Socket;
    BluetoothDevice Device;
    OutputStream OutputStream;
    InputStream InputStream;
    Thread workerThread;
    volatile boolean stopWorker;
    byte[] readBuffer;
    int readBufferPosition;

    //@Override
    public void onCreate(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3contents, container, false);
        BluetoothAdapter my_bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (my_bluetooth == null) {
            // Device does not support Bluetooth
        }
        if (!my_bluetooth.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Set<BluetoothDevice> pairedDevices = blueAdaptor.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-06"))    // Change to match RN42 - node name
                {
                    Device = device;
                    break;
                }
            }
        }

        Log.d(getClass().getSimpleName(), "Test");
    }

    void startBT () throws IOException {
        UUID my_uuid = UUID.fromString("d86ab8e0-0125-11e7-9598-0800200c9a66");
        BluetoothServerSocket temp = null;
        Socket = Device.createRfcommSocketToServiceRecord(my_uuid);
        Socket.connect();
        OutputStream = Socket.getOutputStream();
        InputStream = Socket.getInputStream();

        ListenforData();

    }

    void closeBT() throws IOException
    {
        stopWorker = true;
        OutputStream.close();
        InputStream.close();
        Socket.close();

    }

    void ListenforData() {
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
                                if(b == delimiter)
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
    }
    }


