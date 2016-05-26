package transferS2;

import java.io.Serializable;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.InstanceManager;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.mina.transport.socket.nio.NioDatagramSession;

import transferservice.TransferService;
import utils.MyMessage;
@Component(name="TransferS2")
@Instantiate (name="iTransferS2")
public class TransferS2 {
	@Requires
	private Factory[] factories;
	
	
	
	public TransferS2() {
		System.out.println("start transfer S2");
		//introspect the buffer of Server1 and getBuffer

		//Offer service to Transporter
		TransferServer out;
		try {
			out = new TransferServer("192.168.56.2", 9003);
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
			System.setProperty("java.security.policy", "java.security.AllPermission");
//			System.setProperty("java.rmi.server.hostname", "192.168.56.2");
			try {
				// create the registry and bind the name and object.
				
				registry = LocateRegistry.createRegistry(thisPort);
				registry.rebind("rmiServer2", this);
			}
			catch (RemoteException e) {
				throw e;
			}
		}
		public CircularFifoQueue<MyMessage> getBuffer() {
			return null;
		}
		
		public void setBuffer(CircularFifoQueue<MyMessage> buff) throws RemoteException {
			// TODO Auto-generated method stub
			for (Factory factory : factories) {
				if (factory.getName().equals("Server2")) { //Client is default name of a component name
					ComponentInstance ci = (ComponentInstance) factory.getInstances().get(0);
					Properties props = new Properties();
					props.put("buffer", buff);
					ci.reconfigure(props);
					System.out.println("set buffer to server2");
					break;
				}
			}
		}
		
		public void setMessage(MyMessage msg) {
			// TODO Auto-generated method stub
			System.out.println(msg.getId());
		}
		
		public void processControl(boolean bool) {
			for (Factory factory : factories) {
				if (factory.getName().equals("Server2")) { //Client is default name of a component name
					ComponentInstance im = (ComponentInstance) factory.getInstances().get(0);
			    	
					//get buffer
//					boolean buffer = (boolean) (InstanceManager)im.getFieldValue("enableProcess");
//					System.out.println(buffer);
					
					ComponentInstance ci = (ComponentInstance) im;
					Properties props = new Properties();
					Boolean bool1 = new Boolean(bool);
					props.put("enableProcess", bool1);
					im.reconfigure(props);
					
					boolean buffer = (boolean) ( (InstanceManager)im).getFieldValue("enableProcess");
					if (buffer) System.out.println("start process");
					else System.out.println("stop process");
				}
				
	    	}
		}
//		@Override
//		public void setNioDatagramSession(NioDatagramSession nio)
//				throws RemoteException {
//			// TODO Auto-generated method stub
//			
//		}
		
		
}
}
