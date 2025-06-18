package practice.reflection;

public class PersonExample {
	
	private String name;
	private int age;
	private String secret;
	
	public PersonExample(String name, int age, String secret) {
		this.name = name;
		this.age = age;
		this.secret = secret;
	}
	
	public void printInfo() {
		System.out.println("Name: " + name + ", Age: " + age + ", Secret: " + secret);
	}

}
