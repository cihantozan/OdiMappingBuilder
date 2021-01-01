package odimappingbuilder.components;
import java.util.List;

public class AggregationComponent implements MappingComponent {
	private String name; 
	private String source;
	private List<String[]> columns;
		
	public AggregationComponent(String name, String source, List<String[]> columns) {
		super();
		this.name = name;
		this.source = source;
		this.columns = columns;
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
	public List<String[]> getColumns() {
		return columns;
	}
	public void setColumns(List<String[]> columns) {
		this.columns = columns;
	}	
}
