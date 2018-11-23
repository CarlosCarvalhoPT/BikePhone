package lexlex.bikephone.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import lexlex.bikephone.R;
import lexlex.bikephone.activities.SettingsActivity;
import lexlex.bikephone.helper.DatabaseHelper;
import lexlex.bikephone.models.Settings;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class RegistarFragment extends Fragment{
    private Button start;
    private Button pause;
    private Button stop;
    private Button settings;
    private Chronometer chronometer;
    long timeWhenStopped;
    private Settings sett;
    private DatabaseHelper db;

    public RegistarFragment() {
        // Required empty public constructor
        System.out.print("aaaa");
        Log.v("Button", "created");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Button", "Initting Buttons");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_registar, container, false);

        start = view.findViewById(R.id.start_button);
        pause = view.findViewById(R.id.pause_button);
        stop = view.findViewById(R.id.stop_button);
        settings = view.findViewById(R.id.settings);
        chronometer = view.findViewById(R.id.chronometer);


        initSettings();

        initButtons();

        return view;
    }

    private void initSettings() {
        sett = new Settings("daaaa");
        //ir à base de dados buscar as configurações
        db = new DatabaseHelper(getContext());
        Settings res = db.getSettings();
        if(res!=null){
            sett = new Settings(
                    res.getMac(),
                    res.getUsername(),
                    res.getSamplefreq()
            );
        }
        else{
            /*
            //TODO - pegar mac do telemóvel
            String android_id = android.provider.Settings.Secure.getString(getContext().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            sett = new Settings(android_id);
            */
            sett = new Settings("macTelemóvelA");

            db.createSettings(sett);
        }
        db.closeDB();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                sett = (Settings) data.getSerializableExtra("settings");
                db = new DatabaseHelper(getContext());
                db.updateSettings(sett);
                db.closeDB();
             }

            if (resultCode == RESULT_CANCELED) {
                // do something if there is no result
            }
        }
    }


    void initButtons() {
        pause.setEnabled(false);
        stop.setEnabled(false);

        //TODO - Recolher valores dos sensores

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cronómetro
                long systemCurrTime = SystemClock.elapsedRealtime();
                chronometer.setBase(systemCurrTime);
                chronometer.start();

                showToast(v, getResources().getString(R.string.start_button));
                start.setEnabled(false);
                pause.setEnabled(true);
                stop.setEnabled(false);
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stop.isEnabled()){ //É para resumir corrida
                    //cronómetro
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();


                    showToast(v, getResources().getString(R.string.pause_button));
                    pause.setText(getResources().getString(R.string.pause_button));
                    stop.setEnabled(false);
                }else { //é para pausar a corrida
                    //cronómetro
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();


                    showToast(v, getResources().getString(R.string.resume_button));
                    pause.setText(getResources().getString(R.string.resume_button));
                    stop.setEnabled(true);
                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cronómetro
                long systemCurrTime = SystemClock.elapsedRealtime();
                chronometer.setBase(systemCurrTime);
                timeWhenStopped = 0;

                showToast(v, getResources().getString(R.string.stop_button));
                start.setEnabled(true);
                pause.setText(getResources().getString(R.string.pause_button));
                pause.setEnabled(false);
                stop.setEnabled(false);
            }
        });



        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.putExtra("settings", sett);
                startActivityForResult(intent,1);
                /*
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
            }
        });

    }

    /* This listener will reset this chronometer every 10 seconds.
       So every 10 seconds, the chronometer will start count from 00:00.
       If do not add this listener, the chronometer will count forever.
       You can comment below method to see the effect.

       chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            long systemCurrTime = SystemClock.elapsedRealtime();
            long chronometerBaseTime = chronometer.getBase();
            long deltaTime = systemCurrTime - chronometerBaseTime;

            if(deltaTime > 10000)
            {
                chronometer.setBase(systemCurrTime);
            }
        }
    });



    public boolean registerListener (SensorEventListener listener,
                Sensor sensor,
                int samplingPeriodUs)

    */

    public void showToast(View v, String e){
        Context context = v.getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, e, duration);
        toast.show();
    }

}

