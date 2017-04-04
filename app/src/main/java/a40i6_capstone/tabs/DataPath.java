package a40i6_capstone.tabs;

import android.*;
import android.app.NotificationManager;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.ArrayList;

import android.util.Log;

/**
 * Created by monic on 2017-03-27.
 */


public class DataPath extends Service {

    Tab3 obj = new Tab3();
    double dummy = obj.values;
    static double min_incoming = 0, min_baseline = 0;

    MainActivity mActivity = new MainActivity();
    Tab3 TabActivity = new Tab3();

    static int start_flag = 0; //this flag is used to know the very first time so you can get a baseline
    static int emergency_flag = 0;
    static int j = 0, k = 0;
    static int index_baseline = 0, index_incoming = 0;
    Context context;

    public void DecisionMaking(double numbersFromBluetooth[]) {
        int i = 0;
        int range = 0; //to be changed\
        int m = 0;
        int counter = 0, counter1 = 0;


        //Log.d (DataPath.class.getSimpleName(), "in data path ");

        for (int j = 0; j < 161; j++) {
            //   Log.d (DataPath.class.getSimpleName(), "Input from data path " + numbersFromBluetooth[j]+ " "+ j);

            if (numbersFromBluetooth[j] == -15.00) {
                emergency_flag++;
                Log.d(DataPath.class.getSimpleName(), "I got a -15");
            } else if (numbersFromBluetooth[j] == -10.00) {
                emergency_flag = 0;
                Log.d(DataPath.class.getSimpleName(), "I got a -10");
            }
        }
        if (emergency_flag == 3) {

            Log.d(DataPath.class.getSimpleName(), "emergency flag is " + emergency_flag);
//             TabActivity.flag_emergency = true;

        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}