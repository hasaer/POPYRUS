package printing;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane; 

public class SocketClient { 
	public final static int FILE_SIZE = 6022386;
	private static Socket s;
	
	public SocketClient(){ 
		try { 
			s = new Socket("210.121.154.91", 11111);
		} catch(UnknownHostException uhe) { 
			System.out.println("Unknown Host. Check ip address right.");
			s = null; 
		} catch(IOException ioe) { 
			System.out.println("Cannot connect to the server. Make sure it is running."); 
			s = null;
		} 

		if(s == null) {
			System.exit(-1);
		}
		
		PrintWriter out = null;
		BufferedReader in = null;

		try {			
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(s.getOutputStream())); 
			
			out.println("Request"); 
			out.flush();
			
			String fileName = in.readLine();
			rcvFile(s, fileName);

			out.println("Quit"); 
			out.flush(); 
			
		} catch(IOException ioe) { 
			System.out.println("Exception during communication. Server probably closed connection."); 
		} finally { 
			try	{
				out.close(); 
				s.close(); 
			} catch(Exception e) { 
				e.printStackTrace(); 
			}                
		}
		
	}
	
	private void rcvFile(Socket s, String fileName) throws IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		int bytesRead;
		
		byte[] buffer  = new byte [FILE_SIZE];
		
		try { 
			InputStream is = s.getInputStream();
			fos = new FileOutputStream(fileName);
			bos = new BufferedOutputStream(fos);
			
			//System.out.println("파일 받는 중");
			while ((bytesRead = is.read(buffer)) != -1) {
			    bos.write(buffer, 0, bytesRead);
			}
			
			bos.flush();
			bos.close();
			//System.out.println("파일 받기 완료");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public String[] getData(String userID) {
		String[] attr = new String[7];

		try {
			Connection conn = new DBConnection().getConn();
			PreparedStatement fdQuery = 
					conn.prepareStatement("SELECT printNo, fileName, attrCopies, attrOt, attrRange, attrMin, attrMax FROM filedata WHERE userID=?;");
			ResultSet fdRS = null;
			fdQuery.setString(1, userID);
			
			fdRS = fdQuery.executeQuery();
			
			if(fdRS.next()) {
				for(int i = 0; i < 7; i++) {
					attr[i] = fdRS.getString(i + 1);
				}
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return attr;
	}
}