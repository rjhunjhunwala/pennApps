package zombieMaze;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

/**
 * Utility methods
 * @author rohan
 */

public class Utility {

	/**
	 * Returns a letter (or word the user types)
	 *
	 * @param Prompt for the user
	 * @return a string
	 */
	public static String getStringFromUser(String Prompt) {
		System.out.println(Prompt);
		Scanner sc = new Scanner(System.in);
		return (sc.nextLine());
	}

	/**
	 * gets a psuedo-random number safe for use in non cryptographic applications
	 * @param max is the maximum integer
	 * @return a random from 0 inclusive to max exclusive
	 */
	public static int getRandom(int max) {
Random r = new Random();
		return r.nextInt(max);
	}

		/**
	 * Gets a securely random number
	 * @param max is the maximum integer
	 * @return a random from 0 inclusive to max exclusive
	 */
	public static int getSecureRandom(int max){
	SecureRandom r=new SecureRandom();
	return r.nextInt(max);
}
	/**
	 * Gets an integer from user
	 *
	 * @param prompt
	 * @return an integer
	 */
	public static int getIntFromUser(String prompt) {
		System.out.println(prompt);
		Scanner sc = new Scanner(System.in);
		return (sc.nextInt());
	}
}
