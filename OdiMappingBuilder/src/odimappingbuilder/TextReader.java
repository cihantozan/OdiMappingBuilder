package odimappingbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import odimappingbuilder.components.MappingComponent;
import odimappingbuilder.components.MappingComponentCreator;

public class TextReader {
	
	private String delimiter=";";
	
	public String[] readMappingName(String fileName) throws IOException {
		File file=new File(fileName);
		BufferedReader br=new BufferedReader(new FileReader(file));
		String projectCode=br.readLine().split(delimiter)[1];
        String folder=br.readLine().split(delimiter)[1];
        String mapping=br.readLine().split(delimiter)[1];
		br.close();
		return new String[] {projectCode,folder,mapping};
	}
	public List<MappingComponent> readText(String fileName) throws IOException {
		File file=new File(fileName);
		BufferedReader br=new BufferedReader(new FileReader(file));
		for(int i=0;i<4;i++) {
			br.readLine();
		}
		
		List<MappingComponent> componentList=new ArrayList<MappingComponent>();        
        List<List<String>> propertyList=new ArrayList<List<String>>();
        
        String str;
        while((str=br.readLine())!=null) {
        	List<String> rowArray=Arrays.asList(str.split(delimiter));
        	

            if(rowArray.size()>0 && rowArray.get(0)!= null && rowArray.get(0)!="") {
            	propertyList.add(rowArray);
            }
            else if (propertyList.size()>0) {           
            	MappingComponent mappingComponent=MappingComponentCreator.create(propertyList); 
            	componentList.add(mappingComponent);            	
            	propertyList=new ArrayList<List<String>>();
            }
            
        }
        
        if (propertyList.size()>0) {           
        	MappingComponent mappingComponent=MappingComponentCreator.create(propertyList); 
        	componentList.add(mappingComponent);            	
        	propertyList=new ArrayList<List<String>>();
        }
        
        br.close();
        
        return componentList;
        
	}
}
