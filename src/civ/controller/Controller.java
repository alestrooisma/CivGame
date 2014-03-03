/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

import civ.model.Civilization;
import civ.model.City;
import civ.model.Map;
import civ.model.Tile;
import civ.model.Unit;
import civ.view.CivGUI;
import civ.view.Dialog;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.Timer;

/**
 *
 * @author ale
 */
public class Controller implements Runnable, ActionListener {

	public static final long loopTime = 20000000;
	// Model
	private Map map;
	private int currentPlayer = 0;
	private Civilization[] civilizations;
	// View
	private CivGUI gui;
	private Point2D cameraPosition;
	private String status = "Welcome!";
	private City viewedCity = null;
	private Unit selectedUnit = null;
	private Timer statusTimer = new Timer(5000, this);
	private boolean moveMode = false;
	// Other
	private int turn = 0;
	private long turnStartTime;
	// Temp (for simple building)

	public Controller(Map map, Civilization[] civilizations, CivGUI gui, Point2D cameraPosition) {
		this.map = map;
		this.civilizations = civilizations;
		this.gui = gui;
		this.cameraPosition = cameraPosition;
		Resources.loadResources();
	}

	public CivGUI getGui() {
		return gui;
	}

	public Map getMap() {
		return map;
	}

	public Point2D getCameraPosition() {
		return cameraPosition;
	}

	public void setCameraPosition(Point2D cameraPosition) {
		this.cameraPosition = cameraPosition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status, boolean fade) {
		this.status = status;
		if (fade) {
			statusTimer.restart();
		}
	}

	public City getViewedCity() {
		return viewedCity;
	}

	public Civilization getCurrentCivilization() {
		return civilizations[currentPlayer];
	}

	@Override
	public void run() {
		gui.attachMouseListener(new MouseHandler(this));
		gui.attachKeyboardListener(new KeyHandler(this));
		gui.show();
		statusTimer.start();
		startTurn(getCurrentCivilization());
	}

	public void buildCity(Point position) {
		Tile tile = getMap().getTile(position);
		if (tile.getCity() != null || tile.getTerrain() == Tile.WATER) {
			setStatus("Can't build here", true);
			return;
		}

		String name = "";
		while (name.length() == 0) {
			name = Dialog.getString(gui.getFrame(), "What do you want your city to be called?");

			if (name == null) {
				setStatus("Cancelled building.", true);
				return;
			}
		}
		City c = City.createCity(civilizations[currentPlayer], name, tile.getPosition(), map);
		tile.setCity(c);
		selectedUnit.destroy(map);
		deselectUnit();
		setStatus(name + " founded!", true);
	}

	public void enterCity(City city) {
		setCameraPosition(city.getLocation());
		viewedCity = city;
	}

	public void leaveCity() {
		viewedCity = null;
	}

	public Unit getSelectedUnit() {
		return selectedUnit;
	}
	
	public void selectUnit(Unit unit) {
		setCameraPosition(unit.getPosition());
		selectedUnit = unit;
	}
	
	public void deselectUnit() {
		moveMode = false;
		selectedUnit = null;
	}

	public void moveSelectedUnit(Point tileCoords) {
		double distance = Util.walkDistance(selectedUnit.getPosition(), tileCoords);
		if (distance <= selectedUnit.getMovesRemaining()) {
			map.getTile(selectedUnit.getPosition()).removeUnit(selectedUnit);
			selectedUnit.setPosition(tileCoords);
			map.getTile(tileCoords).addUnit(selectedUnit);
			selectedUnit.reduceMoves(distance);
		}
		moveMode = false;
	}

	public void toggleMoveMode() {
		moveMode = !moveMode;
	}

	public boolean isInMoveMode() {
		return moveMode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		status = "";
		statusTimer.stop();
	}

	public void endTurn() {
		// Finalize turn
		deselectUnit();
		
		// Start next player's turn
		do {
			currentPlayer = (currentPlayer + 1) % civilizations.length;
		} while (getCurrentCivilization().isDefeated());

		startTurn(getCurrentCivilization());
	}

	private void startTurn(Civilization civ) {
		if (currentPlayer == 0) {
			turn++;
		}
		turnStartTime = System.currentTimeMillis();

		// Update cities
		for (City c : civ.getCities()) {
			// Production
			c.addFood(c.getNetFoodYield(map));
			c.addMaterials(c.getNetMaterialsYield(map));
			// Growth
			if (c.getFood() >= c.getGrowsAt()) {
				c.grow();
			} else if (c.getFood() < 0) {
				c.starvation();
			}
		}
		
		// Update units
		for (Unit u : civ.getUnits()) {
			// Reset remaining moves
			u.resetMoves();
		}
	}

	public int getTurn() {
		return turn;
	}

	public long getTurnStartTime() {
		return turnStartTime;
	}
}
