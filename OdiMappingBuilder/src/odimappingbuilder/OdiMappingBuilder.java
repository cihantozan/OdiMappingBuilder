package odimappingbuilder;
import java.util.List;

import odimappingbuilder.components.*;


public class OdiMappingBuilder {
	
	private OdiMappingLib odiMappingLib;
	
	public void build(String fileName,String url, String driver, String schema, String schemapwd, String workrep, String odiuser,String odiuserpwd, String projectCode, String folderName, String mappingName) throws Exception {
		
		ExcelReader excelReader=new ExcelReader();
		List<MappingComponent> componentList=excelReader.readExcel(fileName);
		
		odiMappingLib=new OdiMappingLib();
		odiMappingLib.connect(url, driver, schema, schemapwd, workrep, odiuser, odiuserpwd, projectCode, folderName, mappingName);
		
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
