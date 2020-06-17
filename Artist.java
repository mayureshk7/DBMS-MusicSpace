import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class Artist{

  public static void artistMode(Statement stmt, int userId){
    System.out.print("Artist Name : ");
    Scanner sc = new Scanner(System.in);
    String artist = sc.nextLine();
    if(artist.length() < 3){
      System.out.println("Enter Atleast 4 characters to search");
      return;
    }
    String sql = "call getArtistDetails(\""+artist+"\");";
    try{
      ResultSet rs = stmt.executeQuery(sql);
      int n = 0;
      System.out.println("===== Results =====");
      while(rs.next()) {
        n++;
        System.out.println(n+". Artist Name : "+rs.getString("artistName"));
        System.out.println("Artist Location : "+rs.getString("artistLocation"));
        System.out.println("Artist Hotness : "+rs.getString("artistHotness"));
        System.out.println("-----------------------");
      }
      System.out.println(n+" Results matches : "+artist);
    }catch(Exception e){
      System.out.println("Something went wrong try again"+e);
    }
  }
}
