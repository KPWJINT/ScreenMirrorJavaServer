package screen_mirror;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by Sobczak on 22.05.2017.
 */
public class Server extends Thread{

    private static final int PORT =50243;
    private static final int SO_TIMEOUT =10;
    private static final int SCREENSHOT_W = 424;
    private static final int SCREENSHOT_H = 220;

    private DataOutputStream dos;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createServerSocekt(int timeout) throws Exception
    {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(timeout);
    }

    private  void runServer() throws IOException{
        while (isActive)
        {
            try {
                Socket client = serverSocket.accept();
                dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

                BufferedImage screenshot = createScreenshot();
                screenshot = resize(screenshot, SCREENSHOT_W, SCREENSHOT_H);
                sendScreenshot(screenshot);
                dos.close();

            }catch(SocketTimeoutException e){
                System.out.println("SocketTimeoutException");
            } catch(SocketException e){
                System.out.println("SocketException");
            }catch(IOException e){
                System.out.println("IOException");
            }
        }

    }

    private void sendScreenshot(BufferedImage screenshot) throws IOException
    {
            byte[] screenshotInByte = toByteArray(screenshot);
            dos.flush();
            dos.writeInt(screenshotInByte.length);
            dos.write(screenshotInByte);

    }

    private BufferedImage createScreenshot()
    {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage captureImage = null;
        try {
            captureImage = new Robot().createScreenCapture(screenRect);
        } catch (AWTException e) {
            System.out.println("createScreenshot - AWTException");
        }

        return captureImage;
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        BufferedImage dimg = null;
        if(img != null)
        {
            int w = img.getWidth();
            int h = img.getHeight();

            dimg = new BufferedImage(newW, newH, img.getType());
            Graphics2D g = dimg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
            g.dispose();
        }
        return dimg;
    }

    private byte[] toByteArray(BufferedImage screenshot) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( screenshot, "jpg", baos );
        baos.flush();

        byte[] screenshotInByte = baos.toByteArray();
        baos.close();

        return screenshotInByte;
    }
}
