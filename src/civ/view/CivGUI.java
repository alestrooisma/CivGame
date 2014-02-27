package civ.view;

import civ.controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The GUI for the Pokemon game on ADE.
 * @author Ale Strooisma
 */
public class CivGUI extends AbstractGUI implements ActionListener {

	private JFrame frame;
	private GameField gf;
	private Controller controller;
	
	public static final int maxFrameRate = 60;
	private Timer redrawTimer;

	/**
	 * Initializes an instance.
	 */
	public CivGUI() {
		super();
		redrawTimer = new Timer(1000 / maxFrameRate, this);
	}

	@Override
	protected void createAndShowGUI() {
		// Create frame
		frame = new JFrame("Civ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setBackground(Color.DARK_GRAY);

		// Create game field
		gf = new GameField(this);
		gf.setPreferredSize(new Dimension(1280, 960));
		gf.setBackground(Color.DARK_GRAY);

		// Create chat box
//		ChatBox cb = new ChatBox();
//		Controller.getController().getNetworkManager().addMessageHandler(cb);

		// Add everything
		frame.add(gf, BorderLayout.CENTER);
//		frame.add(cb, BorderLayout.PAGE_END);

		// Start drawing
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	public void show() {
		frame.setVisible(true);
		redrawTimer.start();
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void update() {
		//frame.repaint();
		gf.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		update();
	}
}
