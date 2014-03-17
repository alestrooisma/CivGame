package civ;

import civ.model.Civilization;
import civ.controller.Controller;
import civ.mapgen.MapGenerator;
import civ.model.City;
import civ.model.Map;
import civ.model.Model;
import civ.model.Unit;
import civ.model.buildings.ProductionBuilding;
import civ.view.CivGUI;
import java.awt.Point;
import java.awt.geom.Point2D;

public class CivGame {

	public static void main(String[] args) {

		// Create the map
		int width = 128, height = 128;
		MapGenerator mapgen = new MapGenerator(width, height, 2);
		Map map = mapgen.generate();
		mapgen.display(3);

		// Create the player's civilization
		Civilization netherlands = new Civilization("The Netherlands");

		// Create a city
		Point location = new Point(width / 2, height / 2);
		City amsterdam = City.createCity(netherlands, "Amsterdam", location, map);
		// Prevent starvation in the first turn
		amsterdam.addFood(amsterdam.getPopulation());
		// Add starting buildings
		amsterdam.addBuilding(new ProductionBuilding(amsterdam, "Construction site"));
		amsterdam.addBuilding(new ProductionBuilding(amsterdam, "Training field"));

		// Create a warrior
		location = new Point(location);
		Unit.createUnit(netherlands, Unit.WARRIOR, 2.5, 1, 3, location, map);

		// Create a settler
		location = new Point(width / 2, height / 2 + 1);
		Unit.createUnit(netherlands, Unit.SETTLER, 2.5, 0, 0, location, map);

		// Create the GUI
		CivGUI gui = new CivGUI();
		Model model = new Model(map, new Civilization[]{netherlands});
		Controller controller = new Controller(model, gui,
				new Point2D.Double(width / 2, height / 2));
		gui.setController(controller);
		controller.run();
	}
}