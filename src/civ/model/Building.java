/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.model;

import java.util.List;

/**
 *
 * @author ale
 */
public class Building {

	private City city;
	private String name;
	private String production;

	public Building(City city, String name) {
		this.city = city;
		this.name = name;
	}

	public City getCity() {
		return city;
	}

	public String getName() {
		return name;
	}
	
	public boolean isProducer() {
		return false;
	}
	
	public List<String> getProductionOptions() {
		return null;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}
}
