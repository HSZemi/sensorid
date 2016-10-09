package de.hszemi.sensorid;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adapter for displaying Sensors in a ListView
 */
public class SensorListViewAdapter extends ArrayAdapter<Sensor> {
    public SensorListViewAdapter(Context context, List<Sensor> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Sensor sensor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sensor, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.sensorName);
        TextView tvType = (TextView) convertView.findViewById(R.id.sensorType);
        LinearLayout outerLayout = (LinearLayout) convertView.findViewById(R.id.outerLayout);
        // Populate the data into the template view using the data object
        if(sensor == null){
            tvName.setText("DUMMY_SENSOR_NAME");
            tvType.setText("DUMMY_SENSOR_TYPE");
        } else {
            tvName.setText(sensor.getName());
            tvType.setText(SensorHelper.type2string(sensor.getType()));
            if(SensorHelper.activeSensors.contains(SensorHelper.type2string(sensor.getType()))){
                outerLayout.setBackgroundColor(0xaa59b159);
            } else {
                outerLayout.setBackgroundColor(0x00000000);
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }


}
