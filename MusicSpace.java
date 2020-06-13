import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;

public class MusicSpace{

  public static void adminMode(Connection conn, Statement stmt) throws Exception{
    CreateDatabase cd = new CreateDatabase();
    LoadMusicData md = new LoadMusicData();
    DeleteDatabase dd = new DeleteDatabase();
    CreateStoredProcedures sp = new CreateStoredProcedures();
    while(true){
      System.out.println("\n=========== Options ===========");
      System.out.println("1. Create MusicSpace Database\n2. Truncate the Entire Database");
      System.out.println("3. Load the Databse with music data \n4. Create Procedures and Functions");
      System.out.println("5. Exit User mode");
      System.out.println("===============================");
      Scanner sc = new Scanner(System.in);
      int input = sc.nextInt();
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
          System.out.println("Something went wrong, Please try again");
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
        case 5: System.out.println("Exiting ..."); return;
        default: System.out.println("Invalid Option, Try again");
      }
    }
  }

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
        System.out.println("Password : ");
        password = sc.nextLine();
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
        System.out.println(sql);
        try{
          stmt.execute(sql);
          sql = "SELECT userID FROM user WHERE username =\""+username+"\";";
          ResultSet rs = stmt.executeQuery(sql);
          while(rs.next()){
            userId = rs.getInt("userID");
          }
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

  public static void songMode(Statement stmt, int userId){
    System.out.println("Song Name : ");
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
      System.out.println("1. Like Song\n2. Review Song\n3. Find Song Reviews \n4. Find Song Likes");
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
        default: System.out.println("Invalid Option");;
      }
    }catch(Exception e){
      System.out.println("Something went wrong try again"+e);
    }
  }


  public static void userMode(Connection conn, Statement stmt) throws Exception{
      int userId = getUser(conn, stmt);
      while(true){
        System.out.println("\n=========== Options ===========");
        System.out.println("1. Explore Song\n2. Explore Album");
        System.out.println("3. Search/Like Artist \n4. Know Everything About the Song");
        System.out.println("5. My playlists \n6. Exit");
        System.out.println("===============================");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        sc.nextLine();
        switch(input){
          case 1: songMode(stmt, userId); break;
          case 2: break;
          case 3: break;
          case 4: break;
          case 5: break;
          case 6: System.out.println("Exiting ... "); return;
          default: System.out.println("Invalid option, Try again!");
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
