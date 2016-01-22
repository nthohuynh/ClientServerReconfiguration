package client;
import interceptor.StreamInterceptor;



import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import applicationservice.Service;


public class Client {
	Service client;
    public Client() {

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress("udp://192.168.56.1:9000/HelloWorld");
		
//		Map<String, Object> props = new HashMap<String,Object>();
//		props.put("cxf.synchronous.timeout", new Integer(600000));
//		factory.setProperties(props);

		factory.getInInterceptors().add(new StreamInterceptor());
		client = factory.create(Service.class);
		
//		//1 minute for connection
//		((BindingProvider) factory).getRequestContext().put("com.sun.xml.ws.connect.timeout", 10 * 60 * 1000); 
//
//		//3 minutes for request
//		((BindingProvider) factory).getRequestContext().put("com.sun.xml.ws.request.timeout", 10 * 60 * 1000);
		
		
		
		//factory.setAddress("udp://127.0.0.1:9002/HelloWorld");
     		
		Sending send = new Sending("World 1");
		send.start();
		Sending send2 = new Sending("World 2");
		send2.start();
        			
//       	factory.setAddress("udp://127.0.0.1:9002/HelloWorld");
//    		
//       	client = factory.create(HelloWorld.class);
    } 
    class Sending extends Thread {
    	String str;
		public Sending(String string) {
			// TODO Auto-generated constructor stub
			this.str = string;
		}

		public void run() {
			System.out.println("Client receives "+client.sayHi(str));
		}
	}

    public static void main(String args[]) {
        new Client();
    }

}
