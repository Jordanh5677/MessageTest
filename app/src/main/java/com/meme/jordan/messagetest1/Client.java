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

public class Client implements MessageSender, Runnable {

    private String host;
    private Thread clntThread;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private MessageListener listener;

    public Client(MainActivity activity) {
        this.listener = activity;
        host = PreferenceManager.getDefaultSharedPreferences(activity).getString("host_ip", "127.0.0.1");
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                socket = new Socket();
                socket.bind(null);
                socket.connect(new InetSocketAddress(host, Server.PORT));
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
                while (!Thread.interrupted()) {
                    while(!input.ready() && !Thread.interrupted())
                        Thread.sleep(100);
                    listener.onMessage(input.readLine());
                }
            } catch (IOException exc) {
                Log.w(MainActivity.TAG, exc.getMessage());
            } catch (InterruptedException exc) {
            } finally {
                try {
                    if (input != null)
                        input.close();
                    if (output != null)
                        output.close();
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void start() {
        clntThread = new Thread(this);
        clntThread.start();
    }

    @Override
    public void stop() {
        clntThread.interrupt();
    }

    @Override
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
}
