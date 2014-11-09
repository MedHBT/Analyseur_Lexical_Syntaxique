import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ImgTreePanel extends JPanel{
/**
	 * 
	 */
	private static final long serialVersionUID = 489128653751145727L;
	public Image img;
	
	public void paintComponent(Graphics g){
		try {
			img = ImageIO.read(new File("treeD.png"));
			g.drawImage(img, 0, 0, this);
			//Pour une image de fond
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			//System.out.println(img.getWidth(null)+" ---- "+ img.getHeight(null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
