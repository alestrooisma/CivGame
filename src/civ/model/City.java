/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.model;

import civ.controller.Util;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author ale
 */
public class City {

	private String name;
	private Point position;
	private Civilization civilization;
	private int population;
	private LinkedList<Point> workedTiles = new LinkedList<Point>();
	
	//Temp
	private int food;
	private int materials;

	public City(Civilization civilization, String name, Point position) {
		this.civilization = civilization;
		this.name = name;
		this.position = position;
		this.population = 3;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Point getPosition() {
		return position;
	}

	public Civilization getCivilization() {
		return civilization;
	}

	public void setCivilization(Civilization civilization) {
		this.civilization = civilization;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public void toggleWorkedTile(Point p) {
		if (workedTiles.contains(p)) {
			removeWorkedTile(p);
		} else {
			addWorkedTile(p);
		}
	}

	public void addWorkedTile(Point p) {
		if (workedTiles.size() < population && Util.distanceSquared(position, p) <= 2) {
			workedTiles.add(p);
		}
	}

	public void removeWorkedTile(Point p) {
		workedTiles.remove(p);
	}

	public LinkedList<Point> getWorkedTiles() {
		return workedTiles;
	}

	public int getFood() {
		return food;
	}

	public int getMaterials() {
		return materials;
	}

	public int getNetFoodYield(Map map) {
		return getFoodYield(map)-population;
	}

	public int getFoodYield(Map map) {
		int yield = 0;
		for (Point p : getWorkedTiles()) {
				Tile t = map.getTile(p);
				yield += t.getFoodYield();
			}
		return yield;
	}

	public int getNetMaterialsYield(Map map) {
		return getMaterialsYield(map);
	}

	public int getMaterialsYield(Map map) {
		int yield = 0;
		for (Point p : getWorkedTiles()) {
				Tile t = map.getTile(p);
				yield += t.getMaterialsYield();
			}
		return yield;
	}

	public void addFood(int food) {
		this.food += food;
	}

	public void addMaterials(int materials) {
		this.materials += materials;
	}

	public void reduceFood(int food) {
		this.food -= food;
	}

	public void reduceMaterials(int materials) {
		this.materials -= materials;
	}

	public int getGrowsAt() {
		return Math.max(10, (population - 2) * 10); 
	}

	public void grow() {
		reduceFood(getGrowsAt());
		population++;
	}

	public void starvation() {
		food = 0;
		population--;
	}
}
