/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.mapgen;

import civ.mapgen.noise.NoiseMaps;
import civ.mapgen.noise.PerlinNoise;
import civ.model.Map;
import civ.model.Tile;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ale
 */
public class MapGenerator {

	// Randomizers
	private Random rand;
	private PerlinNoise pngen;
	//
	// Map stuff
	private int w, h;
	private Map map;
	//
	// Subgenerators
	private HeightMapGenerator hmgen;
	private double[][] hm;
	private double[][] humidity;

	public MapGenerator(int w, int h, int hmtype) {
		this(new Random().nextLong(), w, h, hmtype);
	}

	public MapGenerator(long seed, int w, int h, int hmtype) {
		this(new Random(seed), w, h, hmtype);
		System.out.println("Seed: " + seed);
	}


	public MapGenerator(Random rand, int w, int h, int hmtype) {
		this.rand = rand;
		this.w = w;
		this.h = h;
		pngen = new PerlinNoise(rand);
		setHeightMapGenerator(hmtype);
	}

	private void setHeightMapGenerator(int type) {
		switch (type) {
			case 1:
				hmgen = new PeaksHeightMapGenerator(rand, w, h, 20);
				break;
			default:
				hmgen = new PerlinHeightMapGenerator(rand, w, h);
				break;
		}
	}

	public Map generate() {
		hm = hmgen.generate();
//		humidity = NoiseMaps.getFractalNoise(pngen, w, h, 64, 3, 0.25);

		map = new Map(w, h);
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
		return map;
	}

	// Testing stuff
	public void display(int zoom) {
		// Use a label to display the image
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel(new GridLayout(2, 2));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.add(new JLabel(new ImageIcon(heightMapAsImage(zoom))));
		panel.add(new JLabel(new ImageIcon(mapAsImage(zoom))));
//		panel.add(new JLabel(new ImageIcon(humidityAsImage(zoom))));
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

	private BufferedImage humidityAsImage(int zoom) {
		int value;
		BufferedImage bi = new BufferedImage(w * zoom, h * zoom, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < h * zoom; y++) {
			for (int x = 0; x < w * zoom; x++) {
				value = (int) (humidity[x / zoom][y / zoom] * 255);
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
}
