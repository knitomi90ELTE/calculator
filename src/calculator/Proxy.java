package calculator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Proxy {

    public static void main(String[] args) {
        try {
            /*if (args.length != 3) {
                throw new IllegalArgumentException("insuficient arguments");
            }*/
            // and the local port that we listen for connections on
            String host = "localhost";//args[0];
            int remoteport = 3333;//Integer.parseInt(args[1]);
            int localport = 3334;//Integer.parseInt(args[2]);
            // Print a start-up message
            System.out.println("Starting proxy for " + host + ":" + remoteport + " on port " + localport);
            ServerSocket server = new ServerSocket(localport);
            while (true) {
                new ThreadProxy(server.accept(), host, remoteport);
            }
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java ProxyMultiThread " + "<host> <remoteport> <localport>");
        }
    }
}

class ThreadProxy extends Thread {

    private Socket sClient;
    private final String SERVER_URL;
    private final int SERVER_PORT;

    ThreadProxy(Socket sClient, String ServerUrl, int ServerPort) {
        this.SERVER_URL = ServerUrl;
        this.SERVER_PORT = ServerPort;
        this.sClient = sClient;
        this.start();
    }

    @Override
    public void run() {
        try {
            Scanner inFromClient = new Scanner(sClient.getInputStream());
            PrintWriter outToClient = new PrintWriter(sClient.getOutputStream(), true);
            Socket  client = null,
                    server = null;
            // connects a socket to the server

            server = new Socket(SERVER_URL, SERVER_PORT);

            // a new thread to manage streams from server to client (DOWNLOAD)
            Scanner inFromServer = new Scanner(server.getInputStream());
            PrintWriter outToServer = new PrintWriter(server.getOutputStream(), true);
            // a new thread for uploading to the server

            new Thread() {
                public void run() {
                    String string_read = inFromClient.nextLine();
                    System.out.println(string_read);
                    outToServer.println(string_read);
                    
                }
            }.start();
            // current thread manages streams from server to client (DOWNLOAD)
            new Thread() {
                public void run() {
                    String reply = inFromServer.nextLine();
                    System.out.println(reply);
                    outToClient.println(reply);
                }
            }.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
