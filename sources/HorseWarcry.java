import java.util.*;
public class HorseWarcry {

	private static Deque<String> cryStack;
	
	static {
		cryStack = new ArrayDeque<>(shuffle(cryCollection()));
	}

	
	
	public static List<String> cryCollection() {
		return Arrays.asList("WINTER IS COMING!","OORAH", "REBEL YELL!", "UUKHAI!", "BANZAI!",
							 "DEUS VULT!", "ALALA!", "WHOO! WHOO!", "Sons of Bronzebeard, hear me now!",
							 "For Khaz ModaaaaaAAAAAAAAAN!");
	} 

	public synchronized static String getWarCry() {
		String s = "";
		try {
			s = cryStack.pop();
		} catch(Exception ex) {
			cryStack.addAll(cryCollection());
			s = cryStack.pop();
		} finally {
			return s;
		}
	}
	
	private static <T> List<T> shuffle(List<T> list) {
		Collections.shuffle(list);
		return list;
	}
}