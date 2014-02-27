package civ;

import civ.controller.Controller;
import civ.model.Map;
import civ.model.Tile;
import civ.view.CivGUI;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CivGame {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		// Create the map
		Random rand = new Random(123456789);
		int x = 9, y = 9;
		Tile[] tiles = new Tile[x * y];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile(new Point(i % x, i / x), rand.nextInt(3));
		}
		Map map = new Map(0, x-1, 0, y-1, tiles);

		// Create the GUI
		CivGUI gui = new CivGUI();
		try {
			gui.buildGuiAndWait();
		} catch (InterruptedException ex) {
			Logger.getLogger(CivGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(CivGame.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		Controller controller = new Controller(map, gui, new Point2D.Double(4, 4));
		gui.setController(controller);
		controller.run();
	}
}