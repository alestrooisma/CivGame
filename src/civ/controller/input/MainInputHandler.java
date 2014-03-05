package civ.controller.input;

import civ.controller.Controller;
import civ.model.Unit;
import civ.view.Dialog;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Just a convenience class
 * @author Ale Strooisma
 */
public class MainInputHandler extends AbstractInputHandler {

	public MainInputHandler(Controller controller) {
		super(controller);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		Unit u = controller.getSelectedUnit();
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
				if (u != null && u.getType() == Unit.SETTLER) {
					controller.buildCity(new Point(u.getPosition()));
				}
				break;
			case KeyEvent.VK_G:
				if (controller.getSelectedUnit() != null) {
					controller.toggleMoveMode();
				}
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point tileCoords = controller.getGui().windowToTile(e.getPoint());
		if (controller.isInMoveMode()) {
			controller.moveSelectedUnit(tileCoords);
		} else if (controller.getMap().getTile(tileCoords).getCity() != null) {
			controller.enterCity(controller.getMap().getTile(tileCoords).getCity());
		} else if (controller.getViewedCity() != null) {
			controller.getViewedCity().toggleWorkedTile(tileCoords);
		} else if (!controller.getMap().getTile(tileCoords).getUnits().isEmpty()) {
			controller.selectUnit(controller.getMap().getTile(tileCoords).getUnits().getLast());
		} else {
			controller.deselectUnit();
			controller.leaveCity();
		}
	}
}
