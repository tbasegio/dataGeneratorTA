package readResultsFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadGLPKTimeResults1 {

	//ALTERAR O CAMINHO PARA O FOLDER QUE SE DESEJA TRABALHAR
	private static final String PATHNAME = "C:\\MAS_Contest\\Workspace_Eclipse_Luna2\\AAMAS_dataGenerator\\TestData2\\";
	private static final String FILENAME = "GLPKTimeResults.txt";
	
	static String mainFolder="C:\\MAS_Contest\\Workspace_Eclipse_Luna2\\AAMAS_dataGenerator\\TestData2\\";

	public static void main(String[] args) {

		AppendToFile rfile2;
		rfile2= new AppendToFile(mainFolder+"GLPKTimeResultsParsed.txt",false);
		
		BufferedReader br = null;
		FileReader fr = null;

	//MUDAR OS VALORES DE X PARA OUTROS NOMES DE FOLDERS QUE NÃO SEJAM DE 1 A 100	
	
		String finalFileName=PATHNAME+FILENAME;
		try {
			fr = new FileReader(finalFileName);
			br = new BufferedReader(fr);
			String sCurrentLine;
			String sTime = "";
			br = new BufferedReader(new FileReader(finalFileName));
				
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.startsWith("startTime"))
				//startTime 12:05:24,73
				{
					System.out.println(sCurrentLine);
					sCurrentLine=sCurrentLine.substring(10);
					sTime=sCurrentLine;
					//rfile2.appendLine(sCurrentLine);
					System.out.println(sCurrentLine);
				}
				
				if (sCurrentLine.startsWith("endTime"))
					//endTime 12:05:25,68
					{
						System.out.println(sCurrentLine);
						sCurrentLine=sCurrentLine.substring(8);
						sTime=sTime+"|"+sCurrentLine;
						rfile2.appendLine(sTime);
						System.out.println(sCurrentLine);
					}
				
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	rfile2.closeFile();
	}//end main
}