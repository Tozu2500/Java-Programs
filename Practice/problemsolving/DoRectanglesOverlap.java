package com.tozu.problemsolving;

public class DoRectanglesOverlap {

	static class Point {
		int x;
		int y;
		
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	// This method returns true, if two rectangles (l1, r1) and (l2, r2) overlap
	static boolean doOverlap(Point l1, Point r1, Point l2, Point r2) {
		// If one rectangle is to the left of the other
		if (l1.x > r2.x || l2.x > r1.x)
			return false;
		
		// If one rectangle is above the other
		if (r1.y > l2.y || r2.y > l1.y)
			return false;
		
		return true;
	}

	public static void main(String[] args) {
		Point l1 = new Point(50, 10);
		Point r1 = new Point(10, 0);
		Point l2 = new Point(5, 5);
		Point r2 = new Point(5, 0);
		
		if (doOverlap(l1, r1, l2, r2)) {
			System.out.println("Rectangles overlap each other");
		} else {
			System.out.println("Rectangles don't overlap each other");
		}
	}
}
