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

package org.meandre.components.fedora.support;

public class FedoraConstants {
	
	public static final String FALSE = "false";
	public static final String TRUE = "true";
	
	// Generic Fedora input, output, or property names
	
	/* FedoraAPIM object */
	public static final String APIM = "APIM";
	
	/* FedoraAPIA object */
	public static final String APIA = "APIA";
	
	/* Unique fedora identifier */
	public static final String PID = "pid";
	
	/* Message to log when command executes */
	public static final String LOG_MSG = "log_message";
	
	/* Whether something is versionable, has value "true" or "false" */
	public static final String VERSIONABLE = "versionable";
	
	/* The checksum, represented as a hex string */
	public static final String CKSUM = "checksum";
	
	public static final String CKSUM_TYPE = "checksumType";
	
	// START: valid CKSUM_TYPE values
	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA385 = "SHA-385";
    public static final String SHA512 = "SHA-512";
	// END: valid CKSUM_TYPE values
	
	/* A valid MIME type. */
	public static final String DS_MIME = "dsMIME";
	
	/* A datastream ID, an internal component identifier which is 
	 * unique within the object, and not a public identifier.  */
	public static final String DS_ID = "dsID";
	
	/* The datastream state */
	public static final String DS_STATE = "dsState";
	
	// START: Object state codes
	/* Represents an active datastream state, available for reading
	 * and writing. */
	public static final String ACTIVE = "A";
	
	/* Represents a state which is locked for writing, but available
	 * for reading.  Objects can only move to the ACTIVE state from the
	 * LOCKED state.
	 */
	public static final String LOCKED = "L";
	
	/* Represents an object which is being replicated to auxillary 
	 * object storage.  It is locked for writing, but available for
	 * reading.  Objects can only move to the REPLICATING state from
	 * the LOCKED state.  Upon successful completion of replication,
	 * the object moves into the ACTIVE state.
	 */
	public static final String REPLICATING = "R";
	
	/* Represents an object in the process of being created.  It isn't 
	 * available for use yet. */
	public static final String INCOMPLETE = "I";
	
	/* Represents an object which is available only to administrators
	 * and is not available for public readership. */
	public static final String WITHDRAWN = "W";
	
	/* Represents an object which has been marked for deletion. */
	public static final String MARKED = "C";
	
	/* Represents an object which has been approved for deletion. */
	public static final String PENDING_DELETION = "D";
	
	/* Represents an object component which references a broken link */
	public static final String BROKEN_LINK = "B";
	
	// END: Object Status Codes
	
	/* The time at which an object is deleted. */
	public static final String DELETE_TIME = "delete_time";
	
	/* The pathname of a file to ingest. */
    public static final String INGEST_FILE = "ingestFile";
    
    /* ObjectXML output */
    public static final String OBJECT_XML = "objectXML";
    
	/* A datastream location */
	public static final String DS_LOCATION = "dsLocation";
	
	/* A human-readable label for the datastream. */
	public static final String DS_LABEL = "dsLabel";
	
	public static final String FORMAT_URI = "formatURI";
	
	public static final String ALT_IDS = "altIDs";
	
	/* An Owner ID indicator */
	public static final String DS_CONTROL_GROUP = "dsControlGroup";
	
	// START: valid DS_CONTROL_GROUP values
	 
	/* Managed content */
	public static final String MANAGED = "M"; 
	
	/* Externally referenced content */
	public static final String EXTERNAL = "E";
	
	/* Redirected content */
	public static final String REDIRECTED = "R";
	
	/* Inline XML */
	public static final String INLINE_XML = "X";
	
	// END: valid DS_CONTROL_GROUP values
	
	/* Indicates a forced change if true */
	public static final String FORCED = "forced";
	
	/* The timestamp of an operation in ISO8601 format*/
	public static final String TIMESTAMP = "timestamp";
	
	/* The starting timestamp in a range */
	public static final String START_DATE = "startDate";
	
	/* The ending timestamp in a range */
	public static final String END_DATE = "endDate";
	
	/* A specific timestamp which is used to specify a version of
	 * something.
	 */
	public static final String AS_OF_DATE = "asOfDateTime";
	
	/* A list of timestamps for an operation */
	public static final String TIMESTAMP_LIST = "timestamp_list";
	
	/* An id generated by fedora */
	public static final String ASSIGNED_ID = "assignedID";
	
	/* A list of datastreams */
	public static final String DATASTREAM_DEFS = "dsDefs";
	
	/* A valid XML format that fedora understands */
	public static final String FORMAT = "format";
	
	// Start:  Valid FORMAT values

	/* the foxml format */
	public static final String FOXML = "info:fedora/fedora-system:FOXML-1.1";
	/* the mets format */
	public static final String METS = "metslikefedora1";
	
	// End:    Valid FORMAT values
	
	public static final String CONTEXT = "context";
	
	// Start: Valid CONTEXT values
	/* Produces an export file appropriate for use outside the repository,
	 * where all datastream content can be obtained by public callback
	 * URLs.
	 */
	public static final String PUBLIC = "public";
	
	/* Produces an export file which is suitable for migrating an object
	 * from one fedora repository to another.
	 */
	public static final String MIGRATE = "migrate";
	/* Produces a standalone entity, where datastream content is inline XML 
	 * and binary base64-encoded inline.  This option is not available in
	 * fedora 2.0.
	 */
	public static final String ARCHIVE = "archive";
	// End: Valid CONTEXT values
	
	/* The disseminator ID */
	public static final String DISS_ID = "dissID";
	
	/* The object of type Disseminator */
	public static final String DISSEMINATOR = "disseminator";
	
	/* An array of Disseminators */
	public static final String DISSEMINATORS = "disseminators";
	
	/* The desired state of one or more disseminators */
	public static final String DISS_STATE = "dissState";
	
	/* The pid of the behavior mechanism object */
	public static final String B_MECH_PID = "bMechPID";
	
	/* The pid of the behavior definition object */
	public static final String B_DEF_PID = "bDefPID";
	
	/* The human-readable disseminator label */
	public static final String DISS_LABEL = "dissLabel";
	
	/* The fedora datastream binding map */
	public static final String BINDING_MAP = "bindingMap";
	
	/* A fedora mime-typed stream */
	public static final String MT_STREAM = "mt_stream";
	
	/* A list of data for a method, in name/value pair format */
	public static final String PROPERTIES = "properties";
	
	public static final String METHOD_NAME = "methodName";
	
	/* Header of a collection object in the fedora repository */
	public static final String COLLECTION_PATTERN = "collection_pattern";
	
	/* List of collection objects from a fedora repository */
	public static final String COLLECTION_OBJECTS = "collection_objets";
	
	
	public FedoraConstants()
	{}

}
