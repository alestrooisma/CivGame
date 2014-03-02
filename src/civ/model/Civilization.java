package civ.model;

import java.util.LinkedList;

public class Civilization {

	private String name;
	private boolean defeated = false;
	private LinkedList<City> cities = new LinkedList<City>();
	private LinkedList<Unit> units = new LinkedList<Unit>();

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

	public LinkedList<City> getCities() {
		return cities;
	}
	
	public void addCity(City city) {
		cities.add(city);
	}
	
	public void removeCity(City city) {
		cities.remove(city);
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
