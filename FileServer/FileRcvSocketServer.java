import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileRcvSocketServer {
	ServerSocket rcvSocket;
	boolean ServerOn;
	
	public FileRcvSocketServer() {
		try { 
			rcvSocket = new ServerSocket(11112); 
		} catch(IOException ioe) { 
			System.out.println("11112 ��Ʈ ��� �Ұ� ���� ���� ����"); 
			System.exit(-1); 
		}
		
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("���� ���� �ð� : " + formatter.format(now.getTime()));
		
		while(ServerOn) {                        
			try	{ 
				Socket clientSocket = rcvSocket.accept();
				Thread fileRcvThread = new FileRecieveThread(clientSocket);
				fileRcvThread.start(); 
			} catch(IOException ioe) { 
				System.out.println("����Ʈ ���� ���� �� ���� �߻� : "); 
				ioe.printStackTrace(); 
			}
		}
		
		try { 
			rcvSocket.close(); 
			System.out.println("���� ����"); 
		} catch(Exception ioe) { 
			System.out.println("���� ���� �߻� ���� ����"); 
			System.exit(-1); 
		}
	}
	
	public static void main(String[] args) {
		new FileRcvSocketServer();
	}
}
