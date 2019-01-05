package lexlex.bikephone.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lexlex.bikephone.models.Ride;
import lexlex.bikephone.models.Sample;


public class SensorHelper implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor temp;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private int freq;
    private int rideID;
    private Ride ride;
    private DatabaseHelper db;
    private ArrayList<Sample> samples;

    public SensorHelper(Context context, int freq, DatabaseHelper db) {
        this.freq = freq;
        this.db = db;
        this.samples = new ArrayList<>();

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


    //TODO - VER SENSORES DO DISPOSITIVOS E INICIAR APENAS OS QUE TÊM!
    public void init() {
        /*sensorManager.registerListener(SensorHelper.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(SensorHelper.this, temp, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(SensorHelper.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        */
        sensorManager.registerListener(SensorHelper.this, accelerometer, freq);
        db.createSensor(new lexlex.bikephone.models.Sensor("AccX", "Accelerometer X axis", "º"));
        db.createSensor(new lexlex.bikephone.models.Sensor("AccY", "Accelerometer Y axis", "º"));
        db.createSensor(new lexlex.bikephone.models.Sensor("AccZ", "Accelerometer Z axis", "º"));
        db.createSensor(new lexlex.bikephone.models.Sensor("Temp", "Termometer", "ºC"));
        //db.createSensor (new lexlex.bikephone.models.Sensor("AccZ", "Accelerometer Z axis", "º"));

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

    public void stop(String name, int elapsedmillis) {
        pause();
        this.ride.setName(name);
        this.ride.setDuration(elapsedmillis / 1000);
        db.updateRide(rideID, this.ride);
        //TODO - FAZER SET AOS VALORES : CRONÓMETRO E DISTANCIA + NOME

        Thread thread = new Thread() {
            @Override
            public void run() {
                long res = db.createSamples(samples);
                Log.d("Guardar valores", " " + res);
            }
        };
        thread.start();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public Ride getRide() {
        return this.ride;
    }
}
