/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

import civ.model.City;
import civ.model.Map;
import civ.model.Tile;
import civ.view.CivGUI;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author ale
 */
public class Controller implements Runnable, ActionListener {

	public static final long loopTime = 20000000;
	public static final int NORMAL = 0;
	public static final int BUILD = 1;
	public static final int CITY = 2;
	private Map map;
	private CivGUI gui;
	private Point2D cameraPosition;
	private int mode = NORMAL; // Only for temp building
	private String status = "Welcome!";
	private City viewedCity = null;
	private Timer statusTimer = new Timer(5000, this);

	public Controller(Map map, CivGUI gui, Point2D cameraPosition) {
		this.map = map;
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

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
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

	@Override
	public void run() {
		gui.attachMouseListener(new MouseHandler(this));
		gui.attachKeyboardListener(new KeyHandler(this));
		gui.show();
		statusTimer.start();
	}

	public void buildCity(Point position) {
		Tile tile = getMap().getTile(position);
		if (tile.getCity() != null || tile.getTerrain() == Tile.WATER) {
			setStatus("Can't build here", true);
			return;
		}

		String name = "";
		while (name.length() == 0) {
			name = JOptionPane.showInputDialog(
					gui.getFrame(),
					"What do you want your city to be called?",
					"Name your city",
					JOptionPane.PLAIN_MESSAGE);

			if (name == null) {
				setStatus("Cancelled building.", true);
				return;
			}
		}
		City c = new City(name, tile.getPosition());
		tile.setCity(c);
		setStatus(name + " founded!", true);
	}

	public void enterCity(City city) {
		setCameraPosition(city.getPosition());
		viewedCity = city;
	}

	public void leaveCity(City city) {
		viewedCity = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		status = "";
		statusTimer.stop();
	}
}
