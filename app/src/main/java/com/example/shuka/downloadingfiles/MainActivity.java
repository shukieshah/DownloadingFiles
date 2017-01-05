package com.example.shuka.downloadingfiles;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

// IntentService : Uses an intent to start a background service so as not to disturb the UI
// Android Broadcast : Triggers an event that a BroadcastReceiver can act on
// BroadcastReceiver : Acts when a specific broadcast is made

public class MainActivity extends Activity {

    EditText downloadedEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadedEditText = (EditText) findViewById(R.id.downloadedEditText);

        // Allows use to track when an intent with the id TRANSACTION_DONE is executed
        // We can call for an intent to execute something and then tell use when it finishes
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileService.TRANSACTION_DONE);

        // Prepare the main thread to receive a broadcast and act on it
        registerReceiver(downloadReceiver, intentFilter);
    }

    public void startFileService(View view) {

        // Create an intent to run the IntentService in the background
        Intent intent = new Intent(this, FileService.class);

        // Pass the URL that the IntentService will download from
        intent.putExtra("url", "https://www.newthinktank.com/wordpress/lotr.txt");

        // Start the intent service
        this.startService(intent);

    }

    // Is alerted when the IntentService broadcasts TRANSACTION_DONE
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        // Called when the broadcast is received
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("FileService", "Service Received");

            showFileContents();

        }
    };

    // Will read our local file and put the text in the EditText
    public void showFileContents(){

        // Will build the String from the local file
        StringBuilder sb;

        try {
            // Opens a stream so we can read from our local file
            FileInputStream fis = this.openFileInput("myFile");

            // Gets an input stream for reading data
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

            // Used to read the data in small bytes to minimize system load
            BufferedReader bufferedReader = new BufferedReader(isr);

            // Read the data in bytes until nothing is left to read
            sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            // Put downloaded text into the EditText
            downloadedEditText.setText(sb.toString());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}