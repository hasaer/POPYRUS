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
			System.out.println("11112 포트 사용 불가 서버 실행 실패"); 
			System.exit(-1); 
		}
		
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("서버 가동 시간 : " + formatter.format(now.getTime()));
		
		while(ServerOn) {                        
			try	{ 
				Socket clientSocket = rcvSocket.accept();
				Thread fileRcvThread = new FileRecieveThread(clientSocket);
				fileRcvThread.start(); 
			} catch(IOException ioe) { 
				System.out.println("프린트 서버 연결 중 오류 발생 : "); 
				ioe.printStackTrace(); 
			}
		}
		
		try { 
			rcvSocket.close(); 
			System.out.println("서버 종료"); 
		} catch(Exception ioe) { 
			System.out.println("서버 오류 발생 강제 종료"); 
			System.exit(-1); 
		}
	}
	
	public static void main(String[] args) {
		new FileRcvSocketServer();
	}
}
