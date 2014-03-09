/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.placeholder;

import civ.model.Building;
import civ.model.City;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ale
 */
public class TrainingField extends Building {
	
	public static final List<String> options = Arrays.asList("Settler", "Warrior");

	public TrainingField(City city, String name) {
		super(city, name);
	}

	@Override
	public List<String> getProductionOptions() {
		return options;
	}

	@Override
	public boolean isProducer() {
		return true;
	}
}
