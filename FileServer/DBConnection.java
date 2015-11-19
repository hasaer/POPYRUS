/*
 * DB Connection class
 * */

import java.sql.*;

public class DBConnection
{
	Connection conn=null;
	
	public DBConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			final String URL="jdbc:mysql://127.0.0.1:3306/popyrus";
		    final String USER = "root";
		    final String PW = "";
		    conn=DriverManager.getConnection(URL, USER, PW);
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("DB 연동 실패");
			e.printStackTrace();
			return;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public Connection getConn()
	{
		if(conn==null)
		{
			new DBConnection();
		}
		return conn;
    }
	public void setConn(String driver,String url, String user, String pw)
	{
		try
		{
			Class.forName(driver);
			conn=DriverManager.getConnection(url,user,pw);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
