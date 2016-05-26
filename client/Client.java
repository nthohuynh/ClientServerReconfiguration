package client;


import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Property;






import applicationservice.Service;

@Component(name="Client", immediate=true)

public class Client {
	String address = "udp://192.168.56.22:9000/HelloWorld";
	
	JaxWsProxyFactoryBean factory ;
	Service client; //see as a property
    public Client() {
		factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(Service.class);
		
		factory.setAddress(address);
		
		client = (Service)factory.create();
		Sending send = new Sending("Tito");
		send.start();
    } 
    
    @Property (name="setAddr")
	public void setAddress(String add) {
    	this.address = add;
    	//factory.getBus().shutdown(true);
    	factory.setAddress(add+"/HelloWorld");
    	client = (Service)factory.create();
		
    }
    
    class Sending extends Thread {
    	String str;
		public Sending(String string) {
			// TODO Auto-generated constructor stub
			this.str = string;
		}

		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println("Client receives "+client.sayHi(str+" "+i));
			
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

//    public static void main(String args[]) {
//        new Client();
//    }

}
