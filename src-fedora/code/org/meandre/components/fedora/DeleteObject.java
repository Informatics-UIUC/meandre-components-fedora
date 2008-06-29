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

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import fedora.server.management.FedoraAPIM;
import org.meandre.core.ComponentContextProperties;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import java.util.logging.Logger;
import org.meandre.components.fedora.support.FedoraConstants;

/**
 * 
 * <p>
 * Title: DeleteObject
 * </p>
 * 
 * <p>
 * Description: A component that deletes an object from a Fedora store.
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
		name="DeleteObject",
		tags="fedora delete",
		creator="Mary Pietrowicz",
		description="Deletes an object from a Fedora repository.",
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
public class DeleteObject implements ExecutableComponent {

	//INPUT
	@ComponentInput(
	description="An APIM connection to fedora.", 
	name=FedoraConstants.APIM)
	final String DATA_INPUT_1 = FedoraConstants.APIM;

	//PROPERTY
	@ComponentProperty(
	description = "The fedora PID.",
	name = FedoraConstants.PID, 
	defaultValue = "test:100")
	final static String PROPERTY1 = FedoraConstants.PID;

	@ComponentProperty(
	description = "The message to write to the log when the component is deleted.",
	name = FedoraConstants.LOG_MSG, 
	defaultValue = "Deleting a component...")
	final static String PROPERTY2 = FedoraConstants.LOG_MSG;	

	@ComponentProperty(
	description = "Forced delete? (true/false)",
	name = FedoraConstants.FORCED, 
	defaultValue = "false")
	final static String PROPERTY3 = FedoraConstants.FORCED;	

	//OUTPUT
	@ComponentOutput(
	description="The delete time.", 
	name=FedoraConstants.DELETE_TIME)
	final String DATA_OUTPUT_1=FedoraConstants.DELETE_TIME;	

	/* The logger object to use for output. */
	private static Logger logger = null;
	
	FedoraAPIM APIM = null;

	public void dispose(ComponentContextProperties ccp) 
	{
		logger.info("Disposing DeleteObject...");
	}

	/*
	 * Description:  Removes a fedora object
	 *
	 * Inputs:
	 * APIM: A FedoraAPIM object
	 *
	 * Properties:
	 *
	 * PID: the fedora PID of the object
	 * LOG_MSG: a message to log when the command executes
	 * FORCED: if true, forces the deletion, even if it causes
	 *         inconsistencies within the repository; otherwise,
	 *         if false, not a forced deletion.
	 *
     * Outputs:
     * DELETE_TIME: The time which the object was deleted.
     *
	 */
	public void execute(ComponentContext cc)
			throws ComponentExecutionException, ComponentContextException
    {
		logger.info("Firing DeleteObject");

		try
		{
		   String pid = cc.getProperty(PROPERTY1);
           String log_msg = cc.getProperty(PROPERTY2);
           String instr = cc.getProperty(PROPERTY3);
           boolean forced = false;

           logger.info("Deleting "+pid);
           logger.info("Logging: "+log_msg);
           logger.info("forced: "+forced);

           if (instr.compareToIgnoreCase(FedoraConstants.TRUE) == 0)
           {  forced = true; }
           else if (instr.compareToIgnoreCase(FedoraConstants.FALSE) == 0)
           { forced = false; }
           else
           { throw new ComponentExecutionException("Invalid property 'forced' "); }

		   APIM = (FedoraAPIM) cc.getDataComponentFromInput(DATA_INPUT_1);

		   String purgeDateTime = APIM.purgeObject(pid, log_msg, forced);

		   cc.pushDataComponentToOutput(DATA_OUTPUT_1, purgeDateTime);

		}
		catch (ComponentContextException ex1)
		{
			logger.severe("Error in DeleteObject: "+ex1.getMessage());
			throw new ComponentContextException("Error in DeleteObject: "+ex1.getMessage());
		}
		catch (Throwable th)
		{
			logger.severe("Error in DeleteObject:  "+th.getMessage());
			throw new ComponentExecutionException(th);
		}

	}

	public void initialize(ComponentContextProperties ccp) 
	{
		logger = ccp.getLogger();
		logger.info("Initializing DeleteObject");
	}

}
