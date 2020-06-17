import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class Like{

  public static void likeMode(Statement stmt, int userId) throws Exception{
    System.out.println("1. Song Likes \n2. Album Likes");
    Scanner sc = new Scanner(System.in);
    int input = sc.nextInt();
    sc.nextLine();
    String sql = "";
    if(input == 1){
      HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
      sql = "CALL getUserSongLikes("+userId+");";
      try{
        ResultSet rs = stmt.executeQuery(sql);
        int i =0;
        System.out.println("-------------------------------");
        while(rs.next()){
          hm.put(++i, rs.getInt("songLikeID"));
          System.out.println(i+". Song Name : "+rs.getString("songName"));
          System.out.println("-------------------------------");
        }
        System.out.println("Found "+i+" Likes!");
        System.out.println("-------------------------------");
        System.out.println("1. Dislike \n2. Home");
        switch(sc.nextInt()){
          case 1:{
            System.out.println("Select Like Number : ");
            int r = sc.nextInt();
            if(r<0||r>i){
              System.out.println("Invalid option selected");
            }
            sql = "DELETE FROM userlikessong WHERE songLikeID ="+hm.get(r)+";";
            stmt.execute(sql);
            System.out.println("Dislike Successfully");
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
      sql = "CALL getUserAlbumLikes("+userId+");";
      try{
        ResultSet rs = stmt.executeQuery(sql);
        int i =0;
        System.out.println("-------------------------------");
        while(rs.next()){
          hm.put(++i, rs.getInt("albumLikeID"));
          System.out.println(i+". Album Name : "+rs.getString("albumName"));
          System.out.println("-------------------------------");
        }
        System.out.println("Found "+i+" Likes!");
        System.out.println("-------------------------------");
        System.out.println("1. Dislike \n2. Home");
        switch(sc.nextInt()){
          case 1:{
            System.out.println("Select Like Number : ");
            int r = sc.nextInt();
            if(r<0||r>i){
              System.out.println("Invalid option selected");
            }
            sql = "DELETE FROM userlikesalbum WHERE albumLikeID ="+hm.get(r)+";";
            stmt.execute(sql);
            System.out.println("Disliked Successfully");
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
