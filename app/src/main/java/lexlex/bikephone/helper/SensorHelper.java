package lexlex.bikephone.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TimingLogger;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lexlex.bikephone.DatabaseService;
import lexlex.bikephone.interfaces.ButtonDialogListener;
import lexlex.bikephone.interfaces.myLocationListener;
import lexlex.bikephone.models.Ride;
import lexlex.bikephone.models.Sample;


public class SensorHelper implements SensorEventListener {
    private Context context;
    private DatabaseHelper db;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor temp;
    private Sensor gyroscope;
    private int freq;
    private int rideID;
    private Ride ride;
    private ArrayList<Sample> samples;
    private ButtonDialogListener listener;
    private LocationManager locationManager;
    private myLocationListener locationListener;


    @SuppressLint("MissingPermission")
    public SensorHelper(Context context, int freq, DatabaseHelper db) {
        this.freq = freq;
        this.db = db;
        this.samples = new ArrayList<>();
        this.listener = null;
        this.context = context;

        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        locationListener = new myLocationListener();
        //TODO -MUDAR PARA O INIT() + Por o Pause!
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, this.freq, 5, locationListener);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String rideName = dateFormat.format(new Date());

        this.ride = new Ride(rideName, rideName, 0, 0, " ", freq);
        db.createRide(ride);
        this.rideID = db.getLastRideID();
        Log.d("rideID", "" + rideID);


        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        init();
    }


    public void setListener(ButtonDialogListener listener) {
        this.listener = listener;
    }

    //TODO - VER SENSORES DO DISPOSITIVOS E INICIAR APENAS OS QUE TÊM!
    public void init() {
        /*
        sensorManager.registerListener(SensorHelper.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(SensorHelper.this, temp, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(SensorHelper.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        */

        //Acelerómetro
        sensorManager.registerListener(SensorHelper.this, accelerometer, freq);


        //sensorManager.registerListener(SensorHelper.this, temp, freq);
        //sensorManager.registerListener(SensorHelper.this, gyroscope, freq);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        Long timeStamp = System.currentTimeMillis();

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("TAG", "X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);
            //X
            Sample accX = new Sample(rideID, "AccX", timeStamp, event.values[0]);
            samples.add(accX);
            //Y
            Sample accY = new Sample(rideID, "AccY", timeStamp, event.values[1]);
            samples.add(accY);
            //Z
            Sample accZ = new Sample(rideID, "AccZ", timeStamp, event.values[2]);
            samples.add(accZ);
        }
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            Log.d("TAGG", "ºC: " + event.values[0]);
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d("TAGc GYRO", "X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);

        }

    }


    public void pause() {
        sensorManager.unregisterListener(SensorHelper.this);
    }

    public void resume() {
        init();
    }

    public boolean stop(Activity activity, int elapsedmillis) {
        pause();

        showdialog(activity);

        this.ride.setDuration(elapsedmillis / 1000);

        return true;
    }

    private void showdialog(Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Guardar Corrida?");
        alertDialog.setMessage("Insira o nome da corrida:");

        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setText(ride.getName());
        alertDialog.setView(input);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Botão", "sim");
                        ride.setName(input.getText().toString());
                        ride.setDistance((int) locationListener.getDistance());
                        db.updateRide(rideID, ride);


                        //TODO - POR A CORRER UM Background Service!
                        //Intent i = new Intent(context, DatabaseService.class);
                        //i.putExtra("samples", samples);
                        //context.startService(i);


                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                long lStartTime = System.nanoTime();

                                db.createSamples(samples);

                                long lEndTime = System.nanoTime();
                                long output = lEndTime - lStartTime;
                                Log.d("time", " " + output / 1000);
                            } //1000000000
                        };
                        thread.start();


                        listener.SelectedAValue(0);
                        dialog.cancel();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        listener.SelectedAValue(1);
                        Log.d("Botão", "nao");
                        dialog.cancel();
                    }
                });

        alertDialog.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //codigo!
                        listener.SelectedAValue(2);
                        Log.d("Botão", "cancelar");
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public Ride getRide() {
        return this.ride;
    }

    public String getDistance() {
        return locationListener.getDistance() + " ";
    }
}
