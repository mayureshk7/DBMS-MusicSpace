import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class Album{

  public static void albumMode(Statement stmt, int userId) throws Exception{
    System.out.print("Album Name : ");
    Scanner sc = new Scanner(System.in);
    String album = sc.nextLine();
    HashMap<Integer, String> hm = new HashMap<Integer, String>();
    if(album.length() < 3){
      System.out.println("Enter Atleast 4 characters to search");
      return;
    }
    String sql = "call getAlbumDetails(\""+album+"\");";
    try{
      ResultSet rs = stmt.executeQuery(sql);
      int n = 0;
      System.out.println("===== Results =====");
      while(rs.next()) {
        n++;
        hm.put(n, rs.getString("albumID"));
        System.out.println(n+". Album Name : "+rs.getString("albumName"));
        System.out.println("Artist Name : "+rs.getString("artistName"));
        System.out.println("Album Year : "+rs.getString("albumYear"));
        System.out.println("Album Genre : "+rs.getString("genreName"));
        if(rs.getInt("albumYear") != 0)
          System.out.println("Album Year : "+rs.getString("albumYear"));
        System.out.println("------------------");
      }
      System.out.println(n+" Results matches : "+album);
      System.out.println("===================");
      int input = 1;
      if(n == 0) return;
      if(n > 1){
        System.out.println("Select The Album Number : ");
        input = sc.nextInt();
        if(input > n || input < 1){
          System.out.println("Invalid Option");
          return;
        }
      }
      System.out.println("========= Options =========");
      System.out.println("1. Like Album\n2. Review Album\n3. Find Album Reviews \n4. Find Album Likes");
      System.out.println("=========================");
      int o = sc.nextInt();
      sc.nextLine();
      switch(o){
        case 1: {
          sql = "Call userlikesalbum("+userId+", \""+hm.get(input)+"\");";
          stmt.executeQuery(sql);
          System.out.println("<3 Album Liked!!! ");
        }
        break;
        case 2: {
          System.out.println("Enter Review : ");
          String review = sc.nextLine();
          sql = "Call reviewAlbum("+userId+", \""+hm.get(input)+"\", \""+review+"\");";
          stmt.executeQuery(sql);
          System.out.println("\\o/ Album Reviewed!!! ");
        }
        break;
        case 3: {
          sql = "Call getAlbumReviews(\""+hm.get(input)+"\");";
          ResultSet rv = stmt.executeQuery(sql);
          int num = 0;
          System.out.println("Results: ");
          while(rv.next()){
            num++;
            System.out.println("-----------------------");
            System.out.println(num+". "+rv.getString("albumReview"));
          }
          System.out.println("-----------------------");
          System.out.println("Found "+num+" reviews!");
        };
        break;
        case 4:{
          sql = "Call getAlbumLikes(\""+hm.get(input)+"\");";
          ResultSet rv = stmt.executeQuery(sql);
          while(rv.next()){
            System.out.println("Album is Currently Liked By : "+rv.getString("albumCount")+" Users");
          }
        }
        break;
        default: System.out.println("Invalid Option");;
      }
    }catch(Exception e){
      System.out.println("Something went wrong try again"+e);
    }
  }

}
