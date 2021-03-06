package applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
public class PasteScreenApplet extends Applet {    

    // Window and applet stats  
    protected int windowWidth = 280;
    protected int windowHeight = 200;   
    public static final int updateDelay = 20; 
    
    protected Image image;
    protected BufferedImage bImage;

    public PasteScreenApplet() {   
    	
        super();
        
        addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
		        BufferedImage buf = null;
		        try {
		            buf = getImageFromClipboard();
		        } catch (Exception ex) {
		            System.out.println(ex.getMessage());
		        }
		        
		        image = buf.getScaledInstance(windowWidth, windowHeight, Image.SCALE_SMOOTH);
		        bImage = buf;
		        
		        SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		                setVisible(true);
		                repaint();
		            }
		        });
			}
		});
    }
    
    public String getImageDataString() {

        byte[] imageInByte = null;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write( bImage, "jpg", baos );
	        baos.flush();
	        imageInByte = baos.toByteArray();
	        baos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        // get the base 64 string
        Base64.Encoder b64enc = Base64.getEncoder();
        
        Base64.Encoder b64nowrap = b64enc.withoutPadding();
        String imageString = b64nowrap.encodeToString(imageInByte);
        
        return imageString;
    }

    /**
     * Get an image off the system clipboard.
     * 
     * @return Returns an Image if successful; otherwise returns null.
     */
    protected BufferedImage getImageFromClipboard() {

    	Clipboard cb = (Clipboard) Toolkit.getDefaultToolkit().getSystemClipboard();

    	if (cb != null)
    	{
    		try
    		{
    			BufferedImage image = (BufferedImage)cb.getData(DataFlavor.imageFlavor);
    			return image;
    		}
    		catch (UnsupportedFlavorException e)
    		{
    			System.out.println("No suitable data in clipboard");
    		} catch (IOException e) {
    			System.out.println("Can't read clipboard data");
			}
    	}
    	
    	return null;
    }

    public void paint(Graphics g) {
        if (image == null) {
            System.out.println("image null");
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.setColor(Color.BLACK);
            g.drawString("Zwischenablage durch Klicken einf�gen...", 20, 94);
        } else {
            g.drawImage(image, 0, 0, null);
        }
    }
}