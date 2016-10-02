package neuralnet2;

import java.awt.Color;

public class ColorPoint {
	
	private double x;
	private double y;
	private Color c;
	
	
	public ColorPoint(double x, double y, Color c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}


	public double getX() {
		return x;
	}


	public double getY() {
		return y;
	}


	public Color getC() {
		return c;
	}

}
