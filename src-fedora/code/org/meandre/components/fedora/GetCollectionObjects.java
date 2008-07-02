package org.meandre.components.fedora;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.axis.types.NonNegativeInteger;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.core.ComponentContextProperties;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;

import fedora.server.access.FedoraAPIA;
import fedora.server.management.FedoraAPIM;
import fedora.server.types.gen.ComparisonOperator;
import fedora.server.types.gen.Condition;
import fedora.server.types.gen.FieldSearchQuery;
import fedora.server.types.gen.FieldSearchResult;
import fedora.server.types.gen.ObjectFields;

import org.meandre.components.fedora.support.FedoraConstants;
import org.meandre.components.fedora.support.CorpusObject;

/**
 * 
 * <p>
 * Title: GetCollectionOjects
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
 * @author Mary Pietrowicz
 * @version 1.0
 */

@Component(
		name="GetCollectionObjects",
		tags="fedora collection",
		creator="Mary Pietrowicz",
		description="Gets the list of objects in a Fedora repository.",
		dependency={"activation-1.0.2.jar", "axis.jar", "batik-all.jar", "commons-codec-1.3.jar",
				"commons-dbcp-1.2.1.jar", "commons-discovery.jar", "commons-pool-1.2.jar",
				"foxml-merge.xsl", "jai_codec.jar", "jai_core.jar", "java-getopt-1.0.11.jar",
				"jaxrpc.jar", "jhbasic.jar", "jrdf-0.3.3.jar", "log4j-1.2.14.jar",
				"mail.jar", "mets-merge.xsl", "saaj.jar", "saxon.jar",
				"sunxacml-patched.jar", "trippi-1.1.2-core.jar", "wsdl4j-1.5.1.jar", "xercesImpl.jar",
				"xml-apis.jar", "commons-httpclient-3.1-beta1.jar", "commons-logging.jar", 
				"fedora-client.jar", "icu4j-3.8.1.jar", "icu4j-charsets-3.8.1.jar", 
				"jena-2.5.5.jar", "jena-arq-2.5.5.jar", "jena-arq-extra-2.5.5.jar", "jena-iri-2.5.5.jar", 
				"jena-json-2.5.5.jar"}							
		)
public class GetCollectionObjects implements ExecutableComponent
{
	
	//INPUT
	@ComponentInput(
	description="APIA connection", 
	name=FedoraConstants.APIA)
	final String DATA_INPUT_1 = FedoraConstants.APIA;

	//PROPERTY
	@ComponentProperty(
	description = "Fedora collection object type in a given repository",
	name = FedoraConstants.COLLECTION_PATTERN, 
	defaultValue = "monk:collection-*")
	final static String PROPERTY1 = FedoraConstants.COLLECTION_PATTERN;	
	
	//OUTPUT
	@ComponentOutput(
	description="The list of collection objects from a Fedora repository", 
	name=FedoraConstants.COLLECTION_OBJECTS)
	final String DATA_OUTPUT_1=FedoraConstants.COLLECTION_OBJECTS;	
	
  //  FedoraAPIM APIM = null;
    FedoraAPIA APIA = null;

    /* The logger object to use for output. */
	private static Logger logger = null;
	
	// String COLLECTION_PATTERN="monk:collection-*";
	
	public void initialize(ComponentContextProperties ccp)
	{
    	logger = ccp.getLogger();
		logger.info("Initializing GetCollectionObjects");
	}
	
	public void execute(ComponentContext cc)
	  throws ComponentExecutionException, ComponentContextException
	  {
		String collection_pattern = "monk:collection-*";
		ArrayList<CorpusObject> alist = new ArrayList<CorpusObject>(20);		
		
		try
		{	
		    logger.info("Getting the list of collection objects...");
		    
		    APIA = (FedoraAPIA) cc.getDataComponentFromInput(DATA_INPUT_1);
		    collection_pattern = cc.getProperty(PROPERTY1);
		    
		    // Get the Fedora pid and title for all of the objects in the repository
		    // that are of the collection type in a given repository with a valid
		    // pid.
		    
            String[] resultFields = {"pid","title"};
            NonNegativeInteger maxResults = new NonNegativeInteger("" + 10000);
            Condition[] condition = {new Condition("pid", ComparisonOperator.eq, "true")};
            FieldSearchQuery query = new FieldSearchQuery(condition, collection_pattern);
            FieldSearchResult result = null;

            result = APIA.findObjects(resultFields, maxResults, query);
            
            ObjectFields[] fields = result.getResultList();
            for(ObjectFields ofield: fields)
            {
               CorpusObject wobject = new CorpusObject( ofield.getPid());
               wobject.setTitle(ofield.getTitle(0));
               alist.add(wobject);
            }
            
            if(result.getListSession()!=null)
            {
               String token = result.getListSession().getToken();
               int count=1;
               while(result.getResultList().length!=0)
               {
                  count++;
                  if(result.getListSession()!=null)
                  {
                    token = result.getListSession().getToken();
                  }
                  else
                  {
                     break;
                  }
                  try 
                  {
                     result=APIA.resumeFindObjects(token);
                     fields=result.getResultList();
                     for(ObjectFields ofield: fields)
                     {
                       CorpusObject wobject = new CorpusObject( ofield.getPid());
                       wobject.setTitle(ofield.getTitle(0));
                       alist.add(wobject);
                     }
                  } 
                  catch (Throwable th1) 
                  {
                     th1.printStackTrace();
                  }
                } // End while
           } // End if
           	
           cc.pushDataComponentToOutput(DATA_OUTPUT_1, alist);	
		}
		catch (ComponentContextException ex1)
		{
			logger.severe("Error in GetCollectionObjects: "+ex1.getMessage());
			throw new ComponentContextException("Error in GetCollectionObjects: "+ex1.getMessage());
		}
		catch (Throwable th)
		{
			logger.severe("Error in GetCollectionObjects:  "+th.getMessage());
			throw new ComponentExecutionException(th);
		}
		
	  }
	
	public void dispose(ComponentContextProperties ccp)
    {
       logger.info("Disposing GetCollectionObjects");
	}
}
