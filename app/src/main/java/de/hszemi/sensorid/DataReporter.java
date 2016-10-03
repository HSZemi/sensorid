package de.hszemi.sensorid;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by zemanek on 30.06.16.
 */
public class DataReporter extends AsyncTask<Void, Void, Boolean> {
    private SensorData.SensorDataMessage serializedData;
    private String targetHost;

    public DataReporter(SensorData.SensorDataMessage serializedData, String targetHost) {
        this.serializedData = serializedData;
        this.targetHost = targetHost;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Socket socket = null;
        try {

            socket = new Socket(this.targetHost, 54321);

            OutputStream os = socket.getOutputStream();
            serializedData.writeTo(os);

            os.close();

            return true;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(socket != null){
                try{
                    socket.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return false;
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
