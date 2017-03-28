import java.util.concurrent.*;
import java.util.*;
public class OutputManager {

	private Queue <String> stringQueue = new PriorityQueue<>(10);
	private boolean isRacing;
	Thread t1;
	public OutputManager() {
		t1 = new Thread(() -> {
			while(!isRacing) {
				printHead();
			}
		});
		t1.setDaemon(true);
		t1.start();
	}
	
	private void printHead() {
		try {
			String head = stringQueue.poll();
			System.out.print(head = (head != null) ? (head + "\n") : "");
			Thread.sleep(10);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void printQueue(String str) {
		stringQueue.offer(str);
	}
	
}