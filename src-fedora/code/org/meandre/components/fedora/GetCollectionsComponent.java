/**
 * 
 * <p>
 * Title: GetCollectionComponent
 * </p>
 * 
 * <p>
 * Description: Gets the list of collection objects in a Fedora store.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: Automated Learning Group, NCSA
 * </p>
 * 
 * @author Amit Kumar
 * @version 1.0
 */

package org.meandre.components.fedora;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jrdf.graph.Node;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.components.fedora.support.FedoraConstants;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.trippi.TrippiException;
import org.trippi.TupleIterator;

import fedora.client.FedoraClient;
import fedora.common.PID;

@Component(creator="Amit Kumar", description="Returns the list of works that make a collection",
		name="GetCollectionsComponent", tags="fedora monk collections",
		dependency={"activation-1.0.2.jar", "axis.jar", "batik-all.jar", "commons-codec-1.3.jar",
		"commons-dbcp-1.2.1.jar", "commons-discovery.jar", "commons-pool-1.2.jar",
		"foxml-merge.xsl", "jai_codec.jar", "jai_core.jar", "java-getopt-1.0.11.jar",
		"jaxrpc.jar", "jhbasic.jar", "jrdf-0.3.3.jar", "log4j-1.2.14.jar",
		"mail.jar", "mets-merge.xsl", "saaj.jar", "saxon.jar",
		"sunxacml-patched.jar", "trippi-1.1.2-core.jar", "wsdl4j-1.5.1.jar", "xercesImpl.jar",
		"xml-apis.jar", "commons-httpclient-3.1-beta1.jar", "commons-logging.jar", 
		"fedora-client.jar", "icu4j-3.8.1.jar", "icu4j-charsets-3.8.1.jar", 
		"jena-2.5.5.jar", "jena-arq-2.5.5.jar", "jena-arq-extra-2.5.5.jar", "jena-iri-2.5.5.jar", 
		"jena-json-2.5.5.jar","xmlpull_1_1_3_4a.jar"}	)
public class GetCollectionsComponent implements ExecutableComponent {

	@ComponentInput(description="The fedora client.", name="fedoraClient")
	private static final String DATA_IN_1 ="fedoraClient";
	
	@ComponentProperty(defaultValue=FedoraConstants.SUPER_COLLECTION_PREDICATE, description="defines membership predicate",
			name="collectionMembershipPredicate")
	private static final String DATA_PROP_1 ="collectionMembershipPredicate";
	
	@ComponentProperty(defaultValue="10", 
			description="Increase this if the collection has large number of members.",
			name="collectionArrayInitalSize")
	private static final String DATA_PROP_2 ="collectionArrayInitalSize";
	
	
	
	
	@ComponentProperty(defaultValue=FedoraConstants.SUPER_COLLECTION,
			description="The Super collection that each collection is a member of.",
			name="superCollectionPID")
	private static final String DATA_PROP_3 ="superCollectionPID";
	
	
	
	
	@ComponentOutput(description="The arraylist with collectin ids", name="collectionArrayList")
	private static final String DATA_OUT_1 = "collectionArrayList";
	
	@ComponentOutput(description="The Super CollectionId URI", name="superCollectionId")
	private static final String DATA_OUT_2 ="superCollectionId";
	
	
	private Logger logger;
	
	public void dispose(ComponentContextProperties ccp) {
		// TODO Auto-generated method stub

	}

	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException {
			logger.info("In the execute GetCollectionsComponent");
			String predicate = cc.getProperty(DATA_PROP_1);
			String _arrayInitialSize = cc.getProperty(DATA_PROP_2);
			int arrayInitialSize = 100;
			try{
			arrayInitialSize=Integer.parseInt(_arrayInitialSize);
			}catch(Exception ex){
				arrayInitialSize = 100;
			}
			String object = cc.getProperty(DATA_PROP_3);
			FedoraClient fclient = (FedoraClient)cc.getDataComponentFromInput(DATA_IN_1);
		
			ArrayList<String> alist = new ArrayList<String>(arrayInitialSize);
			
			
			if(!object.startsWith("info:")){
				object = PID.toURI(object);
			}
			
			String datatype=null;
			boolean isLiteral = Boolean.FALSE;
			String query = "";
	        if (isLiteral) {
	            if (datatype != null) {
	                query = String.format("select $s from <#ri> where $s <%s> '%s'^^<%s>", predicate, object, datatype);
	            } else {
	                query = String.format("select $s from <#ri> where $s <%s> '%s'", predicate, object);
	            }
	        } else {
	            query = String.format("select $s from <#ri> where $s <%s> <%s>;",
	                        predicate, object);
	        }

			Map<String, String> params = new HashMap<String, String>();
	        params.put("lang", "itql");
	        params.put("flush", "true");
	        params.put("query", query);
	        logger.info("doing a query: " + query);
			
	        try {
				TupleIterator tuples =fclient.getTuples(params);
				while(tuples.hasNext()){
					Map<String,Node> row = tuples.next();
					  for (String key : row.keySet()) {
						  alist.add((row.get(key)).toString());
				        }

					
				}
			} catch (IOException e) {
				logger.finest("error: " + e.getMessage());
				throw new ComponentExecutionException(e.getMessage());
			} catch (TrippiException e) {
				logger.finest("error: " + e.getMessage());
				throw new ComponentExecutionException(e.getMessage());
			}
			
			cc.pushDataComponentToOutput(DATA_OUT_1, alist);
			cc.pushDataComponentToOutput(DATA_OUT_2, object);

	}

	public void initialize(ComponentContextProperties ccp) {
		this.logger = ccp.getLogger();
	}

}
