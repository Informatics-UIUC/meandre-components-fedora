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
import fedora.server.access.FedoraAPIA;
import fedora.client.FedoraClient;
import org.meandre.core.ComponentContextProperties;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import java.util.logging.Logger;
import org.meandre.components.fedora.support.FedoraConstants;

/**
* 
* <p>
* Title: FedoraClientComponent
* </p>
* 
* <p>
* Description: Returns a Fedora Client and APIA/APIM objects for repository
* access and management.
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
* @version 1.1
*/

@Component(
		name="FedoraClientComponent",
		tags="fedora client APIA APIM",
		creator="Amit Kumar",
		description="A client for interacting w/Fedora.",
			dependency={"",""}
		)
public class FedoraClientComponent implements ExecutableComponent {

	// Fedora Properties
	@ComponentProperty(
	description="The protocol to use for talking to Fedora.", 
	name="PROTOCOL",
    defaultValue="http")
	public static final String PROTOCOL = "PROTOCOL";
	
	@ComponentProperty(
	description="The host name of the Fedora server.", 
	name="HOST",
    defaultValue="localhost")
	public static final String HOST = "HOST:PORT";
	
	@ComponentProperty(
	description="The fedora port", name="PORT",
			           defaultValue="8080")
	public static final String PORT = "PORT";
	
	@ComponentProperty(
	description="The fedora user ID", 
	name="USER",
    defaultValue="fedoraAdmin")
	public static final String USER = "USER";
	
	@ComponentProperty(
	description="The fedora password", 
	name="PWORD",
    defaultValue="XXXXXX")
	public static final String PWORD = "PWORD";


	private FedoraClient fclient = null;
	/*          Get information about the repository:
              http://localhost:8080/soapclient/apia?action_=DescribeRepository
          Get the thumbnail datastream (with DSID of DS1) in data object with a PID of demo:5
              http://localhost:8080/soapclient/apia?action_=GetDatastreamDissemination&amp;PID_=demo:5&amp;dsID_=DS1
          Get the Dissemination for a data object with a PID of demo:5 and associated behavior definition object with a PID of demo:1 and methodName of getThumbnail:
              http://localhost:8080/soapclient/apia?action_=GetDissemination&PID_=demo:5&bDefPID_=demo:1&methodName_=getThumbnail
          Get the ObjectHistory for a data object with a PID of demo:5:
              http://localhost:8080/soapclient/apia?action_=GetObjectHistory&PID_=demo:5
          Get the ObjectProfile for a data object with a PID of demo:5:
              http://localhost:8080/soapclient/apia?action_=GetObjectProfile&PID_=demo:5
          List the datastreams for data object with PID of demo:5
              http://localhost:8080/soapclient/apia?action_=ListDatastreams&amp;PID_=demo:5
          List the methods for data object with PID of demo:5
              http://localhost:8080/soapclient/apia?action_=ListMethods&amp;PID_=demo:5

	 */
	private FedoraAPIA APIA = null;
	private FedoraAPIM APIM = null;

	// output data port
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
		logger.info("Initializing FedoraClientComponent");
	}

	public void execute(ComponentContext cc)
	throws ComponentExecutionException, ComponentContextException
	{
		String fedoraURL = null;
		String protocol = "http";
		String host = "localhost:8080";
		String port = "8080";
		String user = "fedoraAdmin";
		String pass = "fedoraAdminPassword";

		logger.info("Firing FedoraClientComponent");
		try
		{

			protocol = cc.getProperty(PROTOCOL);
			host = cc.getProperty(HOST);
			port = cc.getProperty(PORT);
			user = cc.getProperty(USER);
			pass = cc.getProperty(PWORD);


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
			logger.severe("ComponentContextException error in FedoraClientComponent: "+ex1.getMessage());
			throw new ComponentContextException("Error in FedoraClientComponent: "+ex1.getMessage());
		}
		catch (Throwable th)
		{
			logger.severe("ComponentExecutionException error in FedoraClientComponent:  "+th.getMessage());
			throw new ComponentExecutionException(th);
		}
	}

	public void dispose(ComponentContextProperties ccp)
	{
		logger.info("Disposing FedoraClientComponent.");
	}
}

