package zombieMaze;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rohan
 */
public class file {

	/**
	 * Prints each line of the file fileName.
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 */
	public static void printFile(String fileName) {
		try {//A try statment is used when there is a possiblity something won't work
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			while (sc.hasNextLine()) {
				System.out.println(sc.nextLine());
			}
		} catch (Exception e) {

		}
	}

	/**
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 * @return the number of lines in fileName
	 */
	public static int getLengthOfFile(String fileName) {
		int length = 0;
		try {
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			while (sc.hasNextLine()) {
				sc.nextLine();
				length++;
			}
		} catch (Exception e) {

		}
		return length;
	}

	/**
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 * @return an array of Strings where each string is one line from the file
	 * fileName.
	 */
	public static String[] getWordsFromFile(String fileName) {
				int lengthOfFile = getLengthOfFile(fileName);
		String[] wordBank=new String[lengthOfFile];
		int i = 0;
		try {
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			for (i = 0; i < lengthOfFile; i++) {
			wordBank[i] = sc.nextLine();
			}
			return wordBank;
		} catch (Exception e) {
                    System.err.println(e);
			System.exit(55);
		}
		return null;
	}

	/**
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 * @return a String from file
	 */
	public static String getStringFromFile(String fileName) {
		String wordBank;
		int i = 0;
		try {//A try statment is used when there is a possiblity something won't work
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			wordBank = sc.nextLine();
			return wordBank;
		} catch (Exception e) {

		}
		return null;
	}

	public static double getDoubleFromFile(String fileName) {
		String wordBank;
		int i = 0;
		try {//A try statment is used when there is a possiblity something won't work
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			wordBank = sc.nextLine();
			return Double.parseDouble(wordBank);
		} catch (Exception e) {

		}
		return .1;
	}

	public static double getIntFromFile(String fileName) {
		String wordBank;
		int i = 0;
		try {//A try statment is used when there is a possiblity something won't work
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			wordBank = sc.nextLine();
			return Integer.parseInt(wordBank);
		} catch (Exception e) {

		}
		return .1;
	}

	//Pre: fileName contains the name of a txt file in current directory (folder)
	//Post: lines of text are written to fileName

	public static void writeToFile(String fileName, String stuff) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			output.write(stuff);
			output.newLine();//This is needed to get to the next line
			output.close();//file must be closed when you are done
		} catch (Exception e) {

		}
	}

	public static void writeIntToFile(String fileName, int stuff) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			output.write(stuff + "");
			output.newLine();//This is needed to get to the next line
			output.close();//file must be closed when you are done
		} catch (Exception e) {

		}
	}

	public static void writeDoubleToFile(String fileName, double stuff) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			output.write(stuff + "");
			output.newLine();//This is needed to get to the next line
			output.close();//file must be closed when you are done
		} catch (Exception e) {

		}
	}

	public static void writeIntArrayToFile(String fileName, int[] intArray) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			for (int i = 0; i < intArray.length; i++) {
				output.write(intArray[i] + "");
				output.newLine();//This is needed to get to the next line
			}
			output.close();//file must be closed when you are done
		} catch (Exception e) {

		}

	}
	public static void writeIntArrayListToFile(String fileName, ArrayList<Integer> intArray) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			for (int i = 0; i < intArray.size(); i++) {
				output.write(intArray.get(i) + "");
				output.newLine();//This is needed to get to the next line
			}
			output.close();//file must be closed when you are done
		} catch (Exception e) {

		}

	}
	public static int[] getIntArrayFromFile(String fileName) {
		String[] ints = getWordsFromFile(fileName);
		int[] integers = new int[getLengthOfFile(fileName)];
		for (int i = 0; i < ints.length; i++) {
			integers[i] = Integer.parseInt(ints[i]);
		}
		return integers;
	}
        	public static ArrayList<Integer> getArrayListIntFromFile(String fileName) {
		String[] ints = getWordsFromFile(fileName);
		Integer[] integers = new Integer[getLengthOfFile(fileName)];
		for (int i = 0; i < ints.length; i++) {
			integers[i] = Integer.parseInt(ints[i]);
		}
		ArrayList<Integer> returnMe= new ArrayList(0);
                for(int i=0;i<ints.length;i++){
                    returnMe.add(Integer.parseInt(ints[i]));
                }
	return returnMe;
                }
   //################ MAIN METHOD #######################
	//public static void main(String [] args){
	//  printFile("words.txt");

////      	Goal 1: Correctly identify the lenght of the file
//      	int length = getLengthOfFile("words.txt");
//      	System.out.println("File has " + length + " lines");
//      	//Goal 2: Get an array of all the words from the file
//      	String [] fileWords = getWordsFromFile("words.txt");
//         for(int i = 0; i < fileWords.length; i++){
//              System.out.println( "fileWords[" + i + "] = " + fileWords[i]); 
//      }
//      	Goal 3: Write entire list of words to a new file
	// writeToFile("highscore.txt");
}//End of Main
//}
