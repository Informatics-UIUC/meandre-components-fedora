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
import fedora.server.access.FedoraAPIA;
import fedora.server.types.gen.MIMETypedStream;
import fedora.server.types.gen.Property;
import org.meandre.components.fedora.support.FedoraConstants;

/**
 * 
 * <p>
 * Title: GetMonkOject
 * </p>
 * 
 * <p>
 * Description: Gets an html fragment for a requested resource in a demonstration Monk Fedora store.
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
		description="Gets an html fragment for an object from the Monk fedora store.",
		tags="fedora XML Monk Demo", 
		name="GetMonkObject")
public class GetMonkObject implements ExecutableComponent
{
	//INPUT
	@ComponentInput(
	description="An APIA connection", 
	name=FedoraConstants.APIA)
	final String DATA_INPUT_1 = FedoraConstants.APIA;
	
	@ComponentInput(
			description = "The fedora ID of the object to get",
			name = FedoraConstants.PID)
	final static String DATA_INPUT_2 = FedoraConstants.PID;
	
	@ComponentInput(
			description = "The workpart ID",
			name = "WorkpartID") 
	final static String DATA_INPUT_3 = "WorkpartID";

			

	//PROPERTY
	
	@ComponentProperty(
	description = "The behavior definition ID",
	name = FedoraConstants.B_DEF_PID, 
	defaultValue = "monk:behav-def-book")
	final static String PROPERTY2 = FedoraConstants.B_DEF_PID;
	
	@ComponentProperty(
	description = "The method name to call",
	name=FedoraConstants.METHOD_NAME, 
	defaultValue = "getChunk")
	final static String PROPERTY3 = FedoraConstants.METHOD_NAME;
	
	
	//OUTPUT
	@ComponentOutput(
	description="The xml object", 
    name="HTMLFrag")
	final String DATA_OUTPUT_1="HTMLFrag";
	
	FedoraAPIA APIA = null;

	/* The logger object to use for output. */
	private static Logger logger = null;
	
	public void initialize(ComponentContextProperties ccp)
	{	
		logger = ccp.getLogger();
		logger.info("Initializing GetMonkObject");
	}
	
	/*
	 * Gets an html fragment for a requested component in the Monk Fedora
	 * store.
	 * 
	 * Inputs: an APIA connection to fedora
	 * 
	 * Properties:
	 * pid:  The fedora id of the object to get
	 * bDefPid: The behavior definition id
	 * methodName: The method name to call
	 * 
	 * Outputs:
	 * HTMLFrag: the html fragment of a Monk object representation
	 * @see org.meandre.core.ExecutableComponent#execute(org.meandre.core.ComponentContext)
	 */
	public void execute(ComponentContext cc)
	  throws ComponentExecutionException, ComponentContextException
	  {
		 logger.info("Firing GetMonkObject");
		 try
		 {
			logger.info("Getting one XML object...");
			String b_def_pid = cc.getProperty(PROPERTY2);
			String method_name = cc.getProperty(PROPERTY3);
			
			APIA = (FedoraAPIA) cc.getDataComponentFromInput(DATA_INPUT_1);
			String pid =  (String)cc.getDataComponentFromInput(DATA_INPUT_2);
			String workpartId = (String)cc.getDataComponentFromInput(DATA_INPUT_3);

			Property propertiesArray[] = new Property[1];
			propertiesArray[0] = new Property();
			propertiesArray[0].setName("xmlid");
			propertiesArray[0].setValue(workpartId);
			System.out.println("Initialized property array.");
			
			MIMETypedStream mt_stream = null;
			
			//String output_html_fragment = "tester output";
			
			mt_stream = APIA.getDissemination(pid, b_def_pid, method_name, 
					propertiesArray, null);
		    String output_html_fragment = new String(mt_stream.getStream());
			
		    logger.info(output_html_fragment);
		    cc.pushDataComponentToOutput(DATA_OUTPUT_1, output_html_fragment);
		 }
		 catch (ComponentContextException ex1)
		 {
				logger.severe("Error in GetMonkObject: "+ex1.getMessage());
				throw new ComponentContextException("Error in GetMonkObject: "+ex1.getMessage());
		  }
		  catch (Throwable th)
		  {
				logger.severe("Error in GetMonkObject:  "+th.getMessage());
				throw new ComponentExecutionException(th);
		  }		 
	  }

	public void dispose(ComponentContextProperties ccp)
    {
       logger.info("Disposing GetMonkObject");
	}

}
