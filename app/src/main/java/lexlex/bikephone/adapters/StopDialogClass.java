package lexlex.bikephone.adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import lexlex.bikephone.R;
import lexlex.bikephone.models.Ride;

public class StopDialogClass extends AppCompatDialogFragment implements
        android.view.View.OnClickListener{

    public Activity c;
    public Dialog d;
    public EditText rideName;
    public Button yes, no, cancel;
    public Ride ride;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.stop_dialog, null);

//        this.ride = (Ride) savedInstanceState.getSerializable("ride");

        yes = view.findViewById(R.id.btn_yes);
        no = view.findViewById(R.id.btn_no);
        cancel = view.findViewById(R.id.btn_cancel);
        rideName = view.findViewById(R.id.txt_name);

        rideName.setText(ride.getName());
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return builder.create();


    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    /*
    public StopDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.stop_dialog);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        cancel = findViewById(R.id.btn_cancel);
        rideName = findViewById(R.id.txt_name);

        yes.setOnClickListener(this);
        rideName.setText(ride.getName());

        no.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    */

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_yes:
                Log.d("button", "Yes");
                ride.setName(rideName.getText().toString());
                Log.d("Ride Name", ride.getName());
                //listener.buttonSelectedValue(0);
                dismiss();
                break;
            case R.id.btn_no:
                Log.d("button", "No");
                //listener.buttonSelectedValue(1);
                dismiss();
                break;
            case R.id.btn_cancel:
                Log.d("button", "Cancel");
                //listener.buttonSelectedValue(2);
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
//            listener = (ButtonDialogInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public void show() {
    }
}


