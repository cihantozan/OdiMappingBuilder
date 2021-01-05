package odimappingbuilder;
import java.util.List;

import odimappingbuilder.components.*;
import oracle.odi.core.OdiInstance;


public class OdiMappingBuilder {
	
	private OdiMappingLib odiMappingLib;
	
	public void build(String fileName,OdiInstance odiInstance) throws Exception {
		buildMapping(fileName,odiInstance, null, null, null, null, null, null, null);
	}
	public void build(String fileName,String url, String driver, String schema, String schemapwd, String workrep, String odiuser,String odiuserpwd) throws Exception {
		buildMapping(fileName,null,url, driver,  schema,  schemapwd, workrep, odiuser, odiuserpwd);
	}
	
	private void buildMapping(String fileName,OdiInstance odiInstance,String url, String driver, String schema, String schemapwd, String workrep, String odiuser,String odiuserpwd) throws Exception {
		
		List<MappingComponent> componentList;
		String[] mName;
		
		String extension=fileName.substring(fileName.lastIndexOf(".")+1);
		if(extension.equals("xlsx")) {
			ExcelReader excelReader=new ExcelReader();
			componentList=excelReader.readExcel(fileName);
			mName=excelReader.readMappingName(fileName);				
		}
		else {
			TextReader textReader=new TextReader();
			componentList=textReader.readText(fileName);
			mName=textReader.readMappingName(fileName);
		}
		
		
		System.out.println("Project: "+mName[0]+"\nFolder: "+mName[1]+ "\nMapping: "+mName[2]+"\nmapping build start");
		
		
		odiMappingLib=new OdiMappingLib();
		
		if(odiInstance==null) {
			odiMappingLib.connect(url, driver, schema, schemapwd, workrep, odiuser, odiuserpwd, mName[0], mName[1], mName[2]);
		}
		else {
			odiMappingLib.connect(odiInstance, mName[0], mName[1], mName[2]);
		}
		
		
		odiMappingLib.clearMapping();
		
		for(int i=0;i<componentList.size();i++) {
			MappingComponent mappingComponent = componentList.get(i);
			
			if(mappingComponent.getClass().equals(AggregationComponent.class)) {
				AggregationComponent c=(AggregationComponent)mappingComponent;
				odiMappingLib.addAggregation(c.getName(), c.getSource(), c.getColumns());
			}
			else if(mappingComponent.getClass().equals(DistinctComponent.class)) {
				DistinctComponent c=(DistinctComponent) mappingComponent;
				odiMappingLib.addDistinct(c.getName(), c.getSource(), c.getColumns());
			}
			else if(mappingComponent.getClass().equals(ExpressionComponent.class)) {
				ExpressionComponent c=(ExpressionComponent) mappingComponent;
				odiMappingLib.addExpression(c.getName(), c.getSource(), c.getColumns());
			}
			else if(mappingComponent.getClass().equals(FilterComponent.class)) {
				FilterComponent c=(FilterComponent) mappingComponent;
				odiMappingLib.addFilter(c.getName(), c.getSource(), c.getExpression());
			}
			else if(mappingComponent.getClass().equals(JoinComponent.class)) {
				JoinComponent c=(JoinComponent) mappingComponent;
				odiMappingLib.addJoin(c.getName(), c.getSources(), c.getExpression(), c.getJoinType());
			}
			else if(mappingComponent.getClass().equals(TableComponent.class)) {
				TableComponent c=(TableComponent) mappingComponent;
				odiMappingLib.addTable(c.getName(), c.getSource(), c.getModelName(), c.getTableName(), c.getTargetColumns());
			}
			else if(mappingComponent.getClass().equals(UnionAllComponent.class)) {
				UnionAllComponent c=(UnionAllComponent) mappingComponent;
				odiMappingLib.addUnionAll(c.getName(), c.getSources(), c.getColumns(), c.getExpressions());
			}
			
		}
		
		odiMappingLib.save();
		System.out.println("Completed");
	}
	
	
}
