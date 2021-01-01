package odimappingbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import odimappingbuilder.components.MappingComponent;
import odimappingbuilder.components.MappingComponentCreator;

public class ExcelReader {
	public String[] readMappingName(String fileName) throws IOException {
		FileInputStream excelFile = new FileInputStream(new File(fileName));
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);  
        String projectCode=sheet.getRow(0).getCell(1).getStringCellValue();
        String folder=sheet.getRow(1).getCell(1).getStringCellValue();
        String mapping=sheet.getRow(2).getCell(1).getStringCellValue();
        
        workbook.close();
        excelFile.close();
        return new String[] {projectCode,folder,mapping};
	}
	public List<MappingComponent> readExcel(String fileName) throws IOException {
			
		FileInputStream excelFile = new FileInputStream(new File(fileName));
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);        
        DataFormatter dataFormatter = new DataFormatter();
                
        List<MappingComponent> componentList=new ArrayList<MappingComponent>();
        
        List<List<String>> propertyList=new ArrayList<List<String>>();
                        
        
        for(int i=4; i<sheet.getLastRowNum()-sheet.getFirstRowNum()+1; i++) {
        	Row row=sheet.getRow(i);
        	 
        	List<String> rowArray=new ArrayList<String>();
			if (row != null) {
				for (Cell cell : row) {
					String cellValue = dataFormatter.formatCellValue(cell);
					//System.out.print(cellValue + "\t");
					if (cellValue != null && cellValue != "") {
						rowArray.add(cellValue);
					}
				}
			}
            //System.out.println();
            
            
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
        
        workbook.close();
        excelFile.close();
        
        return componentList;
	}
	
	
}
