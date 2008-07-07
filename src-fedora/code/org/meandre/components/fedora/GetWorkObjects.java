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
 * Title: GetWorkOjects
 * </p>
 * 
 * <p>
 * Description: Gets the list of work objects in a Fedora store.
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
		name="GetWorkObjects",
		tags="fedora",
		creator="Mary Pietrowicz",
		description="Gets a list of work objects in a Fedora repository that start with a given pattern.",
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
public class GetWorkObjects implements ExecutableComponent
{
	//INPUT
	@ComponentInput(
	description="APIA connection", 
	name=FedoraConstants.APIA)
	final String DATA_INPUT_1 = FedoraConstants.APIA;

	//PROPERTY
	@ComponentProperty(
	description = "A pattern to match in the pid; look for objects with pids beginning with this string.",
	name = FedoraConstants.COLLECTION_PATTERN, 
	defaultValue = "monk:tcp-")
	final static String PROPERTY1 = FedoraConstants.COLLECTION_PATTERN;	
	
	//OUTPUT
	@ComponentOutput(
	description="The list of work objects from a Fedora repository which have pid identifiers beginning with a given pattern.", 
	name=FedoraConstants.WORK_OBJECTS)
	final String DATA_OUTPUT_1=FedoraConstants.WORK_OBJECTS;	
	
	FedoraAPIA APIA = null;

    /* The logger object to use for output. */
	private static Logger logger = null;
	
	
	public void initialize(ComponentContextProperties ccp)
	{
    	logger = ccp.getLogger();
		logger.info("Initializing GetWorkObjects");
	}
	
	public void execute(ComponentContext cc)
	  throws ComponentExecutionException, ComponentContextException
	{
		
		//String collection_pattern = "monk:collection-*";
		ArrayList<CorpusObject> alist = new ArrayList<CorpusObject>(300);	
		String collection_pattern = "monk:tcp-";
		
		try
		{	
		    logger.info("Getting the list of work objects...");
		    
		    APIA = (FedoraAPIA) cc.getDataComponentFromInput(DATA_INPUT_1);
		
		    collection_pattern = cc.getProperty(PROPERTY1);
		    
            String[] resultFields = {"pid","title"};
            NonNegativeInteger maxResults = new NonNegativeInteger("" + 10000);
        
            Condition[] condition = {new Condition("*", ComparisonOperator.eq, "true")};
            FieldSearchQuery query = new FieldSearchQuery();
            query.setTerms("*");
            query.setConditions(condition);
        
            FieldSearchResult result = null;
       
            result = APIA.findObjects(resultFields, maxResults, query);
        
            ObjectFields[] fields = result.getResultList();
            for(ObjectFields ofield: fields)
            {
                CorpusObject wobject = new CorpusObject( ofield.getPid());
                wobject.setTitle(ofield.getTitle(0));
                if(wobject.getFedoraPid().startsWith(collection_pattern))
                {
                  alist.add(wobject);
                }
            }

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
                   while(result.getListSession()!=null)
                   {
                     token = result.getListSession().getToken();
                     result=APIA.resumeFindObjects(token);
                     fields=result.getResultList();
                     for(ObjectFields ofield: fields)
                     {
                         CorpusObject wobject = new CorpusObject( ofield.getPid());
                         wobject.setTitle(ofield.getTitle(0));
                         //if(wobject.getFedoraPid().startsWith("monk:tcp"))
                         if(wobject.getFedoraPid().startsWith(collection_pattern))
                         {
                           alist.add(wobject);
                         }
                     }
                   //System.out.println(" Number of results "+count+" batch: "+ result.getResultList().length);
                   }
               }  // End try
               catch (Throwable th1) 
               {
                        // TODO Auto-generated catch block
                        th1.printStackTrace();
               }
           } // End while
           cc.pushDataComponentToOutput(DATA_OUTPUT_1, alist);
		}
		catch (ComponentContextException ex1)
		{
			logger.severe("Error in GetWorkObjects: "+ex1.getMessage());
			throw new ComponentContextException("Error in GetWorkObjects: "+ex1.getMessage());
		}
		catch (Throwable th)
		{
			logger.severe("Error in GetWorkObjects:  "+th.getMessage());
			throw new ComponentExecutionException(th);
		}
	}
	
	public void dispose(ComponentContextProperties ccp)
    {
       logger.info("Disposing GetWorkObjects");
	}

}
