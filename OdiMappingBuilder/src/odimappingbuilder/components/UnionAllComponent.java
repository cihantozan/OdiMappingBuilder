package odimappingbuilder.components;
import java.util.List;

public class UnionAllComponent implements MappingComponent {
	private String name; 
	private List<String> sources; 
	private List<String> columns; 
	private List<List<String>> expressions;
	
	public UnionAllComponent(String name, List<String> sources, List<String> columns, List<List<String>> expressions) {
		super();
		this.name = name;
		this.sources = sources;
		this.columns = columns;
		this.expressions = expressions;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getSources() {
		return sources;
	}
	public void setSources(List<String> sources) {
		this.sources = sources;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public List<List<String>> getExpressions() {
		return expressions;
	}
	public void setExpressions(List<List<String>> expressions) {
		this.expressions = expressions;
	}
	
	
}
