/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.mapgen.noise;

/**
 *
 * @author ale
 */
public interface Scaler {

	/**
	 * Scales the given value. Intended for use by NoiseMaps.rescale to rescale 
	 * arrays with random values to a certain distribution.
	 * 
	 * @param value the value to scale
	 * @param min the lowest value in the array
	 * @param max the highest value in the array
	 * @param f 1 / (max - min), used for optimization
	 * @return a scaled value - implementation dependent
	 */
	public double scale(double value, double min, double max, double f);
}
