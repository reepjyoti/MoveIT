package eurecom.moveit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by test on 21/01/2018.
 */

public class Sending_Price extends Fragment implements View.OnClickListener {


    ViewPager viewPager;
    View rootView;

    // Required empty public constructor
    public Sending_Price(){





    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_sending_price, container, false);

        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        rootView.findViewById(R.id.addRequest).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.addRequest:

                Integer price = Integer.parseInt(((EditText) this.rootView.findViewById(R.id.price)).getText().toString());
                ((Send) getActivity()).postRequest(price);
                Toast.makeText(getActivity(), "Request added!", Toast.LENGTH_LONG);

                break;
        }
    }
}