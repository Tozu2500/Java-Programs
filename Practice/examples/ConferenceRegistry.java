package com.tozu.examples;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Let's imagine that we have a conference registration system where people can register
 * through multiple channels, like website, email, walk-in etc... and we need to build
 * the final attendee object list without duplicates 
 * 
 * */
public class ConferenceRegistry {
	
	record Attendee(String email, String name) {
		
		// Two registrations are the same person if the email matches (or vice versa in a real life scenario)
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Attendee other)) return false;
			return this.email.equalsIgnoreCase(other.email);
		}
		
		@Override
		public int hashCode() {
			return email.toLowerCase().hashCode();
		}
		
		@Override
		public String toString() {
			return name + " <" + email + ">";
		}
	}
	
	public static void main(String[] args) {
		
		// Registrations from 3 different channels with duplicates
		List<Attendee> webSignups = List.of(
				new Attendee("alice@example.com", "Alice"),
				new Attendee("bob@example.com", "Bob")
		);
		
		List<Attendee> emailSignups = List.of(
				new Attendee("ALICE@EXAMPLE.com", "Alice"),   // Duplicate but different case
				new Attendee("carol@example.com", "Carol")
		);
		
		List<Attendee> walkIns = List.of(
				new Attendee("bob@example.com", "Bob"),
				new Attendee("dave@example.com", "Dave")
		);

		// OUTPUT: Total registered should be 4, Alice, Bob, Carol and Dave (when removing duplicates)

		
		// Merging sources - HashSet deduplicates automatically
		Set<Attendee> registered = new HashSet<>();
		registered.addAll(webSignups);
		registered.addAll(emailSignups);
		registered.addAll(walkIns);
		
		System.out.println("Total registered: " + registered.size());
		
		// O(1) door check
		Attendee arriving = new Attendee("alice@example.com", "Alice");
		if (registered.contains(arriving)) {
			System.out.println("Welcome, " + arriving.name() + "! You are on the list.");
		} else {
			System.out.println("Sorry, you are not registered.");
		}
		
	}

}