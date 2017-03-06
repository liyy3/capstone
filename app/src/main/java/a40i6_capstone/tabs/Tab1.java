package a40i6_capstone.tabs;

/**
 * Created by grace_000 on 2016-11-19.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Tab1 extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1contents, container, false);
        //Code to get the button from layout file
        Button btn = (Button) rootView.findViewById(R.id.buttonstart);
        final TextView startstopmsg = (TextView) rootView.findViewById(R.id.Home_text);
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
                        break;
                    case 1:
                        ((Button) view).setText("Start");
                        startstopmsg.setText("Device is now OFF");
                        view.setTag(0); //pause
                        break;
                }


            }
        });
        return rootView;
    }





}
