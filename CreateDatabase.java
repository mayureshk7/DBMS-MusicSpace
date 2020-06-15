import java.util.Scanner;
import java.io.File;
import java.sql.*;
import java.io.FileNotFoundException;

public class CreateDatabase {

	public void createArtistTable(Statement stmt) throws Exception{
		System.out.println("Creating Artist Table");
		String sql = "DROP TABLE IF EXISTS artist;";
		stmt.execute(sql);
		sql = "CREATE TABLE artist " +
                   "(artistID VARCHAR(25) PRIMARY KEY, " +
                   " artistName VARCHAR(200), " +
                   " artistTag VARCHAR(50), " +
                   " artistLocation VARCHAR(50), " +
                   " artistHotness DECIMAL(11,10));";
		stmt.execute(sql);
		System.out.println("Artist Table Created Successfully");
	}

	public void createLabelTable(Statement stmt) throws Exception{
		System.out.println("Creating label Table");
		String sql = "DROP TABLE IF EXISTS label;";
		stmt.execute(sql);
		sql = "CREATE TABLE label " +
                   "(labelID VARCHAR(25) PRIMARY KEY, " +
                   " labelName VARCHAR(50), " +
				   "labelStreet VARCHAR(50), " +
				   "labelCity VARCHAR(50), " +
				   "labelCountry VARCHAR(50)," +
				   "labelZip INT(5)," +
				   "labelPhone VARCHAR(10)," +
				   "labelEmail VARCHAR(20));";
		stmt.execute(sql);
		System.out.println("label Table Created Successfully");
	}

	public  void createGenreTable(Statement stmt) throws Exception{
		System.out.println("Creating genre Table");
		String sql = "DROP TABLE IF EXISTS genre;";
		stmt.execute(sql);
		sql = "CREATE TABLE genre " +
                   " (genreID INT PRIMARY KEY, " +
				   " genreName VARCHAR(20));";
		stmt.execute(sql);
		System.out.println("genre Table Created Successfully");
	}

	public  void createAlbumTable(Statement stmt) throws Exception{
		System.out.println("Creating albums Table");
		String sql = "DROP TABLE IF EXISTS albums;";
		stmt.execute(sql);
		sql = "CREATE TABLE album " +
                   "(albumID VARCHAR(25) PRIMARY KEY, " +
                   " albumName VARCHAR(200), " +
                   " albumYear INT, " +
                   " artistID VARCHAR(25), " +
				   " labelID VARCHAR(25), " +
				   " genreID INT, " +
				   " CONSTRAINT artistAlbumConstraint FOREIGN KEY (artistID) REFERENCES artist(artistID)," +
				   " CONSTRAINT genreAlbumConstraint FOREIGN KEY (genreID) REFERENCES genre(genreID)," +
				   " CONSTRAINT labelAlbumConstraint FOREIGN KEY (labelID) REFERENCES label(labelID));";
		stmt.execute(sql);
		System.out.println("albums Table Created Successfully");
	}

	public  void createSongTable(Statement stmt) throws Exception{
		System.out.println("Creating songs Table");
		String sql = "DROP TABLE IF EXISTS song;";
		stmt.execute(sql);
		sql = "CREATE TABLE song " +
                   "(songID VARCHAR(25) PRIMARY KEY, " +
                   " songName VARCHAR(200), " +
                   " songYear INT, " +
				   " songDuration INT, " +
				   " songTimeSignature INT, " +
				   " songHotness DECIMAL(11,10), " +
				   " songTempo INT, " +
                   " albumID VARCHAR(25), " +
				   " genreID INT, " +
				   " CONSTRAINT albumSongConstraint FOREIGN KEY (albumID) REFERENCES album(albumID)," +
				   " CONSTRAINT genreSongConstraint FOREIGN KEY (genreID) REFERENCES genre(genreID));";
		stmt.execute(sql);
		System.out.println("song Table Created Successfully");
	}

	public  void createUserTable(Statement stmt) throws Exception{
		System.out.println("Creating user  Table");
		String sql = "DROP TABLE IF EXISTS user;";
		stmt.execute(sql);
		sql = "CREATE TABLE user " +
                   "(userID int AUTO_INCREMENT PRIMARY KEY, " +
                   " userFName VARCHAR(50), " +
				   " username VARCHAR(20) UNIQUE, " +
				   " userpass VARCHAR(20), " +
				   " userStreet VARCHAR(50), " +
				   " userCity VARCHAR(50), " +
				   " userCountry VARCHAR(50)," +
				   " userZip INT(5)," +
				   " userPhone VARCHAR(10)," +
				   " userEmail VARCHAR(20));";
		stmt.execute(sql);
		System.out.println("user Table Created Successfully");
	}

	public  void createUserLikesAlbumTable(Statement stmt) throws Exception{
		System.out.println("Creating userlikesartist  Table");
		String sql = "DROP TABLE IF EXISTS userlikesartist;";
		stmt.execute(sql);
		sql = "CREATE TABLE userlikesalbum " +
           "(albumLikeID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
           " userID INT, " +
				   " albumID VARCHAR(25)," +
				   " CONSTRAINT ulau FOREIGN KEY (userID) REFERENCES user(userID)," +
				   " CONSTRAINT ulaa FOREIGN KEY (albumID) REFERENCES album(albumID));";
		stmt.execute(sql);
		System.out.println("userlikesalbum Table Created Successfully");
	}

	public  void createUserLikesSongTable(Statement stmt) throws Exception{
		System.out.println("Creating userlikessong  Table");
		String sql = "DROP TABLE IF EXISTS userlikessong;";
		stmt.execute(sql);
		sql = "CREATE TABLE userlikessong " +
          "(songLikeID INT AUTO_INCREMENT PRIMARY KEY, " +
          " userID INT, " +
				   " songID VARCHAR(25)," +
				   " CONSTRAINT ulsu FOREIGN KEY (userID) REFERENCES user(userID)," +
				   " CONSTRAINT ulss FOREIGN KEY (songID) REFERENCES song(songID));";
		stmt.execute(sql);
		System.out.println("userlikessong Table Created Successfully");
	}

	public void createSongReviewTable(Statement stmt) throws Exception{
		System.out.println("Creating songReview Table");
		String sql = "DROP TABLE IF EXISTS songReview;";
		stmt.execute(sql);
		sql = "CREATE TABLE songReview " +
          "(sReviewID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
           " userID INT, " +
				   " songID VARCHAR(25)," +
				   " songReview VARCHAR(400), " +
				   " CONSTRAINT sru FOREIGN KEY (userID) REFERENCES user(userID)," +
				   " CONSTRAINT srs FOREIGN KEY (songID) REFERENCES song(songID));";
		stmt.execute(sql);
		System.out.println("songReview Table Created Successfully");
	}

	public  void createAlbumReviewTable(Statement stmt) throws Exception{
		System.out.println("Creating albumReview  Table");
		String sql = "DROP TABLE IF EXISTS albumReview;";
		stmt.execute(sql);
		sql = "CREATE TABLE albumReview " +
          "(aReviewID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
          " userID INT, " +
				   " albumID VARCHAR(25)," +
				   " albumReview VARCHAR(400), " +
				   " CONSTRAINT aru FOREIGN KEY (userID) REFERENCES user(userID)," +
				   " CONSTRAINT ara FOREIGN KEY (albumID) REFERENCES album(albumID));";
		stmt.execute(sql);
		System.out.println("albumReview Table Created Successfully");
	}

	public  void createPlaylistTable(Statement stmt) throws Exception{
		System.out.println("Creating playlist Table");
		String sql = "DROP TABLE IF EXISTS playlist;";
		stmt.execute(sql);
		sql = "CREATE TABLE playlist " +
          "(playlistID INTEGER AUTO_INCREMENT PRIMARY KEY, " +
				   " playlistName VARCHAR(30), " +
          " userID INT, " +
				   " CONSTRAINT playlistUser FOREIGN KEY (userID) REFERENCES user(userID));";
		stmt.execute(sql);
		System.out.println("playlist Table Created Successfully");
	}

	public  void createPlaylistSongMappingTable(Statement stmt) throws Exception{
		System.out.println("Creating playlist song mapping Table");
		String sql = "DROP TABLE IF EXISTS playlistSongMapping;";
		stmt.execute(sql);
		sql = "CREATE TABLE playlistSongMapping " +
          "(songID VARCHAR(25), " +
          " playlistID INT, " +
				   " CONSTRAINT psms FOREIGN KEY (songID) REFERENCES song(songID), " +
				   " CONSTRAINT psmp FOREIGN KEY (playlistID) REFERENCES playlist(playlistID));";
		stmt.execute(sql);
		System.out.println("playlistSongMapping Table Created Successfully");
	}

 	public void create(Statement stmt) throws Exception{
		createArtistTable(stmt);
		createLabelTable(stmt);
		createGenreTable(stmt);
		createAlbumTable(stmt);
		createSongTable(stmt);
		createUserTable(stmt);
		createUserLikesAlbumTable(stmt);
		createUserLikesSongTable(stmt);
		createSongReviewTable(stmt);
		createAlbumReviewTable(stmt);
		createPlaylistTable(stmt);
		createPlaylistSongMappingTable(stmt);
	}

	/*public static void main(String[] args){
		Connection conn = null;
		Statement stmt = null;
		String username, password, sql;
		String databaseURL = "jdbc:mysql://localhost:3306/musicspace";

		System.out.println("=== Welcome to MusicSpace Workbench ===");
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the Username: ");
		username = sc.nextLine();
		System.out.print("Enter the Password for "+username+" : ");
		password = sc.nextLine();
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(databaseURL,username,password);

			if(conn == null){
				System.out.println("Invalid user credentials");
				return;
			}else{
				System.out.println("Successfully logged In!!!");
			}
			stmt = conn.createStatement();

			createArtistTable(stmt);
			createLabelTable(stmt);
			createGenreTable(stmt);
			createAlbumTable(stmt);
			createSongTable(stmt);
			createUserTable(stmt);
			createUserLikesAlbumTable(stmt);
			createUserLikesSongTable(stmt);
			createSongReviewTable(stmt);
			createAlbumReviewTable(stmt);
			createPlaylistTable(stmt);
			createPlaylistSongMappingTable(stmt);

		}catch(Exception e){
			System.out.println("Something went wrong "+e);
		}
	}*/
}
