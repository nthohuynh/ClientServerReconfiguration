package client;
import interceptor.StreamInterceptor;





import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Requires;

import applicationservice.Service;

@Component
public class Client {
	
	Service client; //see as a property
    public Client() {

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress("udp://:9000/HelloWorld");
		


		factory.getInInterceptors().add(new StreamInterceptor());
		client = factory.create(Service.class);

     		
		Sending send = new Sending("World 1");
		send.start();
		Sending send2 = new Sending("World 2");
		send2.start();
        			
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
