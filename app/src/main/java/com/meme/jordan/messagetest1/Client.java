package com.meme.jordan.messagetest1;

import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Jordan on 10/12/2017.
 */

public class Client implements MessageSender {

    private String host;
    private MessageListener listener;
    private Connection connection;

    public Client(MainActivity activity) {
        this.listener = activity;
        host = PreferenceManager.getDefaultSharedPreferences(activity).getString("host_ip", "127.0.0.1");
    }

    @Override
    public void start() {
        stop();
        connection = new Connection();
        connection.start();
    }

    @Override
    public void stop() {
        if(connection != null)
            connection.interrupt();
    }

    @Override
    public void send(String msg) {
        connection.send(msg);
    }

    private class Connection extends Thread {

        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;

        @Override
        public void run() {
            try {
                socket = new Socket(host, Server.PORT);
                listener.onMessage("Connected to " + host);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
                while (!isInterrupted()) {
                    String msg = input.readLine();
                    if(msg == null) break;
                    listener.onMessage(msg);
                }
            } catch (IOException exc) {
                //Log.w(MainActivity.TAG, exc.getMessage()); // Dont spam the log
            } finally {
                try {
                    if (socket != null) {
                        listener.onMessage("Disconnected from " + host);
                        socket.close();
                    }
                    if (input != null)
                        input.close();
                    if (output != null)
                        output.close();
                } catch (IOException exc) {
                    Log.e(MainActivity.TAG, exc.getMessage());
                }

                Client.this.start();
            }
        }

        public void send(final String msg) {
            if (output != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(output != null) {
                            output.println(msg);
                            output.flush();
                        }
                    }
                }).start();
            }
        }

        @Override
        public void interrupt() {
            try {
                if (socket != null)
                    socket.close();
            } catch(IOException exc) {
                Log.e(MainActivity.TAG, exc.getMessage());
            } finally {
                super.interrupt();
            }
        }
    }
}
