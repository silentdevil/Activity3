import java.util.Scanner;

public class InputManager {
	
	private static Scanner scan = new Scanner(System.in);

	public int getPositiveInt(String id) throws Exception {
		int number = 0;
		System.out.print("Enter a positive integer for " + id + ": ");
		try {
			number = scan.nextInt();
			if(number < 1)
				throw new RuntimeException("Input is not a positive number");
		} catch (NumberFormatException | java.util.InputMismatchException e) {
			throw new NumberFormatException("Input is not parsable");
		}
		return number;
	}

	public static String ordinal(int i) {
    String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
    	switch (i % 100) {
		    case 11:
		    case 12:
		    case 13:
		        return i + "th";
		    default:
		        return i + suffixes[i % 10];
    	}
	}

}