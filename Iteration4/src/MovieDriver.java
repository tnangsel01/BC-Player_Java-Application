import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieDriver {

	public boolean processMovieSong(){
		try{ 	
			Connection allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");

			/*
			 * 
		This method process the data in ms_test_data table in the database.
		This table contains the following columns.
		[1] native_name
		[2] year_made
		[3] title
		[4] execution_status
			 *
			 */

/*
* 
Case 1: 
The movie (native_name and year_made) does not exist in the "movies" table.
It creates an entry into "movies" table. 

Case 2:
The movie (native_name and year_made) exists in "movies" table.
It ignores the creation of the movie in "movies" table.
*
*/
			// Creating a statement to find the next index for the movie
			Statement movieStmt = allConn.createStatement();

			// 3. Sql Query for the movie index of new movie
			ResultSet movieSql = movieStmt.executeQuery("SELECT max(movie_id) FROM movies");

			// assign the next movie ID to nextmovie_id variable
			int nextMovie_id = 0;
			if (movieSql.next()) {
				nextMovie_id = movieSql.getInt(1) + 1;
			}
//			PreparedStatement mstmt = allConn.prepareStatement("INSERT INTO movies(native_name, year_made) SELECT DISTINCT native_name, year_made FROM ms_test_data WHERE NOT EXISTS (SELECT 1 FROM movies WHERE native_name = ms_test_data.native_name and year_made = ms_test_data.year_made)");		
//			int moviesCreated = mstmt.executeUpdate();
			
//			if(moviesCreated > 0){
//				update execution_status to 'M created';
//			}else{
//				update execution_status to 'M ignored' ;
//			}
			
			
/*
* 
Case 3:
"title" doesn't exist in the "songs" table.
It creates an entry into the "songs" table.
		
Case 4:
"title" exist in the "songs" table.
It ignore the creation of an entry into "songs" table
*
*/
			

			// Creating a statement to find the next index for the movie
			Statement songStmt = allConn.createStatement();

			// 3. Sql Query for the movie index of new movie
			ResultSet songSql = songStmt.executeQuery("SELECT max(song_id) FROM songs");

			// assign the next movie ID to nextmovie_id variable
			int nextSong_id = 0;
			if (songSql.next()) {
				nextSong_id = songSql.getInt(1) + 1;
			}
//			PreparedStatement sstmt = allConn.prepareStatement("INSERT INTO songs(title) SELECT DISTINCT title FROM ms_test_data WHERE NOT EXISTS ( SELECT 1 FROM songs WHERE title = ms_test_data.title)");
//			int songsCreated = sstmt.executeUpdate();

//			if(songCreated > 0){
//				update execution_status to 'S created';
//			}else{
//				update execution_status to 'S ignored' ;
//			}
			
/*
* 
Case 5:
Once the movie and song exist in the respective tables, we also need to create a relationship between those.
If the relationship  between the "movie_id" and "song_id" does not exist in "movie_song" table, 
then an entry is created in the "movie_song" table.
			
Case 6:
If the relationship  between the "movie_id" and "song_id" already exists in "movie_song" table, 
then it ignores the creation of the entry in "movie_song" table.
*
*/
			
		
			PreparedStatement msStmt = allConn.prepareStatement("INSERT INTO movie_song (movie_id, song_id) SELECT movies.movie_id, songs.song_id FROM movies, songs WHERE NOT EXISTS (SELECT 1 FROM movie_song WHERE movies.movie_id = movie_song.movie_id and songs.song_id = movie_song.song_id)");
			int movieSongCreated = msStmt.executeUpdate();
			
//			if(msCreated > 0){
//				update execution_status to 'S created';
//			}else{
//				update execution_status to 'S ignored' ;
//			}
			
/*
 * 
Case 7:
Based on the what conditions are met, the "execution_status" will be updated with three of the following entries.
[1] M created
[2] M ignored
[3] S created
[4] S ignored
[5] MS created
[6] MS ignored
*
*/

		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	

	}
	return true;
	
	public static void main(String[] args) {
		processMovieSong();

	}
	



}
