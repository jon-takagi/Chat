import java.io.*;
import java.net.*;

public class AdviceServer {
    String[] advice = {"Success is 99% perspiration, 1% inspiration", "Compound interest is the most powerful force in the universe","It is magnificent, but it is not war", "If you can fill the unforgiving minute with 60 seconds worth of distance run", "Theirs but do and die"};

    public void go() {
        System.out.println("Server Active");
        try {
            ServerSocket serverSock = new ServerSocket(42069);
            while(true) {
                Socket sock = serverSock.accept();
                PrintWriter writer = new PrintWriter(sock.getOutputStream());
                String message = advice[(int)(Math.random() * advice.length)];
                writer.println(message);
                System.out.println(message);
                writer.close();
            }
        } catch(IOException ex) {
        }
    }
    public static void main(String[] args) {
        new AdviceServer().go();
    }
}
