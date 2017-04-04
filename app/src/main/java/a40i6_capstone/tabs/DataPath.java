package a40i6_capstone.tabs;

import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
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

public class DataPath extends Tab3 {

    Tab3 obj = new Tab3();
    double dummy = obj.values;
    static double min_incoming=0, min_baseline=0;

    MainActivity object = new MainActivity();
    double incoming_data_numbers = object.input;

    static int start_flag =0; //this flag is used to know the very first time so you can get a baseline
    static int emergency_flag=0;
    static int j=0, k=0;
    static int index_baseline=0, index_incoming=0;


    public static void DecisionMaking (double numbersFromBluetooth[]){
        int i=0;
        int range=0; //to be changed\
        int m=0;
        double [] baseline = new double[160] ;
        double [] incoming_data= new double [160];
        double [] ST_incoming = new double[24];
        double [] ST_baseline = new double [24];
        int counter=0, counter1=0;
        int emergency=0;




        if (start_flag == 0){
           // Log.d(getClass().getSimpleName(), "start flag ==0");
            for (i=0; i<160; i++){
                baseline[i] = numbersFromBluetooth[i];
                Log.d(DataPath.class.getSimpleName(), "received array: "+numbersFromBluetooth[i]);
                //Log.d(getClass().getSimpleName(),"baseline is "+baseline[i]);

                if (i==80) {
                    min_baseline = incoming_data[i];
                } else if (i>80 && i<90){
                    if (incoming_data[i]<min_baseline) {
                        min_baseline = incoming_data[i];
                        index_baseline=i;
                    }
                } else if (i>=90 && j<24 ){// to ensure that minimum has been inserted (i>90) and that we don't overwrite once ST array is full

                    ST_baseline[j] = baseline[index_baseline + counter];
                    counter++;
                    j++;
                }

            } start_flag++;
        } else {

            for (i = 0; i < 160; i++) {
                incoming_data[i] = numbersFromBluetooth[i];

                // if data ok baseline[i] = (0.2 * incoming_data[i] + 0.8 * baseline[i]);
                //if off : emergency_flag++;
                //Log.d(getClass().getSimpleName(),"incoming data of i "+incoming_data[i]);
                //finding minimum
                if (i==80) {
                    min_incoming = incoming_data[i];
                } else if (i>80 && i<90){
                    if (incoming_data[i]<min_incoming) {
                        min_incoming = incoming_data[i];
                        index_incoming=i;
                        //
                        // Log.d(getClass().getSimpleName(),"i"+i);

                    }
                } else if (i>=90 && k<24 ){// to ensure that minimum has been inserted (i>90) and that we don't overwrite once ST array is full.. Creating ST Array

                    ST_incoming[k] = incoming_data[index_incoming + counter1];
                    //Log.d(getClass().getSimpleName(),"ST_Incoming is "+ ST_incoming[k]);
                    counter1++;
                    k++;
                }


            }
            for (m=0; m<24; m++){
                //Log.d(getClass().getSimpleName(),"diff "+Math.abs(ST_incoming[m] - ST_baseline[m]));
                if (m==0){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 76 ){
                        emergency++;
                    }
                }else if (m==1){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 72 ){
                        emergency++;
                    }
                }else if (m==2){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 70 ){
                        emergency++;
                    }
                }else if (m==3){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 70 ) {
                        emergency++;
                    }
                }else if (m==4){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 69 ){
                        emergency++;

                    }
                }else if (m==5){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 64 ){
                        emergency++;

                    }
                }else if (m==6){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 58 ){
                        emergency++;

                    }
                }else if (m==7){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 52 ){
                        emergency++;

                    }
                }else if (m==8){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 55 ){
                        emergency++;

                    }
                }else if (m==9){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 54){
                        emergency++;

                    }
                }else if (m==10){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 54 ){
                        emergency++;
                    }
                }else if (m==11){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 47 ){
                        emergency++;
                    }
                }else if (m==12){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 42 ){
                        emergency++;
                    }
                }else if (m==13){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 38 ){
                        emergency++;
                    }
                }else if (m==14){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 32 ){
                        emergency++;
                    }
                }else if (m==15){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 30 ){
                        emergency++;
                    }
                }else if (m==16){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 28){
                        emergency++;
                    }
                }else if (m==17){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 26 ){
                        emergency++;
                    }
                }else if (m==18){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 21 ){
                        emergency++;
                    }
                }else if (m==19){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 21 ){
                        emergency++;
                    }
                }else if (m==20){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 16 ){
                        emergency++;
                    }
                }else if (m==21){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 12 ){
                        emergency++;
                    }
                }else if (m==22){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 12 ){
                        emergency++;
                    }
                }else if (m==23){
                    if (Math.abs(ST_incoming[m] - ST_baseline[m]) > 14 ){
                        emergency++;
                    }

                }
            }

        } //Log.d(getClass().getSimpleName(), "emergency should be 24 or more"+ emergency);

        if (emergency_flag>1){
            Log.d(DataPath.class.getSimpleName(), "emergency should be 24 or more"+ emergency);
            //MainActivity.showNotification();
        }
    }



}
