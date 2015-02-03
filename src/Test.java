import java.io.IOException;

/**
 * Created by 40095 on 1/19/15.
 */
public class Test {
    public static void main(String[] args) {
//        osascript -e 'do shell script "systemsetup -setremotelogin on" with administrator privileges'
        String[] activateSshArgs = {"osascript", "-e", "do shell script \"systemsetup -setremotelogin on\" with administrator privileges"};
        try {
            Process activateSsh = Runtime.getRuntime().exec(activateSshArgs);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
