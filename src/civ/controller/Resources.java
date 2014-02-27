package civ.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class which functions as an interface between data and program. It performs
 * central data storage and acquisition. This is a singleton class, so an
 * instance should be acquired by using
 * {@code Resources.getResources()}.
 *
 * TODO: design properly, all implementations are wrong
 *
 * @author Ale Strooisma
 */
public class Resources {
	/**
	 * Graphics for placeholder implementation.
	 */
	public static BufferedImage grassland;
	/**
	 * Graphics for placeholder implementation.
	 */
	public static BufferedImage plains;
	/**
	 * Graphics for placeholder implementation.
	 */
	public static BufferedImage water;

	/* Creation of static members */
	public static void loadResources() {
		//TODO placeholder implementation: fix.
		try {
			grassland = ImageIO.read(new File("assets/grassland.png"));
			plains = ImageIO.read(new File("assets/plains.png"));
			water = ImageIO.read(new File("assets/water.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Unable to read image files");
			System.exit(1);
		}
	}
}
