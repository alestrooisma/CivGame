package civ;

import civ.model.Civilization;
import civ.controller.Controller;
import civ.model.City;
import civ.model.Map;
import civ.model.Tile;
import civ.model.Unit;
import civ.view.CivGUI;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CivGame {

	private static Random rand = new Random(123456789);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		// Create the map
		int x = 13, y = 11;
		Tile[] tiles = new Tile[x * y];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile(new Point(i % x, i / x), randomTerrain());
		}
		Map map = new Map(0, x - 1, 0, y - 1, tiles);
		// Create the player's civilization
		Civilization netherlands = new Civilization("The Netherlands");
		// Create a city
		Point location = new Point(x/2+1, y/2+1);
		City amsterdam = new City(netherlands, "Amsterdam", location);
		amsterdam.addFood(amsterdam.getPopulation()); // To prevent starvation in the first turn
		netherlands.addCity(amsterdam);
		map.getTile(location).setCity(amsterdam);
		// Create a warrior
		location = new Point(location);
		Unit unit = new Unit(netherlands, Unit.WARRIOR, 2.5, 1, 3);
		netherlands.addUnit(unit);
		map.getTile(location).addUnit(unit);
		// Create a settler
		location = new Point(x/2+2, y/2+1);
		unit = new Unit(netherlands, Unit.SETTLER, 2.5, 0, 0);
		netherlands.addUnit(unit);
		map.getTile(location).addUnit(unit);
		
		// Create the GUI
		CivGUI gui = new CivGUI();
		try {
			gui.buildGuiAndWait();
		} catch (InterruptedException ex) {
			Logger.getLogger(CivGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(CivGame.class.getName()).log(Level.SEVERE, null, ex);
		}

		Controller controller = new Controller(map, 
				new Civilization[]{netherlands}, 
				gui, new Point2D.Double(x / 2, y / 2));
		gui.setController(controller);
		controller.run();
	}

	private static int randomTerrain() {
		switch (rand.nextInt(4)) {
			case 1:
			case 2:
				return Tile.GRASSLAND;
			case 3:
				return Tile.PLAINS;
			default:
				return Tile.WATER;
		}
	}
}