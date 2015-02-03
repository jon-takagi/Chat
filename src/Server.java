/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jon
 */

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server {

    public static final int portNo = 42069;
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
            clientOutputStreams.add(new PrintWriter("myfile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ServerSocket serverSock = new ServerSocket(portNo);
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
//        System.out.println("There are " + clientOutputStreams.size() + " clients online");
        for (int i = 0; i < clientOutputStreams.size(); i++) {
            try {
                PrintWriter writer = clientOutputStreams.get(i);
                writer.println(message);
//                System.out.println(message);
                writer.flush();
            } catch (Exception ex) {

            }
        }
        System.out.println(message);
    }
}
