package FinalTest; /**
 * Created by 40095 on 12/16/14.
 */

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientFX extends Application {
    private BorderPane root;
    private Stage stage;
    private Scene scene;
    private Menu cssMenu;
    private MenuBar menuBar;
    private TextArea incoming;
    private TextField IpField, userIdField, outgoing;
    private Button sendButton, getIpButton, getUserIdButton;
    private PrintWriter writer;
    private BufferedReader reader;
    private String IP = "127.0.0.1", userId;
    private int port = 42069;
    private Text alert;
    private InputStreamReader isReader;
    private static final String PATTERN =
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private RadioMenuItem matrix, tron, fb;
    private MenuItem serverOptions;
    private ListenTask lTask;
    private Timer timer;
    private VBox center;
    private VBox usersPanel;
    private HBox serverSettingsBox;
    private ArrayList<Button> userButtons;
    private ArrayList<String> users;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        root = new BorderPane();
        scene = new Scene(root, 600, 500); //width and height of application
        stage.setScene(scene);
        stage.setTitle("Chat Application");  //text for the title bar of the window
        incoming = new TextArea();
        incoming.setEditable(false);
        IpField = new TextField();
        outgoing = new TextField();
        userIdField = new TextField("Name");
        sendButton = new Button("Send");
        getIpButton = new Button("Room #");
        getUserIdButton = new Button("Enter room");
        IpField.setPrefColumnCount(15);
        alert = new Text("Enter room #");
        timer = new Timer();
        scene.getStylesheets().removeAll();
        scene.getStylesheets().setAll("matrix.css");
        lTask = new ListenTask();
        serverSettingsBox = new HBox(15, IpField, getIpButton, userIdField, getUserIdButton);
        center = new VBox(15);
        usersPanel = new VBox();
        userButtons = new ArrayList<>();
        users = new ArrayList<>();
        initMenu();
        center.getChildren().addAll(
                menuBar,
                serverSettingsBox,
                new HBox(incoming, usersPanel),
                new HBox(15, outgoing, sendButton),
                alert
        );
        root.setCenter(center);

        outgoing.setDisable(true);
        sendButton.setDisable(true);
        userIdField.setDisable(true);
        getUserIdButton.setDisable(true);

        usersPanel.setDisable(true);
        setUIListeners();
        stage.show();

        setUpNetworking();

    }

    /**
     * Initializes the menu and puts all the menus in their correct places.*
     */
    private void initMenu() {
        cssMenu = new Menu("CSS Options");
        menuBar = new MenuBar();
        serverOptions = new MenuItem("Server Options");
        Menu fileMenu = new Menu("File");
        matrix = new RadioMenuItem("Matrix");
        tron = new RadioMenuItem("Tron");
        fb = new RadioMenuItem("fb");

        ToggleGroup CssGroup = new ToggleGroup();
        matrix.setToggleGroup(CssGroup);
        tron.setToggleGroup(CssGroup);
        fb.setToggleGroup(CssGroup);

        fileMenu.getItems().addAll(serverOptions);
        cssMenu.getItems().addAll(matrix, tron, fb);
        menuBar.getMenus().addAll(fileMenu, cssMenu);
    }

    /**
     * Adds the correct listeners to all the UI objects
     * When a button is clicked, this method makes it do stuff*
     */
    private void setUIListeners() {
        serverOptions.setOnAction(event -> {
            getServerOptions();
        });
        matrix.setOnAction(event -> {
            System.out.println("matrix mode");
            scene.getStylesheets().removeAll();
            scene.getStylesheets().setAll("matrix.css");

        });
        tron.setOnAction(event -> {
            System.out.println("tron mode");
            scene.getStylesheets().setAll("tron.css");
        });
        fb.setOnAction(event -> {
            System.out.println("Facebook mode");
            scene.getStylesheets().setAll("fb.css");
        });


        getIpButton.setOnMouseClicked(event -> {
            getIP();
        });
        getIpButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                getIP();

                if (IP != null && validate(IP)) {
                    outgoing.requestFocus();
                }
            }

        });
        IpField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("getting IP");
                getIP();

                if (IP != null && validate(IP)) {
                    outgoing.requestFocus();
                }
            }

        });
        getUserIdButton.setOnMouseClicked(event -> {
            getUserId();
        });
        getUserIdButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                getUserId();
            if (event.getCode() == KeyCode.J)
                incoming.textProperty().setValue("Hello World");
            if (event.getCode() == KeyCode.F)
                incoming.textProperty().set("Hello World!");
        });
        userIdField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER
                    ) {
                getUserId();
            }
        });
        outgoing.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
        sendButton.setOnMouseClicked(event -> {
            System.out.println(IP);
            sendMessage();
        });
        lTask.messageProperty().addListener(event -> {
            incoming.textProperty().setValue(incoming.getText() + "\n" + lTask.getMessage());
        });
    }

    /**
     * If a user wants to change their settings, this method brings back the options.*
     */
    private void getServerOptions() {
        if (center.getChildren().get(1).equals(serverSettingsBox)) {
            return;
        }
        center.getChildren().add(1, serverSettingsBox);
        getIpButton.setDisable(false);
        IpField.setDisable(false);
        outgoing.setDisable(true);
        sendButton.setDisable(true);
        userIdField.setDisable(true);
        getUserIdButton.setDisable(true);
    }

    /**
     * This method retrieves the value stored in the IP field ("Room #") and makes sure it is valid*
     */
    private void getIP() {
        if (IpField.getText() != null) {
            IP = IpField.getText();
            if (!validate(IP)) {
                alert.setText("Enter a valid room # (The IP of the server)");
                IpField.setText(null);
                IP = null;
            } else {
                IP = IpField.getText();
                alert.setText("");
                IpField.setDisable(true);
                getIpButton.setDisable(true);
                userIdField.setDisable(false);
                getUserIdButton.setDisable(false);
            }
        }
    }

    /**
     * @param ip the IP to validate
     * @return whether or not the IP is a valid address in the form XXX.XXX.XXX.XXX where X is an int 0 - 9
     */
    private boolean validate(String ip) {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * Handles the user's id field. When a value is submitted, this method is called to retrieve the value
     * This method also hides the server settings box once valid input is granted.* 
     */
    
    private void getUserId() {
        if (userIdField.getText() != null) {
            userId = userIdField.getText();
//            if (userId.length() > 3)
//                userId = userId.substring(0, 3);

            userIdField.setDisable(true);
            getUserIdButton.setDisable(true);
            outgoing.setDisable(false);
            sendButton.setDisable(false);
            outgoing.requestFocus();
            
            //Do a thing where we update our own names

            center.getChildren().removeAll(serverSettingsBox);
        }
    }

    /**
     * This method sets up the connection between the Server and the client
     * Upon a successful connection, it sends the username to the server prefaced with `
     * e.x. "`Jon"
     * to be added to the server's list of online users.
     */
    private void setUpNetworking() {
        System.out.println("Setting up networking");
        try {
            Socket sock = new Socket(IP, port);
            System.out.println("working");
            isReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(isReader);
            writer = new PrintWriter(sock.getOutputStream());
            writer.println("`" + userId);

            alert.setText("Connected");
            System.out.println("Setup Success");


            timer.schedule(new ListenTimerTask(), 0, 2000);

        } catch (IOException ex) {
            alert.setText("Server inactive");


        }
    }

    /**
     * TBH i have no idea what these two classes do. They let me run the listener and the message sender at the same 
     * * time, which is a pain with JavaFX
     * Beyond that, idk.
     * 
     * ListenTask's call() method is where the actual reading lines in happens - I think check for special messages 
     * * and or escaped shit there*
     * 
     * Might be better to look for the serialized list of onlineUsers in the ListenTimerTask but idek* *
     */
    public class ListenTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                lTask.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ListenTask extends Task {
        ListenTask() {
            updateMessage("Hello World");
        }

        protected Object call() throws Exception {
            System.out.println("Listening");
            String message = "error";
            try {
                message = reader.readLine();
                System.out.println(message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            updateMessage(message);
            return null;
        }
    }


    /**
     * This message is called when a message is sent through the UI
     * It sends the line to the server, then prepares to send a new message*
     */
    public void sendMessage() {
        writer.println(userId + ": " + outgoing.getText());
        writer.flush();
        outgoing.setText(null);
        outgoing.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

