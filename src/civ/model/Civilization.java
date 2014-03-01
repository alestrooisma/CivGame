/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.model;

import civ.model.City;
import java.util.LinkedList;

/**
 *
 * @author ale
 */
public class Civilization {

	private String name;
	private boolean defeated = false;
	private LinkedList<City> cities = new LinkedList<City>();

	public Civilization(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isDefeated() {
		return defeated;
	}

	public void setDefeated(boolean defeated) {
		this.defeated = defeated;
	}
	
	public void addCity(City city) {
		cities.add(city);
	}
	
	public void removeCity(City city) {
		cities.remove(city);
	}
	
}
