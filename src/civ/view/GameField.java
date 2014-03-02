package civ.view;

import civ.controller.Controller;
import civ.model.Map;
import civ.model.Tile;
import civ.controller.Resources;
import civ.model.City;
import civ.model.Unit;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

/**
 * A panel on which the map is drawn.
 *
 * @author ale
 */
public class GameField extends JPanel {

	/**
	 * The width and height of a tile. TODO: where to define size? Probably
	 * (partially) a settings thing.
	 */
	public static final int TILE_SIZE = 100;
	public static final int CITY_BAR_HEIGHT = 250;
	public static final Point ZERO = new Point();
	private CivGUI gui;
	private long last = System.nanoTime();
	private double framerate;
	private double[] framerates = new double[60];
	private int n;
	private Point offset;
	private int v, w;
	private Graphics g;
	private Font normalFont;
	private Font boldFont;

	/**
	 * Creates the {@code GameField} component. Requires a GUI to acquire data
	 * through.
	 *
	 * @param gui the GUI this component is part of
	 */
	public GameField(CivGUI gui) {
		super();
		this.gui = gui;

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.g = g;

		normalFont = g.getFont();
		boldFont = normalFont.deriveFont(Font.BOLD);

		offset = worldToWindow(ZERO);
		City c = gui.getController().getViewedCity();

		drawMap();
		drawGeneralHUD();
		if (c == null) {
			drawWorldHUD();
		} else {
			drawCityScreen(c);
		}
	}

	private void drawMap() {
		// x and y are coordinates on the grid
		// v and w are coordinates on this panel

		Map map = gui.getController().getMap();
		Point2D pos = gui.getController().getCameraPosition();

		int vOffset = -offset.x;
		int wOffset = -offset.y;

		int xMin = Math.max(map.getMinX(), vOffset / TILE_SIZE);
		int yMin = Math.max(map.getMinY(), wOffset / TILE_SIZE);
		int xMax = Math.min(map.getMaxX(),
				(int) Math.ceil((double) (vOffset + getSize().width) / TILE_SIZE - 1));
		int yMax = Math.min(map.getMaxY(),
				(int) Math.ceil((double) (wOffset + getSize().height) / TILE_SIZE - 1));

		int x, y;
		Tile tile;
		for (y = yMin; y <= yMax; y++) {
			setDrawTileY(y);
			for (x = xMin; x <= xMax; x++) {
				setDrawTileX(x);
				tile = map.getTile(x, y);
				if (tile != null) {
					drawTile(tile);
				}
			}
		}
	}

	private void drawGeneralHUD() {
		// New turn message
		long dt = System.currentTimeMillis() - gui.getController().getTurnStartTime();
		float delay = 0.5f;
		float fade = 1f;
		if (dt < (delay + fade) * 1000) {
			if (dt < delay * 1000) {
				g.setColor(Color.BLACK);
			} else {
				float alpha = (delay + fade) - ((float) dt / (fade * 1000));
				g.setColor(new Color(0, 0, 0, alpha));
			}
			g.setFont(boldFont.deriveFont(50f));
			drawStringBC(gui.getController().getCurrentCivilization().getName(), getWidth() / 2, 250);
			g.setFont(normalFont.deriveFont(30f));
			drawStringTC("Turn " + gui.getController().getTurn(), getWidth() / 2, 250);
		}

		// FPS
		g.setColor(Color.RED);
		g.setFont(boldFont);
		writeFrameRate();
		writeMode();
	}

	private void drawWorldHUD() {
		// Status message
		g.setColor(Color.RED);
		g.setFont(boldFont);
		drawStringBC(gui.getController().getStatus(), getWidth() / 2, getHeight() - 50);
	}

	private void drawCityScreen(City city) {

		//TODO separate panel

		// Draw bottom bar background
		g.setColor(Color.GRAY);
		g.fillRect(0, getSize().height - CITY_BAR_HEIGHT, getSize().width - 1, CITY_BAR_HEIGHT - 1);
		g.setColor(Color.BLACK);
		g.drawRect(0, getSize().height - CITY_BAR_HEIGHT, getSize().width - 1, CITY_BAR_HEIGHT - 1);

		// Draw text on bar
		Map map = gui.getController().getMap();
		int x1 = 10;
		int x2 = x1 + 200;
		int x3 = x1 + 50;
		int y = getHeight() - CITY_BAR_HEIGHT + 10;

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
		y += g.getFontMetrics().getHeight();
//		drawStringTL("Food:", x1, y);
//		drawStringTL("" + city.getFood(), x2, y);
		y += g.getFontMetrics().getHeight();
		drawStringTL("Materials:", x1, y);
		drawStringTL("" + city.getMaterials(), x2, y);

		// Mark worked tiles
		for (Point p : city.getWorkedTiles()) {
			setDrawTile(p);
			drawImage(Resources.yieldbackdrop);
			Tile t = map.getTile(p);
			g.setColor(Color.GREEN);
			drawStringBC("" + t.getFoodYield(),
					(int) (v + 0.35 * TILE_SIZE), (int) (w + 0.84 * TILE_SIZE));
			g.setColor(Color.BLUE);
			drawStringBC("" + t.getMaterialsYield(),
					(int) (v + 0.65 * TILE_SIZE), (int) (w + 0.84 * TILE_SIZE));
		}

	}

	private void writeFrameRate() {
		calculateFrameRate();
		g.drawString(String.format("%.0f", framerate), 0, g.getFont().getSize());
	}

	private void writeMode() {
		if (gui.getController().getMode() == Controller.BUILD) {
			g.drawString("BUILD MODE", (int) getSize().getWidth() - 100, g.getFont().getSize() + 10);
		}
	}

	private void drawTile(Tile tile) {

		// Draw terrain background
		switch (tile.getTerrain()) {
			case Tile.GRASSLAND:
				drawImage(Resources.grassland);
				break;
			case Tile.PLAINS:
				drawImage(Resources.plains);
				break;
			case Tile.WATER:
				drawImage(Resources.water);
				break;
		}

		// Draw city
		if (tile.getCity() != null) {
			drawImage(Resources.city);
			drawStringBC(tile.getCity().getName() + " (" + tile.getCity().getPopulation() + ")", v + TILE_SIZE / 2, w + TILE_SIZE - 10);
		}

		// Draw units
		for (Unit u : tile.getUnits()) {
			drawImage(Resources.units[u.getType()]);
		}
		
//		Point p = worldToWindow(tile.getPosition());
//		drawStringTL("" + Util.distanceSquared(ZERO, tile.getPosition())
//				+ " / " + String.format("%.2f", Util.distance(ZERO, tile.getPosition())),
//				p.x, p.y);

	}

	private void drawImage(Image img) {
		g.drawImage(img, v, w, null);
	}

	private void setDrawTile(Point p) {
		setDrawTile(p.x, p.y);
	}

	private void setDrawTile(int x, int y) {
		setDrawTileX(x);
		setDrawTileY(y);
	}

	private void setDrawTileX(int x) {
		v = x * TILE_SIZE + offset.x;
	}

	private void setDrawTileY(int y) {
		w = y * TILE_SIZE + offset.y;
	}

	private void drawStringTL(String str, int x, int y) {
		drawStringBL(str, x, y + g.getFontMetrics().getHeight());
	}

	private void drawStringTC(String str, int x, int y) {
		drawStringTL(str, x - g.getFontMetrics().stringWidth(str) / 2, y);
	}

	private void drawStringTR(String str, int x, int y) {
		drawStringTL(str, x - g.getFontMetrics().stringWidth(str), y);
	}

	private void drawStringML(String str, int x, int y) {
		g.drawString(str, x, y + g.getFontMetrics().getHeight() / 2);
	}

	private void drawStringMC(String str, int x, int y) {
		drawStringBL(str, x - g.getFontMetrics().stringWidth(str) / 2, y);
	}

	private void drawStringMR(String str, int x, int y) {
		drawStringBL(str, x - g.getFontMetrics().stringWidth(str), y);
	}

	private void drawStringBL(String str, int x, int y) {
		g.drawString(str, x, y);
	}

	private void drawStringBC(String str, int x, int y) {
		drawStringBL(str, x - g.getFontMetrics().stringWidth(str) / 2, y);
	}

	private void drawStringBR(String str, int x, int y) {
		drawStringBL(str, x - g.getFontMetrics().stringWidth(str), y);
	}

	public Point2D windowToWorld(Point2D windowCoordinates) {
		Point2D camPos = gui.getController().getCameraPosition();
		return new Point2D.Double(Math.round((windowCoordinates.getX() - 0.5 * getSize().getWidth()) / TILE_SIZE + camPos.getX()),
				Math.round((windowCoordinates.getY() - 0.5 * getSize().getHeight()) / TILE_SIZE + camPos.getY()));
	}

	public Point worldToWindow(Point2D worldCoordinates) {
		Point2D camPos = gui.getController().getCameraPosition();
		return new Point((int) Math.ceil((worldCoordinates.getX() - camPos.getX() - 0.5) * TILE_SIZE + 0.5 * getSize().width),
				(int) Math.ceil((worldCoordinates.getY() - camPos.getY() - 0.5) * TILE_SIZE + 0.5 * getSize().height));
	}

//	private static int calculateOffset(double pos, double size, double min, double max) {
//		//TODO change into a worldToCam and camToWorld converter
//		if ((max - min) * TILE_SIZE < size) {
//			return (int) ((0.5 * ((max - min) * TILE_SIZE - size)) + (min + 0.5) * TILE_SIZE);
//		}
//		if ((pos - min) * TILE_SIZE < 0.5 * size) {
//			return (int) min * TILE_SIZE;
//		} else if ((max - pos) * TILE_SIZE < 0.5 * size) {
//			return (int) ((max + 1) * TILE_SIZE - size);
//		} else {
//			return (int) Math.ceil((pos + 0.5) * TILE_SIZE - 0.5 * size);
//		}
//	}
	protected void calculateFrameRate() {
		long next = System.nanoTime();
		framerates[n] = ((double) 1000000000) / (next - last);
		last = next;

		n = (n + 1) % framerates.length;

		if (n == 0) {
			double var = 0;
			for (int i = 0; i < framerates.length; i++) {
				var += framerates[i];
			}
			this.framerate = var / framerates.length;
		}
	}
}
