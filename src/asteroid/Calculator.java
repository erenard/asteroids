package asteroid;

import java.util.Vector;

import asteroid.model.Asteroid;
import asteroid.model.Universe;

public class Calculator extends Thread {
	private int first;
	private int last;
	private Universe universe;
	
	public Calculator(Universe universe, int first, int last) {
		this.universe = universe;
		this.last = last;
		this.first = first;
	}
	
	@Override
	public void run() {
		for(int i = first; i <= last; i++) {
			Vector<Asteroid> asteroids = universe.getAsteroids();
			Asteroid asteroid = asteroids.get(i);
			if(!asteroid.isExist()) continue;
			double x = asteroid.getX();
			double y = asteroid.getY();
			double dx = asteroid.getDx();
			double dy = asteroid.getDy();
			for(Asteroid other : asteroids) {
				if(!other.isExist() || other == asteroid) {
					continue;
				}
				double distX = other.getX() - asteroid.getX();
				double distY = other.getY() - asteroid.getY();
				double distX2 = Math.pow(distX, 2);
				double distY2 = Math.pow(distY, 2);
				double dist2 = distX2 + distY2;
//				if ((Math.pow(distX, 2) + Math.pow(distY, 2)) <= Math.pow(asteroid.getRadius() + other.getRadius(), 2)) {
//					synchronized (other) {
//						// Fusion
//						if(asteroid == universe.getSun() || other == universe.getSun()) {
//							System.out.println("Collision Soleil");
//						}
//						other.setExist(false);
//						dx = (asteroid.getDx() * asteroid.getMass() + other.getDx() * other.getMass()) / (asteroid.getMass() + other.getMass());
//						dy = (asteroid.getDy() * asteroid.getMass() + other.getDy() * other.getMass()) / (asteroid.getMass() + other.getMass());
//						double asteroidSurface = Math.PI * Math.pow(asteroid.getRadius(), 2);
//						double otherSurface = Math.PI * Math.pow(other.getRadius(), 2);
//						x = (other.getX() * otherSurface + asteroid.getX() * asteroidSurface) / (otherSurface + asteroidSurface);
//						y = (other.getY() * otherSurface + asteroid.getY() * asteroidSurface) / (otherSurface + asteroidSurface);
//						double radius = Math.sqrt((otherSurface + asteroidSurface) / Math.PI);
//						asteroid.setRadius(radius);
//						asteroid.setMass(asteroid.getMass() + other.getMass());
//						if (other == universe.getSun()) {
//							universe.setSun(other);
//						}
//					}
//				} else {
					double force = Universe.G * asteroid.getMass() * other.getMass() / dist2;
					dx += Math.signum(distX) * force * distX2 / dist2;
					dy += Math.signum(distY) * force * distY2 / dist2;
//				}
			}
			asteroid.setDx(dx);
			asteroid.setDy(dy);
			asteroid.setX(x);
			asteroid.setY(y);
		}
		System.out.flush();
	}
}
