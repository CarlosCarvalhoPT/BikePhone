package lexlex.bikephone.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import lexlex.bikephone.interfaces.RegistarFragmentListener;
import lexlex.bikephone.R;
import lexlex.bikephone.activities.SettingsActivity;
import lexlex.bikephone.helper.DatabaseHelper;
import lexlex.bikephone.helper.SensorHelper;
import lexlex.bikephone.models.Ride;
import lexlex.bikephone.models.Settings;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class RegistarFragment extends Fragment{
    private RegistarFragmentListener registarFragmentListener;

    private Button start;
    private Button pause;
    private Button stop;
    private Button settings;
    private Chronometer chronometer;
    long timeWhenStopped;
    private Settings sett;
    private DatabaseHelper db;
    SensorHelper sh;

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
        //TODO - alterar funcionamento dos butões
        //POR LONG PRESS PARA PARAR/RETOMAR OU PARAR
        //POR A SMALL PRESS A APARECER TOAST

        //POR A PERGUNTAR AO PARAR A CORRIDA SE QUER GUARDAR OU NÃO!
        //E FAZER A LOGICA ASSOCIADA A ISSO!
        //TODO - Recolher valores dos GPS

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cronómetro
                long systemCurrTime = SystemClock.elapsedRealtime();
                chronometer.setBase(systemCurrTime);
                chronometer.start();

                //Iniciar guardar valores
                sh = new SensorHelper(getActivity(), sett.getSamplefreq(), db);

                showToast(v, getResources().getString(R.string.start));
                start.setEnabled(false);
                pause.setEnabled(true);
                stop.setEnabled(false);
            }
        });


        pause.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                if(!stop.isEnabled()){ //É para pausar corrida
                    //é para pausar a corrida
                    sh.pause();
                    //cronómetro
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();

                    showToast(v, getResources().getString(R.string.pause));
                    pause.setText(getResources().getString(R.string.resume_button));
                    stop.setEnabled(true);
                }
                return true;
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stop.isEnabled()){ //É para resumir corrida
                    sh.resume();
                    //cronómetro
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();

                    showToast(v, getResources().getString(R.string.resume));
                    pause.setText(getResources().getString(R.string.pause_button));
                    stop.setEnabled(false);

                }else {
                    //amostrar toast
                    showToast(v, getResources().getString(R.string.longpress));

                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
                Log.d("tempo", ""+elapsedMillis);
                sh.stop("AAA", elapsedMillis);

                //cronómetro
                long systemCurrTime = SystemClock.elapsedRealtime();
                chronometer.setBase(systemCurrTime);
                timeWhenStopped = 0;

                sendDataBack(sh.getRide() );

                showToast(v, getResources().getString(R.string.stop));
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


    private void sendDataBack(Ride ride) {
        if  (registarFragmentListener != null) {
            registarFragmentListener.sendDataBack(ride);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            registarFragmentListener = (RegistarFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement FragmentOneListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registarFragmentListener = null;
    }



    public void showToast(View v, String e){
        Context context = v.getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, e, duration);
        toast.show();
    }

}

