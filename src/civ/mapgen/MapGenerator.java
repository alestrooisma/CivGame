/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.mapgen;

import civ.model.Map;
import civ.model.Tile;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author ale
 */
public class MapGenerator {

	public static int ZOOM = 10;
	private int w, h;
	private double[][] hm;
	private Map map;
	private Random rand;

	public MapGenerator(int w, int h) {
		this(w, h, new Random().nextLong());
	}

	public MapGenerator(int w, int h, long seed) {
		this(w, h, new Random(seed));
		System.out.println("Seed: " + seed);
	}

	private MapGenerator(int w, int h, Random rand) {
		this.w = w;
		this.h = h;
		this.rand = rand;
	}

	public Map generate() {
		hm = new HeightMapGenerator(rand, w, h, 20).generate();
		Map map = new Map(w, h);
		double z;
		int terrain;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				z = hm[x][y];
				if (z < 0) {
					terrain = Tile.WATER;
//				} else if (z == 0) {
//					terrain = Tile.GRASSLAND;
				} else {
					terrain = rand.nextDouble() > 0.3 ? Tile.GRASSLAND : Tile.PLAINS;
				}
				map.setTile(new Tile(new Point(x, y), terrain), x, y);
			}
		}
		this.map = map;
		return map;
	}

	// Testing stuff
	public void display() {
		// Use a label to display the image
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JLabel lblimage1 = new JLabel(new ImageIcon(heightMapAsImage(ZOOM)));
		frame.getContentPane().add(lblimage1, BorderLayout.CENTER);
		JLabel lblimage2 = new JLabel(new ImageIcon(mapAsImage(ZOOM)));
		frame.getContentPane().add(lblimage2, BorderLayout.LINE_END);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private BufferedImage heightMapAsImage(int zoom) {
		int value;
		BufferedImage bi = new BufferedImage(w * zoom, h * zoom, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < h * zoom; y++) {
			for (int x = 0; x < w * zoom; x++) {
				value = (int) (hm[x / zoom][y / zoom] * 255);
				value = value << 16 | value << 8 | value;
				bi.setRGB(x, y, value);
			}
		}
		return bi;
	}

	private BufferedImage mapAsImage(int zoom) {
		int grass = 0x00bf00;
		int plains = 0xb1bf00;
		int water = 0x00b4ff;
		int error = 0xff0000;

		BufferedImage bi = new BufferedImage(w * zoom, h * zoom, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < h * zoom; y++) {
			for (int x = 0; x < w * zoom; x++) {
				switch (map.getTile(x / zoom, y / zoom).getTerrain()) {
					case Tile.GRASSLAND:
						bi.setRGB(x, y, grass);
						break;
					case Tile.PLAINS:
						bi.setRGB(x, y, plains);
						break;
					case Tile.WATER:
						bi.setRGB(x, y, water);
						break;
					default:
						bi.setRGB(x, y, error);
				}
			}
		}
		return bi;
	}

	public static void main(String[] args) {
//		MapGenerator mt = new MapGenerator(50, 50, 1234);
		MapGenerator mt = new MapGenerator(50, 50);
		mt.generate();
		mt.display();
	}
}
