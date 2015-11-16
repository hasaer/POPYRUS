
import java.rmi.Naming;
import java.util.Scanner;


public class StartFileClient {

	public static void main(String[] args) {
		try{
			FileClient c = new FileClient("imed");			
			FileServerInt server = (FileServerInt)Naming.lookup("rmi://localhost:1099/printfile");
			server.login(c);
			System.out.println("Listening.....");
			System.exit(0);
			/*
			Scanner s=new Scanner(System.in);
			while(true){
				String line=s.nextLine();
			}
			*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}	

}