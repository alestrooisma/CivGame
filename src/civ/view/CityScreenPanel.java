package civ.view;

import civ.model.City;
import civ.model.Map;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CityScreenPanel extends GuiPanel {

	public CityScreenPanel(CivGUI gui) {
		super(gui, 10, 5);
		setPreferredSize(new Dimension(250, 250));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		setVisible(false);
	}

	@Override
	protected void render() {
		City c = gui.getController().getViewedCity();
		if (c != null) {
			drawCityScreen(c);
		}
	}

	private void drawCityScreen(City city) {
		Map map = gui.getController().getMap();
		String str= "" + city.getPopulation();
		if (city.getPopulation() - city.getWorkedTiles().size() > 0) {
			str += " (" + (city.getPopulation() - city.getWorkedTiles().size())
					+ " unemployed)";
		}
		// Column 1
		reset();
		write("Population:");
		write("Growth:");
		spacing(10);
		write("Production", boldFont, 50);
		write("Food:");
		write("Materials:");
		spacing(10);
		write("Stored goods", boldFont, 50);
		write("Materials:");
		
		// Column 2
		newColumn(150);
		write(str);
		write("" + city.getFood() + " / " + city.getGrowsAt());
		spacing(10);
		write();
		write(String.format("%+d (%d produced - %d eaten)",
				city.getNetFoodYield(map), city.getFoodYield(map), city.getPopulation()));
		write(String.format("%+d", city.getNetMaterialsYield(map)));
		spacing(10);
		write();
		write("" + city.getMaterials());
	}
}
