package transporter;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.felix.ipojo.annotations.*;

import transferservice.TransferService;
import utils.MyMessage;


@Component
@Instantiate
public class Transporter {
	public Transporter() {

		TransferService rmiServer1;
		TransferService rmiServer2;
		Registry registry1, registry2;
		
		String serverAddress1 = "192.168.56.22"; //xp
		int serverPort1 = 9001;
		

		String serverAddress2 = "192.168.56.2";//mint
		int serverPort2 = 9003;
		

		
		
		try {
			registry1 = LocateRegistry.getRegistry(serverAddress1, serverPort1);
			rmiServer1 = (TransferService) (registry1.lookup("rmiServer1"));
			CircularFifoQueue<MyMessage> msg = rmiServer1.getBuffer();
			System.out.println("get ok " + msg.size());
	
			
			registry2 = LocateRegistry.getRegistry(serverAddress2, serverPort2);
			rmiServer2 = (TransferService) (registry2.lookup("rmiServer2"));			 
			
			rmiServer2.setBuffer(msg);
			//rmiServer2.setMessage(msg.remove());
			System.out.println("set ok");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) throws Exception {
		new Transporter();
	}

	

}
