import java.util.concurrent.CyclicBarrier;

public class RaceHorse extends Horse implements Runnable {

	private String warCry;
	private int gateDistance;
	private int trackDistance;
	private int strides;
	private  double timeMarker;
	public RaceHorse (CyclicBarrier barrier, OutputManager outputManager, RaceManager manager) {
		horseBehavior = new RaceBehavior(this, barrier, outputManager, manager);
	}

	public RaceHorse trackDistance (int val) {
		trackDistance = val;
		return this;
	}

	public RaceHorse gateDistance (int val) {
		gateDistance = val;
		return this;
	} 

	public RaceHorse warCry (String val) {
		warCry = val;
		return this;
	}

	public void setWarcry(String val)	{
		warCry = val;
	}

	public String getWarCry() {
		return warCry;
	}
	public int getTrackDistance() {
		return trackDistance;
	}

	public int getStrides() {
		return strides;
	}

	public int getGateDistance() {
		return gateDistance;
	}

	public void addStrides(int val) {
		strides+=val;
	}

	public void setStrides(int val) {
		strides = val;
	}

	public boolean isFinished() {
		return trackDistance <= strides;
	}

	public void setTimeEnded(double time) {
		timeMarker = time;
	}

	public double getTimeEnded() {
		return timeMarker;
	}





		
}