package dataGen;

import java.util.ArrayList;
import java.util.HashMap;

public class agent {

	String agentDesc;
	ArrayList<String> capabilities = new ArrayList<String>();
	HashMap aij=new HashMap();
	HashMap aijPlanner=new HashMap();
	HashMap myRoles=new HashMap();
	
	ArrayList<String> roles = new ArrayList<String>();
	ArrayList<role> roleCapabilities = new ArrayList<role>();
	
	public agent(String agentDesc) {
		super();
		this.agentDesc = agentDesc;		
	}

	public String getName() {
		return this.agentDesc;
	}
	
	public HashMap getMyRoles() {
		return myRoles;
	}

	public boolean hasRole(String roleDesc) {
		return myRoles.containsKey(roleDesc);
	}
	
	public int getNumRoles() {
		return myRoles.size();
	}
	
	public void addCapabily(String capability) {
		if(!this.capabilities.contains(capability))
		this.capabilities.add(capability);
	}

	public boolean existCapabily(String capability) {
		if (this.capabilities.contains(capability))
		return true;
		else
		return false;
	}
	
	public void addRole(String roleDesc) {
		this.myRoles.put(roleDesc, roleDesc);
		
	}
	
	
	public void addPayoff(String subtask, int payoff, String role) {
		if (myRoles.containsKey(role)){
		this.aij.put(subtask, payoff);
		this.aijPlanner.put(subtask, payoff);
		}
		else this.aij.put(subtask, payoff);
	}

	
	public int getPayoffAij(String subtask) {
		int payoff=0;
		if (aij.containsKey(subtask))
			payoff=(int) this.aij.get(subtask);
		return payoff;
	}
	
	//aijPlanner - sem payoff para tasks q nao tem capacidade
	public int getPayoffAijPlanner(String subtask) {
		int payoff=0;
		if (aij.containsKey(subtask))
			payoff=(int) this.aij.get(subtask);
		return payoff;
	}
	
	
	//aijPlanner - sem payoff para tasks q nao tem capacidade
	public int getPayoffAijGLPK(String subtask) {
		int payoff=-9999;
		if (aijPlanner.containsKey(subtask))
			payoff=(int) this.aij.get(subtask);
		return payoff;
	}
	
	
	public HashMap getAij() {
		return aij;
	}

	public HashMap getAijPlanner() {
		return aijPlanner;
	}
	public String getAgentDesc() {
		return agentDesc;
	}

	public ArrayList<String> getCapabilities() {
		return capabilities;
	}

	public void checkRolesCapacities(ArrayList<String> rolesRec, ArrayList<role> roleCapabilitiesRec) {
		this.roles=rolesRec;
		this.roleCapabilities=roleCapabilitiesRec;
		String roleCheck="";
		String capabilityRole="";
		boolean validRole=true;
		boolean validCap=false;
		for (int r=0;r<roles.size();r++){
			roleCheck=roles.get(r);
			for (int rc=0;rc<roleCapabilities.size();rc++){
				role roleX= roleCapabilities.get(rc);
				String roleDesc = roleX.getRoleDesc();
				if (roleCheck.equals(roleDesc)){
					ArrayList<String> roleCap = new ArrayList<String>();
					roleCap=roleX.getCapabilities();
					for (int cap=0;cap<roleCap.size();cap++){
						//if cap nao est� nas minhas cap.
						capabilityRole=roleCap.get(cap);
						for (int mycap=0;mycap<capabilities.size();mycap++){
							if(capabilityRole.equals(capabilities.get(mycap)))
								validCap=true;
						}
						if(validCap==false)
							validRole=false;
						
						validCap=false;
					}
					if(validRole==true)
						//add role para minha lista;
						myRoles.put(roleCheck, roleCheck);						
					
					validRole=true;
				}
			}
		}
		
		System.out.println("Populating myRoles:"+this.agentDesc);
		System.out.println(myRoles.toString());
		System.out.println("Capacidades existentes:");
		System.out.println(this.capabilities.toString());
	} //end checkRolesCapacities

	
}//end class






