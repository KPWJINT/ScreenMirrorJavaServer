package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main extends Application {

    private static final int PORT =50268;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));

//        primaryStage.show();

        ServerSocket serverSocket = null;

        try {
            System.out.print("server starting ");
            serverSocket = new ServerSocket(PORT);
            System.out.println("client is connecting");
            Socket client = serverSocket.accept();            //zacznie wykonywac dalej dopiero jak znajdzie!
            System.out.println("client has connected");

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            bufferedWriter.write("hi client!");
//          bufferedWriter.flush();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("message from client "+ bufferedReader.readLine());

            bufferedWriter.close();
            bufferedReader.close();
            serverSocket.close();
        } catch( IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}