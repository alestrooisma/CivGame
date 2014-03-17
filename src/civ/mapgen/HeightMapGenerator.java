/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.mapgen;

import java.util.Random;

/**
 *
 * @author ale
 */
public abstract class HeightMapGenerator {

	protected Random rand;
	protected int width, height;

	public HeightMapGenerator(Random rand, int width, int height) {
		this.rand = rand;
		this.width = width;
		this.height = height;
	}

	public abstract double[][] generate();
}
