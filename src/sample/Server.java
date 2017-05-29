package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server extends Thread{

    private static final int PORT =50268;
    private static final int SO_TIMEOUT = 2000;
    private static int counter=0;
    private boolean isActive;
    private boolean toClose;

    public Server()
    {
        isActive = true;
        toClose = false;
    }

    public boolean isActive()
    {
        return isActive;
    }

    //stops the server until resumeServer method is not called
    public void stopServer()
    {
        isActive = false;
    }

    public void resumeServer()
    {
        isActive = true;
    }

    public void closeServer()
    {
        toClose = true;
    }

    private ServerSocket serverSocket = null;

    @Override
    public void run()
    {
        try {
            createServerSocekt(SO_TIMEOUT);
            runServer();

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createServerSocekt(int timeout) throws IOException
    {
            serverSocket = new ServerSocket(PORT);
    }

    private  void runServer() throws InterruptedException {
       while(true)
          runServerSocket();
    }

    private void runServerSocket()
    {
        try {
            System.out.println("client is connecting");
            Socket client = serverSocket.accept();
            System.out.println("client has connected");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            counter++;
            bufferedWriter.write(""+counter);
            bufferedWriter.close();

        }catch(SocketTimeoutException e){
            System.out.println("SocketTimeoutException");
        }catch ( IOException e){
            System.out.println("IOException");
        }
    }

//    private void sendData(Socket client) throws IOException
//    {
//
//    }
//
//    private void receiveData(Socket client) throws IOException
//    {
//
//    }

}
