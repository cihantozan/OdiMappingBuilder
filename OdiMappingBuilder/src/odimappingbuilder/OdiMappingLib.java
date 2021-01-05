package odimappingbuilder;
import java.util.Collection;
import java.util.List;

import oracle.odi.core.OdiInstance;
import oracle.odi.core.config.MasterRepositoryDbInfo;
import oracle.odi.core.config.OdiInstanceConfig;
import oracle.odi.core.config.PoolingAttributes;
import oracle.odi.core.config.WorkRepositoryDbInfo;
import oracle.odi.core.persistence.transaction.ITransactionDefinition;
import oracle.odi.core.persistence.transaction.ITransactionManager;
import oracle.odi.core.persistence.transaction.ITransactionStatus;
import oracle.odi.core.persistence.transaction.support.DefaultTransactionDefinition;
import oracle.odi.core.security.Authentication;
import oracle.odi.domain.adapter.AdapterException;
import oracle.odi.domain.mapping.IMapComponent;
import oracle.odi.domain.mapping.MapConnectorPoint;
import oracle.odi.domain.mapping.Mapping;
import oracle.odi.domain.mapping.component.AggregateComponent;
import oracle.odi.domain.mapping.component.DatastoreComponent;
import oracle.odi.domain.mapping.component.DistinctComponent;
import oracle.odi.domain.mapping.component.ExpressionComponent;
import oracle.odi.domain.mapping.component.FilterComponent;
import oracle.odi.domain.mapping.component.JoinComponent;
import oracle.odi.domain.mapping.component.SetComponent;
import oracle.odi.domain.mapping.exception.MappingException;
import oracle.odi.domain.model.OdiDataStore;
import oracle.odi.domain.model.finder.IOdiDataStoreFinder;
import oracle.odi.domain.project.OdiFolder;
import oracle.odi.domain.project.OdiProject;
import oracle.odi.domain.project.finder.IOdiProjectFinder;



public class OdiMappingLib {
	
	String url;
	String driver;
	String schema;
	String schemapwd;
	String workrep;
	String odiuser;
	String odiuserpwd;
	String projectCode;
	String folderName;
	String mappingName;
	
	private OdiInstance odiInstance;
	private Mapping mapping;	
	
	ITransactionDefinition txnDef;
	ITransactionManager tm;
	ITransactionStatus txnStatus;
	
	public OdiMappingLib() {
		
	}
	
	public void connect(OdiInstance odiInstance, String projectCode, String folderName, String mappingName) throws Exception {				
		this.odiInstance=odiInstance;
		this.projectCode = projectCode;
		this.folderName = folderName;
		this.mappingName = mappingName;					
		
		txnDef = new DefaultTransactionDefinition();
		tm = odiInstance.getTransactionManager();
		txnStatus = tm.getTransaction(txnDef);
		
		mapping = findMapping(projectCode, folderName, mappingName);
	}	

	public void connect(String url, String driver, String schema, String schemapwd, String workrep, String odiuser,String odiuserpwd, String projectCode, String folderName, String mappingName) throws Exception {				
		this.url = url;
		this.driver = driver;
		this.schema = schema;
		this.schemapwd = schemapwd;
		this.workrep = workrep;
		this.odiuser = odiuser;
		this.odiuserpwd = odiuserpwd;
		this.projectCode = projectCode;
		this.folderName = folderName;
		this.mappingName = mappingName;
		
		MasterRepositoryDbInfo masterInfo = new MasterRepositoryDbInfo(url, driver, schema, schemapwd.toCharArray(),new PoolingAttributes());
		WorkRepositoryDbInfo workInfo = new WorkRepositoryDbInfo(workrep, new PoolingAttributes());

		odiInstance = OdiInstance.createInstance(new OdiInstanceConfig(masterInfo, workInfo));
		Authentication auth = odiInstance.getSecurityManager().createAuthentication(odiuser, odiuserpwd.toCharArray());
		odiInstance.getSecurityManager().setCurrentThreadAuthentication(auth);	
		
		txnDef = new DefaultTransactionDefinition();
		tm = odiInstance.getTransactionManager();
		txnStatus = tm.getTransaction(txnDef);
		
		mapping = findMapping(projectCode, folderName, mappingName);
	}
	
	public void save() {
		odiInstance.getTransactionalEntityManager().persist(mapping);
		tm.commit(txnStatus);
	}
	
	
	
	public void addUnionAll(String name, List<String> sources, List<String> columns, List<List<String>> expressions) throws AdapterException, MappingException {
		SetComponent setComponent = (SetComponent) mapping.createComponent(SetComponent.COMPONENT_TYPE_NAME, name);		
				
		for(int i=0; i<sources.size(); i++) {
			connectComponent(sources.get(i), name);
		}
		
		for(int i=0; i<columns.size(); i++) {
			setComponent.addSetAttribute(columns.get(i), new String[0]);
		}
		
		for(int i=0; i<expressions.size(); i++) {			
			
			MapConnectorPoint mapConnectorPoint = setComponent.getInputConnectorPoint(i);	
			if(i>0) {
				setComponent.setSetOperationType(mapConnectorPoint, SetComponent.SET_OP_TYPE_UNION_ALL);
			}
			
			for(int k=0; k<columns.size(); k++) {				
				setComponent.addSetExpression(columns.get(k), expressions.get(i).get(k), mapConnectorPoint);					
			}						
		}
		
	}
	
	public void addDistinct(String name, String source,List<String[]> columns) throws AdapterException, MappingException {
		DistinctComponent distinctComponent = (DistinctComponent) mapping.createComponent(DistinctComponent.COMPONENT_TYPE_NAME, name);
		for(int i=0; i<columns.size(); i++) {
			distinctComponent.addAttribute(columns.get(i)[0], columns.get(i)[1], null, null, null);
		}
		connectComponent(source,name);
	}	
	
	public void addAggregation(String name, String source,List<String[]> columns) throws AdapterException, MappingException {
		AggregateComponent aggregateComponent = (AggregateComponent) mapping.createComponent(AggregateComponent.COMPONENT_TYPE_NAME, name);
		for(int i=0; i<columns.size(); i++) {
			aggregateComponent.addAttribute(columns.get(i)[0], columns.get(i)[1], null, null, null);
		}
		connectComponent(source,name);
	}
	
	public void addExpression(String name, String source,List<String[]> columns) throws AdapterException, MappingException {
		ExpressionComponent expressionComponent = (ExpressionComponent) mapping.createComponent(ExpressionComponent.COMPONENT_TYPE_NAME, name);
		for(int i=0; i<columns.size(); i++) {
			expressionComponent.addExpression(columns.get(i)[0], columns.get(i)[1], null, null, null);
		}
		connectComponent(source,name);
	}
	
	public void addJoin(String name, List<String> sources, String expression, String joinType) throws AdapterException, MappingException {
		JoinComponent joinComponent = (JoinComponent) mapping.createComponent(JoinComponent.COMPONENT_TYPE_NAME, name);
		joinComponent.setJoinConditionText(expression);
		joinComponent.setJoinType(joinType);
		for(int i=0;i<sources.size();i++) {
			connectComponent(sources.get(i), name);
		}
	}
	
	public void addFilter(String name, String source,String expression) throws AdapterException, MappingException {
		FilterComponent filterComponent = (FilterComponent) mapping.createComponent(FilterComponent.COMPONENT_TYPE_NAME, name);
		filterComponent.setFilterCondition(expression);
		connectComponent(source,name);
	}
	
	public void addTable(String name, String source, String modelName, String tableName, List<String[]> targetColumns) throws Exception {
		
        OdiDataStore odiDatastore = findDataStore(modelName, tableName);                
        IMapComponent component= mapping.createComponent(DatastoreComponent.COMPONENT_TYPE_NAME, odiDatastore);
        component.setName(name);
        
        if(targetColumns!=null) {
	        for(int i=0; i<targetColumns.size(); i++) {
	        	DatastoreComponent.findAttributeForColumn(component, odiDatastore.getColumn(targetColumns.get(i)[0])).setExpressionText(targetColumns.get(i)[1]);
			}
        }
        
        if(source!=null) {
        	connectComponent(source,name);
        }
	}
	
	public void connectComponent(String sourceComponentName, String targetComponentName) throws AdapterException, MappingException {
		IMapComponent sourceComponent = mapping.findComponentByName(sourceComponentName);
		IMapComponent targetComponent = mapping.findComponentByName(targetComponentName);
		sourceComponent.connectTo(targetComponent);
	}
	


	
	public OdiDataStore findDataStore(String modelName, String tableName) throws Exception {
		OdiDataStore odiDatastore = ((IOdiDataStoreFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiDataStore.class)).findByName(tableName, modelName);
		if(odiDatastore==null) throw new Exception(modelName+" - "+tableName+" not found");
		return odiDatastore;
	}
	
	public OdiFolder findFolder(String projectCode,String folderName) throws Exception {
		/*
		Collection<OdiFolder> odiFolders = ((IOdiFolderFinder) odiInstance.getTransactionalEntityManager().getFinder(OdiFolder.class)).findByName(folderName, projectCode);
		if (odiFolders.size() == 0) {
			throw new Exception("Error: cannot find folder " + folderName + " in project " + projectCode);			
		}
		else if (odiFolders.size() > 1) {
			throw new Exception("Error: multiple folders found " + folderName + " in project " + projectCode);			
		}
		else {
			OdiFolder odiFolder = (OdiFolder) (odiFolders.toArray()[0]);
			return odiFolder;
		}
		*/
		
		String[] path=folderName.split("/");		
		
		OdiProject project = ((IOdiProjectFinder)odiInstance.getTransactionalEntityManager().getFinder(OdiProject.class)).findByCode(projectCode);
		if(project==null) throw new Exception(projectCode + " Project not found");
		Collection<OdiFolder> projectFolders= project.getFolders();
		OdiFolder currentFolder=null;
		
		for (int i = 0; i < path.length; i++) {			
			if(i==0) {
				currentFolder=findSubFolder(projectFolders,path[i]);
			}
			else {
				currentFolder=findSubFolder(currentFolder, path[i]);
			}
			
			if(currentFolder==null) throw new Exception(path[i]+" not found");			
		}
		
		return currentFolder;
		
	}

	public OdiFolder findSubFolder(OdiFolder odiFolder, String folderName) {		
		for (OdiFolder subFolder : odiFolder.getSubFolders()) {
			if (subFolder.getName().equals(folderName)) {
				return subFolder;
			}
		}
		return null;
	}
	public OdiFolder findSubFolder(Collection<OdiFolder> folderList, String folderName) {		
		for (OdiFolder subFolder : folderList) {
			if (subFolder.getName().equals(folderName)) {
				return subFolder;
			}
		}
		return null;
	}
	
	public Mapping findMapping(String projectCode, String folderName, String mappingName) throws Exception {		
		
		/*
		Collection<Mapping> mappings = ((IMappingFinder) odiInstance.getTransactionalEntityManager().getFinder(Mapping.class)).findByName(mappingName,projectCode,folderName);
		if(mappings.size()==0) {
			throw new Exception("Error: connot find mapping "+ mappingName + " in project " + projectCode + " folder " + folderName );
		}
		else if(mappings.size()>1) {
			throw new Exception("Error: multiple mappings found "+ mappingName + " in project " + projectCode + " folder " + folderName );
		}
		else {
			Mapping mapping = (Mapping)mappings.toArray()[0];
			return mapping;
		}
		*/
		
		OdiFolder folder=findFolder(projectCode, folderName);
		for(Mapping mapping : folder.getMappings()) {
			if(mapping.getName().equals(mappingName)) return mapping;
		}
		throw new Exception(mappingName+" not found");
		
	}
	public void clearMapping() throws AdapterException, MappingException {
		while(true) {
			if(mapping.getComponentCount()==0) break;
			
			mapping.removeComponent(mapping.getAllComponents().get(0),true);
		}					
	}
}
