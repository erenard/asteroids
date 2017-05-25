package asteroid.model;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import asteroid.Calculator;

public class Universe {
	private Vector<Asteroid> asteroids = new Vector<Asteroid>();

	private double xMin = -100000; //200 km2
	private double xMax = 100000;
	private double yMin = -100000;
	private double yMax = 100000;
	private long age = 0;
	private Asteroid sun = new Asteroid(0.0, 0.0, 10.0);
	private int NB_THREAD = Runtime.getRuntime().availableProcessors();

	public static final double G = 6.67 * Math.pow(10, -11);

	//public static final double G = 0.000667;
	//public static double G = 0.001;
	private static final int nbAsteroid = 49;

	public Universe() {
		Random random = new Random(0);
		for (int i = 0; i < nbAsteroid; i++) {
			double x = random.nextDouble() * (xMax - xMin) - xMax;
			double y = random.nextDouble() * (yMax - yMin) - yMax;
			asteroids.add(new Asteroid(x, y, 10 * random.nextDouble()));
		}
		sun.setColor(Color.RED);
		asteroids.add(sun);
		//Big Bang
		Calculator calculator = new Calculator(this, 0, asteroids.size() - 1);
		calculator.start();
		try {
			calculator.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(Asteroid asteroid : asteroids) {
			asteroid.setDx(asteroid.getDx() * -20.0);
			asteroid.setDy(asteroid.getDy() * -20.0);
		}
		sun.setDx(0d);
		sun.setDy(0d);
	}

	public Vector<Asteroid> getAsteroids() {
		return asteroids;
	}

	public void setAsteroids(Vector<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}

	public double getXMin() {
		return xMin;
	}

	public void setXMin(double min) {
		xMin = min;
	}

	public double getXMax() {
		return xMax;
	}

	public void setXMax(double max) {
		xMax = max;
	}

	public double getYMin() {
		return yMin;
	}

	public void setYMin(double min) {
		yMin = min;
	}

	public double getYMax() {
		return yMax;
	}

	public void setYMax(double max) {
		yMax = max;
	}

	public void anime() {
		age++;
		int nbThread = NB_THREAD;
		int size = asteroids.size();
		if(size - 1 < nbThread) nbThread = size;
		Calculator[] calculators = new Calculator[nbThread];
		int inc = asteroids.size() / nbThread;
		int first = 0;
		int last = inc - 1;
		for (int i = 0; i < nbThread; i++) {
			calculators[i] = new Calculator(this, first, last);
			calculators[i].start();
			first = last + 1;
			last = last + inc - 1;
		}

		try {
			for(Calculator calculator : calculators) {
				calculator.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		synchronized (asteroids) {
			//Phase de déplacement
			for(Asteroid asteroid : asteroids) {
				asteroid.anime();
			}

			//Phase de collisions
			for(Asteroid asteroid : asteroids) {
				if(!asteroid.isExist()) continue;
				for(Asteroid other : asteroids) {
					if(!other.isExist() || other == asteroid) {
						continue;
					}
					double distX = other.getX() - asteroid.getX();
					double distY = other.getY() - asteroid.getY();
					if ((Math.pow(distX, 2) + Math.pow(distY, 2)) <= Math.pow(asteroid.getRadius() + other.getRadius(), 2)) {
						// Fusion
						double x = asteroid.getX();
						double y = asteroid.getY();
						double dx = asteroid.getDx();
						double dy = asteroid.getDy();
						dx = (asteroid.getDx() * asteroid.getMass() + other.getDx() * other.getMass()) / (asteroid.getMass() + other.getMass());
						dy = (asteroid.getDy() * asteroid.getMass() + other.getDy() * other.getMass()) / (asteroid.getMass() + other.getMass());
						double asteroidSurface = Math.PI * Math.pow(asteroid.getRadius(), 2);
						double otherSurface = Math.PI * Math.pow(other.getRadius(), 2);
						x = (other.getX() * otherSurface + asteroid.getX() * asteroidSurface) / (otherSurface + asteroidSurface);
						y = (other.getY() * otherSurface + asteroid.getY() * asteroidSurface) / (otherSurface + asteroidSurface);
						//TODO Utiliser la formule volumique
						double radius = Math.sqrt((otherSurface + asteroidSurface) / Math.PI);
						asteroid.setRadius(radius);
						asteroid.setMass(asteroid.getMass() + other.getMass());
						if (other == sun) {
							sun = asteroid;
							asteroid.setColor(other.getColor());
						}
						other.setExist(false);
						asteroid.setDx(dx);
						asteroid.setDy(dy);
						asteroid.setX(x);
						asteroid.setY(y);
					}
				}
			}
			
			Iterator<Asteroid> asteroidIterator = asteroids.iterator();
			while(asteroidIterator.hasNext()) {
				Asteroid asteroid = asteroidIterator.next();
				if(!asteroid.isExist())
					asteroidIterator.remove();
			}
		}
		//G = (xMax - xMin) * (yMax - yMin) / (20000 * 20000);
	}

	public long getAge() {
		return age;
	}

	public Asteroid getSun() {
		return sun;
	}

	public void setSun(Asteroid sun) {
		this.sun = sun;
	}

	public void calcBounds() {
		xMax = 0;
		xMin = 0;
		yMin = 0;
		yMax = 0;
		for(Asteroid asteroid : asteroids) {
			xMin = Math.min(asteroid.getX(), xMin);
			xMax = Math.max(asteroid.getX(), xMax);
			yMin = Math.min(asteroid.getY(), yMin);
			yMax = Math.max(asteroid.getY(), yMax);
		}
	}
}
