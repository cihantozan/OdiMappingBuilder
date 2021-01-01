package odimappingbuilder.components;
import java.util.List;

public class TableComponent implements MappingComponent{
	private String name; 
	private String source; 
	private String modelName; 
	private String tableName; 
	private List<String[]> targetColumns;
	
	public TableComponent(String name, String source, String modelName, String tableName,
			List<String[]> targetColumns) {
		super();
		this.name = name;
		this.source = source;
		this.modelName = modelName;
		this.tableName = tableName;
		this.targetColumns = targetColumns;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<String[]> getTargetColumns() {
		return targetColumns;
	}
	public void setTargetColumns(List<String[]> targetColumns) {
		this.targetColumns = targetColumns;
	}	
}
