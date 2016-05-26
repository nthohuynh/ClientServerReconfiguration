package transferS1;

import java.io.Serializable;
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
@Component(name="TransferS1")
@Instantiate (name="iTransferS1")
public class TransferS1 {
	@Requires
	private Factory[] factories;
	
	
	CircularFifoQueue<MyMessage> buffer; //generated
	
	public TransferS1() {
		//Offer service to Transporter
		System.out.println("start transfer S1");
		TransferServer out;
		try {
			out = new TransferServer("192.168.56.22", 9001);
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
				//registry = LocateRegistry.getRegistry(thisAddress, thisPort);
				registry.rebind("rmiServer1", this);
			}
			catch (RemoteException e) {
				throw e;
			}
		}
		public CircularFifoQueue <MyMessage> getBuffer() throws RemoteException {
			// TODO Auto-generated method stub
			System.out.println("start get buffer");
			//introspect the buffer of Server1 and getBuffer
			for (Factory factory : factories) {
				if (factory.getName().equals("Server1")) { //Client is default name of a component name
					InstanceManager im = (InstanceManager) factory.getInstances().get(0);
			    	buffer = (CircularFifoQueue<MyMessage>) im.getFieldValue("buffer"); //buffer is variable name in Server1
			    	System.out.println("Server1 have "+buffer.size()+"messages");
				}
			}
			System.out.println("ok");
			return buffer;
		}
		
		public void setBuffer(CircularFifoQueue <MyMessage> msg) throws RemoteException {
			// TODO Auto-generated method stub
			
		}
		public void setMessage(MyMessage msg) {
			// TODO Auto-generated method stub
			
		}
		
		
		public void processControl(boolean bool) {
			for (Factory factory : factories) {
				if (factory.getName().equals("Server1")) { //Client is default name of a component name
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
		
		
		public void setNioDatagramSession(NioDatagramSession nio)
				throws RemoteException {
			// TODO Auto-generated method stub
		}
		
		
}
}
