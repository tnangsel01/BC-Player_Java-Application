import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.sql.Array;


/*
 * Author Tenzin Nangsel.
 * Team Allegator
 * Iteration 9
 */

public class FullyDressedMovie 
{




	public static void displayMovieInfo(int ID) throws SQLException 
        {
            
		Connection allConn = null;    
                
                try {
		allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
                    System.out.println("Connection Successful. ");
                    System.out.println("----------------------------------------------------------------------------------------------------");
		} catch (SQLException e){
                    System.out.println("Connection lost, Please check connection!");
		}
                    
                    
                    
                
//------------------Display From movie Table-----------------------------------------
		PreparedStatement movieInfo = allConn.prepareStatement("SELECT  movie_id,english_name,native_name,year_made FROM movies WHERE movie_id=?" ,ResultSet.TYPE_SCROLL_SENSITIVE,
    ResultSet.CONCUR_READ_ONLY);
                movieInfo.setInt(1,ID);
		ResultSet movie_table = movieInfo.executeQuery();
		          if(movie_table.next()){
				int movieID = movie_table.getInt("movie_id");
				String nName = movie_table.getString("native_name");
				String eName = movie_table.getString("english_name");
				int yMade = movie_table.getInt("year_made");
                                System.out.println("Movie ID: "+movieID+" Native Name: " + nName + " English Name: "+ eName + " Year Made: "+ yMade);
                                System.out.println("==========================================================================================");
                          }
                           
//-----------------------------------------------------------------------People Associated with Movie ----------------------------------------------------------------------

PreparedStatement mpeople = allConn.prepareStatement("SELECT  * FROM movie_people WHERE movie_id=?" ,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                mpeople.setInt(1,ID);
		ResultSet movie_people = mpeople.executeQuery();
		          if(movie_people.next()){
				int pplID = movie_people.getInt("people_id");
				String roleM = movie_people.getString("role");
				String sName = movie_people.getString("screen_name");
				 System.out.println("==============================People Associated with Movie============================================================");
                                System.out.println("People ID: "+ pplID +" Role  : " + roleM + " Screen Name: "+ sName );
                                System.out.println("==========================================================================================");
                          }


 //-----------------------------------------------------------------------------------Getting Song and People Associated with the movie id----------------------
                                        
                                PreparedStatement SongsID = allConn.prepareStatement("SELECT * FROM movie_song where movie_id=?", ResultSet.TYPE_SCROLL_SENSITIVE,
    ResultSet.CONCUR_READ_ONLY);
                                SongsID.setInt(1,ID);
				ResultSet songs_ID  = SongsID.executeQuery();
                                songs_ID.last();
                                System.out.println("Total Songs : " + songs_ID.getRow() );
                                 //int counter =songs_ID.getRow();
                                songs_ID.beforeFirst();
                                 System.out.println("Song ID" + "    Title ");  
                               while(songs_ID.next())
                                        {
                                            
                                            
                                             ArrayList<Integer> z = new ArrayList<>();
                                                       z.add(songs_ID.getInt("song_id")); 
                                                 int msongID = songs_ID.getInt("song_id");       
//                                                       System.out.println(z); 
                                                       
                                             
                                                       
                                            PreparedStatement songtitle = allConn.prepareStatement("SELECT title FROM songs WHERE song_id =?");
                                            songtitle.setInt(1, msongID);
                                            
                                            ResultSet SONGName = songtitle.executeQuery();
                                  
                                
                                                if(SONGName.next())
                                                {

                                                                ArrayList<String> titleArr = new ArrayList<>();
                                                                titleArr.add(SONGName.getString("title")); 
                                                                String ttl = SONGName.getString("title");
                                                                System.out.println(" " + z + "    " + titleArr + "    ");
                                                
                                                
                                                               
                                                 }
                                                
                                            
                                        }
                               




//----------------------------------------------------------------------Song People ID and Roles associated with movie ID----------------------------------------------------

     PreparedStatement songPeopleID = allConn.prepareStatement("SELECT * FROM movie_song where movie_id=?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                                songPeopleID.setInt(1,ID);
				ResultSet sp_ID  = songPeopleID.executeQuery();
                                
                                 System.out.println("Song People ID" + "          Role" );  
                               while(sp_ID.next())
                                        {
                                            
                                            
                                             ArrayList<Integer> y = new ArrayList<>();
                                                 y.add(sp_ID.getInt("song_id")); 
                                                 int psID = sp_ID.getInt("song_id"); 
                                                 
                                                PreparedStatement songrole = allConn.prepareStatement("SELECT * FROM song_people WHERE song_id =?", ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                                                    songrole.setInt(1, psID);

                                                        ResultSet SONGroles = songrole.executeQuery();
                                                        SONGroles.beforeFirst();
                                                                if(SONGroles.next())
                                                                    {


                                                                      ArrayList<String> roleArr = new ArrayList<>();
                                                                      roleArr.add(SONGroles.getString("role")); 
                                                                     String rll = SONGroles.getString("role");
                                                                     
                                                                     ArrayList<Integer> ppID = new ArrayList<>();
                                                                      ppID.add(SONGroles.getInt("people_id")); 
                                                                     int pID = SONGroles.getInt("people_id");
                                                                       System.out.println( ppID + "                    " + roleArr );
                                                                    }
                                                  
                                        }
                               
                                        
                       
                                
                                    
    //---------------------------------People Associated with each of the song-------------------------------------------------------
//				PreparedStatement songID = allConn.prepareStatement("SELECT song_id FROM movie_song where movie_id=?");
//                                songID.setInt(1,ID);
//				ResultSet song_id = songID.executeQuery();
//				if(song_id.next()){
//					int  songs = song_id.getInt("song_id");
//                                        PreparedStatement songpeople = allConn.prepareStatement("SELECT role,people_id FROM song_people WHERE song_id = ?");
//				songpeople.setInt(1, songs);
//                                ResultSet peopleIdSong = songpeople.executeQuery();
//                                if(peopleIdSong.next()){
//                                    String songpeoplerole= peopleIdSong.getString("role");    
//                                    int  songpeopleID= peopleIdSong.getInt("people_id"); 
//                                  System.out.println("Song Id: "+ songs + " Song People Role: " + songpeoplerole+ " Song People ID: " + songpeopleID);
//                                  System.out.println("==========================================================================================");
//                                }
//				}
    
//--------------------------List of Anagrams--------------------------------------------------------------------
System.out.println("==========================================================================================");
                PreparedStatement movieana = allConn.prepareStatement("SELECT *  FROM movie_anagrams WHERE movie_id=?");
                movieana.setInt(1,ID);
		ResultSet anagram_table = movieana.executeQuery();
		          while(anagram_table.next())
                          {				
				String movieAnagram = anagram_table.getString("anagram");				
                                System.out.println("Movie Anagram : "+ movieAnagram);
                                
                          }

        }     
        
        
        
        
public static void main(String[] args) throws SQLException 
                        {

		
				Scanner sc = new Scanner(System.in);
				System.out.println("Enter movie id : ");
				int id = sc.nextInt();
				
			
				displayMovieInfo(id);
				
				System.out.println("==========================================================================================");
			}
		
		

	

}

        
