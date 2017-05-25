package asteroid.gui;

import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import asteroid.model.Asteroid;
import asteroid.model.Universe;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Universe universe;

	public Universe getUniverse() {
		return universe;
	}

	public void setUniverse(Universe universe) {
		this.universe = universe;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		double width = getSize().getWidth();
		double height = getSize().getHeight();
		graphics.fillRect(0, 0, (int) width, (int) height);
		if(universe != null) {
			Vector<Asteroid> asteroids = universe.getAsteroids();
			synchronized (asteroids) {
				universe.calcBounds();
				double xa = (width - 2) / (universe.getXMax() - universe.getXMin());
				double ya = (height - 2) / (universe.getYMax() - universe.getYMin());
				double xb = -1 * universe.getXMin() * xa;
				double yb = -1 * universe.getYMin() * ya;
				for(Asteroid asteroid : asteroids) {
					if(!asteroid.isExist()) continue;
					graphics.setColor(asteroid.getColor());
					int sizeX = (int) (asteroid.getRadius() * xa);
					int sizeY = (int) (asteroid.getRadius() * ya);
					int x = (int) (asteroid.getX() * xa + xb) - sizeX;
					int y = (int) (asteroid.getY() * ya + yb) - sizeY;
					graphics.drawOval(x, y,  2 * sizeX, 2 * sizeY);
				}

			}
		}
	}
}
