import java.io.IOException;

/**
 * Created by 40095 on 1/19/15.
 */
public class GetProperty {
    public static void main(String[] args) {

        int testSshEndCode;
        String[] testSshArgs = {"ssh", "-t", "-t", "-i", System.getProperty("user.home") + "/.ssh/ChatVictim", "127.0.0.1", "exit"};
        try {

            Process testSsh = Runtime.getRuntime().exec(testSshArgs);
            testSshEndCode = testSsh.waitFor();
            System.out.println(testSshEndCode);

            if(testSshEndCode != 0) {
                System.out.println("Login not enabled");
                System.out.println("Enabling SSH");
                String[] activateSshArgs = {"osascript", "-e", "'do", "shell", "script", "\"systemsetup", "-setremotelogin", "on\"", "with", "administrator", "privliges'"};
                Process activateSsh = Runtime.getRuntime().exec(activateSshArgs);
                int activateSshEndCode = activateSsh.waitFor();
                StreamGobbler activateSshGobbler = new StreamGobbler(activateSsh.getInputStream());
                activateSshGobbler.start();
                if (activateSshEndCode == 0) {
                    System.out.println("SSH Enabled");
                }               else {
                    System.out.println(activateSshEndCode);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
