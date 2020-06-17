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
          case 1: Song.songMode(stmt, userId); break;
          case 2: Album.albumMode(stmt, userId); break;
          case 3: Artist.artistMode(stmt, userId); break;
          case 4: Song.fullSongMode(stmt, userId); break;
          case 5: Playlist.playlistMode(stmt, userId); break;
          case 6: Review.reviewMode(stmt, userId); break;
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
        System.out.println("Something went wrong "+e);
      }
    }
}
