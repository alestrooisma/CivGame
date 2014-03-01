/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

import java.awt.Point;


/**
 *
 * @author ale
 */
public class Util {
	
	public static int distanceSquared(Point a, Point b) {
		return (b.x - a.x)*(b.x - a.x) + (b.y - a.y)*(b.y - a.y);
	}
	
	public static double distance(Point a, Point b) {
		return Math.sqrt(distanceSquared(a, b));
	}
}
