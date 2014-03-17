package civ.mapgen.noise;

/**
 * A class with functions for generating 2D arrays filled with various kinds of
 * noise.
 * @author Ale Strooisma
 */
public class NoiseMaps {

	public static final Scaler defaultScaler = new DefaultScaler();

	/**
	 * Returns a 2D array filled with Perlin Noise.
	 * 
	 * @param gen the generator to use for filling the array
	 * @param width the first dimension of the array
	 * @param height the second dimension of the array
	 * @param res a parameter which determines the size of the features in the 
	 * array. A lower res gives smaller features, and a res of 1 basically gives
	 * white noise
	 * @return an array filled with Perlin Noise
	 */
	public static double[][] getPerlinNoise(PerlinNoise gen, int width, int height, double res) {
		double[][] array = new double[width][height];
		double noise;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				noise = gen.noise(((double) x) / res, ((double) y) / res);
				array[x][y] = (noise + 1) / 2;
			}
		}
		return array;
	}

	/**
	 * Returns a 2D array filled with fractal noise generated from Perlin 
	 * Noise.
	 * 
	 * @param pnoise an array of 2D arrays filled with Perlin Noise
	 * @param n the number octaves to use. Must be lower than or equal to the 
	 * first dimension of pnoise. Smaller values give smoother noise
	 * @param p the persistance of higher octaves. Must be less than or equal to
	 * 0.5, unless rescaling is applied. Smaller values give smoother noise
	 * @return an array filled with fractal noise
	 */
	public static double[][] getFractalNoise(double[][][] pnoise, int n, double p) {
		int w = pnoise[0].length;
		int h = pnoise[0][0].length;
		double[][] array = new double[w][h];
		double persistence = 1;
		for (int i = 0; i < n; i++) {
			persistence *= p;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					array[x][y] += pnoise[i][x][y] * persistence;
				}
			}
		}
		return array;
	}

	/**
	 * Returns a 2D array filled with fractal noise generated from Perlin 
	 * Noise, with values between 0 and 1.
	 * 
	 * @param gen the generator to use for getting Perlin Noise
	 * @param width the first dimension of the array
	 * @param height the second dimension of the array
	 * @param res a parameter which determines the size of the features in the 
	 * array. A lower res gives smaller features, and a res of 1 basically gives
	 * white noise
	 * @param n the number octaves to use. Must be lower than or equal to the 
	 * first dimension of pnoise. Smaller values give smoother noise
	 * @param p the persistance of higher octaves. Must be less than or equal to
	 * 0.5, unless rescaling is applied. Smaller values give smoother noise
	 * @return an array filled with fractal noise
	 */
	public static double[][] getFractalNoise(PerlinNoise gen, 
			int width, int height, int res, int n, double p) {
		return getFractalNoise(gen, width, height, res, n, p, defaultScaler);
	}

	/**
	 * Returns a 2D array filled with fractal noise generated from Perlin 
	 * Noise.
	 * 
	 * @param gen the generator to use for getting Perlin Noise
	 * @param width the first dimension of the array
	 * @param height the second dimension of the array
	 * @param res a parameter which determines the size of the features in the 
	 * array. A lower res gives smaller features, and a res of 1 basically gives
	 * white noise
	 * @param n the number octaves to use. Must be lower than or equal to the 
	 * first dimension of pnoise. Smaller values give smoother noise
	 * @param p the persistance of higher octaves. Must be less than or equal to
	 * 0.5, unless rescaling is applied. Smaller values give smoother noise
	 * @param scaler the scaler
	 * @return an array filled with fractal noise
	 */
	public static double[][] getFractalNoise(PerlinNoise gen, 
			int width, int height, int res, int n, double p, Scaler scaler) {
		// Get perlin noise
		double[][][] pnoise = new double[n][width][height];
		for (int i = 0; i < n; i++) {
			pnoise[i] = getPerlinNoise(gen, width, height, res);
			res /= 2;
		}
		// Get actual fractal noise
		return rescale(getFractalNoise(pnoise, n, p), scaler);
	}

	/**
	 * Scales the values in the given array with the given scaler's scaling 
	 * function.
	 * 
	 * @param array the array to scale
	 * @param scaler the object to use for the scaler function
	 * @return the given array, but scaled
	 */
	public static double[][] rescale(double[][] array, Scaler scaler) {

		// Determine array size
		int w = array.length;
		int h = array[0].length;

		// Determine min and max values
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (array[x][y] < min) {
					min = array[x][y];
				} else if (array[x][y] > max) {
					max = array[x][y];
				}
			}
		}

		// Rescale noise
		double f = 1 / (max - min);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				array[x][y] = scaler.scale(array[x][y], min, max, f);
			}
		}
		return array;
	}
	
	/**
	 * Scales the given array to contain values between 0 and 1.
	 * 
	 * @param array the array to scale
	 * @return the given array, but scaled
	 */
	public static double[][] rescale(double[][] array) {
		return rescale(array, defaultScaler);
	}

	/**
	 * A class used to scale values in arrays filled with noise.
	 */
	public static class DefaultScaler implements Scaler {

		/**
		 * Scales the given value to be between 0 and 1. Intended for use by
		 * NoiseMaps.rescale to rescale arrays with random values.
		 * 
		 * @param value the value to scale
		 * @param min the lowest value in the array: if value is equal to this, 
		 * scale will return 0
		 * @param max the highest value in the array: if value is equal to this, 
		 * scale will return 1
		 * @param f 1 / (max - min), used for optimization
		 * @return a value between 0 and 1
		 */
		@Override
		public double scale(double value, double min, double max, double f) {
			return (value - min) * f;
		}
	}
}
