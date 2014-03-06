/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.view;

import java.awt.Font;

/**
 *
 * @author ale
 */
public abstract class GuiPanel extends Panel {

	private int x = 0, y = 0, padX, padY;

	public GuiPanel(CivGUI gui, int padX, int padY) {
		super(gui);
		this.padX = padX;
		this.padY = padY;
	}

	public void write() {
		y += g.getFontMetrics().getHeight();
	}

	public void write(String str) {
		write(str, 0);
	}

	public void write(String str, int indentation) {
		drawStringTL(str, x + indentation, y);
		write();
	}

	public void write(String str, Font font) {
		write(str, font, 0);
	}

	public void write(String str, Font font, int indentation) {
		g.setFont(font);
		write(str, indentation);
		g.setFont(normalFont);
	}

	public void spacing(int height) {
		y += height;
	}

	public void reset() {
		x = padX;
		y = padY;
	}

	public void newColumn(int x) {
		this.x = x;
		y = padY;
	}
}
