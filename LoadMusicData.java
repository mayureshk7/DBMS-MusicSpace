import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashSet;
import java.util.HashMap;

public class LoadMusicData{

	public void loadData(Statement stmt){
		int i = 0;
		String sql;
		HashSet<String> albums, artists;
		HashMap<String, Integer> genres;
		albums = new HashSet<String>();
		artists = new HashSet<String>();
		genres = new HashMap<String, Integer>();
		try{
		  File myObj = new File("music.csv");
			Scanner myReader = new Scanner(myObj);
			myReader.nextLine();
			while(myReader.hasNextLine() && i < 100) {
				i++;
				String data = myReader.nextLine();
				String[] d = data.split(",");
				//21 is genere
				if(!genres.containsKey(d[21])){
					genres.put(d[21], i);
					sql = "INSERT INTO genre (genreID, genreName) VALUES ("
							     +String.valueOf(genres.get(d[21]))+", \""+d[21]+"\");";
					System.out.println(sql);
					stmt.execute(sql);
				}
				//1 is aristID 2 artistName 3 artistTag 9 artistLoc 0 artistHotness
				if(!artists.contains(d[1])){
					artists.add(d[1]);
					sql = "INSERT INTO artist (artistID, artistName, artistTag, artistLocation, artistHotness) VALUES (" +
					"\""+d[1]+"\", " +
					"\""+d[2]+"\", " +
					"\""+d[3]+"\", " +
					"\""+d[9]+"\", " +
					d[0] +
					");";
					System.out.println(sql);
					stmt.execute(sql);
				}
				//12 id 13 name 26 year
				if(!albums.contains(d[12])){
					albums.add(d[12]);
					sql = "INSERT INTO album (albumID, albumName, albumYear, artistID, labelID, genreID) VALUES (" +
					"\""+d[12]+"\", " +
					"\""+d[13]+"\", " +
					d[26]+ ", " +
					"\""+d[1]+"\", " +
					"null, " +
					genres.get(d[21]) +
					");";
					System.out.println(sql);
					stmt.execute(sql);
				}
				//16 is id 25 is name 26 is song year, 4 is duration, 23 is time signature, 15 is hotness, 20 is tempo 12 isalbum id and 21 is genre id
				String hotness = d[15];
				if(hotness.length() == 0) hotness = null;
				System.out.println("Got : "+hotness);
				sql = "INSERT INTO song (songID, songName, songYear, songDuration, songTimeSignature, songHotness, songTempo, albumID, genreID) VALUES (" +
				"\""+d[16]+"\", " +
				"\""+d[25]+"\", " +
				d[26]+ ", " +
				+ (int)Double.parseDouble(d[4]) + ", " +
				d[23]+ ", " +
				hotness +", " +
				+ (int)Double.parseDouble(d[20]) + ", " +
				"\""+d[12]+"\", " +
				genres.get(d[21]) +
				");";
				System.out.println(sql);
				stmt.execute(sql);
			}
			myReader.close();
		}catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
