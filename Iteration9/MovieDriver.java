import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
/*
 * Author Tenzin Nangsel.
 * Team Allegator
 * Iteration 9
 */

public class MovieDriver {


	private static void movieSummary() throws SQLException {
		Connection allConn = null;
		try {
			allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("----------------------------------------------------------------------------------------------------");
		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
		}
		//Find the max movie_id for the for loop limitation
		PreparedStatement pMax = allConn.prepareStatement("SELECT MAX(movie_id) FROM movies");
		ResultSet maxRS = pMax.executeQuery();

		int maxID = 0;
		if (maxRS.next()){
			maxID = maxRS.getInt(1);
		}

		//Select all the datas from movies table with order by year_made.
		PreparedStatement pMovie = allConn.prepareStatement("SELECT DISTINCT year_made FROM movies ORDER BY year_made", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rsMovie = pMovie.executeQuery();

		int countYear = 0;	
		for(int i = 0; i <= maxID; i++){
			if(rsMovie.next()){

				int year_madeTD = rsMovie.getInt("year_made");

				PreparedStatement pCount = allConn.prepareStatement("SELECT COUNT(year_made) FROM movies WHERE year_made = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				pCount.setInt(1, year_madeTD);
				ResultSet ctYear = pCount.executeQuery();

				if(ctYear.next() == true){
					countYear = ctYear.getInt(1);
					System.out.println(year_madeTD + ", " + countYear);
				}
			}
		}	
		allConn.close();
	}

	public static void displayMovieInfo(String role, String stage_name) throws SQLException {
		Connection allConn = null;
		try {
			allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("----------------------------------------------------------------------------------------------------");
		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
		}

		PreparedStatement pMax = allConn.prepareStatement("SELECT MAX(movie_id) FROM movies");
		ResultSet maxRS = pMax.executeQuery();

		int maxID = 0;
		if (maxRS.next()){
			maxID = maxRS.getInt(1);
		}

		PreparedStatement mMovie = allConn.prepareStatement("SELECT * FROM movies");
		ResultSet rsM = mMovie.executeQuery();

		for(int i = 0; i <= maxID; i++){
			if(rsM.next()){
				int movieID = rsM.getInt("movie_id");
				String nName = rsM.getString("native_name");
				String eName = rsM.getString("english_name");
				int yMade = rsM.getInt("year_made");

				PreparedStatement pP = allConn.prepareStatement("SELECT * FROM people");
				ResultSet rsP = pP.executeQuery();
				int peopleID = 0;
				if(rsP.next()){
					peopleID = rsP.getInt("people_id");
				}

				PreparedStatement pMovie = allConn.prepareStatement("SELECT * FROM movie_people WHERE movie_id = ? AND role = ?");
				pMovie.setInt(1, movieID);
				pMovie.setString(2, role);
				ResultSet rspMovie = pMovie.executeQuery();

				PreparedStatement pPeople = allConn.prepareStatement("SELECT * FROM people WHERE people_id = ?");
				pPeople.setInt(1, peopleID);
				ResultSet rsPeople = pPeople.executeQuery();

				if(rspMovie.next() == true && rsPeople.next() == true){
					System.out.println("Movie_ID: "+ movieID + " Native_Name: " + nName + " English_Name: " + eName + " Year_Made: " + yMade);
				}

			}
		}
	}

	public static void movieWithoutSong() throws SQLException{
		Connection allConn = null;
		try {
			allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("----------------------------------------------------------------------------------------------------");
		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
		}

		PreparedStatement pMax = allConn.prepareStatement("SELECT MAX(movie_id) FROM movies");
		ResultSet maxRS = pMax.executeQuery();

		int maxID = 0;
		if (maxRS.next()){
			maxID = maxRS.getInt(1);
		}

		PreparedStatement mMovie = allConn.prepareStatement("SELECT * FROM movies");
		ResultSet rsM = mMovie.executeQuery();

		int countMovies = 0;
		System.out.println("List of movies without any songs:");
		for(int i = 0; i <= maxID; i++){
			if(rsM.next()){
				int movieID = rsM.getInt("movie_id");
				String nName = rsM.getString("native_name");
				String eName = rsM.getString("english_name");
				int yMade = rsM.getInt("year_made");

				PreparedStatement pS = allConn.prepareStatement("SELECT * FROM songs");
				ResultSet rsS = pS.executeQuery();
				int songID = 0;
				if(rsS.next()){
					songID = rsS.getInt("song_id");
				}

				PreparedStatement pMS = allConn.prepareStatement("SELECT * FROM movie_song WHERE song_id = ? AND movie_id = ?");
				pMS.setInt(1, songID);
				pMS.setInt(2, movieID);
				ResultSet rsMS = pMS.executeQuery();

				if(rsMS.next() == false){
					System.out.println("Movie_ID: "+ movieID + "\t Native_Name: " + nName + " \t\t English_Name: " + eName + "\t\t Year_Made: " + yMade);
					countMovies++;
				}

			}
		}
		System.out.println("***********************************************************************");
		System.out.println("There are " + countMovies + " movies without any songs.");
	}

	public static void movieWithoutPeople() throws SQLException{
		Connection allConn = null;
		try {
			allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("----------------------------------------------------------------------------------------------------");
		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
		}

		PreparedStatement pMax = allConn.prepareStatement("SELECT MAX(movie_id) FROM movies");
		ResultSet maxRS = pMax.executeQuery();

		int maxID = 0;
		if (maxRS.next()){
			maxID = maxRS.getInt(1);
		}

		PreparedStatement mMovie = allConn.prepareStatement("SELECT * FROM movies");
		ResultSet rsM = mMovie.executeQuery();

		int countMovies = 0;
		System.out.println("List of movies without any songs:");
		for(int i = 0; i <= maxID; i++){
			if(rsM.next()){
				int movieID = rsM.getInt("movie_id");
				String nName = rsM.getString("native_name");
				String eName = rsM.getString("english_name");
				int yMade = rsM.getInt("year_made");

				PreparedStatement pPeople = allConn.prepareStatement("SELECT * FROM people");
				ResultSet rsPeople = pPeople.executeQuery();
				int peopleID = 0;
				if(rsPeople.next()){
					peopleID = rsPeople.getInt("people_id");
				}

				PreparedStatement pMS = allConn.prepareStatement("SELECT * FROM movie_people WHERE people_id = ? AND movie_id = ?");
				pMS.setInt(1, peopleID);
				pMS.setInt(2, movieID);
				ResultSet rsMS = pMS.executeQuery();

				if(rsMS.next() == false){
					System.out.println("Movie_ID: "+ movieID + "\t Native_Name: " + nName + " \t\t English_Name: " + eName + "\t\t Year_Made: " + yMade);
					countMovies++;
				}

			}
		}
		System.out.println("***********************************************************************");
		System.out.println("There are " + countMovies + " movies without any people.");
	}

	public static void movieInfo(String anagram) throws SQLException{
		Connection allConn = null;
		try {
			allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			System.out.println("Connection Successful. ");
			System.out.println("----------------------------------------------------------------------------------------------------");
		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
		}

		PreparedStatement pMax = allConn.prepareStatement("SELECT MAX(movie_id) FROM movies");
		ResultSet maxRS = pMax.executeQuery();

		int maxID = 0;
		if (maxRS.next()){
			maxID = maxRS.getInt(1);
		}

		PreparedStatement mMovie = allConn.prepareStatement("SELECT * FROM movies");
		ResultSet rsM = mMovie.executeQuery();

		System.out.println("==========================================================================================");
		for(int i = 0; i <= maxID; i++){
			if(rsM.next()){
				int movieID = rsM.getInt("movie_id");
				String nName = rsM.getString("native_name");
				String eName = rsM.getString("english_name");
				int yMade = rsM.getInt("year_made");

				PreparedStatement pMovie = allConn.prepareStatement("SELECT * FROM movie_anagrams WHERE movie_id = ? AND anagram = ?");
				pMovie.setInt(1, movieID);
				pMovie.setString(2, anagram);
				ResultSet rspAnagram = pMovie.executeQuery();

				if(rspAnagram.next() == true){
					System.out.println("Movie_ID: "+ movieID + "\t Native_Name: " + nName + "\t English_Name: " + eName + "\t Year_Made: " + yMade);
				}

			}
		}

	}
	public static void main(String[] args) throws SQLException {

		Scanner scan = new Scanner(System.in);  
		System.out.println("ENTER ONE OF THE FOLLOWING number");
		System.out.println("==========================================================================================");
		System.out.println("ID=1: Movie Summary.");
		System.out.println("ID=2: Movies information given role and stage name.");
		System.out.println("ID=3: Movies without any songs.");
		System.out.println("ID=4: Movies without any people.");
		System.out.println("ID=5: Movies information given its anagram.");
		System.out.println("==========================================================================================");

		for(int i = 1; i <= 5; i++){
			System.out.print("Enter a method call ID: ");
			int method = scan.nextInt();
			if (method == 1){

				movieSummary();
				System.out.println("==========================================================================================");
			}else if (method == 2){
				Scanner sc = new Scanner(System.in);
				System.out.println("Enter a role: ");
				String role = sc.nextLine();
				Scanner scn = new Scanner(System.in);
				System.out.println("Enter a stage_name: ");
				String stage_name = scn.next();
			
				displayMovieInfo(role, stage_name);
				
				System.out.println("==========================================================================================");
			}else if (method == 3){

				movieWithoutSong();
				System.out.println("==========================================================================================");
			}else if (method == 4){

				movieWithoutPeople();
				System.out.println("==========================================================================================");
			}else if (method == 5){
				
				System.out.print("Enter an Anagram: ");
				String anagram = scan.next();
				movieInfo(anagram);
				
				System.out.println("==========================================================================================");
			}
		}
		scan.close();

	}

}
