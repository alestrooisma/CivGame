/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.model;

import java.awt.Point;

/**
 *
 * @author ale
 */
public class Unit {
	
	// Types
	public static final int SETTLER = 0;
	public static final int WARRIOR = 1;
	
	// Fields
	private int type;
	private Point position;
	private Civilization civilization;
	private double movesRemaining;
	private double movesPerTurn;
	private int strength;
	private int currentHealth;
	private int maxHealth;

	public Unit(Civilization civilization, int type, double movesPerTurn, int strength, int maxHealth) {
		this.civilization = civilization;
		this.type = type;
		this.movesPerTurn = movesPerTurn;
		this.strength = strength;
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public double getMovesPerTurn() {
		return movesPerTurn;
	}

	public void setMovesPerTurn(double movesPerTurn) {
		this.movesPerTurn = movesPerTurn;
	}

	public double getMovesRemaining() {
		return movesRemaining;
	}

	public void setMovesRemaining(double movesRemaining) {
		this.movesRemaining = movesRemaining;
	}

	public void resetMoves() {
		movesRemaining = movesPerTurn;
	}
	
	public void reduceMoves(double moves) {
		movesRemaining -= moves;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getType() {
		return type;
	}

	public Civilization getCivilization() {
		return civilization;
	}
	
	public static Unit createUnit(Civilization civilization, int type, 
			double movesPerTurn, int strength, int maxHealth, Point position, Map map) {
		Unit unit = new Unit(civilization, type, movesPerTurn, strength, maxHealth);
		civilization.addUnit(unit);
		map.getTile(position).addUnit(unit);
		unit.setPosition(position);
		return unit;
	}
	
	public void destroy(Map map) {
		getCivilization().removeUnit(this);
		map.getTile(getPosition()).removeUnit(this);
	}
}
