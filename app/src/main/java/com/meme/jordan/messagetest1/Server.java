package com.meme.jordan.messagetest1;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Jordan on 10/10/2017.
 */

public class Server implements Runnable, MessageSender {

    static final int PORT = 8000;

    private MessageListener listener;
    private Thread srvThread;
    private Scanner input;
    private PrintWriter output;
    private ServerSocket server;

    @SuppressWarnings("deprecation")
    public Server(MainActivity activity) {
        this.listener = activity;
        listener.onMessage("Server IP: " + Formatter.formatIpAddress(
                ((WifiManager) activity.getSystemService(Context.WIFI_SERVICE))
                        .getConnectionInfo().getIpAddress()));
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                server = new ServerSocket(PORT);
                Socket client = server.accept();
                listener.onMessage("Connection from " + client.getInetAddress().getHostAddress());
                input = new Scanner(client.getInputStream());
                output = new PrintWriter(client.getOutputStream());
                while (input.hasNextLine() && !Thread.interrupted()) {
                    listener.onMessage(input.nextLine());
                }
            } catch (IOException exc) {
                Log.w(MainActivity.TAG, exc.getMessage());
            } finally {
                try {
                    if (input != null)
                        input.close();
                    if (output != null)
                        output.close();
                    if (server != null)
                        server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void start() {
        srvThread = new Thread(this);
        srvThread.start();
    }

    @Override
    public void stop() {
        srvThread.interrupt();
        try {
            if (input != null)
                input.close();
            if (output != null)
                output.close();
            if (server != null)
                server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(final String msg) {
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
