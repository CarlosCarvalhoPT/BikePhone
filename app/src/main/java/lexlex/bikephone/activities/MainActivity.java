package lexlex.bikephone.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import lexlex.bikephone.interfaces.RegistarFragmentListener;
import lexlex.bikephone.fragments.VerificarFragment;
import lexlex.bikephone.helper.DatabaseHelper;
import lexlex.bikephone.models.Ride;
import lexlex.bikephone.models.Sample;
import lexlex.bikephone.models.Sensor;
import lexlex.bikephone.models.Settings;

public class MainActivity extends AppCompatActivity implements RegistarFragmentListener {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private VerificarFragment verificarFragment;
    private RegistarFragment registarFragment;


    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(getApplicationContext());


        //populateDB();
        db.createSensor(new Sensor("AccX", "Accelerometer X axis", "º"));
        db.createSensor(new Sensor("AccY", "Accelerometer Y axis", "º"));
        db.createSensor(new Sensor("AccZ", "Accelerometer Z axis", "º"));
        db.createSensor(new Sensor("Temp", "Termometer", "ºC"));

        checkLocationPermission();

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

           }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

           }
    }

    private void populateDB() {
        Sensor sensor1 = new Sensor("AccX", "Accelerometer X axis", "º");
        long res = db.createSensor(sensor1);

        sensor1 = new Sensor("AccY", "Accelerometer Y axis", "º");
        db.createSensor(sensor1);

        sensor1 = new Sensor("AccZ", "Accelerometer Z axis", "º");
        db.createSensor(sensor1);

        Settings settings1 = new Settings("bike", "carlos", 50000);
        res = db.createSettings(settings1);
        Log.d("Insert Settings", " " + res);


        //Ride
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = dateFormat.format(new Date());
        Ride ride1 = new Ride("ride1", /*"bike",*/ date, 10 * 60, 1200, "posicaoinicial", 50000);
        res = db.createRide(ride1);
        Log.d("Insert Ride", " " + res);


        //Sample
        Long tsLong = System.currentTimeMillis() / 1000;
        Sample sample1 = new Sample(1, "AccX", tsLong, (long) 12.1);
        res = db.createSample(sample1);
        Log.d("Insert Sample", " " + res);

        db.closeDB();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.registarFragment = new RegistarFragment();
        adapter.addFragment(registarFragment, getResources().getString(R.string.registar));

        Bundle bundle = new Bundle();
        bundle.putSerializable("db", db);
        this.verificarFragment = new VerificarFragment();
        verificarFragment.setArguments(bundle);
        adapter.addFragment(verificarFragment, getResources().getString(R.string.verificar));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void sendDataBack(Ride ride) {
        verificarFragment.addRide(ride);
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


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                      //  locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
