package lexlex.bikephone.interfaces;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class myLocationListener implements LocationListener {
    private Location myLocation;
    private float distance;

    public myLocationListener() {
        this.distance = 0;

    }

    public float getDistance(){return this.distance;}

    @Override
    public void onLocationChanged(Location newLocation) {
        Log.d("Localização", newLocation.getLatitude()+" ");
        if (distance == 0) { //primeira vez
            myLocation = newLocation;
        } else {
            distance = newLocation.distanceTo(myLocation);
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
