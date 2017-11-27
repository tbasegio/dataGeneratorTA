package dataGen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class dataGenerator {
	static String jacamoProjectFolder="D:\\MAScode\\taskAllocProcessTestNamespaceReallocSSIA\\";
	static String jacamoAslFolder="D:\\MAScode\\taskAllocProcessTestNamespaceReallocSSIA\\src\\agt\\agTest\\";
	static String glpkFolder="C:\\GLPK\\winglpk-4.60\\glpk-4.60\\w64\\";
	static String mainFolder="D:\\MAScode\\TestDataAAMAS18\\";

	static String testFolder;
	static String pathFolder;
	static int nSimulacoes = 5;
	static String jobId="job1";
	
	static int nAgents = 10;
	static int ntasksCompL =20;
	static int ntasksCompN = 0;
	static int ntasksCompS = 1;
	static int limitAgents = 80; //tasks por agent
	static int aijMax=40;
	static int nroles = 4;
	static int ncapabilities=4;
	static int ncapRole=3; //maximo de capacidades por role
	//setar tasks a gerar
		static int nsubTasksL=3;//maximo de subtasks de uma tarefa composta L
		static int nsubTasksN=0;//maximo de subtasks de uma tarefa composta N
		static int nsubTasksS=2;//maximo de subtasks de uma tarefa composta N
	//forcar todas as capacidaes nos agentes?
	static	boolean forceAgentCap=true;

	
	static ArrayList<String> agents = new ArrayList<String>();
	static ArrayList<String> roles = new ArrayList<String>();
	static ArrayList<String> capabilities = new ArrayList<String>();
	static ArrayList<role> roleCapabilities = new ArrayList<role>();
	static ArrayList<role> agentCapabilities = new ArrayList<role>();
	static ArrayList<agent> agentsList = new ArrayList<agent>();
	

	static boolean subtasksFix = true; //true significa que eh exato x subtasks
	static ArrayList<task> taskList = new ArrayList<task>();
	static ArrayList<String> tasksCompN = new ArrayList<String>();
	static ArrayList<String> tasksCompL = new ArrayList<String>();
	static ArrayList<String> tasksCompS = new ArrayList<String>();

	//static HashMap tasksPayoffs=new HashMap();
	
	static AppendToFile rfile; 
	static AppendToFile rfile2;
	static AppendToFile rfileJacamo;
	static AppendToFile rfileGLPK;
	
	static int simulacaoAtual=0;
	
public static void main(String[] args) {
	//dataAgents(); //para cada agente e chama data para forï¿½a bruta - PRINT NA TELA
	//dataJcm(); //para adicionar no arquivo jacamo
	//dataAgNetwork(); //arquivo no agNet

/* *************PARTE ARQUIVO EXEC	**************** */
	rfile.createFolder(mainFolder);
	rfile2= new AppendToFile(mainFolder+"folderNames.txt",false);
	rfileJacamo= new AppendToFile(mainFolder+"execJacamo.bat",false);
	rfileGLPK= new AppendToFile(mainFolder+"execGLPK.bat",false);
/* end*************PARTE ARQUIVO EXEC	**************** */	
	
	for (int test=1;test<=nSimulacoes;test++){
		simulacaoAtual=test;
		agents.clear();
		roles.clear();
		capabilities.clear();
		roleCapabilities.clear();
		agentCapabilities.clear();
		agentsList.clear();

		taskList.clear();
		tasksCompN.clear();
		tasksCompL.clear();
		tasksCompS.clear();
		
/* *************PARTE ARQUIVO EXEC	**************** */	
	//gerar o nome do test folder
	testFolder="Test"+test;
	pathFolder=mainFolder+testFolder+"\\";
	rfile.createFolder(pathFolder);
	rfile2.appendLine(pathFolder);
	
	rfileJacamo.appendLine("del "+jacamoAslFolder+"*.asl");
	rfileJacamo.appendLine("del "+jacamoAslFolder+"*.pddl");
	rfileJacamo.appendLine("del "+jacamoAslFolder+"*.mod");
	rfileJacamo.appendLine("copy "+pathFolder+"*.* "+jacamoAslFolder);
	rfileJacamo.appendLine("");
	//rfileJacamo.appendLine("@echo off");
	//rfileJacamo.appendLine("echo %time%");
	//rfileJacamo.appendLine("timeout 5 > NUL");
	rfileJacamo.appendLine("echo %time%");
	rfileJacamo.appendLine("");
	rfileJacamo.appendLine("\"C:\\Program Files\\Java\\jdk1.8.0_144\\bin\\java\" -classpath C:\\MASSource\\jacamo-0.7-SNAPSHOT\\libs\\ant-launcher-1.10.1.jar org.apache.tools.ant.launch.Launcher -e -f  D:\\MAScode\\taskAllocProcessTestNamespaceReallocSSIA\\bin\\taskAllocProcess"+nAgents+".xml"); 
	rfileJacamo.appendLine("");
	rfileJacamo.appendLine("copy "+jacamoProjectFolder+"ResultTestData.txt "+pathFolder);
	rfileJacamo.appendLine("del "+jacamoProjectFolder+"ResultTestData.txt");
	rfileJacamo.appendLine("");
	
//	rfileGLPK.appendLine("del "+jacamoAslFolder+"*.asl");
//	rfileGLPK.appendLine("del "+jacamoAslFolder+"*.pddl");
//	rfileGLPK.appendLine("del "+jacamoAslFolder+"*.mod");
//	rfileGLPK.appendLine("copy "+pathFolder+"*.* "+jacamoAslFolder);
	rfileGLPK.appendLine("");
	//rfileGLPK.appendLine("@echo off");
	//rfileGLPK.appendLine("echo %time%");
	//rfileGLPK.appendLine("timeout 5 > NUL");
	rfileGLPK.appendLine("echo startTime %time%");
	rfileGLPK.appendLine("");
	rfileGLPK.appendLine(glpkFolder+"glpsol"+ " --model "+pathFolder+"glpk"+nAgents+".mod --display "+pathFolder+"tbGLPK.out --wlp "+pathFolder+"tbGLPK.lp --output "+pathFolder+"tbGLPK.sol"); 
	rfileGLPK.appendLine("");
	rfileGLPK.appendLine("echo endTime %time%");
	rfileGLPK.appendLine("");
/* end*************PARTE ARQUIVO EXEC	**************** */	

	
	populateAgents();
	populateRoles();
	populateCapabilities();
	populateRoleCapabilities();
	
	if (forceAgentCap==false)
		populateAgentCapabilities();
	else
		populateAgentCapabilitiesALL();
	
	populateTasks();
	populateAgentRoles();
	checkRolesAgents();
	//checkRolesAgents();
	populateAgentPayoffs();
	dataAgents_toFile();
	dataAnnouncer_toFile();
	dataPlanner_toFile();
	dataGLPK_toFile();
	//dataAnnouncer_toFile();
	
System.out.println("end!!");
	}
	
	rfileJacamo.appendLine("copy "+jacamoProjectFolder+"JacamoResults.txt "+mainFolder);
	rfileJacamo.appendLine("del "+jacamoProjectFolder+"JacamoResults.txt");
	rfileJacamo.appendLine("");

	
	rfile2.closeFile();
	rfileJacamo.closeFile();
	rfileGLPK.closeFile();
	//data
}


public static void dataAgents_toFile() {
	//rfile= new AppendToFile("TDx11_ForcaBrutaN20_T1.txt",false);
	agent agentX;
	String agentDesc;
	for (int r=0;r<nAgents;r++){
		agentX = agentsList.get(r);
		agentDesc = agentX.getAgentDesc();
		System.out.println(pathFolder);
		rfile= new AppendToFile(pathFolder+"pN"+nAgents+"ag"+(r)+".asl",false);
		//rfile= new AppendToFile("src//pN"+nr+"ag"+(r+1)+".asl",false);
		
		rfile.appendLine("");
		rfile.appendLine("//###########################################################");
		rfile.appendLine("//DADOS DO r"+r);
		rfile.appendLine("//"+agentDesc);
		
		rfile.appendLine("{ include(\"identifyRolesTasks.asl\") }");
		rfile.appendLine("");
		
		rfile.appendLine("/* Initial beliefs */");
		rfile.appendLine("agents("+nAgents+"). //number of agents");
		rfile.appendLine("simulacao("+simulacaoAtual+"). //simulacao atual");
		rfile.appendLine("");

		rfile.appendLine("/* capabilities*/");

		ArrayList<String> agCap = new ArrayList<String>();
		agCap = agentX.getCapabilities();
		
		for (int c=0;c<agCap.size();c++){
				//rfile.appendLine("capability(\""+agCap.get(c)+"\").");
			rfile.appendLine("capability("+agCap.get(c)+").");
		}
		rfile.appendLine("");
		
	rfile.appendLine("/* reais*/");
	rfile.appendLine("la("+limitAgents+"). //number of tasks per mission");

	rfile.appendLine("");

	rfile.appendLine("/* payoff value for assignment task j for the current robot*/");
	
	HashMap aijX=new HashMap();
	aijX=agentX.getAij();
	
	Iterator it = aijX.entrySet().iterator();
	while (it.hasNext()) {
	    Map.Entry pair = (Map.Entry)it.next();
	    String nameT=(String)pair.getKey();    
	    int valueT=(int)pair.getValue();
	   // System.out.println("TaskPayoffX.put("+nameT+","+valueT+");");
	    rfile.appendLine("aij("+nameT+","+valueT+").");
	}
	
	rfile.appendLine(" ");
	rfile.appendLine("!taBlackboardRolesTasks::perceptBlackboard(\"role_capability\",\"black_board\"). // initial goal");

//	dataForcaBruta(r,aij);

	rfile.appendLine("");
	rfile.appendLine("//FIM DADOS DO r"+r);
	rfile.appendLine("//###########################################################");
			
	rfile.closeFile();
	}//end for
	//rfile.closeFile();
	//printDataForcaBruta_toFile();
	
}


public static void dataAnnouncer_toFile() {
	
		rfile= new AppendToFile(pathFolder+"announcer.asl",false);
		//rfile= new AppendToFile("src//pN"+nr+"ag"+(r+1)+".asl",false);
		
		rfile.appendLine("");
		rfile.appendLine("//###########################################################");
		rfile.appendLine("//DADOS DO agent announcer");
		rfile.appendLine("");
		
		rfile.appendLine("{ include(\"inc/commonAnnouncer.asl\") }");
		rfile.appendLine("");
		
		rfile.appendLine("!createRolesCapabilitiesArt.");
		rfile.appendLine("!createBlackBoardArt.");
		rfile.appendLine("");

		rfile.appendLine("/* announcing roles/capabilities*/");

		//add role cap info
		for (int rc=0;rc<roleCapabilities.size();rc++){
			role roleX= roleCapabilities.get(rc);
			String roleDesc = roleX.getRoleDesc();
			ArrayList<String> roleCap = new ArrayList<String>();
			roleCap=roleX.getCapabilities();
			for (int cap=0;cap<roleCap.size();cap++){
			rfile.appendLine("!announceRoleCapability("+roleDesc+","+roleCap.get(cap)+").");
			}
		}
		rfile.appendLine("");
		
		//add announce task
		String subtaskL,taskL, tasktypeL, roleL;
		for (int t=0;t<taskList.size();t++){
			task taskX= taskList.get(t);
			subtaskL = taskX.getSubTask();
			taskL = taskX.getTask();
			tasktypeL = taskX.getTaskType();
			roleL = taskX.getRole();
			rfile.appendLine("!announce_task("+jobId+","+subtaskL+","+taskL+","+tasktypeL+","+roleL+""+").");
		}
		rfile.appendLine("");

		
	
	rfile.appendLine(" ");
	rfile.appendLine("!completeAnnounce("+jobId+").");


	rfile.appendLine("");
	rfile.appendLine("//FIM DADOS DO announcer");
	rfile.appendLine("//###########################################################");
			
	rfile.closeFile();

}





public static void populateAgents() {
	for (int a=1;a<=nAgents;a++){
		agents.add("ag"+a);
	}
}

public static void populateRoles() {
	for (int r=1;r<=nroles;r++){
		roles.add("role"+r);
	}
}

public static void populateCapabilities() {
	for (int c=1;c<=ncapabilities;c++){
		capabilities.add("cap"+c);
	}
}

public static void populateRoleCapabilities() {
	Random rn = new Random();
	int qtdCap, capN;
	String roleDesc, capability;
	for (int r=0;r<nroles;r++){
		roleDesc = roles.get(r);
		role roleX = new role(roleDesc);
		qtdCap = rn.nextInt(ncapRole)+1;
		if (qtdCap==0) {
			qtdCap=1;
		}
		
		for (int c=1;c<=qtdCap;c++){
			capN = rn.nextInt(ncapabilities);
			capability=capabilities.get(capN);
			//TO_DO fazer laï¿½o garantir capability ja nao estï¿½ na lista
			//nao vai interferir no momento
			if (roleX.existCapabily(capability))
				c=c-1;
			else
			roleX.addCapabily(capability);
			
		}
		roleCapabilities.add(roleX);
	}
	System.out.println("End populate role/cap");
}

public static void populateAgentCapabilities() {

	//cria o agente e adiciona capacidades
	//inicialmente populando todos os agentes com todas as capacidades
	//TO_DO rever para colocar soh algumas e de acordo com as tasks/roles
	
String agentDesc, capability;
Random rn = new Random();
boolean includeCap=false;	
	
	for (int a=0;a<nAgents;a++){
		agentDesc = agents.get(a);
		agent agentX = new agent(agentDesc);
		for (int c=0;c<ncapabilities;c++){
			includeCap = rn.nextBoolean();
			if(includeCap==true){
				capability=capabilities.get(c);			
				agentX.addCapabily(capability);
			}
		}
		//se ficou sem nada inclui todas
		if((agentX.getCapabilities()).size()==0){
			for (int c2=0;c2<ncapabilities;c2++){
				capability=capabilities.get(c2);			
				agentX.addCapabily(capability);
			}
		}
		agentsList.add(agentX);
	}
	System.out.println("End populate agent/cap");
}

public static void populateAgentCapabilitiesALL() {

	//cria o agente e adiciona capacidades
	//inicialmente populando todos os agentes com todas as capacidades
	//TO_DO rever para colocar soh algumas e de acordo com as tasks/roles
	
String agentDesc, capability;
	
	for (int a=0;a<nAgents;a++){
		agentDesc = agents.get(a);
		agent agentX = new agent(agentDesc);
		for (int c=0;c<ncapabilities;c++){
				capability=capabilities.get(c);			
				agentX.addCapabily(capability);
		}
		agentsList.add(agentX);
	}
	System.out.println("End populate agent/cap ALL");
}


public static void populateAgentRoles() {

	agent agentX;
	for (int a=0;a<nAgents;a++){
		agentX = agentsList.get(a);
		agentX.checkRolesCapacities(roles, roleCapabilities);
		}
	System.out.println("End populate agent/roles");
}


//serve para tentar equlibrar qtde de roles e capacidades de agentes e tasks 
public static void checkRolesAgents() {
	String agentDesc, roleDesc, capability;
	int qtdTasks = taskList.size();
	int qtdCap, capN;
	agent agentX;
	task taskX;
	Random rn = new Random();
	HashMap rolesQtd=new HashMap();
	HashMap rolesAgentQtd=new HashMap();
	int qtdRole;

	System.out.println("-----------------------------------");
	System.out.println("START checkRolesAgents");

//verifica as roles de todas as tasks para ter uma visao geral da qtde de roles necessarias
	for (int st=0;st<qtdTasks;st++){
		taskX = taskList.get(st);
		roleDesc = taskX.getRole();
		if (rolesQtd.containsKey(roleDesc))
		{
			//System.out.println("adicionanado role: "+roleDesc);
			//qtdRole = (int)rolesQtd.get(roleDesc);
			Integer qtdRoleInt=(Integer) rolesQtd.get(roleDesc);
			qtdRoleInt++;
			//qtdRole++;
			//System.out.println("nova qtde::"+qtdRoleInt.toString());
			//System.out.println("Roles list:"+rolesQtd.toString());
			rolesQtd.remove(roleDesc);
			rolesQtd.put(roleDesc,qtdRoleInt);
			//System.out.println("Roles list after add:"+rolesQtd.toString());
		}
		else
		{
			//System.out.println("adicionanado role: "+roleDesc);
			//System.out.println("nova qtde::"+1);
			//System.out.println("Roles list:"+rolesQtd.toString());
			Integer qtdRoleInt=new Integer(1);
			rolesQtd.put(roleDesc,qtdRoleInt);
			//System.out.println("Roles list after add:"+rolesQtd.toString());
		}
		
	}
	System.out.println("qty roles nas tasks");
	System.out.println(rolesQtd.toString());

	
	for (int a=0;a<nAgents;a++){
	agentX = agentsList.get(a);
	HashMap agRoles;
	String roleX;
	agRoles = agentX.getMyRoles();
	
		for (int nr=0;nr<nroles;nr++){
			roleX = roles.get(nr);
			if (agRoles.containsKey(roleX))
			if (rolesAgentQtd.containsKey(roleX))
			{
				Integer qtdRoleInt=(Integer) rolesAgentQtd.get(roleX);
				qtdRoleInt++;
				rolesAgentQtd.remove(roleX);
				rolesAgentQtd.put(roleX,qtdRoleInt);
			}
			else
			{
				Integer qtdRoleInt=new Integer(1);
				rolesAgentQtd.put(roleX,qtdRoleInt);
				//System.out.println("Roles list after add:"+rolesQtd.toString());
			}
		}
	}

	System.out.println("qty roles nos agents");
	System.out.println(rolesAgentQtd.toString());

	System.out.println("ROLES DOS AGENTES");
	for (int a=0;a<nAgents;a++){
		agentX = agentsList.get(a);
		HashMap agRoles;
		agRoles = agentX.getMyRoles();
		
		ArrayList<String> agCap = new ArrayList<String>();
		agCap = agentX.getCapabilities();		
		System.out.println("Agent: "+agentX.getAgentDesc() + " Roles: "+agRoles.toString() + " Capacidades: "+agCap.toString());
	}
	
	
	for (int nr=0;nr<nroles;nr++){
			int qtdRoleX=0;
			int qtdRoleAgX=0;
			roleDesc = roles.get(nr);
			if (rolesQtd.containsKey(roleDesc))
				qtdRoleX=(int) rolesQtd.get(roleDesc);
			if (rolesAgentQtd.containsKey(roleDesc))
				qtdRoleAgX=(int) rolesAgentQtd.get(roleDesc);
			else qtdRoleAgX=0;
			
			//if ((qtdRoleX>(qtdRoleAgX*limitAgents*0.8)) || ((int)rolesQtd.get(roleDesc)==0)) {
			if ((qtdRoleX>(qtdRoleAgX*limitAgents*0.8)) || (rolesQtd.containsKey(roleDesc)==false)) {
				
				//ver a diferenÃ§a e adicionar esse nro de roles nos agentes
				Double qtdRoleAddX=qtdRoleX-(qtdRoleAgX*limitAgents*0.8);
				int qtdRoleAdd = qtdRoleAddX.intValue();
				if ((qtdRoleAdd+1)<=limitAgents)
					qtdRoleAdd++;
				System.out.println("mais " + qtdRoleAdd + " roles " + roleDesc +" nos agentes");
				
				
				//para cada quantidade da role
				for (int i = 0; i < qtdRoleAdd; i++) {
				
				//achar agente com menos roles q nao tenha a q quero adicionar
				//adicionar x roles.
				String agentMinRoles="";
				int agentNumRoles=9999;
				for (int a=0;a<nAgents;a++){
					agentX = agentsList.get(a);
					if(!agentX.hasRole(roleDesc) && agentX.getNumRoles()<agentNumRoles){
						agentMinRoles=agentX.getName();
						agentNumRoles = agentX.getNumRoles();
					}
				}
				
				//pego agente final e adiciono a role
				for (int a=0;a<nAgents;a++){
					agentX = agentsList.get(a);
					if(agentX.getName().equals(agentMinRoles)){
						agentX.addRole(roleDesc);
						
						for (int j = 0; j < roleCapabilities.size(); j++) {
							role roleX= roleCapabilities.get(j);
							if (roleX.getRoleDesc().equals(roleDesc)){
								//percorrer as capacidades da role e adicionar no agente
								ArrayList<String> roleCap = new ArrayList<String>();
								roleCap=roleX.getCapabilities();
								for (int cap=0;cap<roleCap.size();cap++){
									agentX.addCapabily(roleCap.get(cap));
								}
							}
							
						}
						
						
						
					}
				}
				
				
				}
			}
			
		}
		
	System.out.println("ROLES DOS AGENTES");
	for (int a=0;a<nAgents;a++){
		agentX = agentsList.get(a);
		HashMap agRoles;
		agRoles = agentX.getMyRoles();
		
		ArrayList<String> agCap = new ArrayList<String>();
		agCap = agentX.getCapabilities();		
		System.out.println("Agent: "+agentX.getAgentDesc() + " Roles: "+agRoles.toString() + " Capacidades: "+agCap.toString());
	}

	
	//rever roles por causa das novas capacidades
	for (int nr=0;nr<nroles;nr++){
		int qtdRoleX=0;
		int qtdRoleAgX=0;
		role roleX= roleCapabilities.get(nr);
		roleDesc = roleX.getRoleDesc();
		ArrayList<String> roleCap = new ArrayList<String>();
		roleCap=roleX.getCapabilities();
		
		for (int a=0;a<nAgents;a++){
			agentX = agentsList.get(a);
			boolean roleKeep=true;
			for (int cap=0;cap<roleCap.size();cap++){
				if(!agentX.existCapabily(roleCap.get(cap)))
					roleKeep=false;
			}
			if(roleKeep==true)
			{
				//adiciona role no agente
				agentX.addRole(roleDesc);	
			}
		}
	}
		
	System.out.println("ROLES DOS AGENTES");
	for (int a=0;a<nAgents;a++){
		agentX = agentsList.get(a);
		HashMap agRoles;
		agRoles = agentX.getMyRoles();
		
		ArrayList<String> agCap = new ArrayList<String>();
		agCap = agentX.getCapabilities();		
		System.out.println("Agent: "+agentX.getAgentDesc() + " Roles: "+agRoles.toString() + " Capacidades: "+agCap.toString());
	}
	
	
	
	System.out.println("END checkRolesAgents");
	System.out.println("-----------------------------------");

	

}// end checkRolesAgents

//serve para tentar equlibrar qtde de roles e capacidades de agentes e tasks 
public static void checkRolesAgents_v1() {
	String agentDesc, roleDesc, capability;
	int qtdTasks = taskList.size();
	int qtdCap, capN;
	agent agentX;
	task taskX;
	Random rn = new Random();
	HashMap rolesQtd=new HashMap();
	int qtdRole;

	System.out.println("-----------------------------------");
	System.out.println("START checkRolesAgents");

	for (int st=0;st<qtdTasks;st++){
		taskX = taskList.get(st);
		roleDesc = taskX.getRole();
		if (rolesQtd.containsKey(roleDesc))
		{
			//System.out.println("adicionanado role: "+roleDesc);
			//qtdRole = (int)rolesQtd.get(roleDesc);
			Integer qtdRoleInt=(Integer) rolesQtd.get(roleDesc);
			qtdRoleInt++;
			//qtdRole++;
			//System.out.println("nova qtde::"+qtdRoleInt.toString());
			//System.out.println("Roles list:"+rolesQtd.toString());
			rolesQtd.remove(roleDesc);
			rolesQtd.put(roleDesc,qtdRoleInt);
			//System.out.println("Roles list after add:"+rolesQtd.toString());
		}
		else
		{
			//System.out.println("adicionanado role: "+roleDesc);
			//System.out.println("nova qtde::"+1);
			//System.out.println("Roles list:"+rolesQtd.toString());
			Integer qtdRoleInt=new Integer(1);
			rolesQtd.put(roleDesc,qtdRoleInt);
			//System.out.println("Roles list after add:"+rolesQtd.toString());
		}
		
	}
	System.out.println("qty roles nas tasks");
	System.out.println(rolesQtd.toString());

	
	for (int nr=0;nr<nroles;nr++){
		roleDesc = roles.get(nr);
		if (rolesQtd.containsKey(roleDesc)){ 
			Integer qtdRoleInt=(Integer) rolesQtd.get(roleDesc);
			qtdRoleInt = (Integer) rolesQtd.get(roleDesc);
			if(qtdRoleInt>(nAgents*0.33))
			{
			Double value = (nAgents*0.33);
			qtdRoleInt=value.intValue();
			rolesQtd.remove(roleDesc);
			rolesQtd.put(roleDesc,qtdRoleInt);
			}
		}
	}
	
	
	
	
	for (int a=0;a<nAgents;a++){
	agentX = agentsList.get(a);
	HashMap agRoles;
	agRoles = agentX.getMyRoles();
	
		for (int nr=0;nr<nroles;nr++){
			roleDesc = roles.get(nr);
			if (rolesQtd.containsKey(roleDesc)) 
			if (agRoles.containsKey(roleDesc)) {
				Integer qtdRoleInt=(Integer) rolesQtd.get(roleDesc);
				qtdRoleInt = (Integer) rolesQtd.get(roleDesc);
				//System.out.println("to remove");
				//System.out.println(roleDesc);
				//System.out.println(qtdRoleInt.toString());
				qtdRoleInt--;				
				//System.out.println(rolesQtd.toString());
				rolesQtd.remove(roleDesc);
				//System.out.println(rolesQtd.toString());
				//System.out.println(roleDesc);
				if(qtdRoleInt>0)
				{
					//System.out.println("re-adicionando:");
					//System.out.println(roleDesc);
					//System.out.println(qtdRoleInt.toString());
					rolesQtd.put(roleDesc,qtdRoleInt);
				}
			}
		}
	}
	System.out.println("qty roles after agents check");
	System.out.println(rolesQtd.toString());


	System.out.println("ROLES DOS AGENTES");
	for (int a=0;a<nAgents;a++){
		agentX = agentsList.get(a);
		HashMap agRoles;
		agRoles = agentX.getMyRoles();
		
		ArrayList<String> agCap = new ArrayList<String>();
		agCap = agentX.getCapabilities();		
		System.out.println("Agent: "+agentX.getAgentDesc() + " Roles: "+agRoles.toString() + " Capacidades: "+agCap.toString());
	}
	
	
//int nRole=0;
	while (!rolesQtd.isEmpty())
		
		for (int nr=0;nr<nroles;nr++){
		roleDesc = roles.get(nr);
		if (rolesQtd.containsKey(roleDesc)) 
		{
			int contRoleAg=0;
			for (int a=0;a<nAgents;a++){
			agentX = agentsList.get(a);
			HashMap agRoles;
			agRoles = agentX.getMyRoles();
			if (agRoles.containsKey(roleDesc)){
				System.out.println(agentX.getAgentDesc() + " ja tem role: "+ roleDesc);
				contRoleAg++;
				if(contRoleAg>=nAgents)
					rolesQtd.remove(roleDesc);
			}
			else{
				if (rolesQtd.containsKey(roleDesc)){
				agentX.addRole(roleDesc);				
				//diminuir do que ainda falta
				Integer qtdRoleInt=(Integer) rolesQtd.get(roleDesc);
				qtdRoleInt = (Integer) rolesQtd.get(roleDesc);
				qtdRoleInt--;				
				rolesQtd.remove(roleDesc);
				if(qtdRoleInt>0)
					rolesQtd.put(roleDesc,qtdRoleInt);
				
				//ver as capacidades da role e adicionar no agente.
				for (int r=0;r<roleCapabilities.size();r++){
					role roleX = roleCapabilities.get(r);
					if (roleX.getRoleDesc().equals(roleDesc)){
						ArrayList<String> roleCap = new ArrayList<String>();
						roleCap=roleX.getCapabilities();
						for (int cap=0;cap<roleCap.size();cap++){
							if(!agentX.existCapabily(roleCap.get(cap)))
							agentX.addCapabily(roleCap.get(cap));
						}
						
					}
				}
				}
			}
	
			}
		}
	}
	
//just printing
	System.out.println("ROLES DOS AGENTES");
	for (int a=0;a<nAgents;a++){
		agentX = agentsList.get(a);
		HashMap agRoles;
		agRoles = agentX.getMyRoles();
		
		ArrayList<String> agCap = new ArrayList<String>();
		agCap = agentX.getCapabilities();		
		System.out.println("Agent: "+agentX.getAgentDesc() + " Roles: "+agRoles.toString() + " Capacidades: "+agCap.toString());
	}

	
	
	System.out.println("END checkRolesAgents");
	System.out.println("-----------------------------------");

	

}// end checkRolesAgents


public static void populateAgentPayoffs() {

	//String agentDesc, capability;
	int qtdTasks = taskList.size();
	agent agentX;
	task taskX;
	int payoff;
	Random rn = new Random();
	
	for (int a=0;a<nAgents;a++){
		agentX = agentsList.get(a);
		for (int st=0;st<qtdTasks;st++){
			taskX = taskList.get(st);
			payoff = rn.nextInt(aijMax)+1;
			agentX.addPayoff(taskX.getSubTask(), payoff, taskX.getRole());
		}
	}
}


public static void populateTasks() {
String task, subtask, taskType, roleDesc;
int qdtSubT, roleN;
Random rn = new Random();

taskType="tcn";
	for (int tn=1;tn<=ntasksCompN;tn++){
		task="tn"+tn;
		tasksCompN.add(task);
		if (subtasksFix==false)
		qdtSubT = rn.nextInt(nsubTasksN)+1;
		else qdtSubT = nsubTasksN;
		
		if (qdtSubT==0) {
			qdtSubT=2;
		}
		for (int st=1;st<=qdtSubT;st++){
			subtask=task+"st"+st;
			roleN = rn.nextInt(ncapabilities);
			roleDesc=roles.get(roleN);
			taskList.add(new task(subtask,task,taskType,roleDesc));
	}
	}	

taskType="tcl";
	for (int tl=1;tl<=ntasksCompL;tl++){
		task="tl"+tl;
		tasksCompL.add(task);
		if (subtasksFix==false)
			qdtSubT = rn.nextInt(nsubTasksL)+1;
		else qdtSubT = nsubTasksL;
		
		if (qdtSubT==0) {
			qdtSubT=2;
		}
		for (int st2=1;st2<=qdtSubT;st2++){
			subtask=task+"st"+st2;
			roleN = rn.nextInt(ncapabilities);
			roleDesc=roles.get(roleN);
			taskList.add(new task(subtask,task,taskType,roleDesc));
	}
	}

taskType="sd";
	for (int sd=1;sd<=ntasksCompS;sd++){
		task="sd"+sd;
		tasksCompS.add(task);
		if (subtasksFix==false)
			qdtSubT = rn.nextInt(nsubTasksS)+1;
		else qdtSubT = nsubTasksS;
		
		if (qdtSubT==0) {
			qdtSubT=2;
		}
		for (int st3=1;st3<=qdtSubT;st3++){
			subtask=task+"st"+st3;
			roleN = rn.nextInt(ncapabilities);
			roleDesc=roles.get(roleN);
			taskList.add(new task(subtask,task,taskType,roleDesc));
	}
	}	
	
	
}


public static void dataPlanner_toFile() {
	agent agentX, agentX2;
	String agentDesc;
	HashMap sdTasks=new HashMap();
	
	rfile= new AppendToFile(pathFolder+"pddl"+nAgents+".pddl",false);

	rfile.appendLine(";para validar o total somar o valor dos c's no resultado.");
	rfile.appendLine(";por exemplo c1 + c5 + c4 = 10 de custo.");
	rfile.appendLine(";pois o custo total apresentado ï¿½ para minimizaï¿½ï¿½o.");
	rfile.appendLine("");
	rfile.appendLine("(define (problem pb2)");
	rfile.appendLine("(:domain sampling)");
	rfile.appendLine("(:requirements :strips :typing)");
	rfile.appendLine("(:objects ");
	
	//a1 a2 a3 - agente
	String agentes = "";
	for (int r=0;r<nAgents;r++){
		agentX = agentsList.get(r);
		agentDesc = agentX.getAgentDesc();
		agentes = agentes + agentDesc + " "; 
	}
	agentes = agentes + "- agente";
	rfile.appendLine(agentes);
	
	//t1 t2 t3 t4 t5 t6 t7 t8 t9 - tarefa
	String subtask = "";
	for (int t=0;t<taskList.size();t++){
		task taskX= taskList.get(t);
		if(taskX.getTaskType().equals("sd"))
		{
			if (!sdTasks.containsKey(taskX.getTask()))
			{
				subtask = subtask + taskX.getTask()+taskX.getTask()+ " ";
				sdTasks.put(taskX.getTask(), taskX.getTask());
			}
		}
		else
			subtask = subtask + taskX.getSubTask()+ " ";
	}
	subtask = subtask + "- tarefa";
	rfile.appendLine(subtask);

	
	
	
	
	
	//c1 c2 c3 c4 c5 c6 - custo
	String custo="";
		for (int c=0;c<=(aijMax*nsubTasksS);c++){
			custo= custo+ "c"+c+" ";
		}
		custo = custo + "- custo";
		rfile.appendLine(custo);
			
	//l0 l1 l2 l3 l4 l5 - limite 
		String limite="";
		for (int l=0;l<=limitAgents;l++){
			limite= limite+ "l"+l+" ";
		}
		limite = limite + "- limite";
		rfile.appendLine(limite);

		//l0 l1 l2 l3 l4 l5 - limite 
				String peso="";
				for (int p=0;p<=nsubTasksS;p++){
					peso= peso+ "p"+p+" ";
				}
				peso = peso + "- peso";
				rfile.appendLine(peso);		
		
		
		
		
		
		
	//  ) (:init (= (total-cost) 0)
		rfile.appendLine(")");
		rfile.appendLine("(:init ");
		rfile.appendLine("(= (total-cost) 0)");
		
		//(= (pay c1) 1)
		//aqui o valor de p vai ser invertido para funcionar no planner como se fosse maximizaï¿½ï¿½o
		//pois o planner lida apenas com minimizaï¿½ï¿½o
		for (int p=0;p<=(aijMax*nsubTasksS);p++){
			rfile.appendLine("(= (pay c"+p+") "+((aijMax*nsubTasksS)-p)+")");
		}
		
		//(inc l0 l1) (inc l1 l2) (inc l2 l3) (inc l3 l4) (inc l4 l5) 
		for (int l2=0;l2<=limitAgents;l2++){
			rfile.appendLine("(inc l"+l2+" l"+(l2+1)+")");
		}

		//(incpeso p1 l5 l6)
		//(incpeso p2 l0 l2)
 		for (int p=1;p<=nsubTasksS;p++)
			for (int l2=0;l2<=limitAgents;l2++){
					rfile.appendLine("(incpeso p"+p+" l"+l2+" l"+(l2+p)+")");
				}
		//(maior l3 l2)
 		//(maior l4 l2)
 		//(maior l5 l2)
 		for (int l=limitAgents+1;l<=limitAgents*2;l++){
					rfile.appendLine("(maior l"+l+" l"+(limitAgents)+")");
				}
 		//(pesot sd1sd1 p2)
 		//(compN tn1 tn1st1)
 				//(compL tl1 tl1st1)
 		sdTasks.clear();
 		String subtaskX,task, tasktype, role;
 		for (int t=0;t<taskList.size();t++){
 			task taskX= taskList.get(t);
 			subtaskX = taskX.getSubTask();
 			task = taskX.getTask();
 			tasktype = taskX.getTaskType();
 			if (tasktype.equals("sd")){
 				if (!sdTasks.containsKey(taskX.getTask()))
 					rfile.appendLine("(pesot "+task+task+" p"+nsubTasksS+")");
 					sdTasks.put(taskX.getTask(), taskX.getTask());
 			}
 		}
 		
		//(limit a1 l3)
		for (int r=0;r<nAgents;r++){
			agentX2 = agentsList.get(r);
			agentDesc = agentX2.getAgentDesc();
			rfile.appendLine("(limit "+agentDesc+" l"+limitAgents+")"); 
		}
		
		//(totag a1 l0)
		for (int r=0;r<nAgents;r++){
			agentX2 = agentsList.get(r);
			agentDesc = agentX2.getAgentDesc();
			rfile.appendLine("(totag "+agentDesc+" l0)"); 
		}

		//(compN tn1 tn1st1)
		//(compL tl1 tl1st1)
		sdTasks.clear();
		String subtaskL,taskL, tasktypeL, roleL;
		for (int t=0;t<taskList.size();t++){
			task taskX= taskList.get(t);
			subtaskL = taskX.getSubTask();
			taskL = taskX.getTask();
			tasktypeL = taskX.getTaskType();
			roleL = taskX.getRole();
			
			if (tasktypeL.equals("tcn"))
			rfile.appendLine("(compN "+taskL+" "+subtaskL+")");
			else if (tasktypeL.equals("tcl"))
				rfile.appendLine("(compL "+taskL+" "+subtaskL+")");
			else if (tasktypeL.equals("sd"))
				{
				if (!sdTasks.containsKey(taskX.getTask()))
				rfile.appendLine("(compS "+taskL+" "+taskL+taskL+")");
				sdTasks.put(taskX.getTask(), taskX.getTask());
				}
		}
		
		
		//(payoff a1 t1 c1)
		//DONE - TO_DO - filtrar soh as tasks que cada agente pode baseado no role/cap
		for (int r=0;r<nAgents;r++){
			agentX2 = agentsList.get(r);
			HashMap aijX=new HashMap();
			aijX=agentX2.getAijPlanner();
	
			//not sd tasks
			Iterator it = aijX.entrySet().iterator();
			System.out.println("xxxxxxxxxxxxxxxxx");
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				String nameT=(String)pair.getKey();    
				int valueT=(int)pair.getValue();
				boolean sdTask=false;
				for (int t=0;t<taskList.size();t++){
					task taskX= taskList.get(t);
					if ((taskX.getSubTask().equals(nameT)) && (taskX.getTaskType().equals("sd"))) 
						sdTask=true;
				}
				if (sdTask==false)
				rfile.appendLine("(payoff "+agentX2.getAgentDesc()+" "+nameT+" c"+valueT+")");
			}
			
			//sd tasks
			sdTasks.clear();
			Iterator it2 = aijX.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry pair = (Map.Entry)it2.next();
				String nameT=(String)pair.getKey();
				int valueT=(int)pair.getValue();
				//String subtaskL,taskL, tasktypeL, roleL;
				for (int t=0;t<taskList.size();t++){
					task taskX= taskList.get(t);
					if ((taskX.getSubTask().equals(nameT)) && (taskX.getTaskType().equals("sd"))) 
						if (!sdTasks.containsKey(taskX.getTask())){
							Integer valueTask=new Integer(valueT);
							sdTasks.put(taskX.getTask(), valueTask);
						}
						else{
							Integer valueTask=(Integer) sdTasks.get(taskX.getTask());
							Integer valueTaskNew=new Integer(valueT);
							valueTask = valueTask+valueTaskNew;
							sdTasks.remove(taskX.getTask());
							sdTasks.put(taskX.getTask(), valueTask);
							}
				}
			}
			
			
			Iterator it3 = sdTasks.entrySet().iterator();
			while (it3.hasNext()) {
				Map.Entry pair = (Map.Entry)it3.next();
				String nameT=(String)pair.getKey();
				int valueT=(int)pair.getValue();
				rfile.appendLine("(payoff "+agentX2.getAgentDesc()+" "+nameT+nameT+" c"+valueT+")");
			}
				
			
			
			
			
			rfile.appendLine(" ");
		
		}
		
	rfile.appendLine(" ");
	rfile.appendLine(")");
	rfile.appendLine("(:goal (and ");
	
	//(allocated t1)
	sdTasks.clear();
	for (int t2=0;t2<taskList.size();t2++){
		task taskX= taskList.get(t2);
		
		if(taskX.getTaskType().equals("sd"))
		{
			if (!sdTasks.containsKey(taskX.getTask()))
			{
				rfile.appendLine("(allocated "+taskX.getTask()+taskX.getTask()+")");
				sdTasks.put(taskX.getTask(), taskX.getTask());
			}
		}
		else rfile.appendLine("(allocated "+taskX.getSubTask()+")");
		
	}
	
	rfile.appendLine("))");
	rfile.appendLine("(:metric minimize (total-cost))");
	rfile.appendLine(")");

	rfile.closeFile();
}


public static void dataGLPK_toFile() {
	agent agentX, agentX2;
	String agentDesc;
	HashMap sdTasks=new HashMap();
	
	rfile= new AppendToFile(pathFolder+"glpk"+nAgents+".mod",false);

	rfile.appendLine("set ROBOTS;      # i Robots");
	rfile.appendLine("set TASKS;       # j Tasks");
	rfile.appendLine("set SUBTASKS;    # k Subtasks");
	rfile.appendLine("");
	
	rfile.appendLine("param Lmin{TASKS}, integer;        # Minimum number of subtasks each robot can take for each task type");
	rfile.appendLine("param Lmax{TASKS}, integer;        # Maximum number of subtasks each robot can take for each task type");
	rfile.appendLine("param S{SUBTASKS}, integer;                  # size the subtask occupies in the robot task limit");
	rfile.appendLine("");
	
	rfile.appendLine("param U{ROBOTS,SUBTASKS}, integer;        # Utility of subtask to each robot");
	rfile.appendLine("param P{TASKS,SUBTASKS}, binary;          # Task structure: which subtasks belong to which task");
	rfile.appendLine("param N{TASKS}, integer;                  # Task structure: the number of subtasks of each task");
	rfile.appendLine("param L{ROBOTS}, integer;                 # Task limit: the maximum number of tasks each robot can take");
	rfile.appendLine("");
	
	rfile.appendLine("var a{ROBOTS,TASKS}, binary;              # to check whether the allocation below has associated a robot with an overall task");
	rfile.appendLine("var f{ROBOTS,SUBTASKS}, binary;           # this is the allocation we want");
	rfile.appendLine("");
							 
	rfile.appendLine("maximize");
	rfile.appendLine("   objective: sum{i in ROBOTS, k in SUBTASKS} f[i,k]*U[i,k];");
	rfile.appendLine("");
	   
	rfile.appendLine("subject to");
	rfile.appendLine("");
	rfile.appendLine("   C2{k in SUBTASKS}: sum{i in ROBOTS}   f[i,k] <= 1;");
	rfile.appendLine("   C3{i in ROBOTS}:   sum{k in SUBTASKS} f[i,k]*S[k] <= L[i];  ");
	rfile.appendLine("   C4{k in SUBTASKS}: sum{j in TASKS}    P[j,k] =  1;       # we could assume the user gave this data correctly...");
	rfile.appendLine("");
	rfile.appendLine("   C5{j in TASKS, i in ROBOTS}: sum{k in SUBTASKS} f[i,k]*P[j,k] >= Lmin[j];");
	rfile.appendLine("   C6{j in TASKS, i in ROBOTS}: sum{k in SUBTASKS} f[i,k]*P[j,k] <= Lmax[j];");
	rfile.appendLine("");
	rfile.appendLine("# se precisar, card(X) é |X|");
	rfile.appendLine("");
	rfile.appendLine("solve;");
	rfile.appendLine("");
	rfile.appendLine("display f;");
	rfile.appendLine("");
	rfile.appendLine("data;");
	rfile.appendLine("");

//SET ROBOTS	
	//a1 a2 a3 - agente
	String agentes = "set ROBOTS   := ";
	for (int r=0;r<nAgents;r++){
		agentX = agentsList.get(r);
		agentDesc = agentX.getAgentDesc();
		agentes = agentes + agentDesc + " "; 
	}
	agentes = agentes + ";           # i robots";
	rfile.appendLine(agentes);
	
//SET TASKS	
	//tn1 tn2 - taskcompN
	String Tasks = "set TASKS    := ";
	for (int tn=0;tn<ntasksCompN;tn++){
		Tasks = Tasks + tasksCompN.get(tn) + " "; 
	}
	//tl1 tl2 - taskcompL
	for (int tl=0;tl<ntasksCompL;tl++){
		Tasks = Tasks + tasksCompL.get(tl) + " "; 
	}
	//sd1 sd2 - taskcompS
	for (int sd=0;sd<ntasksCompS;sd++){
		Tasks = Tasks + tasksCompS.get(sd) + " "; 
	}
	Tasks = Tasks + ";        # j tasks";
	rfile.appendLine(Tasks);

	
//SET SUBTASKS  - rever sd2st1 sd2sd2 sd2st2 	
	//t1 t2 t3 t4 t5 t6 t7 t8 t9 - tarefa
	String subtask = "set SUBTASKS := ";
	sdTasks.clear();		
	for (int t=0;t<taskList.size();t++){
		task taskX= taskList.get(t);
		if(taskX.getTaskType().equals("sd"))
		{
			if (!sdTasks.containsKey(taskX.getTask()))
			{
				subtask = subtask + taskX.getTask()+taskX.getTask()+ " ";
				sdTasks.put(taskX.getTask(), taskX.getTask());
			}
		}
		else
			subtask = subtask + taskX.getSubTask()+ " ";
	}
	subtask = subtask + ";  # k subtasks";
	rfile.appendLine(subtask);
	rfile.appendLine(" ");

//TASK LMIN
	rfile.appendLine("# Task Lmin (by task type)");
	rfile.appendLine("param Lmin:= # minimum number of subtasks in columns, tasks in rows");
		//taskcompN
		for (int tn=0;tn<ntasksCompN;tn++){
			rfile.appendLine("      "+tasksCompN.get(tn) + "  0"); 
		}
		//taskcompL
		for (int tl=0;tl<ntasksCompL;tl++){
			rfile.appendLine("      "+tasksCompL.get(tl) + "  0"); 
		}
		//taskcompS
		for (int sd=0;sd<ntasksCompS;sd++){
			rfile.appendLine("      "+tasksCompS.get(sd) + "  0"); 
		}
		rfile.appendLine(";");
		rfile.appendLine(" ");
		
//TASK LMAX
		rfile.appendLine("# Task Lmax (by task type)");
		rfile.appendLine("param Lmax:= # maximum number of subtasks in columns, tasks in rows");
		//taskcompN
		for (int tn=0;tn<ntasksCompN;tn++){
			rfile.appendLine("      "+tasksCompN.get(tn) + "  1"); 
		}
		//taskcompL
		for (int tl=0;tl<ntasksCompL;tl++){
			rfile.appendLine("      "+tasksCompL.get(tl) + "  "+ getNumberSubTasks(tasksCompL.get(tl))); 
		}
		//taskcompS
		for (int sd=0;sd<ntasksCompS;sd++){
			rfile.appendLine("      "+tasksCompS.get(sd) + "  1"); 
		}
		rfile.appendLine(";");
		rfile.appendLine(" ");

//AUX ARRAY
		//adiciona todas as tasks em um unico array
		ArrayList<String> tasksAll = new ArrayList<String>();
		for (int tn=0;tn<ntasksCompN;tn++)
			tasksAll.add(tasksCompN.get(tn)); 
		for (int tl=0;tl<ntasksCompL;tl++)
			tasksAll.add(tasksCompL.get(tl)); 
		for (int sd=0;sd<ntasksCompS;sd++)
			tasksAll.add(tasksCompS.get(sd)); 
		
//SUBTASK SIZE/PESO
		rfile.appendLine("# Subtask size (in the task limit)");
		rfile.appendLine("param S:= # maximum number of subtasks in columns, tasks in rows");
		//String subtaskSize = "param S:= # maximum number of subtasks in columns, tasks in rows";
		sdTasks.clear();
		String Tasks2 = "";
		String currTask2="";
		int contSub=0;
		sdTasks.clear();
		//para cada subtask
		for (int t=0;t<taskList.size();t++){
			task taskX= taskList.get(t);
			contSub=0;
			if(!sdTasks.containsKey(taskX.getTask()))
			{
				if(taskX.getTaskType().equals("sd"))
				{
					Tasks2="      "+taskX.getTask()+taskX.getTask();
					sdTasks.put(taskX.getTask(),taskX.getTask());
					//procuro outras subtasks da mesma task
					for (int st=0;st<taskList.size();st++){
						task taskX2 = taskList.get(st);
						if(taskX2.getTask().equals(taskX.getTask()))
							contSub++;
					}
					Tasks2=Tasks2+" "+contSub;
				}
				else
				{
					Tasks2="      "+taskX.getSubTask()+" 1";
					contSub++;
				}
			
				rfile.appendLine(Tasks2);
				Tasks2="";
			}
		}//for subtasks
		
			sdTasks.clear();
		rfile.appendLine(";");
		rfile.appendLine(" ");

//TASK STRUCTURE
		rfile.appendLine("# Task Structure");
		String subtaskStruct = "param P: ";
		sdTasks.clear();
		for (int t=0;t<taskList.size();t++){
			task taskX= taskList.get(t);
			if(taskX.getTaskType().equals("sd"))
			{
				if (!sdTasks.containsKey(taskX.getTask()))
				{
					subtaskStruct = subtaskStruct + taskX.getTask()+taskX.getTask()+ " ";
					sdTasks.put(taskX.getTask(), taskX.getTask());
				}
			}
			else
			subtaskStruct = subtaskStruct + taskX.getSubTask()+ " ";
		}
		subtaskStruct = subtaskStruct + ":= # subtasks in columns, tasks in row";
		rfile.appendLine(subtaskStruct);
		
		//para cada task
		String Tasks3 = "";
		String currTask="";//current task in the loop
		sdTasks.clear();
		for (int ta=0;ta<tasksAll.size();ta++){
			currTask=tasksAll.get(ta);
			Tasks3 = Tasks3 + tasksAll.get(ta) + " ";
			//para cada subtask
			for (int t=0;t<taskList.size();t++){
				task taskX= taskList.get(t);
				
				if(!sdTasks.containsKey(taskX.getTask()))
				if (taskX.getTask().equals(currTask))  
				{
					Tasks3 = Tasks3 + "1 ";
				}
				else
				{
					Tasks3 = Tasks3 + "0 ";
				}
				
				if(taskX.getTaskType().equals("sd"))
					if (!sdTasks.containsKey(taskX.getTask()))
						sdTasks.put(taskX.getTask(), taskX.getTask());
			}//for subtasks
			rfile.appendLine(Tasks3);
			Tasks3="";
			sdTasks.clear();
		}//for task
		rfile.appendLine(";");
		//Tasks3 = Tasks3 + ";";
		
		rfile.appendLine(" ");
		rfile.appendLine(" ");

//SUBTASKS PER TASK 		
		rfile.appendLine("# Number of Subtasks per Task");
		String nsubTk = "param N := ";
		//taskcompN
		for (int tn=0;tn<ntasksCompN;tn++){
			nsubTk=nsubTk+"  "+tasksCompN.get(tn) + "  "+ getNumberSubTasks(tasksCompN.get(tn))+","; 
		}
		//taskcompL
		for (int tl=0;tl<ntasksCompL;tl++){
			nsubTk=nsubTk+"  "+tasksCompL.get(tl) + "  "+ getNumberSubTasks(tasksCompL.get(tl))+","; 
		}
		//taskcompS
		for (int sd=0;sd<ntasksCompS;sd++){
			nsubTk=nsubTk+"  "+tasksCompS.get(sd) + "  "+ getNumberSubTasks(tasksCompS.get(sd))+","; 
		}
		
		nsubTk=nsubTk.substring(0, nsubTk.length()-1);
		
		nsubTk=nsubTk+"; # task 1 has 2 subtasks, etc.";
		rfile.appendLine(nsubTk);
		rfile.appendLine(" ");
		
//ROBOT LIMIT
		rfile.appendLine("# Task Limits per Robot");
		String limitAg = "param L :=";
		for (int r=0;r<nAgents;r++){
			agentX = agentsList.get(r);
			agentDesc = agentX.getAgentDesc();
			limitAg = limitAg + " "+agentDesc + " "+limitAgents+ ","; 
		}
		limitAg=limitAg.substring(0, limitAg.length()-1);
		limitAg = limitAg + "; # 1 6 means robot 1 can handle 6 tasks, etc.";
		rfile.appendLine(limitAg);
		rfile.appendLine("");
		

//UTILITY	
				rfile.appendLine("# Utility");
				//param U: 1 2 3 4 5 6 := # subtasks in columns, robots in rows
				String subtaskUtility = "param U: ";
				sdTasks.clear();
				for (int t=0;t<taskList.size();t++){
					task taskX= taskList.get(t);
					if(taskX.getTaskType().equals("sd"))
					{
						if (!sdTasks.containsKey(taskX.getTask()))
						{
							subtaskUtility = subtaskUtility + taskX.getTask()+taskX.getTask()+ " ";
							sdTasks.put(taskX.getTask(), taskX.getTask());
						}
					}
					else
						subtaskUtility = subtaskUtility + taskX.getSubTask()+ " ";
				}
				subtaskUtility = subtaskUtility + ":= # subtasks in columns, robots in row";
				rfile.appendLine(subtaskUtility);

			String robotPayoff;
			int payoff=0;
			for (int r=0;r<nAgents;r++){
			sdTasks.clear();
			robotPayoff="";
			agentX2 = agentsList.get(r);
			robotPayoff="      "+agentX2.agentDesc+" ";
	
			for (int t=0;t<taskList.size();t++){
				task taskX= taskList.get(t);
				if(taskX.getTaskType().equals("sd"))
				{
					if (!sdTasks.containsKey(taskX.getTask()))
					{
						//procuro subtasks da mesma task para somar
						//System.out.println("somando payoff da task:"+taskX.getTask());
						for (int st=0;st<taskList.size();st++){
							task taskX2 = taskList.get(st);
							if(taskX2.getTask().equals(taskX.getTask())){
								payoff=payoff+agentX2.getPayoffAijGLPK(taskX2.getSubTask());
								//payoff=payoff+agentX2.getPayoffAijPlanner(taskX2.getSubTask());
								
								//System.out.println("payoff da subtask:"+taskX2.getSubTask()+" é:"+agentX2.getPayoffAijPlanner(taskX2.getSubTask()));
							}
						}
						robotPayoff=robotPayoff+payoff+" ";
					    
						subtaskUtility = subtaskUtility + taskX.getTask()+taskX.getTask()+ " ";
						sdTasks.put(taskX.getTask(), taskX.getTask());
						
					}
				}
				else
				{
				    payoff=payoff+agentX2.getPayoffAijGLPK(taskX.getSubTask());
				    //payoff=payoff+agentX2.getPayoffAijPlanner(taskX.getSubTask());
				    robotPayoff=robotPayoff+payoff+" ";					
				}
			//zerar o payoff
				payoff=0;
			}
			rfile.appendLine(robotPayoff);
		}
			rfile.appendLine(";");
			
		
	rfile.appendLine(" ");
	rfile.appendLine("end;");
	rfile.appendLine(" ");

	rfile.closeFile();
}


public static int getNumberSubTasks(String task) {
	int nroSubTasks=0;
	for (int t=0;t<taskList.size();t++){
		task taskZ= taskList.get(t);
		
		if(taskZ.getTask().equals(task))
			nroSubTasks++;
	}
return nroSubTasks; 	
}

public static void identifyPossibleTasksAgents() {
	for (int a=1;a<=nAgents;a++){
		agents.add("ag"+a);
	}
}




}//end class