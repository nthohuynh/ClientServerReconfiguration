package stopS1;

import java.util.Properties;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.InstanceManager;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.service.cm.ConfigurationAdmin;
@Component(name="StopS1")
@Instantiate
public class StopS1 {
    @Requires
	private Factory[] factories;
    private ConfigurationAdmin config;

	public StopS1() {
	  	System.out.println("start Stop S1");
    	for (Factory factory : factories) {
			if (factory.getName().equals("Server1")) { //Client is default name of a component name
				ComponentInstance im = (ComponentInstance) factory.getInstances().get(0);
		    	
				//get buffer
//				boolean buffer = (boolean) (InstanceManager)im.getFieldValue("enableProcess");
//				System.out.println(buffer);
				
				ComponentInstance ci = (ComponentInstance) im;
				Properties props = new Properties();
				Boolean bool = new Boolean(true);
				props.put("enableProcess", bool);
				im.reconfigure(props);
				
				boolean buffer = (boolean) ( (InstanceManager)im).getFieldValue("enableProcess");
				System.out.println(buffer);
			}
			
    	}
//    	for (Factory factory : factories) {
//			if (factory.getName().equals("Server2")) { //Client is default name of a component name
//				ComponentInstance ci = (ComponentInstance) factory.getInstances().get(0);
//				Properties props = new Properties();
//				props.put("buffer", buff);
//				ci.reconfigure(props);
//				System.out.println("set buffer to server2");
//				break;
//			}
//		}
	}

}
