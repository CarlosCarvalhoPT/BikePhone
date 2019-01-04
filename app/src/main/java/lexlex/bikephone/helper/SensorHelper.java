package lexlex.bikephone.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import lexlex.bikephone.models.Sample;

public class SensorHelper implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor temp;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private int flag = 0;
    private int freq;
    private String rideID;
    private DatabaseHelper db;

    public SensorHelper(Context context, String rideID, int freq, DatabaseHelper db) {
        this.freq = freq;
        this.rideID = rideID;
        this.db = db;

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
        sensorManager.registerListener(SensorHelper.this, temp, freq);
        sensorManager.registerListener(SensorHelper.this, gyroscope, freq);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Long timeStamp = System.currentTimeMillis() / 1000;

        //TODO -ADICIONAR VALOR Á BASE DE DADOS!
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d("TAG", "X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);

            //X
            Sample accX = new Sample(rideID,"AccX", timeStamp, event.values[0] );
            long res = db.createSample(accX);
            Log.d("Insert AccX", " "+ res);

            //Y
            Sample accY = new Sample(rideID,"AccY", timeStamp, event.values[1] );
            res = db.createSample(accY);
            Log.d("Insert AccX", " "+ res);

            //Z
            Sample accZ = new Sample(rideID,"AccZ", timeStamp, event.values[2] );
            res = db.createSample(accZ);
            Log.d("Insert AccX", " "+ res);

        }
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            Log.d("TAGG", "ºC: " + event.values[0]);
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d("TAGc GYRO", "X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);

        }

    }


    public void stop() {
        sensorManager.unregisterListener(SensorHelper.this);
    }

    public void resume() {
        init();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
