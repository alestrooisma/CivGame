package civ.view;

import civ.model.Map;
import civ.model.Tile;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
	public static final int TILE_SIZE = 32;
	private CivGUI gui;
	private long last = System.nanoTime();
	private double framerate;
	private double[] framerates = new double[60];
	private int n;
	private int vOffset, wOffset;

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
		System.out.println("paint");
		//TODO rethink only drawing visible

		// x and y are coordinates on the grid
		// v and w are coordinates on this panel

		Map map = gui.getController().getMap();
		Point2D pos = gui.getController().getCameraPosition();

		vOffset = calculateOffset(pos.getX(), getSize().width,
				map.getMinX(), map.getMaxX());
		wOffset = calculateOffset(pos.getY(), getSize().height,
				map.getMinY(), map.getMaxY());

//		int xMin = Math.max(map.getMinX(), vOffset / TILE_SIZE - 1);
//		int yMin = Math.max(map.getMinY(), wOffset / TILE_SIZE - 1);
//		int xMax = Math.min(map.getMaxX() + 1,
//				(int) Math.ceil((double) (vOffset + getSize().width) / TILE_SIZE));
//		int yMax = Math.min(map.getMaxX() + 1,
//				(int) Math.ceil((double) (wOffset + getSize().height) / TILE_SIZE));
		int xMin = map.getMinX();
		int yMin = map.getMinY();
		int xMax = map.getMaxX();
		int yMax = map.getMaxY();

		int x, y, v, w, vEntity, wEntity, l;
		Tile tile;
		for (y = yMin; y < yMax; y++) {
			w = y * TILE_SIZE - wOffset;
			for (x = xMin; x < xMax; x++) {
				tile = map.getTile(x, y);
				if (tile != null) {
					drawTile(tile);
				}
			}
			System.out.println();
		}
		calculateFrameRate();
		g.setColor(Color.red);
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		g.drawString(String.format("%.0f", framerate), 0, g.getFont().getSize());
	}

	private void drawTile(Tile tile) {
		switch (tile.getTerrain()) {
			case Tile.GRASSLAND:
				System.out.print("G");
				break;
			case Tile.PLAINS:
				System.out.print("P");
				break;
			case Tile.WATER:
				System.out.print("~");
				break;
		}
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
