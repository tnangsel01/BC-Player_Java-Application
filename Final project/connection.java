import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class connection {
	public static void readWord(){
		try{
			Connection allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bcplayer", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("-------------------------------------------------------------------------------------");


			PreparedStatement pstmt = allConn.prepareStatement("SELECT max(ID) FROM words");
			ResultSet id = pstmt.executeQuery();
			int maxID = 0;
			if(id.next()){
				maxID = id.getInt(1);
			}

			Statement stmt = allConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM words");

			for(int i = 0; i <= maxID; i ++){
				if(rs.next()){
					int word_id = rs.getInt("ID");
					String word = rs.getString("word");
					int chrt = rs.getInt("characters");
					System.out.println("ID : " + word_id + ", \t Word: " + word + ", \t Characters: " + chrt);
				}
			}
			rs.close();
			allConn.close();
			
		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
			System.exit(0);
		}

	}
	public static void addWord(String newWord, int character, String available){
		try{
			Connection allConn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bcplayer", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("-------------------------------------------------------------------------------------");

			PreparedStatement pstmt = allConn.prepareStatement("SELECT max(ID) FROM words");
			ResultSet id = pstmt.executeQuery();
			int maxID = 0;
			if(id.next()){
				maxID = id.getInt(1);
			}

			Statement stmt = allConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM words");

			for(int i = 0; i <= maxID; i ++){
				if(rs.next()){
					PreparedStatement pastmt = allConn.prepareStatement("SELECT word FROM words WHERE word = ?");
					pastmt.setString(1, newWord);
					ResultSet checkRs = pastmt.executeQuery();
					if(checkRs.next() == false){
						PreparedStatement addstmt = allConn.prepareStatement("INSERT INTO words (word, characters, available) VALUES (?, ?, ?)");
						addstmt.setString(1, newWord);
						addstmt.setInt(2, character);
						addstmt.setString(3, available);
						int wordAdded = addstmt.executeUpdate();
						if(wordAdded == 1){
							System.out.println(newWord + " word is added to the words table.");
						}
					}else{
						System.out.println("The word you are trying to add already exist!");
						System.exit(0);
					}

				}
			}
			rs.close();
			allConn.close();
			


		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
			System.exit(0);
		}

	}

	public static void updateWord(int inputID){
		try{
			Connection allConn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bcplayer", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("---------------------------------------------------------------------------------------");


			PreparedStatement dpstmt = allConn.prepareStatement("SELECT * FROM words WHERE ID = ?");
			dpstmt.setInt(1, inputID);
			ResultSet deleteRS = dpstmt.executeQuery();
			if(deleteRS.next()){

				Scanner wordScan = new Scanner(System.in);
				System.out.println("Enter the new word: ");
				String word = wordScan.next();

				Scanner charScan = new Scanner(System.in);
				System.out.println("Enter the new string length: ");
				int characters = charScan.nextInt();

				Scanner availScan = new Scanner(System.in);
				System.out.println("Enter the availability: ");
				String available = availScan.next();

				PreparedStatement update = allConn.prepareStatement("UPDATE `words` SET `word`=?,`characters`= ?,`available`= ? WHERE ID = ?");
				update.setString(1, word);
				update.setInt(2, characters);
				update.setString(3, available);
				update.setInt(4, inputID);
				update.execute();

				System.out.println("You have updated the word from the words table.");
			}
			deleteRS.close();
			allConn.close();
			

		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");	
			System.exit(0);
		}

	}
	public static void deleteWord(String delWord){
		try{
			Connection allConn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/bcplayer", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("----------------------------------------------------------------------------------------");

			PreparedStatement pstmt = allConn.prepareStatement("SELECT max(ID) FROM words");
			ResultSet id = pstmt.executeQuery();
			int maxID = 0;
			if(id.next()){
				maxID = id.getInt(1);
			}

			Statement stmt = allConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM words");

			for(int i = 0; i <= maxID; i ++){
				if(rs.next()){
					PreparedStatement dpstmt = allConn.prepareStatement("SELECT * FROM words WHERE word = ?");
					dpstmt.setString(1, delWord);
					ResultSet deleteRS = dpstmt.executeQuery();
					if(deleteRS.next()){
						PreparedStatement delst = allConn.prepareStatement("DELETE FROM words WHERE word = ?");
						delst.setString(1, delWord);
						int delRS = delst.executeUpdate();
						if (delRS > 0){
							System.out.println(delWord + " has been deleted from the words table.");
						}
					}else{
						System.out.println(delWord + " word doesn't exist!");
						System.exit(0);
					}

				}
			}
			rs.close();
			allConn.close();
			

		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
			System.exit(0);
		}

	}
}
