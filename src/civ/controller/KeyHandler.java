/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

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
					int n = JOptionPane.showConfirmDialog(
                            controller.getGui().getFrame(), 
							"Are you sure you want to quit?",
                            "Please confirm",
                            JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
				}
				break;
			case KeyEvent.VK_ENTER:
				controller.endTurn();
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
