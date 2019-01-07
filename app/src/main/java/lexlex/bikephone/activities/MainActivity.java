package lexlex.bikephone.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import lexlex.bikephone.models.Setting;

public class MainActivity extends AppCompatActivity implements RegistarFragmentListener {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private VerificarFragment verificarFragment;
    private RegistarFragment registarFragment;

    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    LocationManager locationManager;


    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(getApplicationContext());


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            //requestPermissions(PERMISSIONS, PERMISSION_ALL);
            checkLocationPermission();
        } else requestLocation();
        if (!isLocationEnabled()) {
            showAlert(1);
        }

        //populateDB();
        db.createSensor(new Sensor("AccX", "Accelerometer X axis", "º"));
        db.createSensor(new Sensor("AccY", "Accelerometer Y axis", "º"));
        db.createSensor(new Sensor("AccZ", "Accelerometer Z axis", "º"));
        db.createSensor(new Sensor("Temp", "Termometer", "ºC"));



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

        Setting settings1 = new Setting ("bike", "carlos", 50000);
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

                        isLocationEnabled();
                        //Request location updates:
                        //  locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    checkLocationPermission();
                    //requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }



    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private boolean isPermissionGranted() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("myLog", "Granted permission");
            return true;
        } else {
            Log.v("myLog", "Permission not granted");
            return false;
        }
    }


    public void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);
    }


    private void showAlert(final int status) {

        String message, title, btnText;

        if (status == 1) {
            message = "Your location setting is set to OFF, Please enable Location to " +
                    "on this app";

            title = "enable location";
            btnText = "Grant";
        } else {
            message = "Allow this app access";
            title = "Permission...";
            btnText = "granted";
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (status == 1) {

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                } else {
                    requestPermissions(PERMISSIONS, PERMISSION_ALL);
                }
            }

        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        dialog.show();
    }


}
