import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 40095 on 1/19/15.
 */
public class StreamGobbler extends Thread {
    InputStream is;

    public StreamGobbler(InputStream is) {
        this.is = is;
    }

    public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }

            } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
