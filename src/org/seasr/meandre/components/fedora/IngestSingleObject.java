/**
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright � 2008, NCSA.  All rights reserved.
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
 *//**
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright � 2008, NCSA.  All rights reserved.
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

import java.io.*;
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

/**
 * 
 * <p>
 * Title: IngestSingleOject
 * </p>
 * 
 * <p>
 * Description: Ingests an object into a Fedora store.
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
		name="IngestSingleObject",
		tags="fedora ingest",
		creator="Mary Pietrowicz",
		description="Intests an object into a Fedora repository."
		)
public class IngestSingleObject implements ExecutableComponent {

	//INPUT
	@ComponentInput(
	description="APIM connection", 
	name=FedoraConstants.APIM)
	final String DATA_INPUT_1 = FedoraConstants.APIM;

	//PROPERTY
	@ComponentProperty(
	description = "Format of input",
	name = FedoraConstants.FORMAT, 
	defaultValue = FedoraConstants.FOXML)
	final static String PROPERTY1 = FedoraConstants.FORMAT;	
	
	@ComponentProperty(
	description = "Pathname to file to ingest",
	name = FedoraConstants.INGEST_FILE, 
	defaultValue = "/fedora/obj_test_100.xml")
	final static String PROPERTY2 = FedoraConstants.INGEST_FILE;	
	
	//OUTPUT
	@ComponentOutput(
	description="The ID assigned to the uploaded object", 
	name=FedoraConstants.PID)
	final String DATA_OUTPUT_1=FedoraConstants.PID;	
	
    FedoraAPIM APIM = null;

    /* The logger object to use for output. */
	private static Logger logger = null;
	
    public void initialize(ComponentContextProperties ccp)
	{
    	logger = ccp.getLogger();
		logger.info("Initializing IngestSingleObject");
	}

	public void execute(ComponentContext cc)
	  throws ComponentExecutionException, ComponentContextException
	  {
		try
		{
		     logger.info("Ingesting one object...");

		     APIM = (FedoraAPIM) cc.getDataComponentFromInput(DATA_INPUT_1);
		     String ingest_format = cc.getProperty(PROPERTY1);
		     String ingest_file_name = cc.getProperty(PROPERTY2);

		     logger.info("Ingesting "+ingest_file_name);
		     File ingest_file = new File(ingest_file_name);
		     FileInputStream ingest_stream = new FileInputStream(ingest_file);
		     String log_message = "Ingesting file "+ingest_file_name;

		     ByteArrayOutputStream outstr = new ByteArrayOutputStream();

		     pipeStream(ingest_stream, outstr, 4096);

		     String pid = APIM.ingest(outstr.toByteArray(), ingest_format, log_message);

		     logger.info("Ingested one object:  "+pid);

		     cc.pushDataComponentToOutput(DATA_OUTPUT_1, pid);
		}
		catch (ComponentContextException ex1)
		{
			logger.severe("Error in IngestSingleObject: "+ex1.getMessage());
			throw new ComponentContextException("Error in IngestSingleObject: "+ex1.getMessage());
		}
		catch (Throwable th)
		{
			logger.severe("Error in IngestSingleObject:  "+th.getMessage());
			throw new ComponentExecutionException(th);
		}		
	  }

	public static void pipeStream(InputStream instr, OutputStream outstr, int bsize)
	   throws IOException
	{
		byte[] buffer = null;
		int lengthread = 0;
		try
		{
		    buffer = new byte[bsize];
		    while ( (lengthread = instr.read(buffer)) > 0)
		    {
		    	outstr.write(buffer, 0, lengthread);
		    }
		}
		finally
		{
			try
			{
				instr.close();
				outstr.close();
			}
			catch (IOException ex)
			{  logger.info("Unable to close stream. "+ex.getMessage());
			   ex.printStackTrace();
			}
		}
	}

	public void dispose(ComponentContextProperties ccp)
    {
       logger.info("Disposing IngestSingleObject");
	}
}
