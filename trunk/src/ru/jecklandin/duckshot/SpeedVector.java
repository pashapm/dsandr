package ru.jecklandin.duckshot;

public class SpeedVector {

	public float x;
	public float y;

	SpeedVector(float x, float y) {
		this.x = x;
		this.y = y;

	}
	public void add(SpeedVector s){
		x+=s.x;
		y+=s.y;
	}
	public void multiply(float m){
		x*=m;
		y*=m;
	}
	public double getLength(){
		return Math.hypot(x, y);
	}
}