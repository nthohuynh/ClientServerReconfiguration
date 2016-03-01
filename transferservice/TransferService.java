package transferservice;

import java.rmi.RemoteException;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.mina.transport.socket.nio.NioDatagramSession;

import utils.MyMessage;


 
public interface TransferService extends java.rmi.Remote{
	CircularFifoQueue <MyMessage> getBuffer() throws RemoteException;
	
	void setBuffer(CircularFifoQueue <MyMessage> msg) throws RemoteException;

	void setMessage(MyMessage msg) throws RemoteException;
	
	void setNioDatagramSession(NioDatagramSession nio) throws RemoteException;
}
