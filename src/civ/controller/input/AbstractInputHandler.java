package civ.controller.input;

import civ.controller.Controller;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Just a convenience class
 * @author Ale Strooisma
 */
public abstract class AbstractInputHandler extends AbstractMouseHandler implements KeyListener, MouseListener {

	public AbstractInputHandler(Controller controller) {
		super(controller);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
