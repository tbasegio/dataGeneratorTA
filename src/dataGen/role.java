package dataGen;

import java.util.ArrayList;

public class role {

	String roleDesc;
	ArrayList<String> capabilities = new ArrayList<String>();
	
	public role(String roleDesc) {
		super();
		this.roleDesc = roleDesc;
	}

	public ArrayList<String> getCapabilities() {
		return capabilities;
	}
	
	public void addCapabily(String capability) {
		this.capabilities.add(capability);
	}
	
	public boolean existCapabily(String capability) {
		if (this.capabilities.contains(capability))
		return true;
		else
		return false;
	}
	
	public String getRoleDesc() {
		return roleDesc;
	}
	
	
}