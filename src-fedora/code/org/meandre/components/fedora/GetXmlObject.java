/**
 * University of Illinois/NCSA
 * Open Source License
 * 
 * Copyright (c) 2008, Board of Trustees-University of Illinois.  
 * All rights reserved.
 * 
 * Developed by: 
 * 
 * Automated Learning Group
 * National Center for Supercomputing Applications
 * http://www.seasr.org
 * 
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal with the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: 
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimers. 
 * 
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimers in the 
 *    documentation and/or other materials provided with the distribution. 
 * 
 *  * Neither the names of Automated Learning Group, The National Center for
 *    Supercomputing Applications, or University of Illinois, nor the names of
 *    its contributors may be used to endorse or promote products derived from
 *    this Software without specific prior written permission. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * WITH THE SOFTWARE.
 */ 

package org.meandre.components.fedora;

import java.util.logging.Logger;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.core.ComponentContextProperties;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import fedora.server.management.FedoraAPIM;
import org.meandre.components.fedora.support.FedoraConstants;

/**
 * 
 * <p>
 * Title: GetXmlOject
 * </p>
 * 
 * <p>
 * Description: Gets object xml for a requested resource in a Fedora store.
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
 * @author Mary Pietrowicz
 * @version 1.1
 */

@Component(
creator="Mary Pietrowicz", 
description="Gets an object XML for a resource in a Fedora store.",
tags="fedora XML", 
name="GetXmlObject",
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
public class GetXmlObject implements ExecutableComponent {

	//INPUT
	@ComponentInput(
	description="An APIM connection", 
	name=FedoraConstants.APIM)
	final String DATA_INPUT_1 = FedoraConstants.APIM;

	
	@ComponentInput(
	description = "The Fedora ID of the object to get.",
	name = FedoraConstants.PID) 
	final static String  DATA_INPUT_2 = FedoraConstants.PID;

	//OUTPUT
	@ComponentOutput(
	description="The xml string.", 
	name=FedoraConstants.OBJECT_XML)
	final String DATA_OUTPUT_1=FedoraConstants.OBJECT_XML;
	
	/* The APIM object interface to the store. */
	FedoraAPIM APIM = null;

	/* The logger object to use for output. */
	private static Logger logger = null;
	
	public void initialize(ComponentContextProperties ccp)
	{
		logger = ccp.getLogger();
		logger.info("Initializing GetXmlObject");
	}
	
	/*
	 * Gets object xml for a requested component in Fedora.
	 * 
	 * Inputs: an APIM connection to fedora
	 * 
	 * Properties:
	 * pid:  The fedora id of the object to get
	 * 
	 * Outputs:
	 * objectXML: the xml representation of the object
	 * @see org.meandre.core.ExecutableComponent#execute(org.meandre.core.ComponentContext)
	 */
	public void execute(ComponentContext cc)
	  throws ComponentExecutionException, ComponentContextException
	  {
		 String pid = "demo:5";

		 logger.info("Firing FedoraInfo.");
		 try
		 {
			logger.info("Getting one XML object...");

			pid = (String)cc.getDataComponentFromInput(DATA_INPUT_2);
			
			APIM = (FedoraAPIM) cc.getDataComponentFromInput(DATA_INPUT_1);

		    byte[] objectXML = APIM.getObjectXML(pid);
		    String stringXML = new String(objectXML);
		    System.out.println(stringXML);
		    cc.pushDataComponentToOutput(DATA_OUTPUT_1, stringXML);
		 }
		 catch (ComponentContextException ex1)
		 {
				logger.severe("Error in GetXmlObject: "+ex1.getMessage());
				throw new ComponentContextException("Error in GetXmlObject: "+ex1.getMessage());
		  }
		  catch (Throwable th)
		  {
				logger.severe("Error in GetXmlObject:  "+th.getMessage());
				throw new ComponentExecutionException(th);
		  }
	  }

	public void dispose(ComponentContextProperties ccp)
    {
       logger.info("Disposing GetXmlObject");
	}

}
