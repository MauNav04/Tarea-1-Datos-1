import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.*;

public class Main extends Application {

    Pane mainPane;
    VBox centralPane;

    ChatServer server;
    ChatClient client;


    LinkedList avaList= new LinkedList();

    public class SubThread implements Runnable {
        Thread t;
        SubThread() {
            t = new Thread(this, "Thread");
            t.start();
        }

        public void run() {
            int port = findPort();
            server = new ChatServer(port);
        }

        public int findPort(){
            boolean portFree = false;
            int portNr = 0;
            while(!portFree){
                try (var ignored = new ServerSocket(portNr)) {
                    portFree = true;
                } catch (IOException e) {
                    System.out.println("IOException: Finding another port...");

                portFree = true;
                }

            }

        }
    }

    public void send(String port, String message)throws UnknownHostException {
        boolean Bport = checkPort(port);
        boolean Bmessage = checkMessage(message);

        if((Bport) && (Bmessage)){
            int i = Integer.parseInt(port);
            client = new ChatClient(InetAddress.getLocalHost(), i, message);
        }
    }


    public boolean checkPort (String port){
        if(port != ""){
            int length = port.length();
            if(length < 5){
                return true;
            }
        }
        return false;
    }

    public boolean checkMessage (String message){
        if(message != ""){
            return true;
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new SubThread();

        primaryStage.setTitle("Socket Chat");
        primaryStage.setWidth(800);
        primaryStage.setHeight(650);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("Images/logo.png"));

        mainPane = new Pane();
        mainPane.setPrefSize(800,650);

        centralPane = new VBox(10);
        centralPane.setPrefSize(800,560);
        centralPane.setMaxWidth(Region.USE_PREF_SIZE);
        centralPane.setMaxHeight(Region.USE_PREF_SIZE);
        centralPane.setBackground(Background.EMPTY);
        String style = "-fx-background-color: rgba(255, 255, 255, 0.5);";
        centralPane.setStyle(style);
        centralPane.setLayoutX(0);
        centralPane.setLayoutY(0);

        //SCENE 2
        Label portLabel = new Label("Puerto:");
        portLabel.setPrefSize(170,58);
        portLabel.setFont(new Font("System",30));
        TextField portTextField = new TextField();
        portTextField.setPrefSize(478,66);
        HBox topHbox = new HBox();
        topHbox.setPrefSize(800,66);
        topHbox.getChildren().addAll(portLabel,portTextField);

        Pane fillingPane = new Pane();
        fillingPane.setPrefSize(800,103);

        TextField messageTextField = new TextField();
        messageTextField.setFont(new Font("System",18));
        messageTextField.setPromptText("Escriba su mensaje aqui");
        messageTextField.setAlignment(Pos.TOP_LEFT);
        messageTextField.setPrefSize(691,389);

        Pane fillingPane2 = new Pane();
        fillingPane2.setPrefSize(671,98);
        Button sendButton = new Button("Enviar");
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    send(portTextField.getText(), messageTextField.getText());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
        sendButton.setFont(new Font("System",20));
        sendButton.setPrefSize(103,60);
        HBox bottomHbox = new HBox();
        bottomHbox.setPrefSize(200,100);
        bottomHbox.getChildren().addAll(fillingPane2,sendButton);


        VBox layout1 = new VBox();
        layout1.setPrefSize(800,650);
        layout1.getChildren().addAll(topHbox,fillingPane,messageTextField,bottomHbox);
        Scene scene2 = new Scene(layout1,800,650);
        //FIN DE SCENE 2

        Button sendBtn= new Button("Nuevo Mensaje");
        sendBtn.setPrefSize(112,46);
        sendBtn.setLayoutX(333);
        sendBtn.setLayoutY(510);
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(scene2);
            }
        });

        mainPane.getChildren().addAll(centralPane,sendBtn);
        Scene mainScene = new Scene (mainPane,344,570);


        primaryStage.setScene(mainScene);

        primaryStage.show();
    }

    public static void main(String args[]){
        launch(args);
    }
}
