package odimappingbuilder;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Main {

	public static void main(String[] args) throws Exception {		
		
		/*
		String fileName="/home/cihan/Documents/Mapping.xlsx";		
		String url="jdbc:oracle:thin:@172.17.0.2:1521/ORCLPDB1";
		String driver="oracle.jdbc.OracleDriver";
		String schema="DEV_ODI_REPO";
		String schemapwd="cihan123";
		String workrep="WORKREP";
		String odiuser="SUPERVISOR";
		String odiuserpwd="cihan123";
		String projectCode="MYPROJECT";
		String folderName="MYFOLDER";
		String mappingName="MAPPING1";
		*/
		
		/*
		String fileName=args[0];		
		String url=args[1];
		String driver=args[2];
		String schema=args[3];
		String schemapwd=args[4];
		String workrep=args[5];
		String odiuser=args[6];
		String odiuserpwd=args[7];
		String projectCode=args[8];
		String folderName=args[9];
		String mappingName=args[10];
		*/	
		
		Properties prop = new Properties();
		InputStream is = new FileInputStream("parameters.conf");
		prop.load(is);
		
		String fileName=prop.getProperty("fileName").trim();
		String url=prop.getProperty("url").trim();
		String driver=prop.getProperty("driver").trim();
		String schema=prop.getProperty("schema").trim();
		String schemapwd=prop.getProperty("schemapwd").trim();
		String workrep=prop.getProperty("workrep").trim();
		String odiuser=prop.getProperty("odiuser").trim();
		String odiuserpwd=prop.getProperty("odiuserpwd").trim();
		String projectCode=prop.getProperty("projectCode").trim();
		String folderName=prop.getProperty("folderName").trim();
		String mappingName=prop.getProperty("mappingName").trim();
		
		
		
		OdiMappingBuilder builder=new OdiMappingBuilder();
		builder.build(fileName, url, driver, schema, schemapwd, workrep, odiuser, odiuserpwd, projectCode, folderName, mappingName);
	}

}
