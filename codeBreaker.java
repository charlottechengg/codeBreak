/**
 * About:codeBreaker
 * @author JoeyFang & CharlotteCheng
 *Teacher: Mr.Anandarajan/Ms.R
 *Date:03/03/2018
 */
package Assignments;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
public class codeBreaker {

	static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) {
		final int codeSize = 4; 
		final int rounds = 10; //final values rounds and codeSize
		final String colours = "GRBYOP"; //allowable colours option
		String[] code = createCode("GRBYOP",codeSize);  //calls the function createCode to generate a random secrete code
		String[][] guess = new String[rounds][codeSize];
		String[][] clues = new String[rounds][codeSize];  //declare guess and clues
		initiate2D(guess); 
		initiate2D(clues); //initialize all elements in 2 arrays as blank space
		String input;  //placeholder for user's input in one string variable
		String[] inputAr;  //placeholder for user's input after converting to string array
		String[] roundClue;
		boolean valid; //validity of user's input
		String[][] display;
		boolean lose = true; //a variable that checks if the user is lost or not

		for(int i = 0; i < rounds; i++){
			do{
				System.out.println("Please enter your guess of length " + codeSize + " using the letters " + colours + ":");
				input = scan.nextLine();
				inputAr = new String[input.length()]; //initialize inputAr at the size of input(how many characters)
				for(int a = 0; a < input.length(); a++){
					inputAr[a] = Character.toString(input.charAt(a)); //convert string input to array
				}
				valid = valid(inputAr,colours,codeSize); //checks the validity of input
				if(valid){
					guess[i] = inputAr; //only if the input is valid, store it in guess
				}
				else{
					System.out.println("Invalid data, please try again\n"); //error message
				}
			}while(valid == false); //keeps prompting the users to enter characters until all of their input are fully valid
			System.out.println(); //avoid unwanted shared lines
			
			roundClue = clue(code,inputAr); //invokes the method clue to automatically generate clue for each try of the user.
			//note: the method findFullyCorrect, removeFullyCorrect, findColorCorrect are called inside this method
			clues[i] = roundClue;			
			System.out.println(); //avoid unwanted shared line
			display = displayGame(guess,clues); //calls the method displayGame, which returns a 2d array string that can be used to print 
			print2DAr(display); //calls the method and passes display to it, which will be printed
			if(Arrays.equals(inputAr, code)){  //this condition will be checked for 10 times, if the inputAr is the same as the code
				System.out.println("Congratulations! It took you " + (i+1) + " guesses to find the code.");
				lose = false;  //avoid running the lose message
				break; //the loop will be broken
			}
		}
		if(lose){ //lose message
			System.out.print("I'm sorry you lose. The correct code was " + 
			printStrArr(code)); //calls the method printStrArr which prints the array code
		}
	}
	
	/**
	 * Returns the a randomly generated array secrete code. 
	 *
	 * @param valid options for color, size of the code
	 * @return code a randomly generated array of length size of 
	 * 			    single character strings comprised of the characters in
	 * 			    the given string colour.
	 */
	public static String[] createCode(String colour, int size){ 
		String[] code = new String[size]; //size of 4
		Random random = new Random();
		for(int i = 0;i < size;i++) {
			int randomIndex = random.nextInt(colour.length());//returns a random number between 0 and the index of the last character of the string
			code[i] = Character.toString(colour.charAt(randomIndex)); //code picks up character at random index
		}
		return code;
	}
	
	/**
	 * Checks the size of guess, returns false if the size is invalid(not 4).
	 * Checks the validity of the elements within array guess, returns true if
	 * valid, returns false if invalid.
	 *
	 * @param guess  a array of users' guesses that is composed of single character string,
	 * @param colour a string of allowable color options
	 * @param size   an integer that indicates the required length of guess
	 * @return		 false if guess is not of the correct length or
	 * 				 if the guesses are of the correct length, returns true if
	 * 				 all the elements of string array guess are valid. 
	 * 				 Returns false if the array guess contains invalid color.
	 */
	public static boolean valid(String[] guess, String colour, int size){	
		boolean valid = true;
		if(guess.length != size){
			valid = false; 
			return valid;  // return false if input is wrong length
		}
		else{
			for(int i = 0; i < size; i++) {
				if(colour.contains(guess[i])){ //checks if the guess[i] is one of string character inside color
					valid = true;
				}
				else{
					valid = false;
				}
			}
			return valid;
		}
	}
	
	/**
	 * Returns a string that stores the correct formated header and all the 
	 * single character guesses and clues.
	 *
	 * @param guesses  
	 * @param clues 
	 * @return display array that could be used to printed to the display the
	 *                 current state of the game
	 */
	public static String[][] displayGame(String[][] guesses, String[][] clues){	
		String[][] display = new String[guesses.length + 2][guesses[0].length + clues[0].length + 1]; //merge two arrays 
		ArrayList<String> OneLine; // combine guess and clues on one line
		display[0]=new String[] {"Guess","  "," Clues"," ","  ","","","","",""};
		display[1]=new String[] {"****************","","","","","","",""};
		for (int row = 2; row < display.length; row++) {
			OneLine = new ArrayList<>(Arrays.asList(guesses[row-2])); // first two rows are occupied by the heading
			OneLine.add(" ");
			for(String str : clues[row -2]){ 
				OneLine.add(str);
			}
			display[row] = OneLine.toArray(display[row]); // put OneLine into display  

		}
		return display;
	}
	
	/**
	 * Returns an ArrayList clue that indicates the fully correct pegs in the 
	 * string array guess after comparisons with the code.
	 * 
	 * @param code randomly generated code by the computer
	 * @param guess user's input guess of one try
	 * @return clue an ArrayList that indicates the fully correct pegs in the string 
	 *              array guess
	 */
	public static ArrayList<String> findFullyCorrect(String[] code, String[] guess){
		ArrayList<String> clue = new ArrayList<>(); //declare ArrayList clue because the correct number is unknown yet
		for(int i = 0; i < guess.length; i++){
			if(code[i].equals(guess[i])){ //checks each one at the same position
				clue.add("b");
			}
		}
		return clue;

	}
	
	/**
	 * return an ArrayList clue as a copy of array guess after removing string 
	 * elements from the ArrayList clue that occupy at the same position with 
	 * the same element as the elements in array code.
	 * @param code  randomly generated code by the computer
	 * @param guess user's input guess of one try
	 * @return
	 */
	public static ArrayList<String> removeFullyCorrect(String[] code, String[] guess){
		ArrayList<String> guessLeft = new ArrayList<>(Arrays.asList(guess)); //declare a copy of guess so it won't have effect on the original array guess
		
		for(int i = 0; i < guess.length; i++){
			if(code[i].equals(guess[i])){
				guessLeft.remove(guess[i]); //remove fully correct elements
			}
		}
		return guessLeft;
	}
	
	/**
	 * Returns an ArrayList clue that indicates number of correct color that string 
	 * array guess has after comparisons with the code.
	 * @param code  randomly generated code by the computer
	 * @param guess user's input guess of one try
	 * @return clue an ArrayList that indicates the number of correct color that 
	 *              string array guess has.
	 */
	public static ArrayList<String> findColourCorrect(String[] code, String[] guess){
		ArrayList<String> clue = new ArrayList<>();
		ArrayList<String> codeCopy = new ArrayList<>(Arrays.asList(code));
		for(int i = 0; i < guess.length; i++){
			if(codeCopy.contains(guess[i])){ //checks if guess matches the code's color
				clue.add("w");  //if the color is correct, add w because it represents a peg that is correct in color
				codeCopy.remove(guess[i]); //remove the checked element from the copy, because contains does not care if this element is already checked or not
			}
		}
		return clue;
	}

	/**
	 * Returns an string array that indicates the number of pegs that are fully 
	 * correctly placed and pegs that are only correct in term of its color.
	 * 
	 * @param code  randomly generated code by the computer
	 * @param guess guess user's input guess of one try
	 * @return clue an string array clue that is the combination
	 * 			    of all clues including both fully correct and
	 * 				color correct clues.
	 */
	public static String[] clue(String[] code, String[] guess){ // combines the three methods above
		ArrayList<String> 
		black = findFullyCorrect(code,guess), 
		modGuessList = removeFullyCorrect(code,guess), // remove from guess
		modCodeList = removeFullyCorrect(guess, code), // remove from code
		white; 		
		String[] modGuess = new String[modGuessList.size()], // convert form array list to array, because methods takes array as parameters
				 modCode = new String[modCodeList.size()];
		
		modGuess = modGuessList.toArray(modGuess);
		modCode = modCodeList.toArray(modCode);
		
		white = findColourCorrect(modGuess,modCode);
		
		String[] clue = new String[black.size() + white.size()]; // combine black clue and white clue
	
		for(int i = 0; i < black.size(); i++){
			clue[i] = black.get(i);
		}
		int count = 0;
		for(int i = black.size(); i < clue.length; i++){ //white are placed in the end of of array by convention
			clue[i] = white.get(count); 
			count++;
		}
		return clue;
	
	}
	/**
	 * Return a string variable that stores the passed in string 
	 * array in the form of string with blank space in between.
	 * 
	 * @param  ar    any string array can be passed in. In this case, code is passed in
	 * @return print a string that stores blank space and the elements 
	 * 				 in the passed in string array alternatively.
	 */
	public static String printStrArr(String[] ar){
		String print = "";
		for(String i : ar){
			print = print + " " + i; //stores the elements in the code and blank space one after one
		}
		return print; //will be used as lose message
	}
	/**
	 * Prints the passed in 2D array in rectangular format
	 * 
	 * @param arr any 2D array can be passed in. In this case, display is passed in.
	 */
	public static void print2DAr(String[][] arr){
		for(int i = 0; i < arr.length; i++){
			for(int b = 0; b < arr[i].length; b++){
				System.out.print(arr[i][b] + " "); //prints in rectangular format
			}
			System.out.println();
		}
	}
	/**
	 * Initializes all the 2D arrays as empty.
	 * 
	 * @param arr any 2D array can be passed by. In this case, clue and guess are passed in.
	 */
	public static void initiate2D(String[][] arr){
		for(int i = 0; i < arr.length; i++){
			for(int a = 0; a < arr[i].length; a++){ 
				arr[i][a] = "";   //stores empty in it to replace 0
			}
		}
	}
}
//bonus:Create the list GGGG,...,PPPP of all candidate secret codes
//Start with GGRR.
//Repeat the following 2 steps:
//1) After you got the answer (number of black and number of white pegs)
//eliminate from the list of candidates all codes that would not have produced the same answer if they were the secret code.
//2) Pick the first element in the list and use it as new guess.

