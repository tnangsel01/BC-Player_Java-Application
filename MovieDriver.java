import java.sql.Connection;
import java.util.Scanner;
import java.sql.*;

public class MovieDriver {

    //display contents of a movie 
    //Written by Hamza & Tenzin
    public static void readMovie() {
        try {
            Connection allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");

            Statement allStmt = allConn.createStatement();
            ResultSet allsql = allStmt
                    .executeQuery("SELECT * FROM movies, movie_data WHERE movies.movie_id = movie_data.movie_id");

            while (allsql.next()) {
                System.out.println(allsql.getString("movie_id"));
                System.out.println(allsql.getString("english_name"));
                System.out.println(allsql.getString("native_name"));
                System.out.println(allsql.getString("year_made"));
                System.out.println(allsql.getString("tag_line"));
                System.out.println(allsql.getString("movie_id"));
                System.out.println(allsql.getString("language"));
                System.out.println(allsql.getString("country"));
                System.out.println(allsql.getString("genre"));
                System.out.println(allsql.getString("plot"));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    //This method will return true if a movie is created.
    //Written by: Vianney Pham
    public static boolean createMovie() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/omdb";
            String user = "root";
            String password = "";
            Connection allConn = DriverManager.getConnection(url, user, password);

            // grab input from user for new movie details
            Scanner in = new Scanner(System.in);

            System.out.println("Please enter a Movie Name: ");
            String english_name = in.nextLine();

            System.out.println("Please enter the native name of the movie: ");
            String native_name = in.nextLine();

            System.out.println("Please enter a Movie Year: ");
            String year_made = in.nextLine();

            int nextMovie_id = 0;

            // Creating a statement to find the next index for the movie
            Statement allStmt = allConn.createStatement();

            // 3. Sql Query for the movie index of new movie
            ResultSet allsql = allStmt.executeQuery("SELECT max(movie_id) from movies");

            // assign the next movie ID to nextmovie_id variable
            if (allsql.next()) {
                nextMovie_id = allsql.getInt(1) + 1;
            }

            // statement for the insert query into the db
            String sql = "INSERT INTO movies (movie_id, native_name, english_name, year_made) VALUES (?, ?, ?, ?)";

            // create prepared statement for insertion
            PreparedStatement statement = allConn.prepareStatement(sql);
            statement.setLong(1, nextMovie_id);
            statement.setString(2, native_name);
            statement.setString(3, english_name);
            statement.setString(4, year_made);

            // print a message if added correctly
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("The Movie:" + native_name + " was successfully created!");
            }

            // close connections
            allsql.close();
            in.close();

            // catch for errors of db connection
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return true;
    }

    //return True if a movie was successfully deleted.
    //Written by Vianney Pham
    public static boolean deleteMovie() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/omdb";
            String user = "root";
            String password = "";
            Connection allConn = DriverManager.getConnection(url, user, password);

            // grab input from user for new movie details.
            Scanner in = new Scanner(System.in);

            //Prompt the user to enter a movie id to delete.
            System.out.println("Please enter a movie ID to delete a movie: ");
            int delete_id = in.nextInt();

            // 3. Sql Query for the movie index of new movie
            String query = "DELETE FROM movies where movie_id = ?";
            PreparedStatement preparedStmt = allConn.prepareStatement(query);
            preparedStmt.setInt(1, delete_id);

            //make sure the movie was deleted and display a successful message.
            int deleted = preparedStmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("The movie with ID:" + delete_id + " was successfully deleted!");
            }
            
            // close connections
            in.close();

            // catch for errors of db connection
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return true;
    }

    //This method will update a movie
    //Written by Raju & Sapana
    public void updateMovie(int movie_id, String title, String native_name) {
 public static void updateMovie(int movie_id, String native_name, String english_name, int year_made)
	{
        try{	
			Connection allConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/omdb", "root", "");
			Statement allStmt = allConn.createStatement();
			ResultSet allsql = allStmt.executeQuery("SELECT * FROM movies, movie_data WHERE movies.movie_id = movie_data.movie_id");
                        allStmt.executeUpdate(String.format("UPDATE movies SET native_name='%s', english_name='%s', year_made=%d WHERE movie_id=%d",native_name,english_name,year_made, movie_id));
//           
        }
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
    }

    public static void main(String[] args)
       {
           //createMovie();
           //readMovie();
           //deleteMovie();
        
        //Request user to enter Movie ID and new data for update movie written by Raju and Sapana
                Scanner sc= new Scanner(System.in);  
                
                
                //Request movie id from user for update movie data function  
                System.out.println("Enter movie ID to UPDATE a movie data- ");  
                int id= sc.nextInt();  
                sc.nextLine();
               
                System.out.println("Enter new English name- ");  
                String  englishname = sc.nextLine();   
                
                System.out.println("Enter new Native- ");  
                String  nativename = sc.nextLine();  
                
                System.out.println("Enter new year made- ");  
                int  yearmade= sc.nextInt();  
        
                //calling update movie
                updateMovie(id,englishname,nativename,yearmade);
       }
    }
