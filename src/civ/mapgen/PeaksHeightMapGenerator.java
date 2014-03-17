package civ.mapgen;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Can be used to generate a heightmap, where -1 is sea, and 0 and higher is land.
 * @author Ale Strooisma
 */
public class PeaksHeightMapGenerator extends HeightMapGenerator {

	private double[][] hm;
	private ArrayList<Point> points;
	private int npeaks;

	public PeaksHeightMapGenerator(Random rand, int width, int height, int npeaks) {
		super(rand, width, height);
		this.npeaks = npeaks;
		hm = new double[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				hm[x][y] = -1;
			}
		}
	}

	@Override
	public double[][] generate() {
		// Generate peaks
		int xp, yp;
		ArrayList<Point> peaks = new ArrayList<Point>(npeaks);
		for (int i = 0; i < npeaks; i++) {
			xp = rand.nextInt(width);
			yp = rand.nextInt(height);
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
		if (p.x >= 0 && p.y >= 0 && p.x < width && p.y < height && get(p) == -1) {
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
