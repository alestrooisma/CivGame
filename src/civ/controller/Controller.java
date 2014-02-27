/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

import civ.model.Map;
import civ.view.CivGUI;
import java.awt.geom.Point2D;

/**
 *
 * @author ale
 */
public class Controller implements Runnable{

	public static final long loopTime = 20000000;
	
	private Map map;
	private CivGUI gui;
	private Point2D cameraPosition;

	public Controller(Map map, CivGUI gui, Point2D cameraPosition) {
		this.map = map;
		this.gui = gui;
		this.cameraPosition = cameraPosition;
		Resources.loadResources();
	}

	public Point2D getCameraPosition() {
		return cameraPosition;
	}

	public CivGUI getGui() {
		return gui;
	}

	public Map getMap() {
		return map;
	}

	@Override
	public void run() {
		gui.show();
	}
}
