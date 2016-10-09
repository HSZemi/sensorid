package de.hszemi.sensorid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private List<Sensor> mAccelerometers;
    private List<Sensor> mSensors;
    private List<Sensor> mAllSensors;
    private Map<String, SensorData.SensorDataMessage.Builder> mLogmap;
    SharedPreferences sharedPref;
    private int counter = -1;
    private int numberOfLoops = 20;
    private TextView mTextCounter;
    private EditText mEditText;
    private boolean locked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView sensorListView = (ListView) findViewById(R.id.sensor_list);
        mAccelerometers = new LinkedList<>();
        mSensors = new LinkedList<>();
        mAllSensors = new LinkedList<>();
        mLogmap = new HashMap<>();
        mEditText = (EditText) findViewById(R.id.editText);
        mTextCounter = (TextView) findViewById(R.id.textCounter);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);


        ListAdapter a = new SensorListViewAdapter(this, mAllSensors);

        sensorListView.setAdapter(a);
        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Sensor s = (Sensor) parent.getItemAtPosition(position);
                Log.d("clicked", s.toString());
                //gatherSensorData(s, view, editText.getText().toString());
            }
        });


        // This button triggers data collection and reporting
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    if(locked){
                        Snackbar.make(view, "Do not double touch please. We are still collecting.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        // "lock" the application in order to prevent overlapping data collection
                        locked = true;

                        // get the number of cycles from the settings
                        numberOfLoops = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_NUMBER_OF_CYCLES, "20"));

                        counter = numberOfLoops * mSensors.size();
                        mTextCounter.setText("" + counter);

                        // post all the collection handlers at the corresponding point in time
                        for (int i = 0; i < numberOfLoops; i++) {
                            int k = 0;
                            for (final Sensor s : mSensors) {
                                Handler handler = new Handler();
                                Log.d("handler", "Posting sensor in " + (i * mSensors.size() * 6000 + k * 6000) + "ms");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        gatherSensorData(s, view, mEditText.getText().toString());
                                    }
                                }, i * mSensors.size() * 6000 + k * 6000);
                                k++;

                            }
                        }

                        final Snackbar snackbar = Snackbar.make(view, "Collecting and reporting Data. This will take approx. "+(6*counter)+" seconds.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Action", null);
                        snackbar.show();

                        // post last handler that will:
                        // - remove the snackbar
                        // - unlock the application again
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                snackbar.dismiss();
                                locked = false;
                            }
                        }, counter*6000);

                    }
                }
            });
        }

        // we build two lists with sensors:
        // - mAllSensors contains all available sensors,
        // - mSensors contains the sensor types we generally select for examination
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s : sensors) {
            if (SensorHelper.activeSensors.contains(SensorHelper.type2string(s.getType()))) {
                mSensors.add(s);
            }
            mAllSensors.add(s);
        }

        if (mSensors.isEmpty()) {
            mSensors.add(null);
        }


    }

    /**
     * Gather sensor data from a sensor for 5s and put it into mLogmap
     * Then trigger the reporting.
     * */
    private void gatherSensorData(final Sensor s, final View view, final String target) {

        Log.d("status", "I am now gathering data from: "+s.getName());

        // retrieve the device id from the settings
        String display_name = sharedPref.getString(SettingsActivity.KEY_DISPLAY_NAME, "John Doe");

        mLogmap.put(s.getName(), SensorData.SensorDataMessage.newBuilder().setDisplayname(display_name).setSensorname(s.getName()));
        mSensorManager.registerListener((MainActivity) view.getContext(), s, SensorManager.SENSOR_DELAY_FASTEST);

        Handler handler = new Handler();
        Log.d("status", "Handlering! "+s.getName());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSensorManager.unregisterListener((MainActivity) view.getContext(), s);
                Log.d("status", "Reporting! "+s.getName());
                reportLogmap(target, s.getName());

                // update the counter
                counter--;
                mTextCounter.setText(""+counter);
            }
        }, 5000);


    }

    /**
     * Take data for id from mLogmap and report the data.
     * */
    private void reportLogmap(String target, String id) {
        // Take the data from mLogmap
        SensorData.SensorDataMessage.Builder data = mLogmap.get(id);
        mLogmap.remove(id);

        // Send the data to the server using the DataReporter
        DataReporter dataReporter = new DataReporter(data.build(), target, getApplicationContext());
        dataReporter.execute();

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Just output the change and then ignore it
        Log.d("Sensor: ", "Accuracy changed to " + accuracy);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Build a SensorReading object that contains the recorded sensor data
        // and add it to the SensorDataMessage
        SensorData.SensorDataMessage.Builder builder = mLogmap.get(event.sensor.getName());
        if (builder == null) {
            Log.e("builder", "builder for " + event.sensor.getName() + " is null!");
        } else {
            SensorData.SensorReading.Builder reading = SensorData.SensorReading.newBuilder()
                    .setTimestamp(event.timestamp);

            if(event.values != null && event.values.length > 0){
                reading.setX(event.values[0]);
            }
            if(event.values != null && event.values.length > 1) {
                reading.setY(event.values[1]);
            }
            if(event.values != null && event.values.length > 2) {
                reading.setZ(event.values[2]);
            }
            builder.addSensorreading(reading);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // reset the target server address to the one from the settings
        if(mEditText != null) {
            mEditText.setText(sharedPref.getString(SettingsActivity.KEY_TARGET_IP_ADDRESS, "172.16.1.100"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mSensorManager.unregisterListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
