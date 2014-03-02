/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.view;

import civ.controller.Resources;
import civ.model.City;
import civ.model.Map;
import civ.model.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author ale
 */
public class CityScreenPanel extends Panel {

	public CityScreenPanel(CivGUI gui) {
		super(gui);
		setPreferredSize(new Dimension(250, 250));
		setBackground(Color.GRAY);
		setVisible(false);
	}

	@Override
	protected void render() {
		City c = gui.getController().getViewedCity();
		if (c == null) {
			setVisible(false);
		} else {
			setVisible(true);
			drawCityScreen(gui.getController().getViewedCity());
		}
	}
	
	private void drawCityScreen(City city) {

		//TODO separate panel

		// Draw bottom bar background
//		g.setColor(Color.GRAY);
//		g.fillRect(0, getSize().height - CITY_BAR_HEIGHT, getSize().width - 1, CITY_BAR_HEIGHT - 1);
//		g.setColor(Color.BLACK);
//		g.drawRect(0, getSize().height - CITY_BAR_HEIGHT, getSize().width - 1, CITY_BAR_HEIGHT - 1);

		// Draw text on bar
		Map map = gui.getController().getMap();
		int x1 = 10;
		int x2 = x1 + 200;
		int x3 = x1 + 50;
		int y = 10;

		// Population info
		drawStringTL("Population:", x1, y);
		if (city.getPopulation() - city.getWorkedTiles().size() > 0) {
			drawStringTL("" + city.getPopulation()
					+ " (" + (city.getPopulation() - city.getWorkedTiles().size())
					+ " unemployed)", x2, y);
		} else {
			drawStringTL("" + city.getPopulation(), x2, y);
		}
		y += g.getFontMetrics().getHeight();
		drawStringTL("Growth:", x1, y);
		drawStringTL("" + city.getFood() + " / " + city.getGrowsAt(), x2, y);

		// Production info
		y += g.getFontMetrics().getHeight() * 2;
		drawStringTL("Production:", x3, y);
		y += g.getFontMetrics().getHeight();
		drawStringTL("Food:", x1, y);
		drawStringTL(String.format("%+d (%d produced - %d eaten)",
				city.getNetFoodYield(map), city.getFoodYield(map), city.getPopulation()), x2, y);
		y += g.getFontMetrics().getHeight();
		drawStringTL("Materials:", x1, y);
		drawStringTL(String.format("%+d", city.getNetMaterialsYield(map)), x2, y);

		// Warehouse info
		y += g.getFontMetrics().getHeight() * 2;
		drawStringTL("Stored goods:", x3, y);
//		y += g.getFontMetrics().getHeight();
//		drawStringTL("Food:", x1, y);
//		drawStringTL("" + city.getFood(), x2, y);
		y += g.getFontMetrics().getHeight();
		drawStringTL("Materials:", x1, y);
		drawStringTL("" + city.getMaterials(), x2, y);
	}

}
