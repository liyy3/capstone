package a40i6_capstone.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4contents, container, false);

        Name1 = (EditText) rootView.findViewById(R.id.Name1);
        Relationship1 = (EditText) rootView.findViewById(R.id.Relationship1);
        Number1 = (EditText) rootView.findViewById(R.id.Number1);
        Name2 = (EditText) rootView.findViewById(R.id.Name2);
        Relationship2 = (EditText) rootView.findViewById(R.id.Relationship2);
        Number2 = (EditText) rootView.findViewById(R.id.Number2);

        return rootView;
    }
}
