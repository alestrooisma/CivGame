package civ.mapgen;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Can be used to generate a heightmap, where -1 is sea, and 0 and higher is land.
 * @author Ale Strooisma
 */
public class HeightMapGenerator {

	public static final int X = 0;
	public static final int Y = 1;
	private Random rand;
	private int w, h;
	private double[][] hm;
	private ArrayList<Point> points;
	private int npeaks;

	public HeightMapGenerator(Random rand, int w, int h, int npeaks) {
		this.rand = rand;
		this.w = w;
		this.h = h;
		this.npeaks = npeaks;
		hm = new double[w][h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				hm[x][y] = -1;
			}
		}
	}

	public double[][] generate() {
		double val;
		// Generate peaks
		int xp, yp;
		ArrayList<Point> peaks = new ArrayList<Point>(npeaks);
		for (int i = 0; i < npeaks; i++) {
			xp = rand.nextInt(w);
			yp = rand.nextInt(h);
			peaks.add(new Point(xp, yp));
			hm[xp][yp] = rand.nextDouble();
		}
		points = peaks;
		ArrayList<Point> oldpoints;
		Point p;
		while (!points.isEmpty()) {
			oldpoints = points;
			points = new ArrayList<Point>(oldpoints.size() * 4);

			for (Point origin : oldpoints) {
				genHeight(origin, new Point(origin.x - 1, origin.y));
				genHeight(origin, new Point(origin.x, origin.y - 1));
				genHeight(origin, new Point(origin.x + 1, origin.y));
				genHeight(origin, new Point(origin.x, origin.y + 1));
			}
		}
//		for (int y = 0; y < h; y++) {
//			for (int x = 0; x < w; x++) {
//				if (hm[x][y] == -1) {
//					hm[x][y] = 0;
//				}
//			}
//		}
		return hm;
	}

	private void genHeight(Point origin, Point p) {
		if (p.x >= 0 && p.y >= 0 && p.x < w && p.y < h && get(p) == -1) {
			double val = get(origin) - rand.nextDouble() * 0.3;
			if (val <= 0) {
				val = 0;
			} else {
				points.add(p);
			}
			set(p, val);
		}
	}

	private double get(Point p) {
		return hm[p.x][p.y];
	}

	private void set(Point p, double val) {
		hm[p.x][p.y] = val;
	}
}
