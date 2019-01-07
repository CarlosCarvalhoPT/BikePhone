package lexlex.bikephone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import lexlex.bikephone.helper.DatabaseHelper;
import lexlex.bikephone.models.Sample;

public class DatabaseService extends Service  {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DatabaseService() {
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        final DatabaseHelper db = new DatabaseHelper(this);
        final ArrayList<Sample> samples = (ArrayList<Sample>) intent.getSerializableExtra("samples");
        Thread thread = new Thread() {
            @Override
            public void run() {
                db.createSamples(samples);

            }
        };
        thread.start();
        Log.d("Guardar", samples.size()+" samples");

        Toast.makeText(this, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}