package asteroid.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import asteroid.Animator;
import asteroid.OptionManager;
import asteroid.model.Asteroid;
import asteroid.model.Universe;

public class Frame extends JFrame implements /*KeyListener, MouseListener,*/ ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;

	private Panel panel;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem exit;
	private JMenu languageMenu;
	private JMenuItem animeMenu;
	private JMenuItem frLanguage;
	private JMenuItem enLanguage;
	private Properties messages;
	private Universe universe;
	private Animator animator;

	public Frame() {
		//Chargement du fichier de langue
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("asteroid/properties/messages.properties");
		messages = new Properties();
		try {
			messages.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Menu Principal
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		exit = new JMenuItem();
		exit.addActionListener(this);
		fileMenu.add(exit);
		menuBar.add(fileMenu);

		//Language
		languageMenu = new JMenu("Language");
		frLanguage = new JMenuItem("Français");
		frLanguage.addActionListener(this);
		languageMenu.add(frLanguage);
		enLanguage = new JMenuItem("English");
		enLanguage.addActionListener(this);
		languageMenu.add(enLanguage);
		menuBar.add(languageMenu);

		//Anime
		animeMenu = new JMenuItem("Anime");
		animeMenu.addActionListener(this);
		menuBar.add(animeMenu);

		//Fenetre
		setSize(400, 400);
		Container container = getContentPane();
		panel = new Panel();
		container.add(panel);
		container.add(menuBar, "North");
//		this.addKeyListener(this);
//		this.addMouseListener(this);
		this.addWindowListener(this);
		setLanguage(OptionManager.getInstance().getProperty(OptionManager.LANGUAGE));
		universe = new Universe();
		animator = new Animator(universe, this);
		panel.setUniverse(universe);
		animator.start();
	}
	
	private void setLanguage(String language) {
		fileMenu.setText(messages.getProperty("Menu1."+language));
		exit.setText(messages.getProperty("Menu1.4."+language));
		setTitle(messages.getProperty("Title."+language));
		OptionManager.getInstance().setProperty(OptionManager.LANGUAGE, language);
	}
	
	public void refreshView() {
		List<Asteroid> asteroids = universe.getAsteroids();
		double surface = (universe.getXMax() - universe.getXMin()) * (universe.getYMax() - universe.getYMin());
		setTitle(universe.getAge() + ": " + asteroids.size() + " / " + surface + "km2");
		panel.repaint();
	}
	
	private void closeWindow() {
		OptionManager.getInstance().save();
		System.exit(0);
	}
	//ActionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source.equals(frLanguage)) {
			setLanguage("FR");
		} else if(source.equals(enLanguage)) {
			setLanguage("EN");
		} else if(source.equals(exit)) {
			closeWindow();
		} else if(source.equals(animeMenu)) {
			if(animator.isWaiting()) {
				animeMenu.setText("Stop");
				animator.setWaiting(false);
			} else {
				animeMenu.setText("Anime");
				animator.setWaiting(true);
			}
		}
		refreshView();
	}
/*
	//KeyboardListener
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	//MouseListener
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
*/
	//WindowListener
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
		closeWindow();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
}
