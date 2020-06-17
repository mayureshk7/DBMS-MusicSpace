import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class Playlist{
  public static void playlistMode(Statement stmt, int userId) throws Exception {
    System.out.print("1. Create Playlist \n2. My Playlists\n3. Delete Playlist\n");
    Scanner sc = new Scanner(System.in);
    int input = sc.nextInt();
    String sql = "";
    sc.nextLine();
    if(input == 1){
      System.out.println("Enter the name of Playlist : ");
      String playlist = sc.nextLine();
      sql = "INSERT INTO playlist (playlistName, userID) values (\""+playlist+"\", "+userId+");";
      try{
        stmt.execute(sql);
        System.out.println("Playlist Created Successfully");
      }catch(Exception e){
        System.out.println("Something went wrong");
      }
    }else if(input == 2){
      HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
      sql = "SELECT * FROM playlist WHERE userID = "+userId +";";
      try{
        ResultSet rs = stmt.executeQuery(sql);
        int i = 0;
        while(rs.next()){
          i++;
          hm.put(i, rs.getInt("playlistID"));
          System.out.println(i+". Playlist Name : "+rs.getString("playlistName"));
          System.out.println("-------------------------------");
        }
        System.out.println("Found "+i+" playlists!");
        if(i == 0) return;
        System.out.println("Select Playlist : ");
        input = sc.nextInt();
        sc.nextLine();
        if(input > i || input < 1){
          System.out.println("Invalid playlist Number selected");
          return;
        }
        sql = "CALL getSongsFromPlaylist("+hm.get(input)+");";
        rs = stmt.executeQuery(sql);
        System.out.println("========= Songs in playlist ==========");
        int j = 0;
        HashMap<Integer, String> mm = new HashMap<Integer, String>();
        while(rs.next()){
          j++;
          mm.put(j, rs.getString("songID"));
          System.out.println(j+". "+rs.getString("songName"));
          System.out.println("-------------------------------");
        }
        System.out.println("Found "+j+" songs!");
        System.out.println("-------------------------------");
        System.out.println("1. Delete Playlist\n2. Delete specific Song \n3. Home");
        int ans = sc.nextInt();
        if(ans == 1){
          sql = "DELETE FROM playlist WHERE playlistID = "+hm.get(input)+";";
          stmt.execute(sql);
          System.out.println("Playlist removed Successfully");
        }else if(ans == 2){
          System.out.println("Choose a song number to remove from playlist");
          int song = sc.nextInt();
          if(song<1 || song > j) {
            System.out.println("Invalid Choice!");
            return;
          }
          sql = "DELETE FROM playlistsongmapping WHERE playlistID = "+hm.get(input)+" AND songID = \""+mm.get(song)+"\";";
          stmt.execute(sql);
          System.out.println("Song removed Successfully");
        }if(ans == 3){
          return;
        }else{
          System.out.println("Invalid Choice");
        }
      }catch(Exception e){
        System.out.println("Something went wrong");
      }

    }else{
      System.out.println("Invalid Option");
    }
  }
}
