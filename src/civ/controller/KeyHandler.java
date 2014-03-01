/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

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
				System.exit(0);
				break;
			case KeyEvent.VK_B:
				if (controller.getMode() == Controller.NORMAL) {
					controller.setMode(Controller.BUILD);
					controller.setStatus("Click anywhere on land to build a city.", false);
				} else if (controller.getMode() == Controller.BUILD) {
					controller.setMode(Controller.NORMAL);
				}
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
