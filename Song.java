import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class Song{

  public static void songMode(Statement stmt, int userId) throws Exception {
    System.out.print("Song Name : ");
    Scanner sc = new Scanner(System.in);
    String song = sc.nextLine();
    HashMap<Integer, String> hm = new HashMap<Integer, String>();
    if(song.length() < 3){
      System.out.println("Enter Atleast 4 characters to search");
      return;
    }
    String sql = "call getSongDetails(\""+song+"\");";
    try{
      ResultSet rs = stmt.executeQuery(sql);
      int n = 0;
      System.out.println("===== Results =====");
      while(rs.next()){
        n++;
        hm.put(n, rs.getString("SongID"));
        System.out.println(n+". Song Name : "+rs.getString("songName"));
        System.out.println("Song Duration : "+rs.getString("songDuration"));
        System.out.println("Song Genre : "+rs.getString("genreName"));
        if(rs.getInt("songYear") != 0)
          System.out.println("Song Year : "+rs.getString("songYear"));
        System.out.println("Album Name : "+rs.getString("albumName"));
        System.out.println("Artist Name : "+rs.getString("artistName"));
        System.out.println("------------------");
      }
      System.out.println(n+" Results matches : "+song);
      System.out.println("===================");
      int input = 1;
      if(n == 0) return;
      if(n > 1){
        System.out.println("Select The Song Number : ");
        input = sc.nextInt();
        if(input > n || input < 1){
          System.out.println("Invalid Option");
          return;
        }
      }
      System.out.println("========= Options =========");
      System.out.println("1. Like Song\n2. Review Song\n3. Find Song Reviews \n4. Find Song Likes \n5. Add to Playlist");
        System.out.println("=========================");
      int o = sc.nextInt();
      sc.nextLine();
      switch(o){
        case 1: {
          sql = "Call userlikessong("+userId+", \""+hm.get(input)+"\");";
          stmt.executeQuery(sql);
          System.out.println("<3 Song Liked!!! ");
        }
        break;
        case 2: {
          System.out.println("Enter Review : ");
          String review = sc.nextLine();
          sql = "Call reviewSong("+userId+", \""+hm.get(input)+"\", \""+review+"\");";
          stmt.executeQuery(sql);
          System.out.println("\\o/ Song Reviewed!!! ");
        }
        break;
        case 3: {
          sql = "Call getSongReviews(\""+hm.get(input)+"\");";
          ResultSet rv = stmt.executeQuery(sql);
          int num = 0;
          System.out.println("Results: ");
          while(rv.next()){
            num++;
            System.out.println("-----------------------");
            System.out.println(num+". "+rv.getString("songReview"));
          }
          System.out.println("-----------------------");
          System.out.println("Found "+num+" reviews!");
        };
        break;
        case 4:{
          sql = "Call getSongLikes(\""+hm.get(input)+"\");";
          ResultSet rv = stmt.executeQuery(sql);
          while(rv.next()){
            System.out.println("Song is Currently Liked By : "+rv.getString("songCount")+" Users");
          }
        }
        break;
        case 5: {
          HashMap<Integer, Integer> pl = new HashMap<Integer, Integer>();
          sql = "SELECT * FROM playlist WHERE userID = "+userId +";";
          System.out.println("Select The playlist : ");
          try{
            ResultSet pp = stmt.executeQuery(sql);
            int t = 0;
            while(pp.next()){
              t++;
              pl.put(t, pp.getInt("playlistID"));
              System.out.println(t+". "+pp.getString("playlistName"));
              System.out.println("-------------------------------");
            }
            if(t == 0){ System.out.println("Sorry, There are no playlist!"); return;}
            int p = sc.nextInt();
            if(p>t && p < 1){ System.out.println("Invalid option"); return;}
            sql = "INSERT INTO playlistsongmapping (songID, playlistID) VALUES ("+
                  "\""+hm.get(input)+"\", "+pl.get(p)+");";
            stmt.execute(sql);
            System.out.println("Song added to playlist Successfully!");
          }catch(Exception e){
            System.out.println("Something went wrong");
          }
        }
        break;
        default: System.out.println("Invalid Option");;
      }
    }catch(Exception e){
      System.out.println("Something went wrong try again"+e);
    }
  }

  public static void fullSongMode(Statement stmt, int userId) throws Exception{
    System.out.print("Song Name : ");
    Scanner sc = new Scanner(System.in);
    String song = sc.nextLine();
    if(song.length() < 3){
      System.out.println("Enter Atleast 4 characters to search");
      return;
    }
    String sql = "call getFullSongDetails(\""+song+"\");";
    try{
      ResultSet rs = stmt.executeQuery(sql);
      int n = 0;
      System.out.println("===== Results =====");
      while(rs.next()) {
        n++;
        System.out.println(n+". Song Name : "+rs.getString("songName"));
        System.out.println("Song Year : "+rs.getString("songYear"));
        System.out.println("Song Duration : "+rs.getInt("songDuration"));
        System.out.println("Song Tempo : "+rs.getString("songTempo"));
        System.out.println("Song Time signature : "+rs.getInt("songTimeSignature"));
        System.out.println("Song Hotness : "+rs.getString("songHotness"));
        System.out.println("Album Name : "+rs.getString("albumName"));
        System.out.println("Album Year : "+rs.getInt("albumYear"));
        System.out.println("Artist Name : "+rs.getString("artistName"));
        System.out.println("Artist Location : "+rs.getString("artistLocation"));
        System.out.println("Artist Hotness : "+rs.getString("artistHotness"));
        System.out.println("-----------------------");
      }
      System.out.println(n+" Results matches : "+song);
    }catch(Exception e){
      System.out.println("Something went wrong try again"+e);
    }
  }
}
