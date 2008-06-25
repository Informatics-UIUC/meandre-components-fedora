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
name="GetXmlObject")
public class GetXmlObject implements ExecutableComponent {

	//INPUT
	@ComponentInput(
	description="An APIM connection", 
	name=FedoraConstants.APIM)
	final String DATA_INPUT_1 = FedoraConstants.APIM;

	//PROPERTY
	@ComponentProperty(
	description = "The Fedora ID of the object to get.",
	name = FedoraConstants.PID, 
	defaultValue = "demo:5")
	final static String PROPERTY1 = FedoraConstants.PID;

	//OUTPUT
	@ComponentOutput(
	description="The xml object.", 
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

			pid =  cc.getProperty(PROPERTY1);

			APIM = (FedoraAPIM) cc.getDataComponentFromInput(DATA_INPUT_1);

		    byte[] objectXML = APIM.getObjectXML(pid);
		    String stringXML = new String(objectXML);
		    cc.pushDataComponentToOutput(DATA_OUTPUT_1, objectXML);
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
