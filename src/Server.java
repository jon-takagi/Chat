/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jon
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server {

    ArrayList<PrintWriter> clientOutputStreams;

    public class ClientHandler implements Runnable {

        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {

            }
        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    tellEveryone(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Server online");
        try {
            System.out.println(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        new Server().go();
    }

    public void go() {
        clientOutputStreams = new ArrayList<PrintWriter>();
        try {
            ServerSocket serverSock = new ServerSocket(42069);
            while (true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got connection");
            }
        } catch (IOException ex) {

        }
    }

    public void tellEveryone(String message) {
        for (int i = 0; i < clientOutputStreams.size(); i++) {
            try {
                PrintWriter writer = clientOutputStreams.get(i);
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {

            }
        }
        System.out.println(message);
    }
}
