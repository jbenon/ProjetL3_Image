package code;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Codes provenant et inspiré d'internet
 * 
 * @author Roxane Cellier
 * @version 1.0
 *
 */
public class Internet {
	
	/**
	* Charge l'image dont on a le path dans une BufferedImage (Code du prof)
	* 
	* @param s le chemin de l'image dans l'ordinateur
	* @return le BufferedImage contenant l'image chargée
	*/
	public static BufferedImage chargerImage(String s) {
		File file = new File(s);
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return img;
	}

	
	/**
	 * Permet d'afficher une image (Code du prof)
	 * 
	 * @param image l'image à afficher
	 * @throws IOException si l'image est null
	 */
	public static void afficherImage(BufferedImage image) throws IOException {
	      JFrame frame = new JFrame(); 

	      ImageIcon icon = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(600, 800, Image.SCALE_DEFAULT));
	      
	      frame.getContentPane().add(new JLabel(icon));
	      frame.pack(); 
	      frame.setVisible(true);
	 }

	
	public static BufferedImage rotateClockwise90(BufferedImage src) {
	    int width = src.getWidth();
	    int height = src.getHeight();

	    BufferedImage dest = new BufferedImage(height, width, src.getType());

	    Graphics2D graphics2D = dest.createGraphics();
	    graphics2D.translate((height - width) / 2, (height - width) / 2);
	    graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
	    graphics2D.drawRenderedImage(src, null);

	    return dest;
	}


}
