package com.tozu.practicalexamples;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.TreeSet;

public class DepartureBoard {
	
	record Flight(String code, String destination, LocalTime departure, String gate) {
		private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");
		
		@Override
		public String toString() {
			return String.format("%-8s %-20s %s    Gate %-4s, ",
					code, destination, departure.format(FMT), gate);
		}
	}

	public static void main(String[] args) {
		
		// Tree sorted by departure time
		TreeSet<Flight> board = new TreeSet<>(
				Comparator.comparing(Flight::departure)
						.thenComparing(Flight::code));
		
		board.add(new Flight("AY103", "London Heathrow", LocalTime.of(14, 30), "B12"));
		board.add(new Flight("SK442", "Stockholm Arlanda", LocalTime.of(13, 15), "A3"));
		board.add(new Flight("LH2271", "Frankfurt", LocalTime.of(15, 50), "C7"));
		board.add(new Flight("AY017",  "New York JFK",      LocalTime.of(13, 15), "A5")); // same time as SK442
        board.add(new Flight("FR1234", "Barcelona",         LocalTime.of(16, 40), "B2"));
        board.add(new Flight("AY461",  "Paris CDG",         LocalTime.of(14, 55), "A8"));
        
		/*
		for (Flight flight : board) {
			System.out.println(flight);
		}
		*/
        

        // Full board
        System.out.println("\nDepartures:\n");
        System.out.printf("%-8s %-20s %-5s %s%n", "Flight", "Destination", "Time", "Gate");
        System.out.println("-".repeat(50));
        board.forEach(System.out::println);
        
        // Next departure
        System.out.println("\nNext departure : " + board.first());
        
        // Flights onwards from 14:00
        LocalTime windowStart = LocalTime.of(14, 0);
        LocalTime windowEnd = LocalTime.of(16, 0);
        
        // Sentinel flight to drive range query -- only departure time matters for comparison
        Flight from = new Flight("", "", windowStart, "");
        Flight to = new Flight("", "", windowEnd, "");
        
        System.out.println("\nFlights departing 14:00 - 16:00:");
        board.subSet(from, true, to, false)
        		.forEach(f -> System.out.println(" " + f));
        
        // Time until last flight
        LocalTime now = LocalTime.of(13, 0);
        LocalTime last = board.last().departure();
        System.out.printf("%nTime until last departure from %s: %d minutes%n", now, Duration.between(now, last).toMinutes());
	}

}
