package civ.model;

import java.awt.Point;

public class Tile {

	public static final int GRASSLAND = 0;
	public static final int PLAINS = 1;
	public static final int WATER = 2;
	private Point position;
	private int terrain;

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
}
