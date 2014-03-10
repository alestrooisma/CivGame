/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.model.buildings;

import civ.controller.Controller;
import civ.model.City;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ale
 */
public class ProductionBuilding extends Building {

	public static final List<String> options = Arrays.asList("Market", "Church", "Granary");
	public static final List<Integer> costs = Arrays.asList(15, 30, 20);
	private String production;
	private int materials = 0;

	public ProductionBuilding(City city, String name) {
		super(city, name);
	}

	@Override
	public List<String> getProductionOptions() {
		LinkedList<String> list = new LinkedList<String>(options);
		for (Building b : getCity().getBuildings()) {
			if (list.contains(b.getName())) {
				list.remove(b.getName());
			}
		}
		return list;
	}

	@Override
	public String getProduction() {
		return production;
	}

	@Override
	public void setProduction(String production) {
		this.production = production;
		if (production == null) {
			setMaterialsBalance(0);
		} else {
			setMaterialsBalance(-3);
		}
	}

	@Override
	public void update(Controller controller) {
		if (getProduction() != null) {
			
			int consumption;
			consumption = -getMaterialsBalance();
			if (getCity().getMaterials() < consumption) {
				consumption = getCity().getMaterials();
			}

			getCity().reduceMaterials(consumption);
			materials += consumption;

			int cost = costs.get(options.indexOf(getProduction()));
			if (materials >= cost) {
				materials -= cost;
				getCity().addBuilding(new Building(getCity(), getProduction(), 1, 0));
				setProduction(null);
			}
		}
	}
}
