package de.hszemi.sensorid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * DataReporter sends a SensorDataMessage to the target host
 */
public class DataReporter extends AsyncTask<Void, Void, Boolean> {
    private SensorData.SensorDataMessage serializedData;
    private String targetHost;
    private Context context;

    public DataReporter(SensorData.SensorDataMessage serializedData, String targetHost, Context context) {
        this.serializedData = serializedData;
        this.targetHost = targetHost;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Socket socket = null;
        try {

            socket = new Socket(this.targetHost, 54321);

            OutputStream os = socket.getOutputStream();

            // send the serialized SensorDataMessage
            serializedData.writeTo(os);

            os.close();

            return true;

        } catch (UnknownHostException e) {
            Log.e("SOCKETERROR", "UnknownHostException");
            e.printStackTrace();
        }catch (ConnectException e){
            Log.e("SOCKETERROR", "ConnectException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("SOCKETERROR", "IOException");
            e.printStackTrace();
        } finally{
            if(socket != null){
                try{
                    socket.close();
                } catch(IOException e){
                    Log.e("SOCKETERROR", "IOException");
                    e.printStackTrace();
                }
            }
        }
        // If we reach this part of the code, an exception occurred.
        // Probably the host did not respond.
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(!result){
            // SOMETHING WENT WRONG! ABORT MISSION!
            Toast.makeText(context, "Can't report data. Is the server at "+targetHost+" running?", Toast.LENGTH_SHORT).show();
        }
    }

    public SensorData.SensorDataMessage getSerializedData() {
        return serializedData;
    }

    public void setSerializedData(SensorData.SensorDataMessage serializedData) {
        this.serializedData = serializedData;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

}
