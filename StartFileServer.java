import java.rmi.Naming;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StartFileServer {
	public static void main(String[] args) {
		try{

			java.rmi.registry.LocateRegistry.createRegistry(1099); // rmi 포트 등록

			FileServer fs = new FileServer();

			Connection conn = new DBConnection().getConn();
			PreparedStatement fnQuery = conn.prepareStatement("SELECT fileName FROM filedata WHERE printNo=1;");
			ResultSet fnRS = null;
			fnRS = fnQuery.executeQuery();
			
			if(fnRS.next() == true)
			{
				String fileName = fnRS.getString(1);
				System.out.println(fileName);
			}

			fs.setFile("test.txt");
			Naming.rebind("rmi://210.121.154.91:1099/printfile", fs);
			System.out.println("File Server is Ready");

		}catch(Exception e){
			e.printStackTrace();
		}
	}	
}