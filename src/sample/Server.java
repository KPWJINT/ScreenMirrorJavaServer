package sample;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Sobczak on 22.05.2017.
 */
public class Server extends Thread{

    private static final int PORT =81;
    private static final int SO_TIMEOUT = 2000;

    private boolean isActive;
    private boolean toClose;

    private int counter = 0;

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
            serverSocket.setSoTimeout(timeout);
    }

    private  void runServer() throws InterruptedException {
        while (!toClose)
        {
            while(isActive)
            {
                runServerSocket();
            }
            Thread.sleep(2000);
        }
    }

    private void runServerSocket()
    {
        try {
            System.out.println("client is connecting");
            Socket client = serverSocket.accept();
            System.out.println("client has connected");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( createScreenshot(), "jpg", baos );
            baos.flush();
            byte[] screenshotInByte = baos.toByteArray();
            baos.close();

            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            dos.writeInt(screenshotInByte.length); // write length of the message
            dos.write(screenshotInByte);           // write the message
            dos.close();
//
//            Thread.sleep(2000); //time between messages

        }catch(SocketTimeoutException e){
            System.out.println("SocketTimeoutException");
//        }catch(InterruptedException e) {
//            System.out.println("InterruptedException");
        }catch ( IOException e){
            System.out.println("IOException");
        }
    }

    private BufferedImage createScreenshot()
    {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage captureImage = null;
        try {
            captureImage = new Robot().createScreenCapture(screenRect);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        return captureImage;
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
