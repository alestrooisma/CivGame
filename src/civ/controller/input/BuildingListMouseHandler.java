package civ.controller.input;

import civ.controller.Controller;
import civ.model.Building;
import civ.view.CityScreenPanel.BuildingPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class BuildingListMouseHandler extends AbstractMouseHandler implements ActionListener {

	private boolean popupShown = false;
	private BuildingPanel panel;
	private Building building;

	public BuildingListMouseHandler(Controller controller, BuildingPanel panel) {
		super(controller);
		this.panel = panel;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (popupShown) {
			popupShown = false;
		} else if (e.getX() > panel.getPadX() && e.getX() < panel.getWidth() - panel.getPadX()) {

			int Y = panel.getRectHeight() + BuildingPanel.SPACING;
			int y = e.getY() - panel.getPadY();

			if (y > 0 && y % Y < panel.getRectHeight()) {
				int n = y / Y;
				Building b = controller.getViewedCity().getBuilding(n);
				if (b != null) {
					building = b;
					if (panel.getSelectButton().contains(e.getX(), y % Y)) {
						JPopupMenu popup = new JPopupMenu("Select production:");
						JMenuItem item;
						for (String s : b.getProductionOptions()) {
							item = new JMenuItem(s);
							item.addActionListener(this);
							popup.add(item);
						}
						popup.show(e.getComponent(), e.getX(), e.getY());
						popupShown = true;
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		popupShown = false;
		building.setProduction(e.getActionCommand());
	}
}
