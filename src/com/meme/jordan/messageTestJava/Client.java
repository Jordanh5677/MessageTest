package com.meme.jordan.messageTestJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Jordan on 10/12/2017.
 */

public class Client implements MessageSender {

    private String host;
    private MessageListener listener;
    private Connection connection;

    public Client(MessageListener listener, String host) {
        this.listener = listener;
        this.host = host;
    }

    @Override
    public void start() {
        stop();
        connection = new Connection();
        connection.start();
    }

    @Override
    public void stop() {
        if (connection != null)
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
                    if (msg == null) break;
                    listener.onMessage(msg);
                }
            } catch (IOException exc) {
                //System.err.println(exc.getMessage()); // Dont spam the log
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
                    exc.printStackTrace();
                }

                if (!isInterrupted())
                    Client.this.start();
            }
        }

        public void send(final String msg) {
            if (output != null) {
                new Thread(() -> {
                    if (output != null) {
                        output.println(msg);
                        output.flush();
                    }
                }).start();
            }
        }

        @Override
        public void interrupt() {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            } finally {
                super.interrupt();
            }
        }
    }
}
