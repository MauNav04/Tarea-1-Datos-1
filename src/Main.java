import Sockets.ChatClient;
import Sockets.ChatServer;
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

import java.net.UnknownHostException;
import java.util.*;

public class Main extends Application {

    Pane mainPane;
    Scene menuScene;
    Scene messageScene;
    VBox centralPane;
    int hostingPort;

    ChatServer server;
    ChatClient client;


    public class SocketHosting implements Runnable {
        Thread t;
        SocketHosting() {
            t = new Thread(this, "Thread");
            t.start();
        }

        public void run(){

            Scanner scan = new Scanner(System.in);

            System.out.print("Select a port to host the app : ");
            String output = scan.nextLine();
            int port = Integer.parseInt(output);

            hostingPort = port ;
            server = new ChatServer(port);
        }

    }

    /**
     * Determina que los parametros escritos por el cliente sean correctos y crea un objeto Sockets.ChatClient que creará una conexión con otro servidor.
     * @param ip
     * @param port
     * @param message
     * @throws UnknownHostException
     */
    public void send(String ip, String port, String message)throws UnknownHostException {
        boolean Bport = checkPort(port);
        boolean Bmessage = checkMessage(message);
        boolean Bip = checkip(ip);

        if((Bport) && (Bmessage) && (Bip)){
            int intPort = Integer.parseInt(port);
            if(ip.equals("")){
                client = new ChatClient("", intPort, message);
            }else {
                client = new ChatClient(ip, intPort, message);
            }
        }
        else{
            System.out.println("Debe llenar los espacios de mensaje y servidor obligatoriamente. Si desea utilizar la " +
                    "ip local puede dejar el espacio en blanco");
        }
    }

    /**
     * Revisa el puerto para deterinar que no es vacío y que cumple con el largo establecido.
     * @param port escrito por el cliente
     * @return true o false
     */
    public boolean checkPort (String port){
        if(port != ""){
            int length = port.length();
            if(length < 5){
                return true;
            }
        }
        return false;
    }

    /**
     * Revisa el mensaje para determinar que nos es vacío.
     * @param message string con el mensaje
     * @return true o false
     */
    public boolean checkMessage (String message){
        if(message != ""){
            return true;
        }
        return false;
    }

    /**
     * Revisa la ip para determinar si cumple con los requerimientos
     * @param ip ip ingresada por el cliente
     * @return true o false
     */
    public boolean checkip (String ip){
        if(ip.equals("")){
            return true;
        }else {
            int length = ip.length();
            if ((length == 9)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new SocketHosting();

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
        portTextField.setPrefSize(300,66);

        Label ipLabel = new Label("IP:");
        ipLabel.setPrefSize(170,58);
        ipLabel.setFont(new Font("System",30));
        TextField ipTextField = new TextField();
        ipTextField.setPrefSize(300,66);
        HBox topHbox = new HBox();
        topHbox.setPrefSize(800,66);
        topHbox.getChildren().addAll(portLabel,portTextField,ipLabel,ipTextField);

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
                    send(ipTextField.getText(), portTextField.getText(), messageTextField.getText());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipTextField.setText("");
                messageTextField.setText("");
                primaryStage.setScene(menuScene);
                //centralPane.getChildren().add();
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
        messageScene = scene2;
        //FIN DE SCENE 2

        Button sendBtn= new Button("Nuevo Mensaje");
        sendBtn.setPrefSize(112,46);
        sendBtn.setLayoutX(333);
        sendBtn.setLayoutY(510);
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(messageScene);
            }
        });

        mainPane.getChildren().addAll(centralPane,sendBtn);
        Scene mainScene = new Scene (mainPane,344,570);
        menuScene = mainScene;


        primaryStage.setScene(mainScene);

        primaryStage.show();
    }

    public static void main(String args[]){
        launch(args);
    }
}
