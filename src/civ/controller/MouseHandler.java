/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author ale
 */
class MouseHandler implements MouseListener {

	private Controller controller;

	public MouseHandler(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
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

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
