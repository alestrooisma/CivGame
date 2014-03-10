/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.placeholder;

import civ.controller.Controller;
import civ.model.Building;
import civ.model.City;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ale
 */
public class ConstructionSite extends Building {
	
	public static final List<String> options = Arrays.asList("Market", "Church", "Granary");
	public static final List<Integer> costs = Arrays.asList(15, 30, 20);

	public ConstructionSite(City city, String name) {
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
	public boolean isProducer() {
		return true;
	}

	@Override
	public void update(Controller controller) {
		
	}
}
