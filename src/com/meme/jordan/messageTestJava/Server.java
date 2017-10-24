package com.meme.jordan.messageTestJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Jordan on 10/10/2017.
 */

public class Server implements MessageSender {

    static final int PORT = 8000;

    private MessageListener listener;
    private List<Connection> connections = new CopyOnWriteArrayList<>();
    private ServerSocket server;


    public Server(MessageListener listener) {
        this.listener = listener;

        try {
            listener.onMessage("Server IP: " + InetAddress.getLocalHost().getHostAddress());
            server = new ServerSocket(PORT);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void send(String msg) {
        for (Connection c : connections)
            c.send(msg);
    }

    public void receive(String msg, Connection con) {
        listener.onMessage(msg);
        for(Connection c : connections) {
            if(c != con)
                c.send(msg);
        }
    }

    @Override
    public void start() {
        for(Connection c : connections) {
            if(c.isInterrupted())
                connections.remove(c);
        }
        Connection t = new Connection();
        t.start();
        connections.add(t);
    }

    @Override
    public void stop() {
        try {
            server.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        for(Connection c : connections)
            c.interrupt();
    }


    private class Connection extends Thread {

        private BufferedReader input;
        private PrintWriter output;
        private String clntAddr;
        private Socket client;

        @Override
        public void run() {
            try {
                client = server.accept();
                Server.this.start();
                clntAddr = client.getInetAddress().getHostAddress();
                Server.this.receive(clntAddr + " connected", this);
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                output = new PrintWriter(client.getOutputStream());
                while (!isInterrupted()) {
                    String msg = input.readLine();
                    if(msg == null) break;
                    Server.this.receive(msg, this);
                }
            } catch (IOException exc) {
                System.err.println(exc.getMessage());
            } finally {
                try {
                    if(client != null) {
                        Server.this.receive(clntAddr + " disconnected", this);
                        client.close();
                    }
                    if (input != null)
                        input.close();
                    if (output != null)
                        output.close();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }

        public void send(final String msg) {
            new Thread(() -> {
                if(output != null) {
                    output.println(msg);
                    output.flush();
                }
            }).start();
        }

        @Override
        public void interrupt() {
            try {
                if (client != null)
                    client.close();
            } catch(IOException exc) {
                exc.printStackTrace();
            } finally {
                super.interrupt();
            }
        }
    }
}
