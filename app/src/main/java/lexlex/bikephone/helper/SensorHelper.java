package lexlex.bikephone.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorHelper implements SensorEventListener {

    Context appContext;
    private SensorManager sensorManager;
    Sensor temp;
    Sensor accelerometer;
    Sensor gyroscope;
    private int flag = 0;

    public SensorHelper() {

        sensorManager = (SensorManager) appContext.getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        init();


    }

    public void init(){
        sensorManager.registerListener(SensorHelper.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(SensorHelper.this, temp, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(SensorHelper.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {




            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                Log.d("TAG", "X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);
            }
            if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                Log.d("TAGG", "ºC: " + event.values[0]);
            }
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                Log.d("TAGc GYRO", "X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);

            }



    }


    public void stop(){

        sensorManager.unregisterListener(SensorHelper.this);

    }

    public void resume(){

        init();



    }





    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
