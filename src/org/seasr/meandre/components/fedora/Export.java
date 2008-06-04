/**
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright © 2008, NCSA.  All rights reserved.
 * 
 * Developed by:
 * The Automated Learning Group
 * University of Illinois at Urbana-Champaign
 * http://www.seasr.org
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 * 
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimers.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimers in
 * the documentation and/or other materials provided with the distribution.
 * 
 * Neither the names of The Automated Learning Group, University of
 * Illinois at Urbana-Champaign, nor the names of its contributors may
 * be used to endorse or promote products derived from this Software
 * without specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */

package org.seasr.meandre.components.fedora;

import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import fedora.server.management.FedoraAPIM;
import org.meandre.core.ComponentContextProperties;
import java.util.logging.Logger;

/**
 * 
 * <p>
 * Title: Export
 * </p>
 * 
 * <p>
 * Description: Exports a fedora object according to the specified
 *              XML format.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * 
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
		name="Export",
		tags="fedora export",
		creator="Mary Pietrowicz",
		description="<p>Exports an object from a fedora repository, according "+
		            "to the specified xml format, e.g., foxml1.0 or metslikefedora1.</p>"
		)
public class Export implements ExecutableComponent
{
	
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
    description = "The xml format: valid values are foxml1.0 or metslikefedora1",
    name = FedoraConstants.FORMAT,
    defaultValue= FedoraConstants.FOXML)
	final static String PROPERTY2 = FedoraConstants.FORMAT;	
	
	@ComponentProperty(
	description = "The export context: the intended use of the exported file. "+
	              "Valid values are public, migrate, or archive.",
	name = FedoraConstants.CONTEXT, 
	defaultValue = FedoraConstants.PUBLIC)
	final static String PROPERTY3 = FedoraConstants.CONTEXT;	

	//OUTPUT
	@ComponentOutput(
	description="The delete time.", 
	name=FedoraConstants.OBJECT_XML)
	final String DATA_OUTPUT_1=FedoraConstants.OBJECT_XML;		
	
	/* The logger object to use for output. */
	private static Logger logger = null;
	
	FedoraAPIM APIM = null;

	public void dispose(ComponentContextProperties ccp)
	{
	   logger.info("Disposing Fedora Export...");
	}

	/*
	 * Description:  Exports a fedora object according to the specified
	 *               XML format.
	 *
	 * Inputs:
	 * APIM: A FedoraAPIM object
	 *
	 * Properties:
	 * Assign null to all of the properties which should remain unchanged...
	 *
	 * PID: the fedora PID of the object
	 * FORMAT: the xml format: valid values are "foxml1.0" or
	 *         "metslikefedora1"
	 * CONTEXT: the export context: the intended use of the exported file.
	 *          Valid values are "public", "migrate", or "archive".
     *
     * Outputs:
     * OBJECT_XML: The formatted xml, of type byte[]
     *
	 */
	public void execute(ComponentContext cc)
	throws ComponentExecutionException, ComponentContextException
	{
		logger.info("Firing Fedora Export.execute...");

		try
		{
			APIM = (FedoraAPIM) cc.getDataComponentFromInput(FedoraConstants.APIM);
			String pid =  cc.getProperty(FedoraConstants.PID);
			String format = cc.getProperty(FedoraConstants.FORMAT);
			String context = cc.getProperty(FedoraConstants.CONTEXT);

			byte[] formattedXML = APIM.export(pid, format, context);
		    cc.pushDataComponentToOutput(FedoraConstants.OBJECT_XML, formattedXML);
		}		
		catch (ComponentContextException ex1)
		{
			logger.severe("Error in Export: "+ex1.getMessage());
			throw new ComponentContextException("Error in Export: "+ex1.getMessage());
		}
		catch (Throwable th)
		{
			logger.severe("Error in Export:  "+th.getMessage());
			throw new ComponentExecutionException(th);
		}	
	}

	public void initialize(ComponentContextProperties ccp)
	{
		logger = ccp.getLogger();
		logger.info("Initializing Fedora Export...");
	}
}
