package lexlex.bikephone.interfaces;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class myLocationListener implements LocationListener {
    private Location myLocation;
    private float distance;


    public myLocationListener() {
        this.distance = -10;

    }

    public float getDistance() {
        return this.distance;
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        Log.d("Latitude", newLocation.getLatitude() + " ");
        Log.d("Longitude", newLocation.getLongitude() + " ");


        if (distance < 0) { //primeira vez
            myLocation = newLocation;
            distance = 0;
        } else {
            distance += newLocation.distanceTo(myLocation);
            myLocation = newLocation;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
