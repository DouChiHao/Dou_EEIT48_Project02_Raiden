package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class ConnectionDB {
	
	public String testName = "";
	public boolean connSuccess;
	
	private Connection conn;
	
	public ConnectionDB() {
		connSuccess = false;
		initDB();
	}
	
	public void initDB(){
		
		try {
			
			Properties prop = new Properties();
			
			prop.put("user", "root");
			prop.put("password", "root");
			
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/raidentopic", prop);
			
//			String sql = "SELECT * FROM employees WHERE EmployeeID = 1";
			String sql = "SELECT * FROM member";
			PreparedStatement prepTest = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = prepTest.executeQuery();
			rs.next();
			
			
			
//			testName = rs.getString("LastName");
			
			
//			PreparedStatement prepTest2 = conn.prepareStatement(sql);
			
			connSuccess = true;
			System.out.println("conn OK");
		}catch(Exception e) {
			connSuccess = false;
			System.out.println(e.toString());
		}
	}
	
	
	public String userLogin(String account,String passwd) {
		
		try {
			String sql = "SELECT * FROM member WHERE account = ?";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.setString(1, account);
			ResultSet rs = prep.executeQuery();
			if(rs.next()) {
//				有資料。
				if(rs.getString("passwd").equals(passwd)) {
					
					return rs.getString("account");
				}else {
					System.out.println("密碼錯誤");
					return null;
				}
			}else {
				System.out.println("無帳號");
				return null;
			}
			
			
		}catch(Exception e) {
			System.out.println("Login異常:"+e.toString());
			return null;
		}
	}
	
	public String userRegister(String account,String passwd,String usermail) {
		
		try {
			String sql = "SELECT * FROM member WHERE account = ?";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.setString(1, account);
			ResultSet rs = prep.executeQuery();
			if(rs.next()) {
//				有資料踢出。
				System.out.println("已有帳號");
				return null;
			}else {
//				沒有資料，進行帳號新增。
				String sqlInsert = "INSERT INTO member (account,passwd,usermail) VALUE (?,?,?)";
				prep = conn.prepareStatement(sqlInsert);
				prep.setString(1, account);
				prep.setString(2, passwd);
				prep.setString(3, usermail);
				int successNum = prep.executeUpdate();
				if(successNum != 0) {
					System.out.println("帳號新增成功，登入帳號");					
					return userLogin(account,passwd);
				}else {
					System.out.println("帳號新增失敗");
					return null;
				}				
			}			
			
		}catch(Exception e) {
			System.out.println("Register異常:"+e.toString());
			return null;
		}
		
	}
	
	public boolean userScoreInsert(String account,String gamescore,String playtime) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		try {
			String sqlInsert = "INSERT INTO scorerecord (account,gamescore,playtime,inserttime) VALUE (?,?,?,?)";
			PreparedStatement prep = conn.prepareStatement(sqlInsert);
			prep.setString(1, account);
			prep.setString(2, gamescore);
			prep.setString(3, playtime);
			prep.setString(4, dtf.format(LocalDateTime.now()));
			int successNum = prep.executeUpdate();
			if(successNum != 0) {
				System.out.println("遊戲資料新增成功");					
				return true;
			}else {
				System.out.println("遊戲資料新增失敗");
				return false;
			}
			
			
		}catch(Exception e) {
			System.out.println("ScoreInsert異常:"+e.toString());
			return false;
		}
		
	}
	
	public String[] getTopScore(String account) {
		
		String[] getData = new String[12];
		
		try {
			String sqlQuery = "SELECT account, gamescore, @rank:=@rank+1 as 'rank'"
					+ " FROM scorerecord s,(SELECT @rank:=0) as u ORDER BY gamescore DESC;";
			PreparedStatement prep = conn.prepareStatement(sqlQuery);
			ResultSet rs = prep.executeQuery();
			for(int i = 0; i < 5; i++) {
				rs.next();
				getData[i] = rs.getString("account");
				getData[i+5] = rs.getInt("gamescore")+"";
			}
			
			
			String sqlQueryPlayer = "SELECT account, gamescore,rank FROM"
					+ " (SELECT account,gamescore,(@rank:=@rank+1) AS 'rank'"
					+ " FROM scorerecord,(SELECT @rank:=0) as u"
					+ " ORDER BY gamescore DESC) as c"
					+ " WHERE account = ?"
					+ " ORDER BY rank;";
			
			PreparedStatement prepPlayer = conn.prepareStatement(sqlQueryPlayer);
			prepPlayer.setString(1, account);
			ResultSet rsPlayer = prepPlayer.executeQuery();
			if(rsPlayer.next()) {
				getData[10] = rsPlayer.getString("gamescore");
				getData[11] = rsPlayer.getInt("rank")+"";
			}else {
				getData[10] = "NODATA";
				getData[11] = "NODATA";
			}
			
			
			
			return getData;
		}catch(Exception e) {
			System.out.println("TopScore異常:"+e.toString());
			return null;
		}
		
	}
	
	
	
	
	
	
//	查詢該使用者最高分語法。
//	SELECT * FROM scorerecord WHERE account = 'John' ORDER BY gamescore DESC
//	public void 
	
//	查詢排名語法。
//	SELECT account, gamescore, @rank:=@rank+1 as 'rank' FROM scorerecord s,(SELECT @rank:=0) as u ORDER BY gamescore DESC;
	
//  查詢特定資料的排名語法。
//  SELECT account, gamescore,rank FROM (SELECT account,gamescore,(@rank:=@rank+1) AS 'rank' FROM scorerecord,(SELECT @rank:=0) as u ORDER BY gamescore DESC) as c WHERE account = 'Sheep789' ORDER BY rank;
	
}
