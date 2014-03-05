/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.model;

/**
 *
 * @author ale
 */
public class Model {

	private Map map;
	private Civilization[] civilizations;
	private int currentPlayer;
	private int turn;

	public Model(Map map, Civilization[] civilizations) {
		this.map = map;
		this.civilizations = civilizations;
	}

	public Model(Map map, Civilization[] civilizations, int currentPlayer, int turn) {
		this.map = map;
		this.currentPlayer = currentPlayer;
		this.civilizations = civilizations;
		this.turn = turn;
	}

	public Map getMap() {
		return map;
	}

	public Civilization[] getCivilizations() {
		return civilizations;
	}

	public Civilization getCivilization(int player) {
		return civilizations[player];
	}

	public Civilization getCurrentCivilization() {
		return civilizations[currentPlayer];
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void incrementCurrentPlayer() {
		do {
			currentPlayer = (currentPlayer + 1) % civilizations.length;
		} while (getCurrentCivilization().isDefeated());
	}

	public int getTurn() {
		return turn;
	}

	public void incrementTurn() {
		turn++;
	}
}
