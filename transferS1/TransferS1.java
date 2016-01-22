package transferS1;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentLinkedQueue;

import myCXF.destination.MyDestination;
import myCXF.destination.MyMessage;
import myCXF.transfert_rmi.TransferServer;
import myCXF.transfert_rmi.TransferService;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.InstanceManager;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
@Component
@Instantiate
public class TransferS1 {
	@Requires
	private Factory[] factories;
	
	
	CircularFifoQueue<MyMessage> buffer;
	
	public TransferS1() {
		
		//introspect the buffer of Server1 and getBuffer
		for (Factory factory : factories) {
			if (factory.getName().equals("Server1")) { //Client is default name of a component name
				InstanceManager im = (InstanceManager) factory.getInstances().get(0);
		    	buffer = (CircularFifoQueue<MyMessage>) im.getFieldValue("buffer");
			}
		}
		
		//Offer service to Transporter
		TransferServer out;
		try {
			out = new TransferServer("192.168.56.1", 9001);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
class TransferServer extends UnicastRemoteObject implements TransferService, Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int thisPort;
		String thisAddress;
		Registry registry; // rmi registry for lookup the remote objects.
		public TransferServer(String add, int port) throws RemoteException
		{
			this.thisAddress = add;
			this.thisPort = port; 
			//System.setProperty("java.rmi.server.hostname", add);
		
			try {
				// create the registry and bind the name and object.
				registry = LocateRegistry.createRegistry(thisPort);
				registry = LocateRegistry.getRegistry(thisAddress, thisPort);
				registry.rebind("rmiServer", this);
			}
			catch (RemoteException e) {
				throw e;
			}
			
		}
		public MyMessage getMessage() {
			// TODO Auto-generated method stub
			if (buffer.size() > 0) {
				MyMessage nio = buffer.remove();
				return nio;
			} 
			return null;
		}
		@Override
		public void setMessage(MyMessage msg) throws RemoteException {
			// TODO Auto-generated method stub
			
		}
}
}
