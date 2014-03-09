/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

import civ.CivGame;
import civ.model.Building;
import civ.model.Civilization;
import civ.model.City;
import civ.model.Map;
import civ.model.Model;
import civ.model.Tile;
import civ.model.Unit;
import civ.placeholder.ConstructionSite;
import civ.placeholder.TrainingField;
import civ.view.CivGUI;
import civ.view.Dialog;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ale
 */
public class Controller implements Runnable{

	private Model model;
	private CivGUI gui;
	private City viewedCity = null;
	private Unit selectedUnit = null;
	// Pending cleanup (i.e. not sure what to do with it)
	private long turnStartTime;
	private boolean moveMode = false;
	private Point2D cameraPosition;

	public Controller(Model model, CivGUI gui, Point2D cameraPosition) {
		this.model = model;
		this.gui = gui;
		this.cameraPosition = cameraPosition;
		Resources.loadResources();
	}

	@Override
	public void run() {
		try {
			gui.buildGuiAndWait();
		} catch (InterruptedException ex) {
			Logger.getLogger(CivGame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(CivGame.class.getName()).log(Level.SEVERE, null, ex);
		}
		gui.show();
		startTurn(getCurrentCivilization());
		gui.setStatus("Welcome!", true);
	}

	// Getters
	public Model getModel() {
		return model;
	}

	public CivGUI getGui() {
		return gui;
	}

	public long getTurnStartTime() {
		return turnStartTime;
	}

	// Map wrapper getters
	public Map getMap() {
		return model.getMap();
	}

	public Civilization[] getCivilizations() {
		return model.getCivilizations();
	}

	public Civilization getCivilization(int player) {
		return model.getCivilization(player);
	}

	public Civilization getCurrentCivilization() {
		return model.getCurrentCivilization();
	}

	public int getCurrentPlayer() {
		return model.getCurrentPlayer();
	}

	public int getTurn() {
		return model.getTurn();
	}

	public City getViewedCity() {
		return viewedCity;
	}

	// Controller logic

	public Point2D getCameraPosition() {
		return cameraPosition;
	}

	public void setCameraPosition(Point2D cameraPosition) {
		this.cameraPosition = cameraPosition;
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

	public void buildCity(Point position) {
		Tile tile = getMap().getTile(position);
		if (tile.getCity() != null || tile.getTerrain() == Tile.WATER) {
			gui.setStatus("Can't build here", true);
			return;
		}

		String name = "";
		while (name.length() == 0) {
			name = Dialog.getString(gui.getFrame(), "What do you want your city to be called?");

			if (name == null) {
				gui.setStatus("Cancelled building.", true);
				return;
			}
		}

		City c = City.createCity(getCurrentCivilization(), name, tile.getPosition(), getMap());
		c.addBuilding(new ConstructionSite(c, "Construction site"));
		c.addBuilding(new TrainingField(c, "Training field"));
		selectedUnit.destroy(getMap());
		deselectUnit();
		gui.setStatus(name + " founded!", true);
	}

	public void moveSelectedUnit(Point tileCoords) {
		double distance = Util.walkDistance(selectedUnit.getPosition(), tileCoords);
		if (distance <= selectedUnit.getMovesRemaining()) {
			getMap().getTile(selectedUnit.getPosition()).removeUnit(selectedUnit);
			selectedUnit.setPosition(tileCoords);
			getMap().getTile(tileCoords).addUnit(selectedUnit);
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

	public void endTurn() {
		// Finalize turn
		deselectUnit();

		// Start next player's turn
		model.incrementCurrentPlayer();
		startTurn(getCurrentCivilization());
	}

	private void startTurn(Civilization civ) {
		if (getCurrentPlayer() == 0) {
			model.incrementTurn();
		}
		turnStartTime = System.currentTimeMillis();

		// Update cities
		for (City c : civ.getCities()) {
			// Production
			c.addFood(c.getNetFoodYield(getMap()));
			c.addMaterials(c.getNetMaterialsYield(getMap()));
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
}
