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
	private double[][] temperature;
	private double[][] humidity;
	private double[][] humidity2;
	private double[][] humidity3;

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
		temperature = NoiseMaps.getFractalNoise(pngen, w, h, 8, 3, 0.5);
		double a = -4.0/((w-1)*(w-1));
		double env = 0;
		for (int y = 0; y < h; y++) {
			env = a*y*(y-(h-1));
			for (int x = 0; x < w; x++) {
				temperature[x][y] = temperature[x][y] * env;
			}
		}
		generateHumidityMap();

		map = new Map(w, h);
		double z;
		int terrain;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				z = hm[x][y];
				if (isWater(x, y)) {
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

	private void generateHumidityMap() {
		humidity = new double[w][h];
		humidity2 = new double[w][h];
		humidity3 = new double[w][h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (isWater(x, y)) {
					humidity[x][y] = 0;
				} else {
					humidity[x][y] = Double.MAX_VALUE;
				}
			}
		}

		boolean done = false;
		int i = 1;
		double dist;
		for (i = 1; !done; i++) {
			done = true;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					dist = humidity[x][y];
					if (x > 0 && humidity[x - 1][y] + 1 < dist) {
						dist = humidity[x - 1][y] + 1;
					}
					if (y > 0 && humidity[x][y - 1] + 1 < dist) {
						dist = humidity[x][y - 1] + 1;
					}
					if (x + 1 < w && humidity[x + 1][y] + 1 < dist) {
						dist = humidity[x + 1][y] + 1;
					}
					if (y + 1 < h && humidity[x][y + 1] + 1 < dist) {
						dist = humidity[x][y + 1] + 1;
					}
					if (dist < humidity[x][y]) {
						humidity[x][y] = dist;
						done = false;
					}
				}
			}
		}

		double f = 0.1;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				humidity2[x][y] = 1 / (f * humidity[x][y] + 1);
				if (humidity2[x][y] == 1) {
					humidity3[x][y] = 1;
				} else {
					humidity3[x][y] = humidity2[x][y] * (1 - 0.5*temperature[x][y]);
				}
				humidity[x][y] = humidity[x][y] / 17;
			}
		}
	}

	private boolean isWater(int x, int y) {
		return hm[x][y] < 0;
	}
	// Testing stuff

	public void display(int zoom) {
		// Use a label to display the image
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel(new GridLayout(0, 2));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.add(new JLabel(new ImageIcon(heightMapAsImage(zoom))));
		panel.add(new JLabel(new ImageIcon(mapAsImage(zoom))));
		panel.add(new JLabel(new ImageIcon(temperatureAsImage(zoom))));
		panel.add(new JLabel(new ImageIcon(humidityAsImage(zoom, humidity))));
		panel.add(new JLabel(new ImageIcon(humidityAsImage(zoom, humidity2))));
		panel.add(new JLabel(new ImageIcon(humidityAsImage(zoom, humidity3))));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private BufferedImage heightMapAsImage(int zoom) {
		int value;
		BufferedImage bi = new BufferedImage(w * zoom, h * zoom, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < h * zoom; y++) {
			for (int x = 0; x < w * zoom; x++) {
				value = (int) ((hm[x / zoom][y / zoom] / 2 + 0.5) * 255);
				value = value << 16 | value << 8 | value;
				bi.setRGB(x, y, value);
			}
		}
		return bi;
	}

	private BufferedImage humidityAsImage(int zoom, double[][] humidity) {
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

	private BufferedImage temperatureAsImage(int zoom) {
		int value;
		int red, blue;
		BufferedImage bi = new BufferedImage(w * zoom, h * zoom, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < h * zoom; y++) {
			for (int x = 0; x < w * zoom; x++) {
				red = (int) (temperature[x / zoom][y / zoom] * 255);
				blue = (int) ((1 - temperature[x / zoom][y / zoom]) * 255);
				value = red << 16 | blue;
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
		MapGenerator mg = new MapGenerator(-4251178704391246875L, 128, 128, 2);
		mg.generate();
		mg.display(3);
//		MapGenerator mg = new MapGenerator(-4251178704391246875L, 16, 16, 2);
//		mg.generate();
//		mg.display(30);
	}
}
