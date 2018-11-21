package lexlex.bikephone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import lexlex.bikephone.R;

public class RaceInfoActivity extends AppCompatActivity {
    TextView ride_id;

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

        //TODO - Completar com a informação da corrida
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        ride_id = findViewById(R.id.info_ride_id);
        ride_id.setText(message);

        //TODO - Colocar botão de apagar corrida.... editar nome??
    }

}
