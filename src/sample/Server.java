package sample;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
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
        }
    }

    private void runServerSocket()
    {
        try {
            System.out.println("client is connecting");
            Socket client = serverSocket.accept();
            System.out.println("client has connected");

            BufferedImage screenshot = createScreenshot();
            screenshot = resize(screenshot, 480, 270);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( screenshot, "jpg", baos );
            baos.flush();

            byte[] screenshotInByte = baos.toByteArray();
            baos.close();

            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            dos.writeInt(screenshotInByte.length);
            dos.write(screenshotInByte);
            dos.close();

        }catch(SocketTimeoutException e){
            System.out.println("SocketTimeoutException");
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
