import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class Review{

  public static void reviewMode(Statement stmt, int userId) throws Exception{
    System.out.println("1. Song Review \n2. Album Review");
    Scanner sc = new Scanner(System.in);
    int input = sc.nextInt();
    sc.nextLine();
    String sql = "";
    if(input == 1){
      HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
      sql = "CALL getUserSongReviews("+userId+");";
      try{
        ResultSet rs = stmt.executeQuery(sql);
        int i =0;
        System.out.println("-------------------------------");
        while(rs.next()){
          hm.put(++i, rs.getInt("sReviewID"));
          System.out.println(i+". Song Name : "+rs.getString("songName"));
          System.out.println("Review : "+rs.getString("songReview"));
          System.out.println("-------------------------------");
        }
        System.out.println("Found "+i+" Reviews!");
        System.out.println("-------------------------------");
        System.out.println("1. Delete Review\n2. Update Review\n3. Home");
        switch(sc.nextInt()){
          case 1:{
            System.out.println("Select Review Number : ");
            int r = sc.nextInt();
            if(r<0||r>i){
              System.out.println("Invalid option selected");
            }
            sql = "DELETE FROM songReview WHERE sReviewID ="+hm.get(r)+";";
            stmt.execute(sql);
            System.out.println("Review Deleted Successfully");
          }
          break;
          case 2:{
            System.out.println("Select Review Number : ");
            int r = sc.nextInt();
            sc.nextLine();
            if(r<0||r>i){
              System.out.println("Invalid option selected");
            }
            System.out.println("Enter Updated Review : ");
            String nr = sc.nextLine();
            sql = "UPDATE songReview SET songReview = \""+nr+"\" WHERE sReviewID ="+hm.get(r)+";";
            stmt.execute(sql);
            System.out.println("Review Updated Successfully");
          }
          break;
          default: return;
        }
      }catch(Exception e){
        System.out.println("Something went wronng"+e);
        return;
      }
    }else if(input == 2){
      HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
      sql = "CALL getUserAlbumReviews("+userId+");";
      try{
        ResultSet rs = stmt.executeQuery(sql);
        int i =0;
        System.out.println("-------------------------------");
        while(rs.next()){
          hm.put(++i, rs.getInt("aReviewID"));
          System.out.println(i+". Album Name : "+rs.getString("albumName"));
          System.out.println("Review : "+rs.getString("albumReview"));
          System.out.println("-------------------------------");
        }
        System.out.println("Found "+i+" Reviews!");
        System.out.println("-------------------------------");
        System.out.println("1. Delete Review\n2. Update Review\n3. Home");
        switch(sc.nextInt()){
          case 1:{
            System.out.println("Select Review Number : ");
            int r = sc.nextInt();
            if(r<0||r>i){
              System.out.println("Invalid option selected");
            }
            sql = "DELETE FROM albumReview WHERE aReviewID ="+hm.get(r)+";";
            stmt.execute(sql);
            System.out.println("Review Deleted Successfully");
          }
          break;
          case 2:{
            System.out.println("Select Review Number : ");
            int r = sc.nextInt();
            sc.nextLine();
            if(r<0||r>i){
              System.out.println("Invalid option selected");
            }
            System.out.println("Enter Updated Review : ");
            String nr = sc.nextLine();
            sql = "UPDATE albumReview SET albumReview = \""+nr+"\" WHERE aReviewID ="+hm.get(r)+";";
            stmt.execute(sql);
            System.out.println("Review Updated Successfully");
          }
          break;
          default: return;
        }
      }catch(Exception e){
        System.out.println("Something went wrong"+e);
        return;
      }
    }else{
        System.out.println("Invalid option selected");
    }
  }
}
