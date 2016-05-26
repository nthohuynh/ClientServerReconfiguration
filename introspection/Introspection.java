package introspection;



import java.util.Properties;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.InstanceManager;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.service.cm.ConfigurationAdmin;
@Component(name="Introspection")
@Instantiate
public class Introspection {
    @Requires
	private Factory[] factories;
    private ConfigurationAdmin config;
    @SuppressWarnings("unchecked")
	public Introspection() {
		//introspection: get
    	System.out.println("start introspection");
    	for (Factory factory : factories) {
			if (factory.getName().equals("Server1")) { //Client is default name of a component name
				InstanceManager im = (InstanceManager) factory.getInstances().get(0);
		    	
				//get buffer
				boolean buffer = (boolean) im.getFieldValue("enableProcess");
				
				ComponentInstance ci = (ComponentInstance) factory.getInstances().get(0);
				Properties props = new Properties();
				props.put("enableProcess", false);
				ci.reconfigure(props);
				
				
				System.out.println("introspect: "+buffer);
				
//				get metadata of pojo
//				im.getPojoObjects(); //list pojo object
//				ConfigurationHandler hdl = (ConfigurationHandler)im.getHandler("org.apache.felix.ipojo:properties");
//				PojoMetadata pojo = hdl.getPojoMetadata();
//				String type = pojo.getField("buffer").getFieldType();
				

				
				//set buffer from introspection				
//				ComponentInstance ci = (ComponentInstance) factory.getInstances().get(0);
//				Properties props = new Properties();
//				ConcurrentLinkedQueue<String> newbuffer = new ConcurrentLinkedQueue<String>();
//				newbuffer.add("c1");
//				newbuffer.add("c2");
//			    props.put("buffer", newbuffer);
//				ci.reconfigure(props);
		    	
			}
        }
	}
}
