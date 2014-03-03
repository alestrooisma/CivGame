package civ.view;

import civ.model.Map;
import civ.model.Tile;
import civ.controller.Resources;
import civ.controller.Util;
import civ.model.City;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * A panel on which the map is drawn.
 *
 * @author ale
 */
public class GameField extends Panel {

	/**
	 * The width and height of a tile. TODO: where to define size? Probably
	 * (partially) a settings thing.
	 */
	public static final int TILE_SIZE = 100;
	public static final int CITY_BAR_HEIGHT = 250;
	private long last = System.nanoTime();
	private double framerate;
	private double[] framerates = new double[60];
	private int n;
	private Point offset;
	private int v, w;

	/**
	 * Creates the {@code GameField} component. Requires a GUI to acquire data
	 * through.
	 *
	 * @param gui the GUI this component is part of
	 */
	public GameField(CivGUI gui) {
		super(gui);
		this.gui = gui;

	}

	@Override
	protected void render() {
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
	}

	private void drawWorldHUD() {
		// Status message
		g.setColor(Color.RED);
		g.setFont(boldFont);
		drawStringBC(gui.getController().getStatus(), getWidth() / 2, getHeight() - 50);
	}

	private void drawCityScreen(City city) {
		// Mark worked tiles
		for (Point p : city.getWorkedTiles()) {
			setDrawTile(p);
			drawImage(Resources.yieldbackdrop);
			Tile t = gui.getController().getMap().getTile(p);
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

		// Draw top unit
		if (!tile.getUnits().isEmpty()) {
			drawImage(Resources.units[tile.getUnits().getLast().getType()]);
		}

//		Point p = worldToWindow(tile.getPosition());
//		drawStringTL("" + Util.distanceSquared(ZERO, tile.getPosition())
//				+ " / " + String.format("%.2f", Util.distance(ZERO, tile.getPosition())),
//				p.x, p.y);

		if (gui.getController().isInMoveMode()) {
			double distance = Util.walkDistance(
					gui.getController().getSelectedUnit().getPosition(),
					tile.getPosition());
			if (distance <= gui.getController().getSelectedUnit().getMovesRemaining()) {
				drawStringTL("" + distance, v, w);
			}
		}
	}

	private void drawImage(Image img) {
		drawImage(img, v, w);
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
