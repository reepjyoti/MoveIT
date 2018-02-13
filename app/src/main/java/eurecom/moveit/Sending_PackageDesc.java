package eurecom.moveit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;


public class Sending_PackageDesc extends Fragment implements View.OnClickListener{

    TabLayout tabLayout;
    Button buttonToDest;
    View rootView;

    public Sending_PackageDesc() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.continueButton:

                Integer height = validateInput(R.id.heightInput, "height");
                Integer width = validateInput(R.id.widthInput,  "width");
                Integer depth = validateInput(R.id.depthInput, "depth");

                String description = ((EditText) rootView.findViewById(R.id.description)).getText().toString();

                if (height != -1 && width != -1 && depth != -1)
                {
                    ((Send) getActivity()).saveDescription(height, width, depth, description);
                    tabLayout.getTabAt(1).select();
                }

                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sending_package_desc, container, false);

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);

        buttonToDest = rootView.findViewById(R.id.continueButton);
        buttonToDest.setOnClickListener(this);

        return rootView;
    }

    protected Integer validateInput(int fieldId, String fieldName)
    {
        EditText input = (EditText) rootView.findViewById(fieldId);


        if (TextUtils.isEmpty(input.getText()))
        {
            input.setError("You need to enter a valid " + fieldName);
            return -1;
        }

        Integer value;
        try
        {
            value = Integer.parseInt(input.getText().toString());

        } catch (NumberFormatException e)
        {
            input.setError(fieldName + " is not an integer");
            return -1;
        }

        if (value > 1000 || value <= 0)
        {
            input.setError(fieldName + " should be a correct value");
            return -1;
        }

        return value;
    }
}
