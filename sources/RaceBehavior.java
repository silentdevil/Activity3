import java.util.*;
import java.util.concurrent.*;
import java.time.Instant;

public class RaceBehavior implements HorseBehavior {
	private CyclicBarrier barrier;
	private String name;
	private boolean boost;
	private RaceHorse horse;
	private Random rand = new Random();
	private OutputManager output;
	private java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("H:mm:ss.SSS");
	private RaceManager manager;

		Thread booster = new Thread(() -> {
			while(!horse.isFinished()) {
				RaceHorse lastHorse = manager.horseCurrentStanding().get(0);

				if(lastHorse.equals(horse) && 
						horse.getStrides() != manager.horseCurrentStanding().get(1).getStrides()) {
					goBoost();
				}

			}	
		});

	public RaceBehavior (RaceHorse horse, CyclicBarrier barrier, OutputManager output, RaceManager manager) {
		this.horse = horse;	
		this.barrier = barrier;
		this.output = output;
		this.manager = manager;
		name = horse.toString();
	}

	public void run() {
		try {
			toTheGate();
			barrier.await();
			booster.start();
			runRace();
			barrier.await();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}	

	public void goBoost() {
		synchronized(this) {
			try {
				boost = true;
				this.wait();
			} catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public void toTheGate() throws Exception {
		long timeStarted = System.nanoTime();
		while(horse.getStrides() < horse.getGateDistance()) {
			takbo();
			Thread.sleep(500);
		}

		horse.setStrides(0);
		System.out.printf("%s ran to the gate for %.2f seconds \n", 
					 	name, (float)(System.nanoTime() - timeStarted) / 1e9 + 5);
	}

	public String takbo() throws Exception {
		synchronized(this) {
			int a = (boost) ? 20: rand.nextInt(10);
			String output = (boost) ? " Boooost!" : "";
			horse.addStrides(a);

			this.notify();

			boost = false;			
			output = (name + "ran " + a + " units total is = " + horse.getStrides() + output);
			return output = horse.isFinished() ? output.toUpperCase() + "\tFINISHED " + horse.getWarCry() : output;
		}
	}

	public void runRace() throws Exception {
		while(!horse.isFinished()) {
			output.printQueue("[" + timeFormat.format(Date.from(Instant.now())) + "]\t" + takbo());
			horse.setTimeEnded((double) System.nanoTime());
			Thread.sleep(500);
		}
	}

	public void setName(String name) {
		this.name = name;
	}
}