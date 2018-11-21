package lexlex.bikephone.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import lexlex.bikephone.models.Sensor;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ridesManager";

    // Table Names
    private static final String TABLE_CONFIG = "configs";
    private static final String TABLE_SENSOR = "sensors";
    private static final String TABLE_RIDE = "rides";
    private static final String TABLE_SAMPLE = "samples";

    // Common column names
    // CONFIGS Table - column names
    private static final String KEY_CONFIG_MAC = "config_mac"; //chave primária
    private static final String KEY_CONFIG_USERNAME = "config_username";

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
    private static final String KEY_SAMPLE_NUMBER = "sample_number"; //chave primária, (juntamente com os outros 2 ??)
    private static final String KEY_SAMPLE_VALUE = "sample_value";


    // Table Create Statements
    private static final String CREATE_TABLE_CONFIG =
            "CREATE TABLE " + TABLE_CONFIG + "("
                    + KEY_CONFIG_MAC + " TEXT PRIMARY KEY,"
                    + KEY_CONFIG_USERNAME + " TEXT"
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
                    + KEY_CONFIG_MAC + " TEXT,"
                    + KEY_RIDE_DURATION + " INTEGER,"
                    + KEY_RIDE_DISTANCE + " INTEGER,"
                    + KEY_RIDE_SAMPLERATE + " INTEGER,"
                    + KEY_RIDE_TIMESTAMP + " NUMERIC,"
                    + " FOREIGN KEY ("+ KEY_CONFIG_MAC + ") REFERENCES " +TABLE_CONFIG +"("+KEY_CONFIG_MAC+")"
                    + ")";

    private static final String CREATE_TABLE_SAMPLE =
            "CREATE TABLE " + TABLE_SAMPLE + "("
                    + KEY_RIDE_ID + " TEXT,"
                    + KEY_SENSOR_TYPE + " TEXT,"
                    + KEY_SAMPLE_NUMBER + " INTEGER,"
                    + KEY_SAMPLE_VALUE + " INTEGER,"
                    + " FOREIGN KEY ("+ KEY_RIDE_ID + ") REFERENCES " +TABLE_RIDE +"("+KEY_RIDE_ID+"),"
                    + " FOREIGN KEY ("+ KEY_SENSOR_TYPE + ") REFERENCES " +TABLE_SENSOR +"("+KEY_SENSOR_TYPE+"),"
                    + " PRIMARY KEY ("+ KEY_RIDE_ID + ", "+ KEY_SENSOR_TYPE +", " + KEY_SAMPLE_NUMBER + ") "
                    + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("CREATE_TABLE_CONFIG", CREATE_TABLE_CONFIG);
        Log.d("CREATE_TABLE_SENSOR", CREATE_TABLE_SENSOR);
        Log.d("CREATE_TABLE_RIDE", CREATE_TABLE_RIDE);
        Log.d("CREATE_TABLE_SAMPLE", CREATE_TABLE_SAMPLE);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        Log.d("Entrou", "ON DATABASE CREATE");
        db.execSQL(CREATE_TABLE_CONFIG);
        db.execSQL(CREATE_TABLE_SENSOR);
        db.execSQL(CREATE_TABLE_RIDE);
        db.execSQL(CREATE_TABLE_SAMPLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
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


}
