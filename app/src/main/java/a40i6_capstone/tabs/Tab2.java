package a40i6_capstone.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by grace_000 on 2016-11-19.
 */

public class Tab2 extends Fragment {
    DataPath test = new DataPath();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2contents, container, false);
        Button btn = (Button) rootView.findViewById(R.id.tab2button);
        if (test.emergency_flag == 3) {
            btn.performClick();
        }

        return rootView;
    }
}

