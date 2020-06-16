import java.sql.*;

public class DeleteDatabase{

	public void deleteUser(Statement stmt, int userId) throws Exception{
		String sql = "DELETE FROM user WHERE userID = "+userId+";";
		stmt.execute(sql);
		System.out.println("User Deleted Successfully");
	}

	public void deleteTables(Statement stmt) throws Exception{
		String sql = "";
    sql= "drop table userlikesalbum;";
    stmt.execute(sql);
    System.out.println("Deleting userlikesalbum table");
    sql= "drop table userlikessong;";
    stmt.execute(sql);
    System.out.println("Deleting userlikessong table");
    sql= "drop table playlistSongMapping;";
    stmt.execute(sql);
    System.out.println("Deleting playlistSongMapping table");
    sql= "drop table playlist;";
    stmt.execute(sql);
    System.out.println("Deleting playlist table");
    sql= "drop table songreview;";
    stmt.execute(sql);
    System.out.println("Deleting songreview table");
    sql= "drop table albumreview;";
    stmt.execute(sql);
    System.out.println("Deleting albumreview table");
    sql= "drop table song;";
    stmt.execute(sql);
    System.out.println("Deleting song table");
    sql= "drop table album;";
    stmt.execute(sql);
    System.out.println("Deleting album table");
    sql= "drop table artist;";
    stmt.execute(sql);
    System.out.println("Deleting artist table");
    sql= "drop table genre;";
    stmt.execute(sql);
    System.out.println("Deleting genre table");
    sql= "drop table label;";
    stmt.execute(sql);
    System.out.println("Deleting label table");
    sql= "drop table user;";
    stmt.execute(sql);
    System.out.println("Deleting user table");
	}
}
