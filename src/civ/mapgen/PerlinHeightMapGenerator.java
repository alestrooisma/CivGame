package civ.mapgen;

import civ.mapgen.noise.NoiseMaps;
import civ.mapgen.noise.PerlinNoise;
import civ.mapgen.noise.Scaler;
import java.util.Random;

/**
 *
 * @author ale
 */
public class PerlinHeightMapGenerator extends HeightMapGenerator implements Scaler {

	public PerlinHeightMapGenerator(Random rand, int width, int height) {
		super(rand, width, height);
	}

	@Override
	public double[][] generate() {
		return NoiseMaps.getFractalNoise(new PerlinNoise(rand), width, height, 24, 4, 0.5, this);
	}

	@Override
	public double scale(double value, double min, double max, double f) {
		return (value - min) * 2 * f - 1;
	}
}
