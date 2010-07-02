package ru.jecklandin.duckshot;

public class SpeedVector {

	public final double x;
	public final double y;

	SpeedVector(double x, double y) {
		this.x = x;
		this.y = y;

	}
	public SpeedVector add(SpeedVector s){
		return new SpeedVector(x+s.x,y+s.y);
	}
	public SpeedVector multiply(double m){
		return new SpeedVector(x*m,y*m);
	}
	public double getLength(){
		return Math.hypot(x, y);
	}
}