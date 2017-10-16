package com.meme.jordan.messagetest1;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Jordan on 10/10/2017.
 */

public class Server implements MessageSender {

    static final int PORT = 8000;

    private MessageListener listener;
    private ArrayList<Connection> connections = new ArrayList<>();
    private ServerSocket server;


    public Server(MainActivity activity) {
        this.listener = activity;
        listener.onMessage("Server IP: " + activity.getServerIp());
    }

    @Override
    public void send(final String msg) {
        for (Connection c : connections)
            c.send(msg);
    }

    @Override
    public void start() {
        for(Connection c : connections) {
            if(c.isInterrupted())
                connections.remove(c);
        }
        Connection t = new Connection(this);
        t.start();
        connections.add(t);
    }

    @Override
    public void stop() {
        for(Connection c : connections)
            c.interrupt();
    }


    private class Connection extends Thread {

        private Server serverCtl;
        private BufferedReader input;
        private PrintWriter output;
        private String clntAddr;

        Connection(Server server) {
            this.serverCtl = server;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    server = new ServerSocket(PORT);
                    Socket client = server.accept();
                    serverCtl.start();
                    clntAddr = client.getInetAddress().getHostAddress();
                    listener.onMessage("Connection from " + clntAddr);
                    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    output = new PrintWriter(client.getOutputStream());
                    while (!Thread.interrupted()) {
                        while(!input.ready() && !Thread.interrupted())
                            Thread.sleep(100);
                        String msg = input.readLine();
                        listener.onMessage(msg);
                        serverCtl.send(msg);
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
                        if (server != null)
                            server.close();
                        String msg = "Disconnected from " + clntAddr;
                        listener.onMessage(msg);
                        serverCtl.send(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }


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

        @Override
        public void interrupt() {
            try {
                if (server != null)
                    server.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                super.interrupt();
            }
        }
    }
}
