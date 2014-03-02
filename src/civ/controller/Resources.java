package civ.controller;

import civ.model.Unit;
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
	public static BufferedImage grassland;
	public static BufferedImage plains;
	public static BufferedImage water;
	public static BufferedImage city;
	public static BufferedImage yieldbackdrop;
	public static BufferedImage[] units;

	/* Creation of static members */
	public static void loadResources() {
		//TODO placeholder implementation: fix.
		units = new BufferedImage[2];
		try {
			grassland = ImageIO.read(new File("assets/grassland.png"));
			plains = ImageIO.read(new File("assets/plains.png"));
			water = ImageIO.read(new File("assets/water.png"));
			city = ImageIO.read(new File("assets/city.png"));
			yieldbackdrop = ImageIO.read(new File("assets/yieldbackdrop.png"));
			units[Unit.SETTLER] = ImageIO.read(new File("assets/settler.png"));
			units[Unit.WARRIOR] = ImageIO.read(new File("assets/warrior.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Unable to read image files");
			System.exit(1);
		}
	}
}
