package civ.controller.input;

import civ.controller.Controller;
import civ.model.Tile;
import civ.model.Unit;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Just a convenience class
 * @author Ale Strooisma
 */
public class MainInputHandler extends AbstractInputHandler implements ActionListener {

	private boolean popupShown = false;
	private Tile tile;

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
				} else if (controller.getSelectedUnit() != null) {
					controller.deselectUnit();
				} else {
//					if (Dialog.getConfirmation(controller.getGui().getFrame(),
//							"Are you sure you want to quit the game?")) {
					System.exit(0);
//					}
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
		tile = controller.getMap().getTile(tileCoords);
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (popupShown) {
				popupShown = false;
			} else if (controller.isInMoveMode()) {
				controller.moveSelectedUnit(tileCoords);
			} else if (tile.getCity() != null) {
				controller.enterCity(tile.getCity());
			} else if (controller.getViewedCity() != null) {
				controller.getViewedCity().toggleWorkedTile(tileCoords);
			} else if (!tile.getUnits().isEmpty()) {
				controller.selectUnit(tile.getUnits().getLast());
			} else {
				controller.deselectUnit();
				controller.leaveCity();
			}
		} else if (e.getButton() == MouseEvent.BUTTON3 && controller.getViewedCity() == null) {
			JPopupMenu popup = new JPopupMenu();
			JMenuItem item;
			if (tile.getCity() != null) {
				item = new JMenuItem("View " + tile.getCity().getName());
				item.setActionCommand("C");
				item.addActionListener(this);
				popup.add(item);
			} else if (tile.getUnits().isEmpty()) {
				return;
			}
			for (int i = 0; i < tile.getUnits().size(); i++) {
				Unit u = tile.getUnits().get(i);
				String name;
				switch (u.getType()) {
					case Unit.SETTLER:
						name = "Settler";
						break;
					case Unit.WARRIOR:
						name = "Warrior";
						break;
					default:
						name = "unit";
				}
				item = new JMenuItem("Select " + name);
				item.setActionCommand("" + i);
				item.addActionListener(this);
				popup.add(item);
			}
			popup.show(e.getComponent(), e.getX(), e.getY());
			popupShown = true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		popupShown = false;
		if (e.getActionCommand().equals("C")) {
			controller.enterCity(tile.getCity());
		} else {
			int i = Integer.parseInt(e.getActionCommand());
			controller.selectUnit(tile.getUnits().get(i));
		}
	}
}
