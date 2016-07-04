package reconfigurator;

import java.awt.BorderLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.InstanceManager;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.jmx.framework.BundleStateMBean;
import org.osgi.jmx.framework.FrameworkMBean;


@Component(name="Reconfigurator", immediate=true)
@Instantiate

public class Reconfigurator extends JFrame implements ActionListener{
	@Requires
	private Factory[] factories;					//  It requires Factory service to get all factory object which packaged and deployed in OSGi.\
	BundleContext ctx;
	JFrame fr;
	
	
	public Reconfigurator() {
		
		fr = new JFrame("Controller");
	    fr.setSize(150,80);
	    fr.setLayout(new BorderLayout());
	    fr.setLocation(900,100);
	    
	    final JButton bt2 = new JButton("Reconfigure");
	    fr.add(bt2, BorderLayout.SOUTH);
	    
	    ctx = FrameworkUtil.getBundle(Reconfigurator.class).getBundleContext();
		bt2.addActionListener(this); 
		
		fr.setVisible(true);
    	
		
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		// TODO Auto-generated method stub
		MBeanServerConnection connecToRemoteHost = connecToRemoteHost("192.168.56.2", "root", "karaf", "karaf");

		ExtBundle getBundle = getBundle(connecToRemoteHost, "f45010d8-631b-4abc-b36d-6b4146f0f89c", "server2");
		System.out.print(getBundle.bundleId);
		boolean start = startBundleOnRemoteHost(connecToRemoteHost,getBundle.bundleId,"f45010d8-631b-4abc-b36d-6b4146f0f89c");
		System.out.println("start server2 "+start);
		
		//change connection of client
		for (Factory factory : factories) {
			if (factory.getName().equals("Client")) { //Client is default name of a component name
				ComponentInstance im = (ComponentInstance) factory.getInstances().get(0);
		    	System.out.println("start reconfig");

		    	
				ComponentInstance ci = (ComponentInstance) im;
				Properties props = new Properties();
				props.put("setAddr", "udp://192.168.56.2:9002");
				im.reconfigure(props);
				
				String buffer = (String) ( (InstanceManager)im).getFieldValue("address");
				System.out.println(buffer );
			}
			
    	}
	}
	
	public MBeanServerConnection connecToRemoteHost(String host, 
			String karafHostName, //karaf
			String username, //karaf
			String password) //karaf 
	{
		
		HashMap<String, String[]> environment = new HashMap<String, String[]>();
		String[] credentials = new String[] { username, password };
		environment.put("jmx.remote.credentials", credentials);
		
		JMXServiceURL url;
		JMXConnector jmxc;
		MBeanServerConnection mbsc = null;
		
		try {
			//service:jmx:rmi://localhost:44444/jndi/rmi://localhost:1099/karaf-karaf
			url = new JMXServiceURL("service:jmx:rmi://"+host+":44444/jndi/rmi://"+host+":1099/karaf-"+karafHostName);
			jmxc = JMXConnectorFactory.connect(url, environment);
			mbsc = jmxc.getMBeanServerConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Can not connecto to "+host);
			e.printStackTrace();
		}
		System.out.println("Connected");
		return mbsc;
	}
	
	public List<ExtBundle> getBundleListOnRemoteHost(MBeanServerConnection mbsc,
			String felixFrameworkUUID) {
		
		List<ExtBundle> listRemoteBundle = new ArrayList<ExtBundle>();
		ObjectName objState;
		BundleStateMBean bundleState;
		TabularData bundleData;
		try {
			objState = new ObjectName("osgi.core:type=bundleState,version=1.7,"
			 		+ "framework=org.apache.felix.framework,uuid="+felixFrameworkUUID);
			bundleState = JMX.newMBeanProxy(mbsc, objState,
					 BundleStateMBean.class);
			bundleData=bundleState.listBundles();
			for (Object v : bundleData.values()) {
				 CompositeData row = (CompositeData) v;
				 
	 			 ExtBundle extBundle = new ExtBundle();
	 			 int i = 0;
				 for (Object rv: row.values()) {
					 i ++;
					 switch (i) {
					case 7:
						extBundle.setBundleId(Integer.parseInt(rv.toString()));
						break;
					case 10:
						extBundle.setBundleLocation(rv.toString());
						break;
					case 19:
						extBundle.setState(rv.toString());
						break;
					case 20:
						extBundle.setBundleName(rv.toString());
						break;
					case 21:
						extBundle.setVersion(rv.toString());
						break;
					default:
						break;
					}
				 }
				 listRemoteBundle.add(extBundle);
			 }
		} catch (MalformedObjectNameException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listRemoteBundle;
	}
	
	public ExtBundle getBundle(MBeanServerConnection mbsc,
			String felixFrameworkUUID, String bundleName) {
		ExtBundle bundle = null;
		
		
		 List<ExtBundle> listBundle = getBundleListOnRemoteHost(mbsc, felixFrameworkUUID);
		 for (ExtBundle bdl : listBundle) {
			 if (bdl.getBundleName().equals(bundleName)) {
				 return bdl;
			 }
		 }
		return bundle; 
		
	}
	public boolean startBundleOnRemoteHost(MBeanServerConnection mbsc, 
			long bundleId,
			String felixFrameworkUUID) {
		boolean result = false;
		ObjectName mbeanName;
		try {
			mbeanName = new ObjectName("osgi.core:type=framework,version=1.7,"
				 		+ "framework=org.apache.felix.framework,uuid="+felixFrameworkUUID);
			FrameworkMBean osgiFrameworkProxy = JMX.newMBeanProxy(mbsc, mbeanName,
					FrameworkMBean.class);
			osgiFrameworkProxy.startBundle(bundleId);
			return true;
		} catch (MalformedObjectNameException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public boolean stopBundleOnRemoteHost(MBeanServerConnection mbsc, 
			long bundleId,
			String felixFrameworkUUID) {
		boolean result = false;
		ObjectName mbeanName;
		try {
			mbeanName = new ObjectName("osgi.core:type=framework,version=1.7,"
				 		+ "framework=org.apache.felix.framework,uuid="+felixFrameworkUUID);
			FrameworkMBean osgiFrameworkProxy = JMX.newMBeanProxy(mbsc, mbeanName,
					FrameworkMBean.class);
			osgiFrameworkProxy.stopBundle(bundleId);
			return true;
		} catch (MalformedObjectNameException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}



	
}
class Pause extends Thread {
	public void run(){
		
	}
}