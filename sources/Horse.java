
public class Horse {
		
	private static int counter = 0;
	private final int id = counter++;
	private String name = id + "";

	protected HorseBehavior horseBehavior; 

	public String toString() { 
		return "[Horse " + id + "] "; 
	}

	public void run() {
		horseBehavior.run();
	}

	public void toTheGate() throws Exception {
		horseBehavior.toTheGate();
	}

	public void takbo() throws Exception {
		horseBehavior.takbo();
	}

	public void setName(String name) {
		horseBehavior.setName(name);
	}

	public String getName() {
		return name;
	}

}