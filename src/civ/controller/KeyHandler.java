/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

import civ.model.Unit;
import civ.view.Dialog;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author ale
 */
class KeyHandler implements KeyListener {

	private Controller controller;

	public KeyHandler(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				if (controller.getViewedCity() != null) {
					controller.leaveCity();
				} else {
					if (Dialog.getConfirmation(controller.getGui().getFrame(),
							"Are you sure you want to quit the game?")) {
						System.exit(0);
					}
				}
				break;
			case KeyEvent.VK_ENTER:
				controller.endTurn();
				break;
			case KeyEvent.VK_B:
				Unit u = controller.getSelectedUnit();
				if (u != null && u.getType() == Unit.SETTLER) { 
					controller.buildCity(new Point(u.getPosition()));
				}
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
