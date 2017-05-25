package asteroid.model;

import java.awt.Color;

public class Asteroid {
	private boolean exist = true;
	private Double x = 0.0;
	private Double y = 0.0;
	private Double radius = 0.0;
	private Double mass = 0.0;
	private Double dx = 0.0;
	private Double dy = 0.0;
	private Color color = Color.WHITE;
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Double getDx() {
		return dx;
	}
	public void setDx(Double dx) {
		this.dx = dx;
	}
	public Double getDy() {
		return dy;
	}
	public void setDy(Double dy) {
		this.dy = dy;
	}
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double size) {
		this.radius = size;
	}
	
	public Asteroid(Double x, Double y, Double rayon_en_metre) {
		this.x = x;
		this.y = y;
		this.radius = rayon_en_metre;
		double volume = 4.0 / 3.0 * Math.PI * Math.pow(rayon_en_metre * 100, 3);
		this.mass = volume * 5; // Masse volumique de 5 g/cm3
	}
	
	public void anime() {
		x += dx;
		y += dy;
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	public Double getMass() {
		return mass;
	}
	public void setMass(Double mass) {
		this.mass = mass;
	}
}
