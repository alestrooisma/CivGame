package civ;

import civ.model.Civilization;
import civ.controller.Controller;
import civ.model.City;
import civ.model.Map;
import civ.model.Model;
import civ.model.Tile;
import civ.model.Unit;
import civ.model.buildings.ProductionBuilding;
import civ.placeholder.ConstructionSite;
import civ.placeholder.TrainingField;
import civ.view.CivGUI;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

public class CivGame {

	private static Random rand = new Random(12345678);

	public static void main(String[] args) {

		// Create the map
		int x = 25, y = 25;
		Tile[] tiles = new Tile[x * y];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile(new Point(i % x, i / x), randomTerrain());
		}
		Map map = new Map(0, x - 1, 0, y - 1, tiles);
		// Create the player's civilization
		Civilization netherlands = new Civilization("The Netherlands");
		// Create a city
		Point location = new Point(x / 2, y / 2);
		City amsterdam = City.createCity(netherlands, "Amsterdam", location, map);
		amsterdam.addFood(amsterdam.getPopulation()); // To prevent starvation in the first turn
		// Add starting buildings
		amsterdam.addBuilding(new ProductionBuilding(amsterdam, "Construction site"));
		amsterdam.addBuilding(new ProductionBuilding(amsterdam, "Training field"));
		// Create a warrior
		location = new Point(location);
		Unit unit = Unit.createUnit(netherlands, Unit.WARRIOR, 2.5, 1, 3, location, map);
		// Create a settler
		location = new Point(x / 2, y / 2 + 1);
		unit = Unit.createUnit(netherlands, Unit.SETTLER, 2.5, 0, 0, location, map);

		// Create the GUI
		CivGUI gui = new CivGUI();
		Model model = new Model(map, new Civilization[]{netherlands});
		Controller controller = new Controller(model, gui, new Point2D.Double(x / 2, y / 2));
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