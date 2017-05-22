package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Sobczak on 22.05.2017.
 */
public class Server extends Thread{

    private static final int PORT =50268;
    private static final int SO_TIMEOUT = 2000;

    @Override
    public void run()
    {
        ServerSocket serverSocket = null;
        try
        {
            System.out.print("server starting ");
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(SO_TIMEOUT);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        while(true)
        {
            try {

                System.out.println("client is connecting");
                Socket client = serverSocket.accept();            //zacznie wykonywac dalej dopiero jak znajdzie!
                System.out.println("client has connected");
                Thread.sleep(5000);

    //            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    //            bufferedWriter.write("hi client!");
    ////          bufferedWriter.flush();
    //
    //            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    //            System.out.println("message from client "+ bufferedReader.readLine());
    //
    //            bufferedWriter.close();
    //            bufferedReader.close();
//                serverSocket.close();
            }catch(SocketTimeoutException e){
                System.out.println("SocketTimeoutException");
            }catch(InterruptedException e) {
                System.out.println("InterruptedException");
            }catch ( IOException e){
                e.printStackTrace();
            }
        }
    }
}
