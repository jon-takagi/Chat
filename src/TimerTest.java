import javafx.concurrent.Task;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 40095 on 12/16/14.
 */
public class TimerTest {
    public static void main(String[] args) {
        TimerTest timerTest = new TimerTest();
        timerTest.go();
    }

    public void go() {

        Timer timer = new Timer();
        timer.schedule(new Beep(), 0, 2000);
    }

    class Beep extends TimerTask {
        public void run() {
            System.out.println("Starting BeepTask");
            try {
                new BeepTask().call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class BeepTask extends Task {

        @Override
        protected Object call() throws Exception {
            System.out.println("Beep");
            return null;
        }
    }
}
