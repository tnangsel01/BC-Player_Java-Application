package Iteration7.Iteration7.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieDriver {
	public static boolean processMovieSongs(){
		Connection allConn = null;
		try{ 	
			allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			Statement allStmt = allConn.createStatement();
			ResultSet rs = allStmt.executeQuery("SELECT * FROM mspr_test_data");

			PreparedStatement pmaxid = allConn.prepareStatement("SELECT max(ID) FROM mspr_test_data");
			ResultSet maxIDrs = pmaxid.executeQuery();
			int maxID = 0;
			if( maxIDrs.next()){
				maxID = maxIDrs.getInt(1);
			}

			for (int i = 0; i < maxID; i++){
				
				rs.next();
				int movieCreated = 0;
				int songCreated = 0;
				int msCreated = 0;

				//Check and Create Movies
				String nativeNameTD = rs.getString("native_name");
				int yearMadeTD = rs.getInt("year_made");
			
				PreparedStatement ps = allConn.prepareStatement("SELECT * FROM movies WHERE native_name = ? AND year_made = ?");
				ps.setString(1, nativeNameTD);
				ps.setInt(2,yearMadeTD);

				ResultSet mrs = ps.executeQuery();
				mrs.beforeFirst();
				if (mrs.next() == true)
				{
					System.out.println("Movie already exists.");
				}
				else
				{
					PreparedStatement moviePS = allConn.prepareStatement("INSERT INTO movies (native_name, year_made, english_name) VALUES (?,?,?)");
					moviePS.setString(1,nativeNameTD);
					moviePS.setInt(2,yearMadeTD);
					moviePS.setString(3,nativeNameTD);

					int row = moviePS.executeUpdate();
					System.out.println("Movie has been added!");
					movieCreated += 1;
				}

				//Check and Create Songs
				String titleTD = rs.getString("title");
				titleTD = titleTD.replace("\"","");
				PreparedStatement song_ps = allConn.prepareStatement("SELECT title FROM songs WHERE title = ?");
				song_ps.setString(1, titleTD);

				ResultSet srs = song_ps.executeQuery();
				
				srs.beforeFirst();
				if (srs.next() == true)
				{
					System.out.println("Song already exists.");
				}
				else
				{
					PreparedStatement songPS = allConn.prepareStatement("INSERT INTO songs (title,theme) VALUES (?,?)");
					songPS.setString(1,titleTD);
					songPS.setString(2, "Null");

					int row = songPS.executeUpdate();
					System.out.println("Song has been added!");
					songCreated += 1;
				}
				
				//check and create MS
				PreparedStatement psmovie = allConn.prepareStatement("SELECT * from movies where native_name = ?");
				psmovie.setString(1, nativeNameTD);
				ResultSet movieRS = psmovie.executeQuery();

				PreparedStatement pssong = allConn.prepareStatement("SELECT * from songs where title = ?");
				pssong.setString(1, titleTD);
				ResultSet songRS = pssong.executeQuery();
				
				movieRS.next();
				songRS.next();
				int movieID = movieRS.getInt("movie_id");
				int songID = songRS.getInt("song_id");

				PreparedStatement checkMSTable = allConn.prepareStatement("SELECT * FROM movie_song where movie_id = ? AND song_id = ?");
				checkMSTable.setInt(1, movieID);
				checkMSTable.setInt(2, songID);
				ResultSet msExistTable = checkMSTable.executeQuery();
				
				msExistTable.beforeFirst();

				if (msExistTable.next() == true)
				{
					System.out.println("Movie and Song already exist in the movie_song table.");
				}
				
				else
				{
					PreparedStatement movieSongPS = allConn.prepareStatement("INSERT INTO movie_song(movie_id, song_id) VALUES (?, ?)");
					movieSongPS.setInt(1, movieID);
					movieSongPS.setInt(2, songID);

					int row = movieSongPS.executeUpdate();
					System.out.println("MS created!");
					msCreated += 1;
				}

				//Inserting into people if not existant

				int peopleCreated = 0;
				int songPeopleCreated = 0;
				int idTD = rs.getInt("id");

				String stageNameTD = rs.getString("stage_name");
			
				PreparedStatement people_ps = allConn.prepareStatement("SELECT * FROM people WHERE stage_name = ?");
				people_ps.setString(1, stageNameTD);

				ResultSet prs = people_ps.executeQuery();
				prs.beforeFirst();
				if (prs.next() == true)
				{
					System.out.println("People(s) already exists.");
				}
				else
				{
					PreparedStatement peoplePS = allConn.prepareStatement("INSERT INTO people (stage_name) VALUES (?)");
					peoplePS.setString(1,stageNameTD);

					int row = peoplePS.executeUpdate();
					System.out.println("People(s) has been added!");
					peopleCreated += 1;
				}

				//Create Song_People
				PreparedStatement pspeople = allConn.prepareStatement("SELECT * from people where stage_name = ?");
				pspeople.setString(1, stageNameTD);
				ResultSet peopleRS = pspeople.executeQuery();
				
				peopleRS.next();
				songRS.next();
				int peopleID = peopleRS.getInt("people_id");
				
				String role = rs.getString("role");

				PreparedStatement checkSPTable = allConn.prepareStatement("SELECT * FROM song_people where song_id = ? AND people_id = ? ",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				checkSPTable.setInt(1,songID);
				checkSPTable.setInt(2,peopleID);
				
				ResultSet psExistTabletest = checkSPTable.executeQuery();
			
				psExistTabletest.beforeFirst();

				if (psExistTabletest.next() == true)
                                    
				{
					System.out.println("People, Song, and Role already exist in the movie_song table.");
				}
				
                else
				{
					PreparedStatement peopleSongPS = allConn.prepareStatement("INSERT INTO song_people(song_id, people_id, role) VALUES (?, ?, ?)");
					peopleSongPS.setInt(1, songID);
					peopleSongPS.setInt(2, peopleID);
					peopleSongPS.setString(3, role);

					int row = peopleSongPS.executeUpdate();
					System.out.println("PS created!");
					songPeopleCreated += 1;
				}
				
				System.out.println(movieCreated);
				System.out.println(songCreated);
				System.out.println(msCreated);
				System.out.println(peopleCreated);
				System.out.println(songPeopleCreated);

				if(peopleCreated > 0 && songPeopleCreated > 0){
					PreparedStatement songPeopleUpdate = allConn.prepareStatement("UPDATE mspr_test_data SET PS_execution_status = 'P Created PS Created' WHERE id = ?");
					songPeopleUpdate.setInt(1,idTD);
					songPeopleUpdate.execute();
					System.out.println("C-C");
				} 
				else if (peopleCreated == 0 && songPeopleCreated > 0){
					PreparedStatement songPeopleUpdate = allConn.prepareStatement("UPDATE mspr_test_data SET PS_execution_status = 'P Ignored PS Created' WHERE id = ?");
					songPeopleUpdate.setInt(1,idTD);
					songPeopleUpdate.execute();
					System.out.println("I-C");
				} 
				else if (peopleCreated == 0 && songPeopleCreated == 0){
					PreparedStatement songPeopleUpdate = allConn.prepareStatement("UPDATE mspr_test_data SET PS_execution_status = 'P Ignored PS Ignored' WHERE id = ?");
					songPeopleUpdate.setInt(1,idTD);
					songPeopleUpdate.execute();
					System.out.println("I-I");
				} 

				if(movieCreated > 0 && songCreated > 0 && msCreated > 0){
					PreparedStatement movieStatusUpdateM = allConn.prepareStatement("UPDATE mspr_test_data SET execution_status = 'M Created S Created MS Created' WHERE title = ?");
					movieStatusUpdateM.setString(1,titleTD);
					movieStatusUpdateM.execute();
				} 
				else {
					PreparedStatement movieStatusUpdateM = allConn.prepareStatement("UPDATE mspr_test_data SET execution_status = 'M Ignored S Ignored MS Ignored' WHERE title = ?");
					movieStatusUpdateM.setString(1,titleTD);
					movieStatusUpdateM.execute();
				}

				if (movieCreated > 0 && songCreated == 0 && msCreated == 0){
					PreparedStatement movieStatusUpdateM = allConn.prepareStatement("UPDATE mspr_test_data SET execution_status = 'M Created S Ignored MS Ignored' WHERE title = ?");
					movieStatusUpdateM.setString(1,titleTD);
					movieStatusUpdateM.execute();
				}
				
				else if(movieCreated == 0 && songCreated > 0 && msCreated == 0){
					PreparedStatement movieStatusUpdateM = allConn.prepareStatement("UPDATE mspr_test_data SET execution_status = 'M Ignored S Created MS Ignored' WHERE title = ?");
					movieStatusUpdateM.setString(1,titleTD);
					movieStatusUpdateM.execute();
				}

				else if(movieCreated == 0 && songCreated == 0 && msCreated > 0){
					PreparedStatement movieStatusUpdateM = allConn.prepareStatement("UPDATE mspr_test_data SET execution_status = 'M Ignored S Ignored MS Created' WHERE title = ?");
					movieStatusUpdateM.setString(1,titleTD);
					movieStatusUpdateM.execute();
				}

				else if(movieCreated > 0 && songCreated > 0 && msCreated == 0){
					PreparedStatement movieStatusUpdateM = allConn.prepareStatement("UPDATE mspr_test_data SET execution_status = 'M Created S Created MS Ignored' WHERE title = ?");
					movieStatusUpdateM.setString(1,titleTD);
					movieStatusUpdateM.execute();
				}

				else if(movieCreated > 0 && songCreated == 0 && msCreated > 0){
					PreparedStatement movieStatusUpdateM = allConn.prepareStatement("UPDATE mspr_test_data SET execution_status = 'M Created S Ignored MS Created' WHERE title = ?");
					movieStatusUpdateM.setString(1,titleTD);
					movieStatusUpdateM.execute();
				}

				else if(movieCreated == 0 && songCreated > 0 && msCreated > 0){
					PreparedStatement movieStatusUpdateM = allConn.prepareStatement("UPDATE mspr_test_data SET execution_status = 'M Ignored S Created MS Created' WHERE title = ?");
					movieStatusUpdateM.setString(1,titleTD);
					movieStatusUpdateM.execute();
				}

				System.out.println("\n");
			}

		allConn.close();
		}

		catch(Exception ex){
			ex.printStackTrace();
		}
		return true;
	}
	public static void main(String[] args) {
		processMovieSongs();
	}
}