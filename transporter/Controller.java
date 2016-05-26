package transporter;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


import transferservice.TransferService;


public class Controller {
	public Controller() {

		TransferService rmiServer1;
		TransferService rmiServer2;
		Registry registry1, registry2;
		
		String serverAddress1 = "192.168.56.22"; //xp
		int serverPort1 = 9001;
		

//		String serverAddress2 = "192.168.56.2";//mint 
//		int serverPort2 = 9003;
		try {
			
			registry1 = LocateRegistry.getRegistry(serverAddress1, serverPort1);
			
			rmiServer1 = (TransferService) (registry1.lookup("rmiServer1"));
			
			rmiServer1.processControl(true);
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) throws Exception {
		new Controller();
	}
}
