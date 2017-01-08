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

public class Tab1 extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1contents, container, false);
        //Code to get the button from layout file
        Button btn = (Button) rootView.findViewById(R.id.buttonstart);
        btn.setTag(0);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Implement the code to run on button click here
                //((Button) view).setText ("Stop");
                final int status = (Integer) view.getTag();
                switch (status) {
                    case 0:
                        ((Button) view).setText("Stop");
                        view.setTag(1); //pause
                        break;
                    case 1:
                        ((Button) view).setText("Start");
                        view.setTag(0); //pause
                        break;
                }
            }
        });
        return rootView;
    }

//    public void TextChange(View view) {
//        Button startstop = (Button) view;
//        ((Button) view).setText ("Stop");
//    }



}
