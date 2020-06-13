import java.util.Scanner;
import java.io.File;
import java.sql.*;
import java.io.FileNotFoundException;

public class CreateStoredProcedures{

	public void createFindSongDetails(Statement stmt) throws Exception{
		String sql = "DROP PROCEDURE IF EXISTS getSongDetails;";
		stmt.execute(sql);
		sql = " CREATE PROCEDURE getSongDetails(IN input VARCHAR(50)) " +
		" BEGIN " +
		" SET @song= concat(\"%\", input, \"%\");" +
		" SELECT s.songID, s.songName, a.albumName, aa.artistName " +
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

	public void createGetAlbumDetails(Statement stmt) throws Exception{
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

	public void createReviewSong(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS reviewSong;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE reviewSong(IN u INT, IN s VARCHAR(40), IN r VARCHAR(400)) " +
			" BEGIN " +
			" INSERT INTO songreview (userID, songID, songReview) VALUE (u, s, r);" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("reviewSong procedure created");
	}

	public void createUserSongLikes(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS userlikessong;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE userlikessong(IN u INT, IN s VARCHAR(40)) " +
			" BEGIN " +
			" INSERT INTO userlikessong (userID, songID) VALUE (u, s);" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("userlikessong procedure created");
	}

	public void CreateGetSongReviews(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getSongReviews;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getSongReviews(IN s VARCHAR(40)) " +
			" BEGIN " +
			" SELECT songReview FROM songreview WHERE songID = s;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getSongReviews procedure created");
	}

	public void CreateGetSongLikes(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getSongLikes;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getSongLikes(IN s VARCHAR(40)) " +
			" BEGIN " +
			" SELECT COUNT(*) as songCount FROM userlikessong WHERE songID = s;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getSongLikes procedure created");
	}

	public void createProcedures(Statement stmt) throws Exception{
			createFindSongDetails(stmt);
			createGetAlbumDetails(stmt);
			createReviewSong(stmt);
			createUserSongLikes(stmt);
			CreateGetSongReviews(stmt);
			CreateGetSongLikes(stmt);
	}
}
