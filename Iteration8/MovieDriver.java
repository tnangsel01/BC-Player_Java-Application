import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieDriver {
	public static class Movie_Anagrams {
		private String native_name;
		private Integer year_made;
		private String anagrams;

		public Movie_Anagrams(String native_name, int year_made, String anagrams){
			this.native_name = native_name;
			this.year_made = year_made;
			this.anagrams = anagrams;
		}

		public String getNative_name() {
			return native_name;
		}

		public void setNative_name(String native_name) {
			this.native_name = native_name;
		}

		public Integer getYear_made() {
			return year_made;
		}

		public void setYear_made(Integer year_made) {
			this.year_made = year_made;
		}

		public String getAnagrams() {
			return anagrams;
		}

		public void setAnagrams(String anagrams) {
			this.anagrams = anagrams;
		}

		@Override
		public String toString() {
			String output = "";
			output += "native_name = " + native_name + ",\t" +	"year_made = " + year_made + ", \t" + "anagrams = " + anagrams
					+ "\n";
			return output;
		}

	}

	public static void processMovieAnagrams() throws SQLException{

		ArrayList maList = new ArrayList();

		Connection allConn = null;
		try {
			allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			System.out.println("Connection Successful. ");

		} catch (SQLException e){
			System.out.println("Connection lost, Please check connection!");
		}
		try {
			File file = new File("movie_anagrams.txt");
			Scanner scan = new Scanner(file);
			//Reading txt file line by line
			while (scan.hasNextLine()){
				String line = scan.nextLine();
				String[] line_array = line.split(",(\\s*)");
				String current_nativeName = line_array[0];
				int current_yearMade = Integer.parseInt(line_array[1]);
				String current_anagram = line_array[2];

				//Adding txt file data line by line into the maList
				Movie_Anagrams currentMovie = new MovieDriver.Movie_Anagrams(current_nativeName, current_yearMade, current_anagram);
				maList.add(currentMovie);
			}
			System.out.println("================================================================");
			System.out.println(maList.toString());
			System.out.println("================================================================");
			scan.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//Checking the movie txt file already exist in the movie table
		String current_name = null, current_anagram = null; 
		int current_yearMade = 0;

		for(int i = 0; i < maList.size(); i++){
			Movie_Anagrams native_nameTD = (Movie_Anagrams) maList.get(i);
			current_name = native_nameTD.getNative_name();
			Movie_Anagrams year_madeTD = (Movie_Anagrams) maList.get(i);
			current_yearMade = year_madeTD.getYear_made();
			Movie_Anagrams anagramTD = (Movie_Anagrams) maList.get(i);
			current_anagram = anagramTD.getAnagrams();

			//checking whether movies from txt file already exist in the movies table.
			PreparedStatement pMovie = allConn.prepareStatement("SELECT * FROM movies WHERE native_name = ? AND year_made = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pMovie.setString(1, current_name);
			pMovie.setInt(2, current_yearMade);
			ResultSet checkMovie = pMovie.executeQuery();

			if(checkMovie.next() == true){
				System.out.println(current_name + " already Exist in the movies table.");
			}else{
				PreparedStatement createMovie = allConn.prepareStatement("INSERT INTO movies (native_name, year_made)VALUES (?, ?)");
				createMovie.setString(1, current_name);
				createMovie.setInt(2, current_yearMade);
				int insertMovie = createMovie.executeUpdate();
				System.out.println(current_name + " is added into movies table.");
			}
			//Check and create of movie anagrams if doesn't exist already
			PreparedStatement pstmt = allConn.prepareStatement("SELECT * FROM movies WHERE native_name = ?");
			pstmt.setString(1, current_name);
			ResultSet rsid = pstmt.executeQuery();
			int movie_idTD = 0;
			while(rsid.next()){
				movie_idTD = rsid.getInt(1);
			}

			PreparedStatement pmID = allConn.prepareStatement("SELECT * FROM movies WHERE movie_id = ? AND native_name = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pmID.setInt(1, movie_idTD);
			pmID.setString(2, current_name);
			ResultSet rsID = pmID.executeQuery();
			rsID.next();



			PreparedStatement aMovie = allConn.prepareStatement("SELECT * FROM movie_anagrams WHERE movie_id = ? AND anagram = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			aMovie.setInt(1, movie_idTD);
			aMovie.setString(2, current_anagram);
			ResultSet checkAnagram = aMovie.executeQuery();

			if (checkAnagram.next() == true && rsID.next() == false){
				System.out.println(movie_idTD + " and " + current_anagram + " already existed.");
			}else {
				PreparedStatement createAnagram = allConn.prepareStatement("INSERT INTO movie_anagrams (movie_id, anagram) VALUES (?, ?)");
				createAnagram.setInt(1, movie_idTD);
				createAnagram.setString(2, current_anagram);
				int insertAnagram = createAnagram.executeUpdate();
				System.out.println(movie_idTD + " and " + current_anagram + " are added to the movie_anagrams table.");
			}

		}
		allConn.close();
	}
	public static void main(String[] args) throws SQLException {
		processMovieAnagrams();
	}

}
