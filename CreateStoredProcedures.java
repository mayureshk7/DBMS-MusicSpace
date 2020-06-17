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
		" SELECT s.songID, s.songName, a.albumName, aa.artistName, s.songDuration, s.songYear, g.genreName " +
		" FROM song AS s JOIN album AS a JOIN artist AS aa JOIN genre AS g" +
		" ON s.albumID = a.albumID AND a.artistID = aa.artistID AND s.genreID = g.genreID" +
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
		" FROM song AS s JOIN album AS a JOIN artist AS aa JOIN genre AS g" +
		" ON s.albumID = a.albumID AND a.artistID = aa.artistID AND s.genreID = g.genreID" +
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
		" FROM album AS a JOIN artist AS aa JOIN genre as g " +
		" ON a.artistID = aa.artistID AND a.genreID = g.genreID " +
		" WHERE a.albumName LIKE @album; " +
		" END";
		stmt.executeUpdate(sql);
		System.out.println("createGetAlbumDetails procedure created");
	}

	public void createGetArtistDetails(Statement stmt) throws Exception{
		String sql = "DROP PROCEDURE IF EXISTS getArtistDetails;";
		stmt.execute(sql);
		sql = " CREATE PROCEDURE getArtistDetails(IN input VARCHAR(50)) " +
		" BEGIN " +
		" SET @artist= concat(\"%\", input, \"%\");" +
		" SELECT * " +
		" FROM artist " +
		" WHERE artistName LIKE @artist; " +
		" END";
		stmt.executeUpdate(sql);
		System.out.println("createGetArtistDetails procedure created");
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

	public void createReviewAlbum(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS reviewAlbum;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE reviewAlbum(IN u INT, IN a VARCHAR(40), IN r VARCHAR(400)) " +
			" BEGIN " +
			" INSERT INTO albumreview (userID, albumID, albumReview) VALUE (u, a, r);" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("reviewAlbum procedure created");
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

	public void createUserAlbumLikes(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS userlikesalbum;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE userlikesalbum(IN u INT, IN a VARCHAR(40)) " +
			" BEGIN " +
			" INSERT INTO userlikesalbum (userID, albumID) VALUE (u, a);" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("userlikesalbum procedure created");
	}

	public void createGetSongReviews(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getSongReviews;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getSongReviews(IN s VARCHAR(40)) " +
			" BEGIN " +
			" SELECT songReview FROM songreview WHERE songID = s;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getSongReviews procedure created");
	}

	public void createGetAlbumReviews(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getAlbumReviews;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getAlbumReviews(IN a VARCHAR(40)) " +
			" BEGIN " +
			" SELECT albumReview FROM albumreview WHERE albumID = a;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getAlbumReviews procedure created");
	}

	public void createGetSongLikes(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getSongLikes;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getSongLikes(IN s VARCHAR(40)) " +
			" BEGIN " +
			" SELECT COUNT(*) as songCount FROM userlikessong WHERE songID = s;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getSongLikes procedure created");
	}

	public void createGetAlbumLikes(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getAlbumLikes;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getAlbumLikes(IN a VARCHAR(40)) " +
			" BEGIN " +
			" SELECT COUNT(*) as albumCount FROM userlikesalbum WHERE albumID = a;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getAlbumLikes procedure created");
	}

	public void createGetSongsFromPlaylist(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getSongsFromPlaylist;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getSongsFromPlaylist(IN p INT) " +
			" BEGIN " +
			" SELECT * FROM playlistSongMapping AS pm JOIN song AS s " +
			" ON pm.songID = s.songID " +
			" WHERE playlistID = p;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getSongsFromPlaylist procedure created");
	}

	public void createGetUserSongReviews(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getUserSongReviews;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getUserSongReviews(IN u INT) " +
			" BEGIN " +
			" SELECT * FROM songReview AS r JOIN song AS s " +
			" ON r.songID = s.songID " +
			" WHERE userID = u;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getUserSongReviews procedure created");
	}

	public void createGetUserAlbumReviews(Statement stmt) throws Exception{
			String sql = "DROP PROCEDURE IF EXISTS getUserAlbumReviews;";
			stmt.execute(sql);
			sql = " CREATE PROCEDURE getUserAlbumReviews(IN u INT) " +
			" BEGIN " +
			" SELECT * FROM albumReview AS r JOIN album AS a" +
			" ON r.albumID = a.albumID " +
			" WHERE userID = u;" +
			" END";
			stmt.executeUpdate(sql);
			System.out.println("getUserAlbumReviews procedure created");
	}

	public void createProcedures(Statement stmt) throws Exception{
			createFindSongDetails(stmt);
			createGetAlbumDetails(stmt);
			createGetArtistDetails(stmt);
			createReviewSong(stmt);
			createUserSongLikes(stmt);
			createGetSongReviews(stmt);
			createGetSongLikes(stmt);
			createReviewAlbum(stmt);
			createUserAlbumLikes(stmt);
			createGetAlbumReviews(stmt);
			createGetAlbumLikes(stmt);
			createGetSongsFromPlaylist(stmt);
			createGetUserSongReviews(stmt);
			createGetUserAlbumReviews(stmt);
	}
}
