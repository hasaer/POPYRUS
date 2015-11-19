import java.net.*; 
import java.io.*; 

public class SocketClient { 
	public final static int FILE_SIZE = 6022386;
	private static Socket s;
	
	public static void main(String[] args){ 
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
	
	static void rcvFile(Socket s, String fileName) throws IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		int bytesRead;
		
		byte[] buffer  = new byte [FILE_SIZE];
		
		try { 
			InputStream is = s.getInputStream();
			fos = new FileOutputStream(fileName);
			bos = new BufferedOutputStream(fos);
			
			System.out.println("파일 받는 중");
			while ((bytesRead = is.read(buffer)) != -1) {
			    bos.write(buffer, 0, bytesRead);
			}
			
			bos.flush();
			bos.close();
			System.out.println("파일 받기 완료");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}