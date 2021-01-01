package odimappingbuilder.components;

import java.util.ArrayList;
import java.util.List;

public class MappingComponentCreator {
	public static MappingComponent create(List<List<String>> propertyList) {
		String type=propertyList.get(0).get(1);
		MappingComponent component=null;
		
		if(type.equals("AGGREGATION")) {
			String name=propertyList.get(0).get(2); 
			String source=propertyList.get(0).get(3);			
			List<String[]> columns=new ArrayList<String[]>();			
			for(int i=1; i<propertyList.get(1).size(); i++) {
				String[] colEx=new String[] {propertyList.get(1).get(i),propertyList.get(2).get(i)};
				columns.add(colEx);
			}
			component=new AggregationComponent(name, source, columns);			
		}
		else if(type.equals("DISTINCT")) {
			String name=propertyList.get(0).get(2); 
			String source=propertyList.get(0).get(3);			
			List<String[]> columns=new ArrayList<String[]>();			
			for(int i=1; i<propertyList.get(1).size(); i++) {
				String[] colEx=new String[] {propertyList.get(1).get(i),propertyList.get(2).get(i)};
				columns.add(colEx);
			}
			component=new DistinctComponent(name, source, columns);			 		
		}
		else if(type.equals("EXPRESSION")) {
			String name=propertyList.get(0).get(2); 
			String source=propertyList.get(0).get(3);			
			List<String[]> columns=new ArrayList<String[]>();			
			for(int i=1; i<propertyList.get(1).size(); i++) {
				String[] colEx=new String[] {propertyList.get(1).get(i),propertyList.get(2).get(i)};
				columns.add(colEx);
			}
			component=new ExpressionComponent(name, source, columns);			 				
		}
		else if(type.equals("FILTER")) {
			String name=propertyList.get(0).get(2);  
			String expression=propertyList.get(0).get(3);
			String source=propertyList.get(0).get(4);
			component=new FilterComponent(name, source, expression);
		}
		else if(type.equals("JOIN")) {
			String name=propertyList.get(0).get(2);
			String expression=propertyList.get(0).get(3);
			String joinType=propertyList.get(0).get(4);
			List<String> sources=propertyList.get(0).subList(5, propertyList.get(0).size()); 			
			component=new JoinComponent(name, sources, expression, joinType);
		}
		else if(type.equals("TABLE")) {
			String name=propertyList.get(0).get(2); 			 
			String modelName=propertyList.get(0).get(3); 
			String tableName=propertyList.get(0).get(4);
			String source=null;
			List<String[]> targetColumns=null;
			if(propertyList.get(0).size()>5) {
				source=propertyList.get(0).get(5);
				targetColumns=new ArrayList<String[]>();			
				for(int i=1; i<propertyList.get(1).size(); i++) {
					String[] colEx=new String[] {propertyList.get(1).get(i),propertyList.get(2).get(i)};
					targetColumns.add(colEx);
				}
			}
			component=new TableComponent(name, source, modelName, tableName, targetColumns);			
		}
		else if(type.equals("UNION_ALL")) {
			String name=propertyList.get(0).get(2);
			List<String> sources=propertyList.get(0).subList(3, propertyList.get(0).size()); 
			List<String> columns=propertyList.get(1).subList(1, propertyList.get(1).size()); 
			
			List<List<String>> expressions=new ArrayList<List<String>>();
			for(int i=0;i<propertyList.size()-2;i++) {
				List<String> exps=propertyList.get(i+2).subList(1, propertyList.get(i+2).size());
				expressions.add(exps);
			}
			
			component=new UnionAllComponent(name, sources, columns, expressions);			
		}		
		
		return component;
	}
}
