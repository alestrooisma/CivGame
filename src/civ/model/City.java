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
	private int population;
	private LinkedList<Point> workedTiles = new LinkedList<Point>();

	public City(String name, Point position) {
		this.name = name;
		this.position = position;
		this.population = 3;
	}

	public String getName() {
		return name;
	}

	public Point getPosition() {
		return position;
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
}
