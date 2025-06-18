package practice.reflection;

public class Main {

	public static void main(String[] args) {
	    
		PersonExample first = new PersonExample("Alice", 30, "Loves Chocolate");
		PersonExample copy = new PersonExample("Placeholder", 0, "None");
		
		System.out.println("Before copy");
		copy.printInfo();
		
		ReflectionCopier.copyFields(first, copy);
		
		System.out.println("After copy:");
		copy.printInfo();

	}

}
