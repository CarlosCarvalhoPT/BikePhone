package lexlex.bikephone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import lexlex.bikephone.R;
import lexlex.bikephone.activities.RaceInfoActivity;
import lexlex.bikephone.adapters.RideAdapter;
import lexlex.bikephone.models.Ride;


public class VerificarFragment extends Fragment{
    ArrayList<Ride> rideList;
    ListView listView;
    private static RideAdapter rideAdapter;


    public VerificarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verificar, container, false);

        listView = view.findViewById(R.id.ride_list);

        //TODO - Remover este populate e usar as medições verdadeiras
        populateLV();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ride ride= rideList.get(position);

                Snackbar.make(view, ride.getId(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                Intent intent = new Intent(getActivity(), RaceInfoActivity.class);
                intent.putExtra("message", ride.getId());
                startActivity(intent);
            }
        });

        return view;
    }

    private void populateLV() {
        rideList= new ArrayList<>();

        rideList.add(new Ride(
                "Corrida 1",
                "12-12-2018",
                30*60
        ));

        rideList.add(new Ride(
                "Corrida 2",
                "13-12-2018",
                35*60
        ));
        rideList.add(new Ride(
                "Corrida 3",
                "14-12-2018",
                50*60
        ));

        rideAdapter= new RideAdapter(rideList,getActivity().getApplicationContext());
        listView.setAdapter(rideAdapter);
    }

}

