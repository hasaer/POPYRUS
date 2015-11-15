import java.rmi.Naming;

public class StartFileServer {
	public static void main(String[] args) {
		try{

			java.rmi.registry.LocateRegistry.createRegistry(1099);

			FileServer fs=new FileServer();
			fs.setFile("test.txt");			
			Naming.rebind("rmi://localhost:1099/file", fs);
			System.out.println("File Server is Ready");

		}catch(Exception e){
			e.printStackTrace();
		}
	}	
}