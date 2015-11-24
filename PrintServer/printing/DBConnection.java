package printing;


import java.sql.*;

public class DBConnection
{
	Connection conn=null;
	
	public DBConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			final String URL="jdbc:mysql://210.121.154.91:3306/popyrus";
		    final String USER = "popyrus";
		    final String PW = "1234";
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
