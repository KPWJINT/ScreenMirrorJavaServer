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

    private ServerSocket serverSocket = null;

    @Override
    public void run()
    {
        createServerSocekt(SO_TIMEOUT);

        runServerSocket();

        closeServerSocket();
    }

    private void createServerSocekt(int timeout)
    {
        try
        {
            System.out.print("server starting ");
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(timeout);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private  void runServerSocket()
    {
        while(true)
        {
            try {
                System.out.println("client is connecting");
                Socket client = serverSocket.accept();
                System.out.println("client has connected");
                Thread.sleep(5000);

                sendData(client);

                receiveData(client);

            }catch(SocketTimeoutException e){
                System.out.println("SocketTimeoutException");
            }catch(InterruptedException e) {
                System.out.println("InterruptedException");
            }catch ( IOException e){
                System.out.println("IOException");
            }
        }
    }

    private void sendData(Socket client) throws IOException
    {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        bufferedWriter.write("hi client!");
        bufferedWriter.close();
    }

    private void receiveData(Socket client) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println("message from client "+ bufferedReader.readLine());
        bufferedReader.close();
    }

    private void closeServerSocket()
    {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
