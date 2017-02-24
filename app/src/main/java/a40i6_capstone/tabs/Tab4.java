package a40i6_capstone.tabs;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static a40i6_capstone.tabs.R.layout.tab4contents;
import static android.R.attr.button;
import static android.R.id.content;

/**
 * Created by grace_000 on 2016-11-22.
 */

public class Tab4 extends Fragment {

    private EditText Name1;
    private EditText Relationship1;
    private EditText Number1;
    private EditText Name2;
    private EditText Relationship2;
    private EditText Number2;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(tab4contents, container, false);

        Name1 = (EditText) rootView.findViewById(R.id.Name1);
        Relationship1 = (EditText) rootView.findViewById(R.id.Relationship1);
        Number1 = (EditText) rootView.findViewById(R.id.Number1);
        Name2 = (EditText) rootView.findViewById(R.id.Name2);
        Relationship2 = (EditText) rootView.findViewById(R.id.Relationship2);
        Number2 = (EditText) rootView.findViewById(R.id.Number2);

        button = (Button) rootView.findViewById(R.id.call_button1);
        button.setTag(0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //int permissionCheck = ContextCompat.checkSelfPermission(this,
                        //Manifest.permission.WRITE_CALENDAR);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:16476207478"));
                    startActivity(callIntent);

            }
        });


        return rootView;
    }
}
