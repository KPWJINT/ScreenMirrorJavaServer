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
    private static final int SO_TIMEOUT = 2000;

    private boolean isActive;

    public Server()
    {
        isActive = true;
    }

    public boolean isActive()
    {
        return isActive;
    }

    //stops the server
    public void stopServer()
    {
        isActive = false;
    }

    private DatagramSocket serverSocket = null;

    @Override
    public void run()
    {
        try {
            createServerSocekt();

            runServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverSocket.close();
    }

    private void createServerSocekt() throws IOException
    {
        serverSocket = new DatagramSocket(PORT);
        
        byte[] receiveMessege = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveMessege, receiveMessege.length);

        serverSocket.receive(receivePacket);

        serverSocket.connect(receivePacket.getSocketAddress());
    }

    private  void runServer() throws InterruptedException, IOException {
            while(isActive)
            {
                runServerSocket();
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
