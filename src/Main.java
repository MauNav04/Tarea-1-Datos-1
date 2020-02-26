import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    BorderPane mainPane;
    VBox leftSide;
    Pane centralPane;
    ChatServer server;
    LinkedList avaList= new LinkedList();

    class ThreadDemo implements Runnable {
        Thread t;
        ThreadDemo() {
            t = new Thread(this, "Thread");
            System.out.println("Child thread: " + t);
            t.start();
        }
        public void run() {
            try {
                System.out.println("Child Thread");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("The child thread is interrupted.");
            }
            System.out.println("Exiting the child thread");
        }
    }
    public class Thread {
        public static void main(String args[]) {
            new ThreadDemo();
            try {
                System.out.println("Main Thread");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("The Main thread is interrupted");
            }
            System.out.println("Exiting the Main thread");
        }
    }

    public void initServer(){
        int port = findPort();
        ChatServer server = null;
        server = new ChatServer(port);
    }

    public int findPort(){
        int port = 0;
        for(int i = 1; i<=9999; i++){
            if(!avaList.contains(i)) {
                avaList.add(i);
                port=i;
                break;
            }
        }
        return port;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initServer();

        primaryStage.setTitle("Socket Chat");
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("Images/logo.png"));

        mainPane = new BorderPane();
        leftSide = new VBox(10);
        leftSide.setPrefSize(250,400);
        leftSide.setMaxWidth(Region.USE_PREF_SIZE);
        leftSide.setMaxHeight(Region.USE_PREF_SIZE);
        leftSide.setBackground(Background.EMPTY);
        String style = "-fx-background-color: rgba(255, 255, 255, 0.5);";
        leftSide.setStyle(style);
        mainPane.setLeft(leftSide);


        centralPane = new Pane();
        TextField messageField = new TextField();
        messageField.setPromptText("Write here");
        messageField.setPrefSize(281,25);
        messageField.setLayoutX(28);
        messageField.setLayoutY(510);

        Button sendBtn= new Button("Send");
        sendBtn.setLayoutX(333);
        sendBtn.setLayoutY(510);
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        centralPane.getChildren().addAll(sendBtn,messageField);
        mainPane.setCenter(centralPane);
        Scene mainScene = new Scene (mainPane,800,700);

        primaryStage.setScene(mainScene);

        primaryStage.show();

    }



    public static void main(String args[]){
        launch(args);
    }
}
