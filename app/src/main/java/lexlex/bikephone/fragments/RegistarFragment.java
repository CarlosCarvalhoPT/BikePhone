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

import lexlex.bikephone.interfaces.ButtonDialogListener;
import lexlex.bikephone.interfaces.RegistarFragmentListener;
import lexlex.bikephone.R;
import lexlex.bikephone.activities.SettingsActivity;
import lexlex.bikephone.helper.DatabaseHelper;
import lexlex.bikephone.helper.SensorHelper;
import lexlex.bikephone.models.Ride;
import lexlex.bikephone.models.Setting;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class RegistarFragment extends Fragment {

    private RegistarFragmentListener registarFragmentListener;
    private Button start;
    private Button pause;
    private Button stop;
    private Button settings;
    private Chronometer chronometer;
    long timeWhenStopped;
    private Setting  sett;
    private DatabaseHelper db;
    SensorHelper sh;

    int elapsedMillis;

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
        View view = inflater.inflate(R.layout.fragment_registar, container, false);

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
        sett = new Setting ("daaaa");
        //ir à base de dados buscar as configurações
        db = new DatabaseHelper(getContext());
        Setting res = db.getSettings();
        if (res != null) {
            sett = new Setting (
                    res.getMac(),
                    res.getUsername(),
                    res.getSamplefreq()
            );
        } else {
            /*
            //TODO - pegar mac do telemóvel
            String android_id = android.provider.Settings.Secure.getString(getContext().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            sett = new Settings(android_id);
            */
            sett = new Setting ("macTelemóvelA");
            db.createSettings(sett);
        }
        db.closeDB();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                sett = (Setting) data.getSerializableExtra("settings");
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

                if (!stop.isEnabled()) { //É para pausar corrida
                    //é para pausar a corrida
                    sh.pause();
                    //cronómetro
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());


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
                if (stop.isEnabled()) { //É para resumir corrida
                    sh.resume();
                    //cronómetro

                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();
                    showToast(v, getResources().getString(R.string.resume));

                    pause.setText(getResources().getString(R.string.pause_button));
                    stop.setEnabled(false);
                } else {
                    //showToast(v, sh.getDistance()+" " );
                    showToast(v, getResources().getString(R.string.longpress));
                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //showToast(v, getResources().getString(R.string.stop));


                sh.setListener(new ButtonDialogListener() {
                                   @Override
                                   public void SelectedAValue(int value) {
                                       switch (value) {
                                           case 0:
                                               //YES!
                                               //cronómetro
                                                showToast(v, "A Guardar Dados - Aguarde!");
                                               Log.d("aaa", "YES YES YES");
                                               chronometer.setBase(SystemClock.elapsedRealtime());
                                               timeWhenStopped = 0;
                                               sendDataBack(sh.getRide());

                                               start.setEnabled(true);
                                               pause.setText(getResources().getString(R.string.pause_button));
                                               pause.setEnabled(false);
                                               stop.setEnabled(false);
                                               break;
                                           case 1: //NO - descartar tudo
                                               //cronómetro
                                               Log.d("aaa", "NO NO NO");
                                               chronometer.setBase(SystemClock.elapsedRealtime());
                                               timeWhenStopped = 0;
                                               start.setEnabled(true);
                                               pause.setText(getResources().getString(R.string.pause_button));
                                               pause.setEnabled(false);
                                               stop.setEnabled(false);
                                               break;
                                           case 2:
                                               Log.d("aaa", "CANCEL CANCEL");
                                               break;
                                           case 3:
                                               showToast(v, "Dados Guardados");
                                               break;
                                           default:
                                               break;
                                       }
                                   }
                               }
                );


                Log.d("tempo", "" + elapsedMillis);
                sh.stop(getActivity(), elapsedMillis);


            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.putExtra("settings", sett);
                startActivityForResult(intent, 1);
                /*
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
            }
        });

    }

    private void sendDataBack(Ride ride) {
        if (registarFragmentListener != null) {
            registarFragmentListener.sendDataBack(ride);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            registarFragmentListener = (RegistarFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentOneListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registarFragmentListener = null;
    }

    public void showToast(View v, String e) {
        Context context = v.getContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, e, duration);
        toast.show();
    }

}

