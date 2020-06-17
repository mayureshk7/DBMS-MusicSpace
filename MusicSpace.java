import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class MusicSpace{

  public static int getUser(Connection conn, Statement stmt) throws Exception{
    int userId = -1;
    while(true){
      System.out.println("1. Log In \n2. Register");
      Scanner sc = new Scanner(System.in);
      int input = sc.nextInt();
      if(input == 1){
        sc.nextLine();
        System.out.println("Please Enter username : ");
        String username = sc.nextLine();
        System.out.println("Please Enter Password : ");
        String password = sc.nextLine();
        String sql = "SELECT userID FROM user WHERE username =\""
                    +username+"\" and userpass = \""+password+"\" ;";
        try{
          ResultSet rs = stmt.executeQuery(sql);
          while(rs.next()){
            userId = rs.getInt("userID");
          }
          rs.close();
          if(userId == -1){
            System.out.println("Username or Password is incorrect, Please try again!");
            continue;
          }else{
            return userId;
          }
        }catch(Exception e){
          System.out.println("userMode Something went wrong"+e);
        }
        continue;
      }else if(input == 2){
        sc.nextLine();
        System.out.println("=== New Registration ===");
        String name, username, password, street, city, country, phone, email;
        int zip;
        System.out.println("Please Enter Following details: ");
        System.out.println("Full Name : ");
        name = sc.nextLine();
        System.out.println("Username : ");
        username = sc.nextLine();
        if(username.length() < 5){
          System.out.println("Username should be 5 characters long");
          continue;
        }
        System.out.println("Password : ");
        password = sc.nextLine();
        if(password.length() < 5){
          System.out.println("password should be 5 characters long");
          continue;
        }
        System.out.println("Street : ");
        street = sc.nextLine();
        System.out.println("City : ");
        city = sc.nextLine();
        System.out.println("Country : ");
        country = sc.nextLine();
        System.out.println("Zip Code : ");
        zip = sc.nextInt();
        sc.nextLine();
        System.out.println("Phone : ");
        phone = sc.nextLine();
        System.out.println("Email : ");
        email = sc.nextLine();
        String sql = "INSERT INTO user (userFName, username, userpass, userStreet, userCity, userCountry, userZip, userPhone, userEmail) " +
                      "VALUE (\""+name+"\",  \""+username+"\", \""+password+"\", \""+street+"\",\""+city+"\",\""+country+"\", "+zip+", \""+phone+"\", \""+email+"\");";
        try{
          stmt.execute(sql);
          sql = "SELECT userID FROM user WHERE username =\""+username+"\";";
          ResultSet rs = stmt.executeQuery(sql);
          while(rs.next()){
            userId = rs.getInt("userID");
          }
          System.out.println("User Created Successfully");
          rs.close();
          return userId;
        }catch(Exception e){
          System.out.println("Username already exists");
        }
        continue;
      }else{
        System.out.println("Invalid Option, Try again");
      }
    }
  }

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

  public static void userMode(Connection conn, Statement stmt) throws Exception{
      int userId = getUser(conn, stmt);
      while(true){
        System.out.println("\n=========== Options ===========");
        System.out.println("1. Explore Song\n2. Explore Album");
        System.out.println("3. Explore Artist \n4. Know Everything About the Song");
        System.out.println("5. Explore playlists \n6. My Reviews \n7. Exit");
        System.out.println("===============================");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        sc.nextLine();
        switch(input){
          case 1: songMode(stmt, userId); break;
          case 2: albumMode(stmt, userId); break;
          case 3: artistMode(stmt, userId); break;
          case 4: fullSongMode(stmt, userId); break;
          case 5: playlistMode(stmt, userId); break;
          case 6: reviewMode(stmt, userId); break;
          case 7: System.out.println("Exiting ... "); return;
          default: System.out.println("Invalid option, Try again!");
        }
      }
  }

  public static void adminMode(Connection conn, Statement stmt) throws Exception{
    CreateDatabase cd = new CreateDatabase();
    LoadMusicData md = new LoadMusicData();
    DeleteDatabase dd = new DeleteDatabase();
    CreateStoredProcedures sp = new CreateStoredProcedures();
    while(true){
      System.out.println("\n=========== Options ===========");
      System.out.println("1. Create MusicSpace Database\n2. Truncate the Entire Database");
      System.out.println("3. Load the Database with music data \n4. Create Procedures and Functions\n5. Delete User");
      System.out.println("6. Exit Admin mode");
      System.out.println("===============================");
      Scanner sc = new Scanner(System.in);
      int input = sc.nextInt();
      sc.nextLine();
      System.out.println("You entered : "+input);
      switch(input){
        case 1:
        try{
          System.out.println("Creating Databases ");
          cd.create(stmt);
        }catch(Exception e){
          System.out.println("Something went wrong, make sure DB is empty");
        }
        break;
        case 2:
        try{
          System.out.println("Deleting Databases ");
          dd.deleteTables(stmt);
        }catch(Exception e){
          System.out.println("Something went wrong, Please try again "+e);
        }
        break;
        case 3:
        try{
          System.out.println("loading Databases ");
          md.loadData(stmt);
        }catch(Exception e){
          System.out.println("Something went wrong, Try again");
        }
        break;
        case 4:
        try{
          System.out.println("loading Procedures ");
          sp.createProcedures(stmt);
        }catch(Exception e){
          System.out.println("Something went wrong, Try again");
        }
        break;
        case 5: {
          HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
          String sql = "SELECT * FROM user;";
          try{
            ResultSet rs = stmt.executeQuery(sql);
            int i = 0;
            while(rs.next()){
              hm.put(++i, rs.getInt("userID"));
              System.out.println(i+". Username: "+rs.getString("username")+"\t"+rs.getString("userFName"));
            }
            System.out.println("Found "+i+"users!\nSelect User to delete : ");
            int u = sc.nextInt();
            if(u<1 || u>i){
              System.out.println("Invalid number of user");
            }else{
              dd.deleteUser(stmt, hm.get(u));
            }
          }catch(Exception e){
            System.out.println("Something went wrong");
          }
        }
        break;
        case 6: System.out.println("Exiting ..."); return;
        default: System.out.println("Invalid Option, Try again");
      }
    }
  }


	public static void main(String[] args){
    System.out.println(" === Welcome To MusicSpace, Welcome to the Eternity of Music === ");
    Connection conn = null;
		Statement stmt = null;
    String databaseURL = "jdbc:mysql://localhost:3306/musicspace";
    // admin access
    try{
  	   Class.forName("com.mysql.cj.jdbc.Driver");
  	    conn = DriverManager.getConnection(databaseURL,"root","root");
        if(conn == null){
  				System.out.println("Unable to connect to Database");
  				return;
  			}
        stmt = conn.createStatement();
        if(args.length == 2){
          if(args[0].equals("admin") && args[1].equals("ilikedbms")){
            System.out.println("Warning! You are running in a admin mode");
            adminMode(conn, stmt);
            return;
          }else{
            System.out.println("Admin Access Denied! Running in a user mode");
          }
        }
        //User access
        userMode(conn, stmt);
      }catch(Exception e){
        System.out.println("Something went wrong");
      }
    }
}
