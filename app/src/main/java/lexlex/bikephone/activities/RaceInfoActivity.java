package lexlex.bikephone.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import lexlex.bikephone.R;
import lexlex.bikephone.models.Ride;

public class RaceInfoActivity extends AppCompatActivity {
    TextView ride_id;
    TextView ride_date;
    TextView ride_duration;
    TextView ride_distance;
    TextView ride_samplerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        Intent intent = getIntent();
        Ride ride = (Ride) intent.getSerializableExtra("ride");
        ride_id = findViewById(R.id.info_ride_id);
        ride_id.setText(ride.getId());

        ride_date = findViewById(R.id.info_ride_date);
        ride_date.setText(ride.getDate());

        ride_duration = findViewById(R.id.info_ride_duration);
        ride_duration.setText(formatSeconds( ride.getDuration() ));

        ride_distance = findViewById(R.id.info_ride_distance);
        ride_distance.setText(String.valueOf(ride.getDistance()) + " " +getString(R.string.distanceunits) );

        ride_samplerate = findViewById(R.id.info_ride_samplerate);
        ride_samplerate.setText(String.valueOf(ride.getSample_freq()/1000) + " Hz");

        //TODO - Colocar botão de apagar corrida.... editar nome (depois de fazer as querries na BD)
    }

    public static String formatSeconds(int seconds){
        int hr = seconds/3600;
        int rem = seconds%3600;
        int mn = rem/60;
        int sec = rem%60;
        String HH = (hr<10 ? "0" : "")+hr;
        String MM = (mn<10 ? "0" : "")+mn;
        String SS = (sec<10 ? "0" : "")+sec;
        return HH + ":" + MM + ":" + SS;
    }
}
