package odimappingbuilder.components;
public class FilterComponent implements MappingComponent{
	private String name; 
	private String source;
	private String expression;
	
	public FilterComponent(String name, String source, String expression) {
		super();
		this.name = name;
		this.source = source;
		this.expression = expression;
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
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}	
}
