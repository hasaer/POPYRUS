import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class FileRecieveThread extends Thread {
	Socket myClientSocket;
	boolean m_bRunThread = true;
	public final static int FILE_SIZE = 6022386;

	public FileRecieveThread() { 
		super(); 
	} 

	FileRecieveThread(Socket s) { 
		myClientSocket = s;
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
			
			while ((bytesRead = is.read(buffer)) != -1) {
			    bos.write(buffer, 0, bytesRead);
			}
			
			bos.flush();
			bos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void run() {            
		BufferedReader funcIn = null;
		String clientCommand;

		System.out.println("Ŭ���̾�Ʈ ���� �Ϸ� - " + myClientSocket.getInetAddress().getHostName()); 

		try	{
			funcIn = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));


			while(m_bRunThread) {                    
				clientCommand = "";

				while((clientCommand = funcIn.readLine()) != null) {
					switch (clientCommand) {
					case "Quit":
						System.out.println("Stop transmission");
						break;
					case "Request":
						System.out.println("���� ���� ����");
						rcvFile(myClientSocket, "test");
						System.out.println("���� ���� �Ϸ�");
						clientCommand = funcIn.readLine();
						break;
					default:
						break;								
					}
				}
			} 
		} catch(SocketException soe) {
			System.out.println("Socket Closed");
		} catch(Exception e) {
			e.printStackTrace(); 
		} finally { 
			// Clean up 
			try	{
				System.out.println("Ŭ���̾�Ʈ ���� ���� - " + myClientSocket.getInetAddress().getHostName());
				myClientSocket.close(); 
			} catch(IOException ioe) { 
				ioe.printStackTrace(); 
			} 
		}
	}
} 
