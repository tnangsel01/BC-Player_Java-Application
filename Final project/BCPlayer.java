import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class BCPlayer {

	public static void displayRules(int number){
		System.out.println("--------------------------------GAME RULES------------------------------------------------");
		System.out.println("I am thinking of " + number + " letter word. Try to guess it.\n"+
				"I will give you clues for each guess you make. \n"+
				"[Bulls]: You have guessed the correct letter and it's the correct location in the word.\n"+
				"[Cows]: You have guessed the correct letter, but not in the correct location in the word.\n"+
				"You will win by guessing all " + number + " letters in the correct locations.");
		System.out.println("==========================================================================================");
	}

	public static void adminOptions(int number){
		

		Scanner scann = new Scanner(System.in);
		System.out.println("Are you System Admin? ");
		System.out.print("Enter Y for Yes and N for No : ");
		String admin = scann.nextLine();

		if(admin.equalsIgnoreCase("N")){
			System.out.println("Sorry, you are not allowed to edit the database.");
			System.exit(0);
		}
			
		else{
			System.out.println("======================================================================================");
			System.out.println("\t\tFollowing methods are for system Admin use only.");
			System.out.println("\t\t\tMethod options");
			System.out.println("**************************************************************************************");
			System.out.println("\t\t\t[6] ---> Read the words table.");
			System.out.println("\t\t\t[7] ---> Add a new word.");
			System.out.println("\t\t\t[8] ---> Update an existing word.");
			System.out.println("\t\t\t[9] ---> Delete an existing word.");
			System.out.println("======================================================================================");

			for(int i = 1; i <= 4; i++){
				System.out.print("Enter a method call ID: ");
				int method = scann.nextInt();

				if(method == 6){
					connection.readWord();
				}else if(method == 7){
					Scanner chart = new Scanner(System.in);
					System.out.println("Enter word's length (3-5): ");
					int character = chart.nextInt();
					Scanner scn = new Scanner(System.in);
					System.out.println("Enter a new word : ");
					String newWord = scn.next();
					String available = "Yes";
					connection.addWord(newWord, character, available);
				}else if(method == 8){
					Scanner idScan = new Scanner(System.in);
					System.out.println("Enter the ID of the word you want to update: ");
					int inputID = idScan.nextInt();

					connection.updateWord(inputID);
					
				}else if(method == 9){
					Scanner delScan = new Scanner(System.in);
					System.out.println("Enter the word you want to delete: ");
					String delWord = delScan.next();
				
					connection.deleteWord(delWord);
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws SQLException {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Please enter your first name: ");
		String userName = scan.next();		
		System.out.println("___________________________________________________________________________________");

		System.out.println("\t\t\tWelcome to Bulls and Cows word game!");
		System.out.println("\n\t\tPlease enter the number of letters to play this game.");
		System.out.println("***********************************************************************************");
		System.out.println("\t\t\t[3] ---> 3 letters word.");
		System.out.println("\t\t\t[4] ---> 4 letters word.");
		System.out.println("\t\t\t[5] ---> 5 letters word.");
		System.out.println("-----------------------------------------------------------------------------------");	
		System.out.println("\t\t\t[0] ---> End Game.");
		System.out.println("\t\t\t[9] ---> Admin Options.");

		
		System.out.println("***********************************************************************************");

		System.out.print("Enter a number here : ");
		int numberWord = scan.nextInt();
		if (numberWord == 9) {
			adminOptions(numberWord);
		}
		displayRules(numberWord);

		System.out.println("\t\tYour "+ numberWord + " letters word guessing game starts now!");
		System.out.println("\t\tYou will have ten tries to win this game.");
		System.out.println("\t\t\tBest of Luck!");
		System.out.println("______________________________________________________________________________________");

		int guessNumber = 0; 
		int remainingGuesses = 10;
		boolean gameEnd = false;
		String quitGame = "0";
		int bullsCount = 0;
		int cowsCount = 0;
		

		char a = '\0', t = '\0';

		Connection allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bcplayer", "root", "");
		PreparedStatement pst = allConn.prepareStatement("SELECT word FROM words WHERE characters = ? AND available = ? ORDER BY RAND()");
		pst.setInt(1, numberWord);
		pst.setString(2, "Yes");
		ResultSet rs = pst.executeQuery();

		if(rs.next()){
			String target = rs.getString(1);

			// For the testing purposes.
			System.out.println(target);

			ArrayList<Character> targetWord = new ArrayList<Character>();
			for(int i = 0; i < target.length(); i++){
				t = target.charAt(i);
				targetWord.add(t);
			}

			int loseGame = 0;	
			do{
				if(guessNumber < 10){
					try{
						Scanner scn = new Scanner(System.in);
						System.out.print("Guess word: ");
						String inputWord = scn.nextLine();

						if(inputWord.equals(quitGame)){
							System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
							System.out.println("Thanks for playing. Goodbye!");
							quitGame = "1";
							break;
						}
						guessNumber++;
						remainingGuesses--;

						ArrayList<Character> guessWord = new ArrayList<Character>();
						for(int i = 0; i < inputWord.length(); i++){
							a = inputWord.charAt(i);
							guessWord.add(a);
						}

						for(int i = 0; i < targetWord.size(); i++){
							int compare = Character.compare(guessWord.get(i), targetWord.get(i));				
							if(compare == 0){
								bullsCount++;
								if(bullsCount == numberWord){
									PreparedStatement pt = allConn.prepareStatement("UPDATE words SET available = 'No' WHERE word = ?");
									pt.setString(1, target);
									pt.execute();
									
									System.out.println("Congratulations "+ userName + ", You have Won!");
									gameEnd = true;
								}
							} 
							if(targetWord.contains(guessWord.get(i))){
								cowsCount++;
							}
						}	
						if(bullsCount == numberWord){
							gameEnd = true;
						}else{
							System.out.println("Your Name: " + userName + ",  BullsCount: " + bullsCount + ",  CowsCount: " + cowsCount + ",  Remaining Guess left: " + remainingGuesses);
							bullsCount = 0;
							cowsCount = 0;
						}
					}catch (IndexOutOfBoundsException E){
						System.out.println("Your input word count is out of range.");
					}
				}else{
					System.out.println("You lose! ");
					loseGame++;
				}

			}while(!gameEnd); 
			if(loseGame == 0 && quitGame.equals("0")){
				System.out.println("You won after " + guessNumber + " guesses!");
			}
		}
		
		rs.close();
	
		System.out.println("------------------------Summary of "+ userName + " game record--------------------------");

		PreparedStatement pstmt = allConn.prepareStatement("SELECT max(ID) FROM words");
		ResultSet id = pstmt.executeQuery();
		int maxID = 0;
		if(id.next()){
			maxID = id.getInt(1);
		}

		Statement sStmt = allConn.createStatement();
		ResultSet rsS = sStmt.executeQuery("SELECT DISTINCT characters FROM words ORDER BY characters");
		int wordCount = 0;
		int winCount = 0;
		for(int i = 0; i <= maxID; i ++){
			if(rsS.next()){
				int character = rsS.getInt("characters");
				PreparedStatement wp = allConn.prepareStatement("SELECT COUNT(available) FROM words WHERE characters =? AND available = ?");
				wp.setInt(1, character);
				wp.setString(2, "No");
				ResultSet winRS= wp.executeQuery();
				if(winRS.next() == true){
					winCount = winRS.getInt(1);
				}
				PreparedStatement pword = allConn.prepareStatement("SELECT COUNT(word) FROM words WHERE characters = ?");
				pword.setInt(1, character);
				ResultSet wCount = pword.executeQuery();
				if(wCount.next() == true){
					wordCount = wCount.getInt(1);
					
					System.out.println("Length: "+ character + ", \tWord Count: "+ wordCount + ", \tWin Count: " + winCount);
				}
				
			}
		}
		
	}

}
