package com.tozu.misc;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DigitalClock {

	public static void main(String[] args) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		while (true) {
			LocalTime now = LocalTime.now();
			String timeString = now.format(formatter);
			System.out.print("\r" + timeString);
			System.out.flush();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread interrupted!");
				break;
			}
		}
		
		System.exit(0);
	}

}