package civ.view;

import civ.model.Map;
import civ.model.Tile;
import civ.controller.Resources;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
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
	private CivGUI gui;
	private long last = System.nanoTime();
	private double framerate;
	private double[] framerates = new double[60];
	private int n;
	private int v, w;
	private Graphics g;

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

		// x and y are coordinates on the grid
		// v and w are coordinates on this panel

		Map map = gui.getController().getMap();
		Point2D pos = gui.getController().getCameraPosition();

		int vOffset = calculateOffset(pos.getX(), getSize().width,
				map.getMinX(), map.getMaxX());
		int wOffset = calculateOffset(pos.getY(), getSize().height,
				map.getMinY(), map.getMaxY());


		int xMin = Math.max(map.getMinX(), vOffset / TILE_SIZE);
		int yMin = Math.max(map.getMinY(), wOffset / TILE_SIZE);
		int xMax = Math.min(map.getMaxX(),
				(int) Math.ceil((double) (vOffset + getSize().width) / TILE_SIZE - 1));
		int yMax = Math.min(map.getMaxX(),
				(int) Math.ceil((double) (wOffset + getSize().height) / TILE_SIZE -1));

		int x, y;
		Tile tile;
		for (y = yMin; y <= yMax; y++) {
			w = y * TILE_SIZE - wOffset;
			for (x = xMin; x <= xMax; x++) {
				v = x * TILE_SIZE - vOffset;
				tile = map.getTile(x, y);
				if (tile != null) {
					drawTile(tile);
				}
			}
		}
		calculateFrameRate();
		g.setColor(Color.red);
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		g.drawString(String.format("%.0f", framerate), 0, g.getFont().getSize());
	}

	private void drawTile(Tile tile) {
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
	}

	private void drawImage(Image img) {
		g.drawImage(img, v, w, null);
	}

	private static int calculateOffset(double pos, double size, double min, double max) {
		if ((max - min) * TILE_SIZE < size) {
			return (int) ((0.5 * ((max - min) * TILE_SIZE - size)) + (min + 0.5) * TILE_SIZE);
		}
		if ((pos - min) * TILE_SIZE < 0.5 * size) {
			return (int) min * TILE_SIZE;
		} else if ((max - pos) * TILE_SIZE < 0.5 * size) {
			return (int) ((max + 1) * TILE_SIZE - size);
		} else {
			return (int) Math.ceil((pos + 0.5) * TILE_SIZE - 0.5 * size);
		}
	}

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
