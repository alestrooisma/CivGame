/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package civ.view;

import java.awt.Font;

public abstract class GuiPanel extends Panel {

	protected int x = 0, y = 0, lineX = 0;
	private int padX, padY;

	public GuiPanel(CivGUI gui, int padX, int padY) {
		super(gui);
		this.padX = padX;
		this.padY = padY;
	}

	@Override
	protected void render() {
		reset();
		renderGUI();
	}

	abstract protected void renderGUI();

	protected void writeln() {
		y += g.getFontMetrics().getHeight();
		lineX = x;
	}

	protected void writeln(String str) {
		writeln(str, 0);
	}

	protected void writeln(String str, int indentation) {
		drawStringTL(str, lineX + indentation, y);
		writeln();
	}

	protected void writeln(String str, Font font) {
		writeln(str, font, 0);
	}

	protected void writeln(String str, Font font, int indentation) {
		g.setFont(font);
		writeln(str, indentation);
		g.setFont(normalFont);
	}

	protected void write(String str) {
		write(str, 0);
	}

	protected void write(String str, int indentation) {
		drawStringTL(str, lineX + indentation, y);
		lineX += g.getFontMetrics().stringWidth(str) + indentation;
	}

	protected void write(String str, Font font) {
		write(str, font, 0);
	}

	protected void write(String str, Font font, int indentation) {
		g.setFont(font);
		write(str, indentation);
		g.setFont(normalFont);
	}

	protected void verticalSpacing(int height) {
		y += height;
	}

	protected void horizontalSpacing(int width) {
		x += width;
	}

	protected void reset() {
		x = padX;
		y = padY;
		lineX = x;
	}

	protected void newColumn(int x) {
		this.x = x;
		y = padY;
	}

	protected void newLine(int y) {
		this.y = y;
		x = padX;
		lineX = x;
	}

	public int getPadX() {
		return padX;
	}

	public int getPadY() {
		return padY;
	}
}
