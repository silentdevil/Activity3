import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class RaceManager {
	private List<RaceHorse> horseList;

	private InputManager im = new InputManager();
	private ScheduledExecutorService executor;
	private CyclicBarrier barrier;

	private int numHorse;
	private int trackDistance;
	private int gateDistance =  new Random().nextInt(50);
	private boolean isRacing = true;
	
	private OutputManager outputManager = new OutputManager();
		
	private RaceHorse winnerHorse;

	public synchronized List<RaceHorse> horseCurrentStanding () {
		return horseList.parallelStream()
					 	.sorted((n1,n2) -> {
							 	return Integer.compare(n1.getStrides(), n2.getStrides());
					 	 	})
					    .collect(Collectors.toList());
	}

	private void createHorses() {
		for(int i = 0 ; i < numHorse; i++) {
			horseList.add(new RaceHorse(barrier, outputManager, this).
						trackDistance(trackDistance).gateDistance(gateDistance).warCry(HorseWarcry.getWarCry()));
		}
	}

	private void setUpRace(int numHorse,int distance) {
		executor = Executors.newScheduledThreadPool(numHorse + 1);
		this.trackDistance = distance;
		this.numHorse = numHorse;
		horseList = new ArrayList<>();
		barrier = new CyclicBarrier(numHorse, () -> {
				if(!isRacing) {
					getFinishers();
					return;
				}

				System.out.println("All horses are at the gate");
				countDown(3);
			});

		createHorses();

		System.out.println("Waiting for all horses to go to the gate...");
	}
	
	public void startRace() throws Exception {
		setUpRace(im.getPositiveInt("number of horses"), im.getPositiveInt("length of race track"));
		horseList.parallelStream().forEach((h) -> executor.execute(h));
		winnerHorse = getWinner();
	}

	private RaceHorse getWinner() throws ExecutionException, InterruptedException {
		Future<RaceHorse> futureWinner = executor.submit(() -> {
				while(isRacing) {
					Optional <RaceHorse> optinalWinner = horseList.stream()
														      .filter(RaceHorse::isFinished)
														      .sorted((n1,n2) -> Double.compare(n1.getTimeEnded(), n2.getTimeEnded()))
														      .findFirst();
					if(optinalWinner.isPresent()) {	
						executor.shutdown();
						isRacing = false;
						return optinalWinner.get();
					}
				}
				return winnerHorse;
		});
		return futureWinner.get();
	}	
	
	private void getFinishers() {
		List<RaceHorse> finishers = horseCurrentStanding().stream()
														   .sorted((n1,n2) -> Double.compare(n1.getTimeEnded(), n2.getTimeEnded()))
														   .collect(Collectors.toList());

		System.out.println(winnerHorse.toString() + " won!" +winnerHorse.getWarCry());

		for(int i = 0; i < finishers.size(); i++) {
			System.out.println(InputManager.ordinal(finishers.indexOf(finishers.get(i)) + 1) 
								+ " " + finishers.get(i) + finishers.get(i).getWarCry());
		}
	}

	private void countDown(int time) {
		for(int i = time ; i > 0; i--) {
			System.out.println("Count down......" + i);
			try {
				Thread.sleep(1000);
			} catch(Exception ex) {}
		}
		System.out.println("Go!!");
	}

	public static void main(String[] args) {
		try {
			new RaceManager().startRace();
		} catch(Exception ex) {
			System.err.println("Input is not a positive number");
		}
	}
}