package com.meme.jordan.messagetest1;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MessageListener {

    final static String TAG = " MessageTest";

    private TextView textdisplay;
    private Button sendButton;
    private EditText editText;
    private MessageSender sender;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        textdisplay = (TextView) findViewById(R.id.TextDisplay);
        textdisplay.setMovementMethod(new ScrollingMovementMethod());
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                sender.send(name + ": " + msg);
                textdisplay.append(name + ": " + msg + "\n");
                editText.setText("");
            }
        });

        editText = (EditText) findViewById(R.id.MessageBox);
    }

    @Override
    public void onMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textdisplay.append(msg + "\n");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        name = PreferenceManager.getDefaultSharedPreferences(this).getString("name", "User");

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("is_server", true))
            sender = new Server(this);
        else
            sender = new Client(this);
        sender.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        sender.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
    }
}
