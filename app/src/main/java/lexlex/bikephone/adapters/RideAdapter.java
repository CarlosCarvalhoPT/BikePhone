package lexlex.bikephone.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import lexlex.bikephone.R;
import lexlex.bikephone.models.Ride;

public class RideAdapter extends ArrayAdapter<Ride> implements View.OnClickListener{
    private ArrayList<Ride> rideSet;
    Context mContext;

    private static class ViewHolder {
        TextView rideID;
        TextView rideDate;
    }

    public RideAdapter(ArrayList<Ride> ride, Context context) {
        super(context, R.layout.ride_entry, ride);
        this.rideSet = ride;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        /*
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Ride ride=(Ride)object;


        switch (v.getId())
        {
            //abrir nova activitie com a info dessa corrida
            case R.id.ride_id:
                Snackbar.make(v, "Release date " +ride.getId(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ride dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ride_entry, parent, false);
            viewHolder.rideID = (TextView) convertView.findViewById(R.id.ride_id);
            viewHolder.rideDate = (TextView) convertView.findViewById(R.id.ride_duration);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.rideID.setText(dataModel.getId());
        viewHolder.rideDate.setText(dataModel.getDate());
        // Return the completed view to render on screen
        return convertView;
    }


}
