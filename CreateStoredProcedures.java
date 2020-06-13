import java.util.Scanner;
import java.io.File; 
import java.sql.*;
import java.io.FileNotFoundException;

public class CreateStoredProcedures{
	
	public static void createFindSongDetails(Statement stmt) throws Exception{
		String sql = "DROP PROCEDURE IF EXISTS getSongDetails;";
		stmt.execute(sql);
		sql = " CREATE PROCEDURE getSongDetails(IN input VARCHAR(50)) " +
		" BEGIN " +
		" SET @song= concat(\"%\", input, \"%\");" +
		" SELECT s.songName, a.albumName, aa.artistName " +
		" FROM song AS s JOIN album AS a JOIN artist AS aa " +
		" ON s.albumID = a.albumID AND a.artistID = aa.artistID " +
		" WHERE s.songName LIKE @song; " +
		" END";
		stmt.executeUpdate(sql);
		System.out.println("getSongDetails procedure created");
		
		sql = "DROP PROCEDURE IF EXISTS getFullSongDetails;";
		stmt.execute(sql);
		sql = " CREATE PROCEDURE getFullSongDetails(IN input VARCHAR(50)) " +
		" BEGIN " +
		" SET @song= concat(\"%\", input, \"%\");" +
		" SELECT * " +
		" FROM song AS s JOIN album AS a JOIN artist AS aa " +
		" ON s.albumID = a.albumID AND a.artistID = aa.artistID " +
		" WHERE s.songName LIKE @song; " +
		" END";
		stmt.executeUpdate(sql);
		System.out.println("getFullSongDetails procedure created");
	}
	
	public static void createGetAlbumDetails(Statement stmt) throws Exception{
		String sql = "DROP PROCEDURE IF EXISTS getAlbumDetails;";
		stmt.execute(sql);
		sql = " CREATE PROCEDURE getAlbumDetails(IN input VARCHAR(50)) " +
		" BEGIN " +
		" SET @album= concat(\"%\", input, \"%\");" +
		" SELECT * " +
		" FROM album AS a JOIN artist AS aa " +
		" ON a.artistID = aa.artistID " +
		" WHERE a.albumName LIKE @album; " +
		" END";
		stmt.executeUpdate(sql);
		System.out.println("createGetAlbumDetails procedure created");
	}
	
	public static void main(String[] args){
	    Connection conn = null;
		Statement stmt = null;
		String username, password, sql;
		String databaseURL = "jdbc:mysql://localhost:3306/musicspace";
		
		System.out.println("=== Welcome to MusicSpace Workbench ===");
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(databaseURL,"root","root");
			
			if(conn == null){
				System.out.println("Invalid user credentials");
				return;
			}else{
				System.out.println("Successfully logged In!!!");
			}
			stmt = conn.createStatement();
			createFindSongDetails(stmt);
			createGetAlbumDetails(stmt);
			
		}catch(Exception e){
			System.out.println("Something went wrong "+e);
		}
	}
}