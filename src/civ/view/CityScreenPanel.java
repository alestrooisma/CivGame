package civ.view;

import civ.controller.input.BuildingListMouseHandler;
import civ.model.Building;
import civ.model.City;
import civ.model.Map;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;

public class CityScreenPanel extends GuiPanel {

	private BuildingPanel buildingPanel;

	public CityScreenPanel(CivGUI gui) {
		super(gui, 10, 5);
		setPreferredSize(new Dimension(250, 250));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		setVisible(false);

		buildingPanel = new BuildingPanel(gui, 10, 9);
		add(buildingPanel, BorderLayout.LINE_END);
	}

	@Override
	protected void renderGUI() {
		Map map = gui.getController().getMap();
		City c = gui.getController().getViewedCity();
		if (c == null) {
			return;
		}

		String str = "" + c.getPopulation();
		if (c.getPopulation() - c.getWorkedTiles().size() > 0) {
			str += " (" + (c.getPopulation() - c.getWorkedTiles().size())
					+ " unemployed)";
		}

		// Column 1
		writeln("Population:");
		writeln("Growth:");
		verticalSpacing(10);
		writeln("Production", boldFont, 50);
		writeln("Food:");
		writeln("Materials:");
		verticalSpacing(10);
		writeln("Stored goods", boldFont, 50);
		writeln("Materials:");

		// Column 2
		newColumn(150);
		writeln(str);
		writeln("" + c.getFood() + " / " + c.getGrowsAt());
		verticalSpacing(10);
		writeln();
		writeln(String.format("%+d (%d produced - %d eaten)",
				c.getNetFoodYield(map), c.getFoodYield(map), c.getPopulation()));
		writeln(String.format("%+d", c.getNetMaterialsYield(map)));
		verticalSpacing(10);
		writeln();
		writeln("" + c.getMaterials());
	}

	public class BuildingPanel extends GuiPanel {

		public static final int SPACING = 10;
		public static final int PADDING = 5;
		private int rectHeight = getFontMetrics(getFont()).getHeight() * 3 + PADDING;
		private int yRect;

		public BuildingPanel(CivGUI gui, int padX, int padY) {
			super(gui, padX, padY);
			setPreferredSize(new Dimension(300, 600));
			addMouseListener(new BuildingListMouseHandler(gui.getController(), this));
		}

		@Override
		protected void renderGUI() {
			City c = gui.getController().getViewedCity();
			if (c == null) {
				return;
			}
			
			for (Building b : c.getBuildings()) {
				startRect();
				writeln(b.getName(), boldFont);
				writeln("Idle.");
				endRect();
			}
		}

		private void startRect() {
			g.drawRect(getPadX(), y, getWidth() - 2 * getPadX(), rectHeight);
			yRect = y;
			x += PADDING;
			lineX += PADDING;
		}

		private void endRect() {
			newLine(yRect + rectHeight + SPACING);
		}

		public int getRectHeight() {
			return rectHeight;
		}
	}
}
