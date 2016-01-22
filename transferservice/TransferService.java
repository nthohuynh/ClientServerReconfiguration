package transferservice;

import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

import org.apache.cxf.message.Message;
import org.apache.mina.transport.socket.nio.NioDatagramSession;
import org.apache.mina.transport.socket.nio.NioSession;
import org.msgpack.MessagePack;

import myCXF.destination.IoSessionInputStream;
import myCXF.destination.MyDestination.UDPDestinationOutputStream;
import myCXF.destination.MyMessage;
 
public interface TransferService extends java.rmi.Remote{
	MyMessage getMessage() throws RemoteException;
	
	void setMessage(MyMessage msg) throws RemoteException;


	
}
