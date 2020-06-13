import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;

public class MusicSpace{

  public static void adminMode(Connection conn, Statement stmt) throws Exception{
    CreateDatabase cd = new CreateDatabase();
    LoadMusicData md = new LoadMusicData();
    DeleteDatabase dd = new DeleteDatabase();
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
        case 4: break;
        case 5: return;
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


  public static void userMode(Connection conn, Statement stmt) throws Exception{
      int userId = getUser(conn, stmt);
      System.out.println(userId);
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
