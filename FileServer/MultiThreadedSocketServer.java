import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MultiThreadedSocketServer {

	ServerSocket myServerSocket;
	boolean ServerOn = true;

	public MultiThreadedSocketServer() { 
		try { 
			myServerSocket = new ServerSocket(11111); 
		} catch(IOException ioe) { 
			System.out.println("11111 ��Ʈ ��� �Ұ� ���� ���� ����"); 
			System.exit(-1); 
		}

		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("���� ���� �ð� : " + formatter.format(now.getTime()));

		while(ServerOn) {                        
			try	{ 
				Socket clientSocket = myServerSocket.accept();
				Thread cliThread = new ClientServiceThread(clientSocket);
				cliThread.start(); 
			} catch(IOException ioe) { 
				System.out.println("����Ʈ ���� ���� �� ���� �߻� : "); 
				ioe.printStackTrace(); 
			}
		}

		try { 
			myServerSocket.close(); 
			System.out.println("���� ����"); 
		} catch(Exception ioe) { 
			System.out.println("���� ���� �߻� ���� ����"); 
			System.exit(-1); 
		}
	}

	public static void main (String[] args) { 
		new MultiThreadedSocketServer();        
	} 

	class ClientServiceThread extends Thread {

		Socket myClientSocket;
		boolean m_bRunThread = true; 

		public ClientServiceThread() { 
			super(); 
		} 

		ClientServiceThread(Socket s) { 
			myClientSocket = s;
		}
		
		public String getFileName() {
			String fileName = null;

			try {
				Connection conn = new DBConnection().getConn();
				PreparedStatement fnQuery = conn.prepareStatement("SELECT fileName FROM filedata WHERE printNo=1;");
				ResultSet fnRS = null;
				fnRS = fnQuery.executeQuery();

				if(fnRS.next() == true) {
					fileName = fnRS.getString(1);
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}

			return fileName;
		}
		
		public void sendFile() throws IOException {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			OutputStream os = null;
			
			PrintWriter filePath = null;
			
			try {
				File myFile = new File(getFileName());
				
				filePath = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));
				filePath.println(myFile.getName());
				filePath.flush();
				
				byte[] buffer  = new byte[(int)myFile.length()];
				fis = new FileInputStream(myFile);
				
				bis = new BufferedInputStream(fis);				
				bis.read(buffer, 0, buffer.length);

				os = myClientSocket.getOutputStream();
				os.write(buffer, 0, buffer.length);
				os.flush();
				
				filePath.close();
				fis.close();
				bis.close();
				os.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		public void run() {            
			BufferedReader funcIn = null;
			String clientCommand;

			System.out.println("����Ʈ ���� ���� �Ϸ� - " + myClientSocket.getInetAddress().getHostName()); 

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
							sendFile();
							System.out.println("���� ���� �Ϸ�");
							clientCommand = funcIn.readLine();
							break;
						default:
							break;								
						}
					}
					/*
					System.out.println("Client Says :" + clientCommand);

					if(!ServerOn) {
						System.out.print("Server has already stopped"); 
						m_bRunThread = false;
					} 

					if(clientCommand.equalsIgnoreCase("quit")) { 
						m_bRunThread = false;   
						System.out.print("Stopping client thread for client : "); 
					} else if(clientCommand.equalsIgnoreCase("end")) { 
						m_bRunThread = false;   
						System.out.print("Stopping client thread for client : "); 
						ServerOn = false;
					}
					else {
						sendFile();
					}
					*/
				} 
			} catch(SocketException soe) {
				System.out.println("Socket Closed");
			} catch(Exception e) {
				e.printStackTrace(); 
			} finally { 
				// Clean up 
				try	{
					System.out.println("����Ʈ ���� ���� ���� - " + myClientSocket.getInetAddress().getHostName());
					myClientSocket.close(); 
				} catch(IOException ioe) { 
					ioe.printStackTrace(); 
				} 
			}
		}
	} 
}