package civ.view;

import civ.controller.input.BuildingListMouseHandler;
import civ.model.buildings.Building;
import civ.model.City;
import civ.model.Map;
import civ.model.buildings.ProductionBuilding;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
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
		private int rectHeight;
		private Rectangle selectButton = new Rectangle();
		private int yRect;

		public BuildingPanel(CivGUI gui, int padX, int padY) {
			super(gui, padX, padY);
			setPreferredSize(new Dimension(300, 600));
			addMouseListener(new BuildingListMouseHandler(gui.getController(), this));
		}

		@Override
		protected void renderGUI() {
			calculateValues();
			City c = gui.getController().getViewedCity();
			if (c == null) {
				return;
			}

			for (Building b : c.getBuildings()) {
				String production = b.getProduction();
				if (production == null) {
					production = "Idle";
				} else {
					production = "Producing: " + production;
				}
				
				startItem(b);
				writeln(b.getName(), boldFont);
				if (b instanceof ProductionBuilding) {
					writeln(production);
				}
				if (b.getFoodBalance() != 0) {
					write(String.format("%+d Food", b.getFoodBalance()));
				}
				if (b.getMaterialsBalance() != 0) {
					write(String.format("%+d Materials", b.getMaterialsBalance()));
				}
				writeln();
				endItem();
			}
		}

		private void startItem(Building b) {
			// Draw border
			g.drawRect(getPadX(), y, getWidth() - 2 * getPadX(), rectHeight);

			// Draw select button
			if (b instanceof ProductionBuilding) {
				g.setColor(Color.GRAY);
				g.fillRect(selectButton.x, y + selectButton.y,
						selectButton.width, selectButton.height);
				g.setColor(Color.BLACK);
				g.drawRect(selectButton.x, y + selectButton.y,
						selectButton.width, selectButton.height);
				drawStringBC("S", selectButton.x + selectButton.width / 2,
						y + selectButton.y + (selectButton.height + getStringHeight("S")) / 2 + 1);
			}

			// Set positions
			yRect = y;
			x += PADDING;
			lineX += PADDING;
		}

		private void endItem() {
			// Reset positions
			newLine(yRect + rectHeight + SPACING);
		}

		public int getRectHeight() {
			return rectHeight;
		}

		public Rectangle getSelectButton() {
			return selectButton;
		}

		private void calculateValues() {
			rectHeight = g.getFontMetrics().getHeight() * 3 + PADDING;
			int buttonSize = rectHeight / 3;
			selectButton.setBounds(
					getWidth() - getPadX() - buttonSize,
					(rectHeight - buttonSize) / 2,
					buttonSize, buttonSize);
		}

		private int getStringHeight(String str) {
			FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();
			GlyphVector gv = ((Graphics2D) g).getFont().createGlyphVector(frc, str);
			return gv.getPixelBounds(null, 0, 0).height;
		}
	}
}
