/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.model.buildings;

import civ.controller.Controller;
import civ.model.City;
import civ.model.ModelElement;
import java.util.List;

/**
 *
 * @author ale
 */
public class Building implements ModelElement {

	private City city;
	private String name;
	private int foodBalance;
	private int materialsBalance;

	public Building(City city, String name) {
		this(city, name, 0, 0);
	}

	public Building(City city, String name, int foodBalance, int materialsBalance) {
		this.city = city;
		this.name = name;
		this.foodBalance = foodBalance;
		this.materialsBalance = materialsBalance;
	}

	public City getCity() {
		return city;
	}

	public String getName() {
		return name;
	}
	
	public List<String> getProductionOptions() {
		return null;
	}

	public String getProduction() {
		return null;
	}

	public void setProduction(String production) {
	}
	
	public int getFoodBalance() {
		return foodBalance;
	}
	
	public int getMaterialsBalance() {
		return materialsBalance;
	}

	public void setFoodBalance(int foodBalance) {
		this.foodBalance = foodBalance;
	}

	public void setMaterialsBalance(int materialsBalance) {
		this.materialsBalance = materialsBalance;
	}
	
	@Override
	public void update(Controller controller) {
//		city.addFood(getFoodBalance());
//		city.addMaterials(getMaterialsBalance());
	}
}
