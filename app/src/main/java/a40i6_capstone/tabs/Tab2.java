package a40i6_capstone.tabs;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by grace_000 on 2016-11-19.
 */

public class Tab2 extends Fragment {
    DataPath test = new DataPath();
    private Context mContext = this.getContext();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2contents, container, false);
        Button btn = (Button) rootView.findViewById(R.id.tab2button);
        if (test.emergency_flag == 3) {
            btn.performClick();
            Log.d(getClass().getSimpleName(), "activity"+getActivity());
            NotificationCompat.Builder notification = new NotificationCompat.Builder (getActivity())
                    .setSmallIcon(R.drawable.icon);
            notification.setLights(Color.BLUE, 500, 500);
            long[] pattern = {500,500,500,500,500,500,500,500,500};
            notification.setVibrate(pattern);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.setSound(alarmSound);
            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, notification.build());


        }

        return rootView;
    }

/*    @Override
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
                            Button btn = (Button) rootView.findViewById(R.id.tab2button);
                            if (test.emergency_flag == 3) {
                                btn.performClick();

                            }
                        }
                    });

                }
            }
        }).start();
    }*/
}

