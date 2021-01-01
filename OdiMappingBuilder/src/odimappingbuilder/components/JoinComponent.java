package odimappingbuilder.components;
import java.util.List;

public class JoinComponent implements MappingComponent{
	private String name; 
	private List<String> sources; 
	private String expression;
	private String joinType;
	
	public JoinComponent(String name, List<String> sources, String expression, String joinType) {
		super();
		this.name = name;
		this.sources = sources;
		this.expression = expression;
		this.joinType = joinType;
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
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getJoinType() {
		return joinType;
	}
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}	
}
