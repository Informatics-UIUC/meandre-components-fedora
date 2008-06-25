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
import org.meandre.annotations.ComponentOutput;
import fedora.server.management.FedoraAPIM;
import fedora.server.access.FedoraAPIA;
import fedora.client.FedoraClient;
import org.meandre.components.fedora.support.FedoraConstants;

/**
* 
* <p>
* Title: DemoFedoraClientComponent
* </p>
* 
* <p>
* Description: Returns a Fedora Client and APIA/APIM objects for repository
* access and management for the demonstration Monk store.
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
		name="DemoFedoraClientComponent",
		tags="fedora client APIA APIM Demo Monk",
		creator="Mary Pierowicz",
		description="A client for interacting with the demonstration Monk Fedora store."
		)
public class DemoFedoraClientComponent implements ExecutableComponent {

    /* The Fedora client object output by this component. */
	private FedoraClient fclient = null;
	
	/* The APIA interface object output by this component. */
	private FedoraAPIA APIA = null;
	
	/* The APIM interface object output by this component. */
	private FedoraAPIM APIM = null;

	// OUTPUTS
	@ComponentOutput(
	description="The fedora client object.", 
	name="fedoraClient")
	public final static String DATA_PORT_OUT_1 = "fedoraClient";
	
	@ComponentOutput(
	description="The apia interface.", 
	name="apia")
	public final static String DATA_PORT_OUT_2 = "apia";
	
	@ComponentOutput(
	description="The apim interface.", 
	name="apim")
	public final static String DATA_PORT_OUT_3 = "apim";

	/* The logger object to use for output. */
	private static Logger logger = null;

	public void initialize(ComponentContextProperties ccp)
	{
		logger = ccp.getLogger();
		logger.info("Initializing DemoFedoraClientComponent");
	}

	public void execute(ComponentContext cc)
	throws ComponentExecutionException, ComponentContextException
	{
		String fedoraURL = null;
		String protocol = "https";
		String host = "monk.lis.uiuc.edu";
		String port = "443";
		String user = "fedoraAdmin";
		String pass = "treem0nkey";

		logger.info("Firing DemoFedoraClientComponent");
		try
		{
			fedoraURL = protocol+"://"+host+":"+port+"/fedora";
			logger.info("FedoraURL: "+fedoraURL);

			fclient = new FedoraClient(fedoraURL, user, pass);
			if(fclient==null){
				throw new ComponentExecutionException("Error getting FedoraClient");
			}
			APIA = fclient.getAPIA();
			APIM = fclient.getAPIM();


			cc.pushDataComponentToOutput(DATA_PORT_OUT_1, fclient);
			cc.pushDataComponentToOutput(DATA_PORT_OUT_2, APIA);
			cc.pushDataComponentToOutput(DATA_PORT_OUT_3, APIM);
		}
		catch (ComponentContextException ex1)
		{
			logger.severe("ComponentContextException error in DemoFedoraClientComponent: "+ex1.getMessage());
			throw new ComponentContextException("Error in DemoFedoraClientComponent: "+ex1.getMessage());
		}
		catch (Throwable th)
		{
			logger.severe("ComponentExecutionException error in DemoFedoraClientComponent:  "+th.getMessage());
			throw new ComponentExecutionException(th);
		}
	}

	public void dispose(ComponentContextProperties ccp)
	{
		logger.info("Disposing DemoFedoraClientComponent");
	}
}
