package lexlex.bikephone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import lexlex.bikephone.R;
import lexlex.bikephone.models.Ride;
import lexlex.bikephone.models.Setting ;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button save;
    private Button cancel;
    private Spinner spinner;
    private EditText username;
    private TextView mac;
    private Setting  setting;
    private int frequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        setting = (Setting) intent.getSerializableExtra("settings");

        spinner = findViewById(R.id.config_spinner);
        username = findViewById(R.id.config_username);
        mac = findViewById(R.id.config_mac);
        save = findViewById(R.id.config_save);
        cancel = findViewById(R.id.config_cancel);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.freq_precision, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        setSelection();
        spinner.setOnItemSelectedListener(this);


        username.setText(setting.getUsername());
        mac.setText(setting.getMac());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("freqaaa", frequence+"");
                Setting  res = new Setting (mac.getText().toString(), username.getText().toString() ,frequence);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("settings", res);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                //returnIntent.putExtra("mData", object);
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    private void setSelection() {
        Log.d("freqSelection", ""+ setting.getSamplefreq());
        switch (setting.getSamplefreq()) {
            case 200000:
                spinner.setSelection(0);
                break;
            case 100000:
                spinner.setSelection(1);
                break;
            case 50000:
                spinner.setSelection(2);
                break;
            default:
                spinner.setSelection(0);
        }
      }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.d("selecao fre", pos+"");
        //TODO - Por frequências mais baixas ou assim....
        switch (pos) {
            case 0:
                frequence = 200000; //5 Hz
                break;
            case 1:
                frequence = 100000; //10 Hz
                break;
            case 2:
                frequence = 50000; //20 Hz
                break;
            case 3:
                frequence = 25000; //40 Hz
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

}
