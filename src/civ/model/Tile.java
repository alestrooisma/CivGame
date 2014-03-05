package civ.model;

import java.awt.Point;
import java.util.LinkedList;

public class Tile {

	// Terrains (temp)
	public static final int GRASSLAND = 0;
	public static final int PLAINS = 1;
	public static final int WATER = 2;
	public static final int[] FOOD_YIELD = {2, 1, 1};
	public static final int[] MATERIAL_YIELD = {0, 1, 0};
	// Actual tile data
	private Point position;
	private int terrain;
	private City city;
	private LinkedList<Unit> units = new LinkedList<Unit>();

	public Tile(Point position, int terrain) {
		this.position = position;
		this.terrain = terrain;
	}

	public Point getPosition() {
		return position;
	}

	public int getTerrain() {
		return terrain;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	public int getFoodYield() {
		return FOOD_YIELD[terrain]; // + improvements etc
	}
	
	public int getMaterialsYield() {
		return MATERIAL_YIELD[terrain]; // + improvements etc
	}

	public LinkedList<Unit> getUnits() {
		return units;
	}
	
	public void addUnit(Unit unit) {
		units.add(unit);
	}
	
	public void removeUnit(Unit unit) {
		units.remove(unit);
	}
}
