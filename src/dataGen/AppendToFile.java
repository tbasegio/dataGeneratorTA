package dataGen;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class AppendToFile 
{

	String fileName;
	String folderName;
	File file;
	FileWriter fileWritter;
	BufferedWriter bufferWritter;
	
	public AppendToFile(String fileName, boolean keepFile)  
    {	
		this.fileName=fileName;
    	try{
    		File file =new File(fileName);
    		
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		//true = append file - sem o true - new file
    		//this.fileWritter = new FileWriter(file.getName(),keepFile);
    		this.fileWritter = new FileWriter(file.getAbsolutePath(),keepFile);
    		//FileWriter fileWritter = new FileWriter(file.getName(),true);
    		this.bufferWritter = new BufferedWriter(fileWritter);
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
	
	public void appendLine(String lineText)  
    {	
    	try{
    		this.bufferWritter.write(lineText);
    		this.bufferWritter.newLine();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    
    }
	
	public void appendSameLine(String text)  
    {	
    	try{
    		this.bufferWritter.write(text);
    	    //bufferWritter.newLine();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    
    }
	
	public void closeFile()  
    {	
    	try{
    		this.bufferWritter.flush();
    		this.bufferWritter.close();
    	    System.out.println("File done: "+fileName);
    	    
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    
    }
	

	public static void createFolder(String path) {

//      File file = new File(path);
//      if (!file.exists()) {
//          if (file.mkdir()) {
//              System.out.println("Directory is created!");
//          } else {
//              System.out.println("Failed to create directory!");
//          }
//      }

      File files = new File(path);
      if (!files.exists()) {
          if (files.mkdirs()) {
              System.out.println("Multiple directories are created!");
          } else {
              System.out.println("Failed to create multiple directories!");
          }
      }

  }	
	
	
	
	//main
	
//	AppendToFile file = new AppendToFile("result33.txt");
//	file.appendLine("aaaaaaaaaaaa");
//	file.appendLine("bbbbbbbbbbbbbb");
//	file.appendLine("ccccccccccccccccc");
//	file.closeFile();


	
	
}