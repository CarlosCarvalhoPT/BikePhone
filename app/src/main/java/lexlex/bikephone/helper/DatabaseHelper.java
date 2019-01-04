package lexlex.bikephone.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import lexlex.bikephone.models.Ride;
import lexlex.bikephone.models.Sample;
import lexlex.bikephone.models.Sensor;
import lexlex.bikephone.models.Settings;


//TODO - Acabar a database - Colocar as querries de inserir, remover e verificar
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ridesManager";

    // Table Names
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_SENSOR = "sensors";
    private static final String TABLE_RIDE = "rides";
    private static final String TABLE_SAMPLE = "samples";

    // Common column names
    // SETTINGS Table - column names
    private static final String KEY_SETTINGS_MAC = "config_mac"; //chave primária
    private static final String KEY_SETTINGS_USERNAME = "config_username";
    private static final String KEY_SETTINGS_SAMPLEFREQ = "config_samplefreq";

    // SENSORS Table - column names
    private static final String KEY_SENSOR_TYPE = "sensor_type";
    private static final String KEY_SENSOR_DESCRIPTION = "sensor_descripton";
    private static final String KEY_SENSOR_UNIT = "sensor_unit";

    // RIDES Table - column names
    private static final String KEY_RIDE_ID = "ride_id"; //chave primária
    private static final String KEY_RIDE_DURATION = "ride_duration";
    private static final String KEY_RIDE_DISTANCE = "ride_distance";
    private static final String KEY_RIDE_SAMPLERATE = "ride_samplerate";
    private static final String KEY_RIDE_TIMESTAMP = "ride_timestamp";


    // SAMPLES Table - column names
    private static final String KEY_SAMPLE_TIME = "sample_time"; //chave primária, (juntamente com os outros 2 ??)
    private static final String KEY_SAMPLE_VALUE = "sample_value";


    // Table Create Statements
    private static final String CREATE_TABLE_SETTINGS =
            "CREATE TABLE " + TABLE_SETTINGS + "("
                    + KEY_SETTINGS_MAC + " TEXT PRIMARY KEY,"
                    + KEY_SETTINGS_USERNAME + " TEXT,"
                    + KEY_SETTINGS_SAMPLEFREQ + " INTEGER"
                    + ")";

    private static final String CREATE_TABLE_SENSOR =
            "CREATE TABLE " + TABLE_SENSOR + "("
                    + KEY_SENSOR_TYPE + " TEXT PRIMARY KEY,"
                    + KEY_SENSOR_DESCRIPTION + " TEXT,"
                    + KEY_SENSOR_UNIT + " TEXT"
                    + ")";


    private static final String CREATE_TABLE_RIDE =
            "CREATE TABLE " + TABLE_RIDE + "("
                    + KEY_RIDE_ID + " TEXT PRIMARY KEY,"
                    + KEY_SETTINGS_MAC + " TEXT,"
                    + KEY_RIDE_DURATION + " INTEGER,"
                    + KEY_RIDE_DISTANCE + " INTEGER,"
                    + KEY_RIDE_SAMPLERATE + " INTEGER,"
                    + KEY_RIDE_TIMESTAMP + " NUMERIC,"
                    + " FOREIGN KEY (" + KEY_SETTINGS_MAC + ") REFERENCES " + TABLE_SETTINGS + "(" + KEY_SETTINGS_MAC + ") ON UPDATE CASCADE"
                    + ")";

    private static final String CREATE_TABLE_SAMPLE =
            "CREATE TABLE " + TABLE_SAMPLE + "("
                    + KEY_RIDE_ID + " TEXT,"
                    + KEY_SENSOR_TYPE + " TEXT,"
                    + KEY_SAMPLE_TIME + " INTEGER,"
                    + KEY_SAMPLE_VALUE + " REAL,"
                    + " PRIMARY KEY (" + KEY_RIDE_ID + ", " + KEY_SENSOR_TYPE + ", " + KEY_SAMPLE_TIME + "), "
                    + " FOREIGN KEY (" + KEY_RIDE_ID + ") REFERENCES " + TABLE_RIDE + "(" + KEY_RIDE_ID + ") ON DELETE CASCADE ON UPDATE CASCADE,"
                    + " FOREIGN KEY (" + KEY_SENSOR_TYPE + ") REFERENCES " + TABLE_SENSOR + "(" + KEY_SENSOR_TYPE + ")ON DELETE CASCADE ON UPDATE CASCADE"
                    + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("CREATE_TABLE_CONFIG", CREATE_TABLE_SETTINGS);
        Log.d("CREATE_TABLE_SENSOR", CREATE_TABLE_SENSOR);
        Log.d("CREATE_TABLE_RIDE", CREATE_TABLE_RIDE);
        Log.d("CREATE_TABLE_SAMPLE", CREATE_TABLE_SAMPLE);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        Log.d("Entrou", "ON DATABASE CREATE");
        db.execSQL(CREATE_TABLE_SETTINGS);
        db.execSQL(CREATE_TABLE_SENSOR);
        db.execSQL(CREATE_TABLE_RIDE);
        db.execSQL(CREATE_TABLE_SAMPLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAMPLE);

        // create new tables
        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /*****SENSORES*******/

    public long createSensor(Sensor sensor1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SENSOR_TYPE, sensor1.getId());
        values.put(KEY_SENSOR_DESCRIPTION, sensor1.getDescription());
        values.put(KEY_SENSOR_UNIT, sensor1.getUnit());

        return db.insert(TABLE_SENSOR, null, values);
    }


    /***** SETTINGS *******/
    public long createSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SETTINGS_MAC, settings.getMac());
        values.put(KEY_SETTINGS_USERNAME, settings.getUsername());
        values.put(KEY_SETTINGS_SAMPLEFREQ, settings.getSamplefreq());

        return db.insert(TABLE_SETTINGS, null, values);
    }

    public Settings getSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Settings settings = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            settings = new Settings(
                    c.getString(c.getColumnIndex(KEY_SETTINGS_MAC)),
                    c.getString(c.getColumnIndex(KEY_SETTINGS_USERNAME)),
                    c.getInt(c.getColumnIndex(KEY_SETTINGS_SAMPLEFREQ))
            );
            return settings;
        }
        return settings;

    }

    public long updateSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SETTINGS_USERNAME, settings.getUsername());
        values.put(KEY_SETTINGS_SAMPLEFREQ, settings.getSamplefreq());

        return db.update(TABLE_SETTINGS, values, KEY_SETTINGS_MAC + " = ?",
                new String[]{String.valueOf(settings.getMac())});
    }


    /*****  RIDES *******/
    public long createRide(Ride ride) {
        SQLiteDatabase db = this.getWritableDatabase();

        //no inicio: id: padrão disntance=0, duration=0
        ContentValues values = new ContentValues();
        values.put(KEY_RIDE_ID, ride.getId());
        values.put(KEY_SETTINGS_MAC, ride.getMac());
        values.put(KEY_RIDE_DISTANCE, ride.getDistance());
        values.put(KEY_RIDE_DURATION, ride.getDuration());
        values.put(KEY_RIDE_SAMPLERATE, ride.getSample_freq());
        values.put(KEY_RIDE_TIMESTAMP, ride.getDate());

        return db.insert(TABLE_RIDE, null, values);
    }

    public long updateRide(String rideID, Ride ride) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RIDE_ID, ride.getId());
        values.put(KEY_RIDE_DISTANCE, ride.getDistance());
        values.put(KEY_RIDE_DURATION, ride.getDuration());

        return db.update(TABLE_RIDE, values, KEY_RIDE_ID + " = ?",
                new String[]{rideID});

    }

    public void deleteRide(String rideID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RIDE, KEY_RIDE_ID + " = ?",
                new String[]{rideID});
    }

    //TODO
    public Ride getRide(String rideID) {
        return null;
    }

    //TODO
    public ArrayList<Ride> getAllRides() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Ride> rides = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_RIDE;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Settings settings = null;

        if (c.getCount() > 0) {
            do {
                c.moveToFirst();
                Ride ride = new Ride(
                        c.getString(c.getColumnIndex(KEY_RIDE_ID)),
                        c.getString(c.getColumnIndex(KEY_SETTINGS_MAC)),
                        c.getString(c.getColumnIndex(KEY_RIDE_TIMESTAMP)),
                        c.getInt(c.getColumnIndex(KEY_RIDE_DURATION)),
                        c.getInt(c.getColumnIndex(KEY_RIDE_DISTANCE)),
                        null,
                        c.getInt(c.getColumnIndex(KEY_RIDE_SAMPLERATE))
                );

                rides.add(ride);
            } while (c.moveToNext());
        }
        return rides;
    }


    /*****  SAMPLES *******/
    //TODO - TESTAR
    public long createSample(/*String rideID,*/ Sample sample) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RIDE_ID, sample.getCorridaID());
        values.put(KEY_SENSOR_TYPE, sample.getTypeID());
        values.put(KEY_SAMPLE_TIME, sample.getTimestamp());
        values.put(KEY_SAMPLE_VALUE, sample.getValue());

        return db.insert(TABLE_SAMPLE, null, values);

    }

    //TODO
    public Sample getSample(String rideID, Sensor sensor) {
        return null;
    }

    //TODO - TESTAR
    public ArrayList<Sample> getRideSamples(String rideID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Sample> samples = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_SAMPLE
                + " WHERE " + KEY_RIDE_ID + " = '" + rideID + "'";

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0) {
            do {
                c.moveToFirst();
                Sample sample = new Sample(
                        c.getString(c.getColumnIndex(KEY_RIDE_ID)),
                        c.getString(c.getColumnIndex(KEY_SENSOR_TYPE)),
                        c.getLong(c.getColumnIndex(KEY_SAMPLE_TIME)),
                        c.getLong(c.getColumnIndex(KEY_SAMPLE_VALUE))
                );
                samples.add(sample);

            } while (c.moveToNext());
        }
        return samples;

    }
}
