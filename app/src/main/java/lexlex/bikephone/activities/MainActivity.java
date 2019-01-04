package lexlex.bikephone.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toolbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lexlex.bikephone.R;
import lexlex.bikephone.fragments.RegistarFragment;
import lexlex.bikephone.fragments.VerificarFragment;
import lexlex.bikephone.helper.DatabaseHelper;
import lexlex.bikephone.models.Ride;
import lexlex.bikephone.models.Sample;
import lexlex.bikephone.models.Sensor;
import lexlex.bikephone.models.Settings;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(getApplicationContext());


        //TODO - ADICIONAR MAIS MATERIAL Á BASE DE DADOS
        populateDB();



        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void populateDB() {
        Sensor sensor1 = new Sensor("AccX", "Accelerometer X axis", "º");
        long res = db.createSensor (sensor1);

        sensor1 = new Sensor("AccY", "Accelerometer Y axis", "º");
        db.createSensor (sensor1);

        sensor1 = new Sensor("AccZ", "Accelerometer Z axis", "º");
        db.createSensor (sensor1);

        Settings settings1 = new Settings("bike", "carlos", 50000);
        res = db.createSettings(settings1);
        Log.d("Insert Settings", " "+ res);


        //Ride
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = dateFormat.format(new Date());
        Ride ride1 = new Ride("ride1", "bike", date, 10*60, 1200, "posicaoinicial", 50000);
        res = db.createRide(ride1);
        Log.d("Insert Ride", " "+ res);


        //Sample
        Long tsLong = System.currentTimeMillis()/1000;
        Sample sample1 = new Sample("ride1","AccX", tsLong, (long) 12.1);
        res = db.createSample(sample1);
        Log.d("Insert Sample", " "+ res);

        db.closeDB();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RegistarFragment(), getResources().getString(R.string.registar));
        adapter.addFragment(new VerificarFragment(), getResources().getString(R.string.verificar));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
