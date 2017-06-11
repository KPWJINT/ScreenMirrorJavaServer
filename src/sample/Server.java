package sample;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

/**
 * Created by Sobczak on 22.05.2017.
 */
public class Server extends Thread{

    private static final int PORT =81;

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

    private DatagramSocket serverSocket = null;

    @Override
    public void run()
    {
        try {
            serverSocket = new DatagramSocket(PORT);

            runServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverSocket.close();
    }

    private  void runServer() throws InterruptedException, IOException
    {
        while(!toClose)
        {
            while(!isActive())
                Thread.sleep(1000);
            connectToClient();
            while(isActive)
            {
                runServerSocket();
            }
        }
    }

    private void runServerSocket() throws IOException
    {
        BufferedImage screenshot = createScreenshot();
        screenshot = resize(screenshot, 480, 270);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( screenshot, "jpg", baos );
        baos.flush();

        byte[] screenshotInByte = baos.toByteArray();
        System.out.println(screenshotInByte.length);
        baos.close();

        DatagramPacket sendPacket = new DatagramPacket(screenshotInByte, screenshotInByte.length);
        serverSocket.send(sendPacket);
    }

    private void connectToClient() throws IOException
    {
        byte[] receiveMessege = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveMessege, receiveMessege.length);

        serverSocket.receive(receivePacket);

        serverSocket.connect(receivePacket.getSocketAddress());
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

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        double proportion_w = w/newW;
        double proportion_h = h/newH;

        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, (int)(w/(proportion_w-1)), (int)(h/(proportion_h-1)), 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }
}
