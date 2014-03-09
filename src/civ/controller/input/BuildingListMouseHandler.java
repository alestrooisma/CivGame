package civ.controller.input;

import civ.controller.Controller;
import civ.view.CityScreenPanel.BuildingPanel;
import java.awt.event.MouseEvent;

public class BuildingListMouseHandler extends AbstractMouseHandler {

	private BuildingPanel panel;

	public BuildingListMouseHandler(Controller controller, BuildingPanel panel) {
		super(controller);
		this.panel = panel;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println(e.getPoint());
		if (e.getX() > panel.getPadX() && e.getX() < panel.getWidth() - panel.getPadX()) {

			int Y = panel.getRectHeight() + BuildingPanel.SPACING;
			int y = e.getY() - panel.getPadY();

			if (y > 0 && y % Y < panel.getRectHeight()) {
				int n = y / Y;
				System.out.println("n = " + n);
			}
		}
	}
}
