package civ.mapgen.noise;

import java.util.LinkedList;
import java.util.Random;

/**
 * A class for generating coherent noise, based on the "Improved Noise" by Kevin 
 * Perlin.
 * @author Ale Strooisma
 */
public final class PerlinNoise {

	private final int p[] = new int[512];
	private Random rand;
	
	public PerlinNoise() {
		this(new Random());
	}
	
	public PerlinNoise(long seed) {
		this(new Random(seed));
	}

	public PerlinNoise(Random rand) {
		this.rand = rand;
		seed();
	}

	public double noise(double x, double y) {
		int X = (int) Math.floor(x) & 255;
		int Y = (int) Math.floor(y) & 255;
		x -= Math.floor(x);
		y -= Math.floor(y);
		double Sx = fade(x);
		double Sy = fade(y);
		int A = p[X] + Y, AA = p[A], AB = p[A + 1],
				B = p[X + 1] + Y, BA = p[B], BB = p[B + 1];

		double s = grad(p[AA], x, y);
		double t = grad(p[BA], x - 1, y);
		double u = grad(p[AB], x, y - 1);
		double v = grad(p[BB], x - 1, y - 1);

		return lerp(Sy, lerp(Sx, s, t), lerp(Sx, u, v));
	}

	public double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	public double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}

	public double grad(int hash, double x, double y) {
		return ((hash & 1) == 0 ? x : -x) + ((hash & 2) == 0 ? y : -y);
	}
	
	public void reseed() {
		seed();
	}
	
	private void seed() {
		LinkedList<Integer> numbers = new LinkedList<Integer>();
		for (int i = 0; i < 256; i++) {
			numbers.add(i);
		}
		// Pick numbers from the list
		int n = 256;
		for (int i = 0; i < 256; i++) {
			p[256 + i] = p[i] = numbers.remove(rand.nextInt(n));
			n--;
		}
	}
}
