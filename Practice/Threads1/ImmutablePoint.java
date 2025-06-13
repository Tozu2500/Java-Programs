package com.tozu.practice.concurrency.one;

public final class ImmutablePoint {
	
	private final int x;
	private final int y;
	
	public ImmutablePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public static void main(String[] args) {
		
		ImmutablePoint point = new ImmutablePoint(10, 20);
		System.out.println("X: " + point.getX());
		System.out.println("Y: " + point.getY());
		
	}

}
