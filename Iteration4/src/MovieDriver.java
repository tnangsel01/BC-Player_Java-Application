import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieDriver {

	public static void main(String[] args) {

		try{ 	
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			Statement stmt = conn.createStatement();
			ResultSet sql = stmt.executeQuery("SELECT * FROM ms_test_data"); 
			
			
		/*
		* 
		This method process the data in ms_test_data table in the database.
		This table contains the following columns.
		[1] movie_name
		[2] year_made
		[3] title
		[4] execution_status
		*
		*/

			while (sql.next()) {
				System.out.println(sql.getString("movie_name"));
				System.out.println(sql.getString("year_made"));
				System.out.println(sql.getString("title"));
				System.out.println(sql.getString("execution_status"));
			} 
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/*
	 * 
	Case 1: 
	The movie (native_name and year_made) does not exist in the "movies" table.
	It creates an entry into "movies" table. 
	 *
	 */
	//Statement sql = "SELECT * FROM movies, ms_test_data WHERE movies.native_name = ms_test_data.movie_name, movies.year_made = ms_test_data.year_made";
	
	//Statement query = "ALTER TABLE movies ADD movie_name VARCHAR NOT NULL, year_made YEAR NOT NULL";

	//PreparedStatement pstmt = conn.prepareStatement("UPDATA movies SET movie_name = ? where year_made = ?");
	/*
	 * 
	 *Case 2:
	The movie (native_name and year_made) exists in "movies" table.
	It ignores the creation of the movie in "movies" table.
	 *
	 */
	
	
	/*
	 * 
	 *Case 3:
	"title" doesn't exist in the "songs" table.
	It creates an entry into the "songs" table.
	 *
	 */
	
/*
 * 
Case 4:
"title" exist in the "songs" table.
It ignore the creation of an entry into "songs" table

Case 5:
Once the movie and song exist in the respective tables, we also need to create a relationship between those.
If the relationship  between the "movie_id" and "song_id" does not exist in "movie_song" table, 
then an entry is created in the "movie_song" table.

Case 6:
If the relationship  between the "movie_id" and "song_id" already exists in "movie_song" table, 
then it ignores the creation of the entry in "movie_song" table.

Case 7:
Based on the what conditions are met, the "execution_status" will be updated with three of the following entries.
[1] M created
[2] M ignored
[3] S created
[4] S ignored
[5] MS created
[6] MS ignored

*/

}
