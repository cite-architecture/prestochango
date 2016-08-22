package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

import org.apache.commons.io.FilenameUtils

/** A CollectionArchive consists of an inventory and a data repository.
 */
class CollectionArchive {

  public Integer debug = 0

  /** Root directory of file system containing archival files.  */
  File baseDirectory
  
  /** Hash map of CiteCollection objects keyed by String value
   * of Collection URN.*/
  LinkedHashMap collections

  /** Hash map of CiteDataSource objects keyed by String value
   * of Collection URN.*/
  LinkedHashMap dataSources

  /** HashMap of three Dublin Core metadata values for the archive: 
   * description, title and rights */
  def dcMeta = [:]

  
  /** Hash map with rdf verbs for each supported extension. */
  LinkedHashMap extensionsMap = [:]

  /** Groovy XML namespace for CITE. */
  final groovy.xml.Namespace cite = new groovy.xml.Namespace("http://chs.harvard.edu/xmlns/cite")

  /** Groovy XML namespace for Dublin Core. */
  final groovy.xml.Namespace dc = new groovy.xml.Namespace("http://purl.org/dc/elements/1.1/")

  /** RDF namespace declarations. */
  final String prefix = """
@prefix cite:        <http://www.homermultitext.org/cite/rdf/> .
@prefix citedata:        <http://www.homermultitext.org/citedata/> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. 
@prefix  xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix olo:     <http://purl.org/ontology/olo/core#> .
@prefix dse:  <http://www.homermultitext.org/dse/rdf/> .
@prefix hmt:  <http://www.homermultitext.org/datans/> .
""".toString()

  /** Default character encoding, can be reset dynamically. */
  String charEnc = "UTF-8"



  /** Constructor for CollectionArchive using local file storage.
   * @param inv Collection inventory.
   * @param baseDir Directory where collection data are stored, 
   * one file per collection.
   */
  CollectionArchive(File inv, String schemaFileName, File baseDir) 
  throws Exception {
    try {
      if (!baseDir.canRead()) {
	throw new Exception("CollectionArchive: cannot read directory ${baseDir}")
      }
      this.baseDirectory = baseDir
      if (debug > 0) { System.err.println "constructing CA from inventory ${inv} with baseDir ${baseDir}"}

      try {
	validateInventory(inv, schemaFileName)
      } catch (Exception invException) {
	throw invException
      }

      this.dcMeta = configureMetaData(inv)
      this.collections = configureCollections(inv)
      this.extensionsMap = mapExtensions(inv)

      this.dataSources = configureDataSources(inv)

      if (debug > 0) { System.err.println "Collections = " + this.collections} 

    } catch (Exception e) {
      throw e
    }
  }

  /** Constructor for CollectionArchive using local file storage.
   * @param inv Collection inventory.
   * @param schemaFile Schema for inventory (an RNG file).
   * @param baseDir Directory where collection data are stored, 
   * one file per collection.
   */
  CollectionArchive(File inv, File schemaFile, File baseDir) 
  throws Exception {
    if (!baseDir.canRead()) {
      throw new Exception("CollectionArchive: cannot read directory ${baseDir}")
    }
    this.baseDirectory = baseDir
    if (debug > 0) { System.err.println "constructing CA from inventory ${inv} with baseDir ${baseDir}"}

    try {
      validateInventory(inv,schemaFile)
    } catch (Exception invException) {
      System.err.println ("Could not validate inventory ${inv}")
      throw invException
    }
    this.dcMeta = configureMetaData(inv)
    this.extensionsMap = mapExtensions(inv)
    this.collections = configureCollections(inv)
    this.dataSources = configureDataSources(inv)

    if (debug > 0) { System.err.println "Configuration map = " + this.collections} 
  }


  LinkedHashMap configureDataSources(File inv) {
    groovy.util.Node root 
    try {
      root = new XmlParser().parse(inv)
    } catch (Exception e) {
      throw new Exception("CollectionArchive: unable to parse inventory file ${f}")
    }
    
    def configuredSources = [:]
    root[cite.citeCollection].each { c ->
      if (debug > 0) { System.err.println "Configure data source for " + c.'@urn'}
      configuredSources.putAt("${c.'@urn'}", configureDataSource(c))
    }
    return configuredSources
  }


  // configure a single collection
  CiteDataSource configureDataSource(groovy.util.Node c) {
    CiteDataSource cds
    c[cite.source].each { src ->
      switch (src.'@type') {
      case "file":
      File f = new File(baseDirectory,src.'@value')
      cds = new LocalFileSource(f)
      break
      
      default:
      throw new Exception("CollectionArchive no implementation for data source with type " + src.'@type')
      break
      }
    }
    return cds    
  }
  
  /** Validates the XML serialization of the collection's schema
   * against the published schema for a CITE TextInventory.
   * @param schemaFileName String name of a file with RNG schema for inventory.
   * @throws Exception if the XML does not validate.
   */
  void validateInventory(File inventory, String schemaFileName) 
  throws Exception {
    try {
      File schemaFile = new File(schemaFileName)
      validateInventory(inventory, schemaFile)
    } catch (Exception e) {
      throw e
    }
  }

  
  /** Validates the XML serialization of the collection's schema
   * against the published schema for a CITE TextInventory.
   * @param schemaFile File with RNG schema for inventory.
   * @throws Exception if the XML does not validate.
   */
  void validateInventory(File inventory, File schemaFile) 
  throws Exception {
    System.setProperty("javax.xml.validation.SchemaFactory:"+XMLConstants.RELAXNG_NS_URI,  "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");
    def factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI)
    def schema = factory.newSchema(schemaFile)
    def validator = schema.newValidator()
    try {
      validator.validate(inventory)
    } catch (Exception e) {
      throw e
    }
  }


  LinkedHashMap mapExtensions(File f) {
    groovy.util.Node root 
    try {
      root = new XmlParser().parse(f)
    } catch (Exception e) {
      throw new Exception("CollectionArchive: unable to parse inventory file ${f}")
    }
    def extensionsMap = [:]
    root[cite.extensions][cite.extension].each { extension ->
      if (debug > 0){ System.err.println "Extension; ${extension}" }
      extensionsMap[extension.'@abbr'] = extension.'@uri'
    }
    extensionsMap.each { if (debug > 0){System.err.println it} }
    if (debug > 0) { System.err.println "Extension Map:" }
    if (debug > 0) { System.err.println extensionsMap }
    return extensionsMap
  }



  LinkedHashMap  configureMetaData(inv) {
    def dcmetadata = [:]
    groovy.util.Node root 
    try {
      root = new XmlParser().parse(inv)
    } catch (Exception e) {
      throw new Exception("CollectionArchive: unable to parse inventory file ${inv}")
    }

    root[cite.citeCollection].each { c ->
      dcmetadata.putAt("${c.'@urn'}",configureDcForCollection(c))
    }    
    return dcmetadata
  }

  LinkedHashMap configureDcForCollection(groovy.util.Node c) {
    CiteUrn collUrn
    try {
      collUrn = new CiteUrn(c.'@urn')
    } catch (Exception e) {
      System.err.println("Could not configure collection from URN value "  + c.'@urn')
      throw e
    }
    def propertyHash = [:]
    c[dc.title].each {
      propertyHash["title"] = it.text()
    }

    c[dc.description].each {
      propertyHash["description"] = it.text()
    }

    c[dc.rights].each {
      propertyHash["rights"] = it.text()
    }
    return propertyHash
  }
  
  /** Creates a map of the configuration data 
   * in an XML capabilities file.
   * @param f The XML capabilities file.
   * @returns A map of configuration data.
   * @throws Exception if the file could not be parsed.
   */
  LinkedHashMap configureCollections(File f) {
    groovy.util.Node root 
    try {
      root = new XmlParser().parse(f)
    } catch (Exception e) {
      throw new Exception("CollectionArchive: unable to parse inventory file ${f}")
    }
    
    def configuredCollections = [:]
    root[cite.citeCollection].each { c ->
      if (debug > 0) { System.err.println "Configure collection " + c.'@urn'}
      configuredCollections.putAt("${c.'@urn'}", configureCollection(c))
    }
    return configuredCollections
  }

  CitePropertyType propTypeForXmlAttr(String s) {
    switch (s) {

    case "ctsurn":
    return CitePropertyType.CTS_URN
    break
    case "citeurn":
    return CitePropertyType.CITE_URN
    break
    case "string":
    return CitePropertyType.STRING
    break
    case "number":
    return CitePropertyType.NUM
    break
    case "boolean":
    return CitePropertyType.BOOLEAN
    break
    case "markdown":
    return CitePropertyType.MARKDOWN
    break
    }
    //citeimg ??
  }

  ArrayList collectExtensions(groovy.util.Node c ) {
    ArrayList extensions = []
    c[cite.extendedBy].each { ce ->
      extensions << "${ce.'@extension'}"
    }
    return extensions
  }


  ArrayList collectProperties(groovy.util.Node c ) {
    ArrayList propertyList = []
    c[cite.citeProperty].each { cp ->
      CiteProperty citeProperty
            
      String propName = "${cp.'@name'}"
      String propLabel = "${cp.'@label'}"

      
      String rdfVerb = ""
      String inverseVerb = ""
      cp[cite.indexRelation].each { idx ->
	rdfVerb = idx.'@rdfverb'
	if (idx.'@inverseverb') {
	  inverseVerb = idx.'@inverseverb'
	}
      }
      RdfVerb rdf = null
      if ((rdfVerb != "") && (inverseVerb != "")) {
	rdf = new RdfVerb(rdfVerb,inverseVerb)
      }
      
      def valList = []
      cp[cite.valueList][cite.value].each {
	valList.add("${it.text()}")
      }
      def propType = propTypeForXmlAttr("${cp.'@type'}")

      if (valList.size() > 0) {
	citeProperty = new CiteProperty(propName,propLabel,valList as Set)
      } else {
	citeProperty = new CiteProperty(propName,propType,propLabel)
      }
      if (rdf != null) {
	citeProperty.setRdfPair(rdf)
      }

      if ("${cp.'@universalValue'}") {
	citeProperty.setSingleValue("${cp.'@universalValue'}")
      }
	
      propertyList.add(citeProperty)
    }
    return propertyList
  }
  
 

  /** Finds a CiteProperty by name from a list of CiteProperty objects.
   * @param propList List of CiteProperty objects to searech.
   * @param Name of property to find.
   * @returns A CiteProperty object.
   */
  CiteProperty findPropertyByName(ArrayList propList, String propName) {
    return propList.find {it.propertyName == propName}
  }


  /** Creates a CiteCollection from a parsed XML inventory document.
   * @param c Parsed Node for a single collection
   * @returns A CiteCollection object.
   */
  CiteCollection configureCollection(groovy.util.Node c) {
    CiteUrn collUrn
    try {
      collUrn = new CiteUrn(c.'@urn')
    } catch (Exception e) {
      System.err.println("Could not configure collection from URN value "  + c.'@urn')
      throw e
    }

    String descr = "Collection ${collUrn}"
    c[dc.description].each {
      // take last one at random:
      descr = it.text()
    }

    // can you have more than 1 ns mapping?
    String nsAbbr = "${c[cite.namespaceMapping][0].'@abbr'}"
    String nsFull ="${c[cite.namespaceMapping][0].'@uri'}"

    ArrayList extensions = collectExtensions(c)
    ArrayList collProps = collectProperties(c)

    String orderingPropName = ""
    if (c.orderedBy) {
      orderingPropName = "${c.orderedBy[0].'@property'}"
    }

    CiteProperty idProp = findPropertyByName(collProps, c.'@canonicalId')
    if (idProp == null) {
      throw new Exception("NO PROPERTY FOR  " + c.'@canonicalId')
    } else {
      if (debug > 0) {System.err.println "Using canonical property " + idProp }
    }
    CiteProperty labelProp = findPropertyByName(collProps, c.'@label')
    if (labelProp == null) {
      throw new Exception("NO PROPERTY FOR  #" + c.'@canonicalId' + "# in " + collUrn + " w properties " + collProps)
    } else {
      if (debug > 0) { System.err.println "Using labelling property " + labelProp }
    }

    CiteProperty orderedByProp = findPropertyByName(collProps, orderingPropName)
    
    return new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)
  }

  // get a collection identified by URN
  CiteCollection getCollection(CiteUrn urn) {
    return collections[urn.toString()]
  }

  // get a list of all collections in archive
  ArrayList getCollections() {
    return collections.values()
  }

  
  /** Finds canonical ID property for collection identifed by a URN.
   * @param urn The Collection in question.
   * @returns Property for canonical identification
   */
  CiteProperty getCanonicalIdProperty(CiteUrn urn) 
  throws Exception {
    def config =  this.collections[urn.toString()]
    return config.canonicalIdProp
  }

  /** Finds name of property with labelling
   * information usable in rdf:label description.
   * @param urn The Collection in question.
   * @returns Name of the property.
   * @throws Exception if urn is not a configured collection.
   */
  CiteProperty getLabelProperty(CiteUrn urn) 
  throws Exception {
    def config =  this.collections[urn.toString()]
    return config.labelProp
  }

  // get list of properteis of collection IDed by URN
  ArrayList getProperties(CiteUrn urn) {
    return this.collections[urn.toString()].collProperties
  }
  
  // get description of collection IDed by URN
  String getDescription(CiteUrn urn) {
    return this.collections[urn.toString()].description
  }

  // get namespace abbr of collection IDed by URN
  String getNsAbbr(CiteUrn urn) {
    return this.collections[urn.toString()].nsAbbr
  }

  // get full namespace id of collection IDed by URN
  String getNsFull(CiteUrn urn) {
    return this.collections[urn.toString()].nsFull
  }
  
  
  // ORDERING
  CiteProperty getOrderedByProperty(CiteUrn urn) {
    if (debug > 5) {
      System.err.println "Get ordered prop for " + urn
    }
    def config =  this.collections[urn.toString()]
    return config.orderedByProp
  }

  boolean isOrdered(CiteUrn urn) {
    def config =  this.collections[urn.toString()]
    if (config == null) {
      throw new Exception("CollectionArchive: no collection " + urn)
    } else {
      return config.isOrdered()
    }
  }

  // controlled vocabulary
  ArrayList getVocabulary(CiteUrn urn, String propertyName){
    return this.collections[urn.toString()].getVocabList(propertyName)
  }

  /** Finds the value of the rdfVerb for a given property.
   * @param urn The Collection in question.
   * @param propertyName Name of the property.
   * @returns The rdf verb, or null if none configured.
   * @throws Exception if urn is not a configured collection or
   * if propertyName does not exist in that collection.
   */
  RdfVerb getRdfVerb(CiteUrn urn, String propertyName) 
  throws Exception {
    return this.collections[urn.toString()].getRdf(propertyName)
  }


  Object getSingleValue(CiteUrn urn, String propertyName){
    return this.collections[urn.toString()].getSingleValue(propertyName)
  }










  
  //////// COLLCECTIONS: VERSION 2.0 TESTS
  // UNIVERSAL VALUES


  





  

  



  /// VERSION 2.1 TESTS:
  // DC METADATA


  
	String getUriForExtension(String extensAbbr) 
		throws Exception {
		try {
			return this.extensionsMap[extensAbbr]

		} catch (Exception e) {
			throw new Exception("CollectionArchive:  no extension ${extensAbbr} configured.")
		}
	}


	/** Finds list of extensions configured
	 * for a Collection.
	 * @param urn The Collection in question.
	 * @returns List of strings with the abbreviated
	 * name for each extensions.
	 * @throws Exception if urn is not a configured collection.
	 */
	ArrayList getExtensionList(CiteUrn urn) 
	throws Exception {
	try {
		def config =  this.collections[urn.toString()]
		return config['citeExtensions']
	} catch (Exception e) {
		throw new Exception("CollectionArchive:getExtensionList: no collection ${urn} configured.")
	}
	}







	/** Finds a single pairing of source type and source name
	 * for a Collection.  Currently, only 'file' source type is
	 * implemented, and source name should be a local file name.
	 * @param urn The Collection in question.
	 * @returns An ArrayList containing the two items.
	 */

  ///
	ArrayList getSourcePair(CiteUrn urn) {
		def config = this.collections[urn.toString()]
		def pair = [config['sourceType'], config['source']]
		return pair
	}

  //
	String getDCMetadata() {
	}












	/**  Writes an RDF description, in TTL format, of the data about
	 * a collection expressed by the Collection Inventory.
	 * @returns A String composed of TTL statements.
	 * @throws Exception if inventory is misconfigured.
	 */
	String turtlizeInventory() 
	throws Exception {
	StringBuffer ttl = new StringBuffer()
	/*
	def invroot = new XmlParser().parse(this.inventory)

	invroot[cite.extensions][cite.extension].each { extension ->

		String extensionUri = extension.'@uri'
		String extensionAbbr = extension.'@abbr'

		if (debug > 0){System.err.println "CollectionArchive: examine exension ${extensionUri}" }
		if (debug > 0){System.err.println "It has type ${extensionAbbr}" }

		ttl.append("<${extensionUri}> rdf:type cite:CiteExtension .\n")
		ttl.append("<${extensionUri}> cite:abbreviatedBy ${extensionAbbr} .\n")
	}

	invroot[cite.citeCollection].each { cc ->
		if (!cc.'@urn') {
			if (debug > 0){
				System.err.println "CollectionArchive:  cannot turtlieze collection with no URN!"
				System.err.println "Parsed record was " + cc
			}
			throw new Exception("No urn defined for collection.")
		}
		CiteUrn urn = new CiteUrn(cc.'@urn')
		String labelProperty = getLabelProperty(urn)
		def nsMap = cc[cite.namespaceMapping][0]

		ttl.append("<${nsMap.'@uri'}> rdf:type cite:DataNs .\n")
		ttl.append("<" + nsMap.'@uri' + "> cite:abbreviatedBy " + '"' + nsMap.'@abbr' +  '" .\n\n')

		ttl.append("<urn:cite:${nsMap.'@abbr'}:${urn.getCollection()}> rdf:type cite:CiteCollection . \n")
		def rdfLabel = getTitle(urn).replaceAll(/\n/,'')

		ttl.append("<urn:cite:${nsMap.'@abbr'}:${urn.getCollection()}> rdf:label " + '"' + rdfLabel  + '" . \n')

		ttl.append("<urn:cite:${nsMap.'@abbr'}:${urn.getCollection()}> cite:canonicalId citedata:${urn.getCollection()}_${cc.'@canonicalId'} . \n")

		if (cc[cite.extendedBy]){
			String tempExtensionName = ""
			cc[cite.extendedBy].each { ext ->
				tempExtensionName = ext.'@extension'	
				ttl.append("<urn:cite:${nsMap.'@abbr'}:${urn.getCollection()}> cite:extendedBy ${tempExtensionName} . \n")
				ttl.append("${tempExtensionName} cite:extends <urn:cite:${nsMap.'@abbr'}:${urn.getCollection()}> . \n")
			}
		}

		if (cc[cite.orderedBy]) {
			ttl.append("<urn:cite:${nsMap.'@abbr'}:${urn.getCollection()}>  cite:orderedBy citedata:${urn.getCollection()}_${cc[cite.orderedBy][0].'@property'} .\n")
		}
		ttl.append("\n")


		// document configured properties:
		cc[cite.citeProperty].each { prop ->
			String propUri = "citedata:${urn.getCollection()}_${prop.'@name'}"
			ttl.append( "<urn:cite:${nsMap.'@abbr'}:${urn.getCollection()}>  cite:collProperty  ${propUri} .\n")
			ttl.append( "${propUri} rdf:type rdf:Property .\n")
			ttl.append ("${propUri} cite:propLabel " + '"' + prop.'@label' +  '".\n')

			switch (prop.'@type') {
				case ("citeurn"):
				ttl.append ("${propUri} cite:propType cite:CiteUrn .\n")
				break
				case ("ctsurn"):
				ttl.append ("${propUri} cite:propType cite:CtsUrn .\n")
				break

				default:
				ttl.append ("${propUri} cite:propType " + '"' + prop.'@type' +  '".\n')
				break
			}
			ttl.append("\n")
		}

	}*/
	return ttl.toString()
	}


  /**  Writes an RDF description, in TTL format, of a single object.
   * @param cols An array of data values.
   * @param headingIndex An array of property names, in the same order
   * as the data values in the cols array.
   * @param canonical Name of the canonical ID property in this collection.
   * @param label Name of the rdf:label property in this collection.
   * @throws Exception if collection not configured
   */
  String turtlizeOneRow(ArrayList cols, ArrayList headingIndex, String canonical, String label, boolean ordered) 
  throws Exception {
    if (debug > 2) {
      System.err.println "TURLTELIZE ROW " + cols
      System.err.println "Use headings " + headingIndex
    }

    StringBuffer oneRow  = new StringBuffer()
    CiteUrn urn
    CiteUrn collUrn

    // Generate statements about canonical ID of object:
    cols.eachWithIndex { column, idx ->
      if (debug > 3) { System.err.println "${idx}. Looking at ${headingIndex[idx]} vs ${canonical}" }
      if (headingIndex[idx] == canonical) {
	
	if (debug > 3) { 
	  System.err.println "Found canonical ${canonical}:" 
	  System.err.println "at ${column}"
	}

	try {
	  if (debug > 3) { System.err.println "Try to make URN from ${column}" }

	  urn = new CiteUrn(column)
	  if (debug > 3) { System.err.println "OK!" }
	  
	  collUrn = new CiteUrn("urn:cite:${urn.getNs()}:${urn.getCollection()}")
	  oneRow.append("<${column}> cite:belongsTo <${collUrn}> .\n")
	  oneRow.append("<${collUrn}> cite:possesses <${column}> .\n")

	  if (urn.hasVersion()){
	    oneRow.append("<${urn}> cite:isVersionOf <${urn.reduceToObject()}> .\n")
	    oneRow.append("<${urn.reduceToObject()}> cite:hasVersion <${urn}> .\n")
	    oneRow.append("<${urn.reduceToObject()}> cite:belongsTo <${collUrn}> .\n")
	    oneRow.append("<${collUrn}> cite:possesses <${urn.reduceToObject()}> .\n")
	  }


	  if (!ordered) {
	    oneRow.append("<${column}> cite:ordered " + '"false" .\n')
	  }

	  if (debug > 3) { System.err.println "appended to oneRow, now ${oneRow}" }
	} catch (Exception e) {
	  System.err.println "turtlizeOneRow: unable to make urn from ${column}"
			}
      }
    }

    def collConf = this.collections["${collUrn}"]

    if (collConf == null) {
      throw new Exception("CollectionArchive:turtlizeOneRow: no configuration for collection ${collUrn}")
    }

    /*
      if (debug > 0) {
      System.err.println "Config for ${collUrn} is " + collConf
      }
    */

    collConf["properties"].each { confProp ->
      if ((confProp["universalValue"] != null) && (confProp["universalValue"] != "null")){
	def c = confProp["universalValue"]
	switch (confProp["type"]) {

	case "boolean":
	if ( (c == true) || (c == "true")){
	  oneRow.append("<${urn}> citedata:${urn.getCollection()}_${confProp['name']} true .\n")
	} else {
	  oneRow.append("<${urn}> citedata:${urn.getCollection()}_${confProp['name']} false .\n")
	}
	break

	case "number":
	oneRow.append("<${urn}> citedata:${urn.getCollection()}_${confProp['name']} ${c} .\n")
	
	if (getRdfVerb(collUrn, confProp["name"])) {
	  System.err.println "doing getRdfVerb"
	  System.err.println "<${urn}> ${getRdfVerb(collUrn, confProp['name'])} ${c} .\n"
	  oneRow.append( "<${urn}> ${getRdfVerb(collUrn, confProp['name'])} ${c} .\n")
	}
	if (getInverseVerb(collUrn, confProp["name"])) {
	  oneRow.append( "${c} ${getInverseVerb(collUrn, confProp['name'])} <${urn}> .\n")
	}
	break

	case "markdown":              
	case "geojson":
	case "string":
	oneRow.append("<${urn}> citedata:${urn.getCollection()}_${confProp['name']} " + '"' + c + '" .\n')
	if (getRdfVerb(collUrn, confProp["name"])) {
	  oneRow.append( "<${urn}> ${getRdfVerb(collUrn, confProp['name'])} " + '"' + c + '" .\n')
	}
	if (getInverseVerb(collUrn, confProp["name"])) {
	  oneRow.append( "'" + c + "' ${getInverseVerb(collUrn, confProp['name'])} <${urn}> .\n")
	}

	break

	case "citeurn":
	case "citeimg":
	case "ctsurn":
	oneRow.append("<${urn}> citedata:${urn.getCollection()}_${confProp['name']} <${c}> .\n")
	
	if (getRdfVerb(collUrn, confProp["name"])) {
	  oneRow.append( "<${urn}> ${getRdfVerb(collUrn, confProp['name'])} <${c}> .\n")
	}
	if (getInverseVerb(collUrn, confProp["name"])) {
	  oneRow.append( "<${c}> ${getInverseVerb(collUrn, confProp['name'])} <${urn}> .\n")
	}
	break


	default : 
	System.err.println "UNRECOGNIZED TYPE:" + confProp["type"]
	break
	}
      }
    }

    cols.eachWithIndex { c, i ->
      if (headingIndex[i] == label) {
	oneRow.append("<${urn}> rdf:label " + '"' + c + '" .\n')
      } 
      if ((c != null) && (c != "") ){
	if (debug > 4) { System.err.println "column value of ${headingIndex[i]} is #" + c + "#" }
	// NS change:  also output property for rdf:label (so it appears *twice*)
	if (headingIndex[i] != canonical) {
	  collConf["properties"].each { confProp ->
	    if (confProp["name"] == headingIndex[i]) {
	      switch (confProp["type"]) {
		
	      case "boolean":
	      if ( (c == true) || (c == "true")){
		oneRow.append("<${urn}> citedata:${urn.getCollection()}_${headingIndex[i]} true .\n")
	      } else {
		oneRow.append("<${urn}> citedata:${urn.getCollection()}_${headingIndex[i]} false .\n")
	      }
	      break
	      
	      case "number":
	      oneRow.append("<${urn}> citedata:${urn.getCollection()}_${headingIndex[i]} ${c} .\n")
	      
	      if (getRdfVerb(collUrn, headingIndex[i])) {
		System.err.println "doing getRdfVerb"
		System.err.println "<${urn}> ${getRdfVerb(collUrn, headingIndex[i])} ${c} .\n"
		oneRow.append( "<${urn}> ${getRdfVerb(collUrn, headingIndex[i])} ${c} .\n")
	      }
	      if (getInverseVerb(collUrn, headingIndex[i])) {
		oneRow.append( "${c} ${getInverseVerb(collUrn, headingIndex[i])} <${urn}> .\n")
	      }

	      break

	      case "markdown":              
	      case "geojson":
	      case "string":
	      oneRow.append("<${urn}> citedata:${urn.getCollection()}_${headingIndex[i]} " + '"' + c + '" .\n')
	      if (getRdfVerb(collUrn, headingIndex[i])) {
		oneRow.append( "<${urn}> ${getRdfVerb(collUrn, headingIndex[i])} " + '"' + c + '" .\n')
	      }
	      if (getInverseVerb(collUrn, headingIndex[i])) {
		oneRow.append( "'" + c + "' ${getInverseVerb(collUrn, headingIndex[i])} <${urn}> .\n")
	      }

	      break

	      case "citeurn":
	      case "citeimg":
	      case "ctsurn":
	      oneRow.append("<${urn}> citedata:${urn.getCollection()}_${headingIndex[i]} <${c}> .\n")

	      if (getRdfVerb(collUrn, headingIndex[i])) {
		oneRow.append( "<${urn}> ${getRdfVerb(collUrn, headingIndex[i])} <${c}> .\n")
	      }
	      if (getInverseVerb(collUrn, headingIndex[i])) {
		oneRow.append( "<${c}> ${getInverseVerb(collUrn, headingIndex[i])} <${urn}> .\n")
	      }
	      
	      break



	      default : 
	      System.err.println "UNRECOGNIZED TYPE:" + confProp["type"]
	      break
	      
	      }
	    } 
	  }
	}
	
      } 
    }
    if (debug > 3) { System.err.println "Turtleized row: " + oneRow }
    return oneRow.toString()
  }




	/** Formats sequencing statments in the OLO ontologry for an ordered collection
	 * represented by a tsv file.
	 * @param f File with data in tsv format.  First row should
	 * be property names, subsequent rows should contain data.
	 * @param collUrn Cite URN of the collection.
	 * @returns String of RDF statements in the OLO ontology.
	 */
	String addOloCsvData(File f, CiteUrn collUrn) {
	  /*
		String canonical = getCanonicalIdProperty(collUrn)
		String label = getLabelProperty(collUrn)
		String orderProp = getOrderedByProperty(collUrn)

		def headingIndex = [:]
		def seqs = [:]

		// First, cycle though file to construct a map of sequence numbers to URNs: 
		int count = 0
		CSVReader reader = new CSVReader(new FileReader(f))        
		reader.readAll().each { cols ->

			if (count == 0) {
				headingIndex = cols
			} else if (cols.size() > 1) {
				headingIndex.toList().eachWithIndex { h, i ->
					if (h == orderProp) {
						def keyCount = cols[i]
						String urnVal
						headingIndex.toList().eachWithIndex { h2, i2 ->
							if (h2 == canonical) {
								urnVal = cols[i2]
							}
						}
						seqs[keyCount] = urnVal

					}
				}
			} else {
				// empty or not parseable
			}
			count++;
		}
		if (debug > 0) {System.err.println "Generated ${seqs.keySet().size()} sequence mappings"}
		//then, use the sequences index to generate prev-next 
		//statements for each object in the list: 
		StringBuffer replyBuff = new StringBuffer()
		def lnCount = 0


		CSVReader reader2 = new CSVReader(new FileReader(f))        
		reader2.readAll().each { cols ->
			if (lnCount == 0) {
			} else if (cols.size() > 1) {
				headingIndex.toList().eachWithIndex { h, i ->
					if (h == orderProp) {
						def keyStr = cols[i]
						String subj = seqs[keyStr]
						String oloitem = "<${subj}> olo:item ${keyStr} .\n"
						replyBuff.append(oloitem)

						def keyCount = keyStr.toInteger()
						def smaller = keyCount - 1
						def larger = keyCount + 1

						if (seqs[smaller.toString()] != null) {
							String obj = seqs[smaller.toString()]
							String prev = "<${subj}> olo:previous <${obj}> .\n"
							replyBuff.append(prev)
						}


						if (seqs[larger.toString()] != null) {
							String obj = seqs[larger.toString()]
							String nxt = "<${subj}> olo:next <${obj}> .\n"
							replyBuff.append(nxt)
						}
					}
				}
			}
			lnCount++
		} */
		return replyBuff.toString()
	}



	// CATCH NULL SEQUENCE WHILE ALLOWING EMPTY LINES!
	/** Formats data rows from a csv file as TTL.
	 * First constructs an index of the property names for this object,
	 * then cycles through all data rows, and constructs and an array of values
	 * that are then passed off to the generic turtleizeOneRow method.
	 *
	 * @param collUrn URN of the collection
	 * @returns String expressing the contents of the row in TTL.
	 * @throws Exception if f cannot be fully parsed.
	 */
	String ttlBasicCsvData(File f, CiteUrn collUrn, boolean ordered) 
	throws Exception {

	String canonical = getCanonicalIdProperty(collUrn)
	String label = getLabelProperty(collUrn)
	String orderProp = getOrderedByProperty(collUrn)

	StringBuffer reply = new StringBuffer()

	def headingIndex = []
	def lineCount = 0
	/*
	Reader wrapper = new InputStreamReader(new FileInputStream(f), "utf-8");
	CSVReader reader = new CSVReader(wrapper)
	reader.readAll().each { cols ->
		if (debug >= 4) {System.err.println "READ COLS " + cols + " of size " + cols.size()}
		reply.append("\n")
		if (lineCount == 0) {
			headingIndex = cols.toList()

		} else if (cols.size() > 1) {
			String rowTtl
			try {
				rowTtl = turtlizeOneRow(cols.toList(), headingIndex.toList(), canonical, label, ordered)
			} catch (Exception e) {
				System.err.println "CollectionArchive: turtlizeOneRow failed for ${cols}"
				System.err.println "BECAUSE OF " + e
				throw e
			}

			reply.append(rowTtl)
		} else {
			// empty line, ignore
		}
		lineCount++
		}*/
	return reply.toString()
	}





  /**  Writes an RDF description, in TTL format, of the data 
   * contained in a single file in comma-separated value format.
   * @param f The file of data.
   * @param urnVal The URN, as a String, identifying the collection.
   * @returns A String composed of TTL statements.
   * @throws Exception if data in f cannote be parsed.
   */
  String turtlizeCsv(File f, String urnVal) 
  throws Exception {
    // change this to get Collection URN from any level CITE URN.
    CiteUrn collUrn

    try {
      collUrn = new CiteUrn(urnVal)
    } catch (Exception e) {
      System.err.println "Unable to parse urn " + urnVal
      throw e
    }
    boolean ordered = isOrdered(collUrn)

    StringBuffer rowBuffer = new StringBuffer()

    String basicCsv 

    try {
      basicCsv = ttlBasicCsvData(f,collUrn, ordered)
    } catch (Exception e) {
      System.err.println("CollectionArchive:turtlizeCsv: unable to turtlize data in ${f}")
      throw e
    }
    
    if (debug > 0 ) {System.err.println "Got basic csv len " + basicCsv.size()}
    rowBuffer.append (basicCsv )

    if (debug > 0) { System.err.println "Got basic CSV data OK (string size ${basicCsv.size()})"}

    if (ordered) {
      if (debug > 0) { System.err.println "${collUrn} is ordered, so get olo data" }
      String oloData = addOloCsvData(f, collUrn)
      rowBuffer.append(oloData)
    } 

    return rowBuffer.toString()
  }




	/** Formats data rows from a tsv file as TTL.
	 * First constructs an index of the property names for this object,
	 * then cycles through all data rows, and constructs and an array of values
	 * that are then passed off to the generic turtleizeOneRow method.
	 *
	 * @param f File with data in tsv format.  First row should
	 * be property names, subsequent rows should contain data.
	 * @param collUrn Cite URN of the collection.
	 * @returns String expressing the contents of the row in TTL.
	 */
	String ttlBasicTsvData(File f, CiteUrn collUrn, boolean ordered) {
		String canonical = getCanonicalIdProperty(collUrn)
		String label = getLabelProperty(collUrn)
		String orderProp = getOrderedByProperty(collUrn)

		StringBuffer replyBuff = new StringBuffer()
		def headingIndex = []
		def lineCount = 0
		f.eachLine { l ->
			replyBuff.append("\n")
			def cols = l.split(/\t/)

			if (lineCount == 0) {
				headingIndex = cols
			} else if (cols.size() > 1)  {
				String rowTtl  = ""
				try {
					rowTtl = turtlizeOneRow(cols.toList(), headingIndex.toList(), canonical, label, ordered)
				} catch (Exception e) {
					System.err.println "FAILED TO PROCESS ${cols.size()} columns, " + cols
					System.err.println "BECAUSE OF " + e
				}
				replyBuff.append(rowTtl)
			} else {
				// empty line: ignore
			}
			lineCount++;
		}
		return replyBuff.toString()
	}


	/** Formats sequencing statments in the OLO ontologry for an ordered collection
	 * represented by a tsv file.
	 * @param f File with data in tsv format.  First row should
	 * be property names, subsequent rows should contain data.
	 * @param collUrn Cite URN of the collection.
	 * @returns String of RDF statements in the OLO ontology.
	 */
	String addOloTsvData(File f, CiteUrn collUrn) {
		String canonical = getCanonicalIdProperty(collUrn)
		String label = getLabelProperty(collUrn)
		String orderProp = getOrderedByProperty(collUrn)

		def hdrIndex = []
		def seqs = [:]
		/* First, cycle though file to construct a map of sequence numbers to URNs: */
		int count = 0
		f.eachLine { ln ->
			def cols = ln.split(/\t/)
			if (count == 0) {
				hdrIndex = cols
			} else if (cols.size() > 1) {
				hdrIndex.toList().eachWithIndex { h, i ->
					if (h == orderProp) {
						def keyCount = cols[i]
						String urnVal
						hdrIndex.toList().eachWithIndex { h2, i2 ->
							if (h2 == canonical) {
								urnVal = cols[i2]
							}
						}
						seqs[keyCount] = urnVal

					}
				}
			}
			count++;
		}
		if (debug > 0) {System.err.println "Generated ${seqs.keySet().size()} sequence mappings"}
		/* then, use the sequences index to generate prev-next 
		statements for each object in the list: */
		StringBuffer replyBuff = new StringBuffer()
		def lnCount = 0
		f.eachLine { l ->
			def cols = l.split(/\t/)
			if (lnCount == 0) {
			} else if (cols.size() > 1) {
				hdrIndex.toList().eachWithIndex { h, i ->
					if (h == orderProp) {
						def keyStr = cols[i]
						String subj = seqs[keyStr]
						String oloitem = "<${subj}> olo:item ${keyStr} .\n"
						replyBuff.append(oloitem)

						def keyCount = keyStr.toInteger()
						def smaller = keyCount - 1
						def larger = keyCount + 1

						if (seqs[smaller.toString()] != null) {
							String obj = seqs[smaller.toString()]
							String prev = "<${subj}> olo:previous <${obj}> .\n"
							replyBuff.append(prev)
						}


						if (seqs[larger.toString()] != null) {
							String obj = seqs[larger.toString()]
							String nxt = "<${subj}> olo:next <${obj}> .\n"
							replyBuff.append(nxt)
						}
					}
				}
			}
			lnCount++
		}

		return replyBuff.toString()
	}

	/**  Writes an RDF description, in TTL format, of the data 
	 * contained in a single file in tab-separated value format.
	 * @param f The file of data.
	 * @param urnVal The URN, as a String, identifying the collection.
	 * @returns A String composed of TTL statements, or a null String
	 * if unable to parse urnVal as a CITE URN.
	 */
	String turtlizeTsv(File f, String urnVal) 
	throws Exception {
	StringBuffer replyBuff = new StringBuffer()

	CiteUrn collUrn 
	try {
		collUrn = new CiteUrn(urnVal)
	} catch (Exception e) {
		System.err.println "Unable to get URN for value ${urnVal}"
		return ""
	}

	// TAKE ACCOUNT OF THIS TO AVOID DUPLICATION OF COLLECTION-LEVEL DATA...
	//def collectionsSeen = []
	if (debug > 0)  { System.err.println "Make basic data for tsv of ${collUrn}"}
	boolean ordered = isOrdered(collUrn)
	String basicData = ttlBasicTsvData(f, collUrn, ordered)

	replyBuff.append(basicData)
	if (ordered) {
		if (debug > 0) { System.err.println "${collUrn} is ordered, so add ordered data."}
		String oloData = addOloTsvData(f, collUrn)
		replyBuff.append(oloData)

	}
	return replyBuff.toString()
	}


	/** Writes a complete RDF description of a Collection Archive
	 * in TTL format, without RDF prefix definitions, to a local file.
	 * This can be useful when a system like dse that already has prefix
	 * statements wants to generate a larger composite RDF description
	 * concatenating RDF from multiple sources.
	 * @param outFile A local file where output is written.
	 * @throws Exception if unable to parse data in all files of 
	 * the Collection Archive.
	 */
	void ttl(File outFile) 
	throws Exception{
	if (debug > 2) {
		System.err.println "Trying to turtilize entire archive ..."
	}
	try {
		ttl(outFile, false)
	} catch (Exception e) {
		throw e
	}
	if (debug > 2) {
		System.err.println "Ran ttl without exceptions"
	}
	}

	/** Writes a complete RDF description of a Collection Archive
	 * in TTL format to a local file.
	 * @param ttl A local file where output is written.
	 * @param includePrefix Whether or not to include RDF prefix
	 * statements in the output.
	 * @throws Exception if unable to parse data in all files of 
	 * the Collection Archive.
	 */
	void ttl(File ttl, boolean includePrefix) 
	throws Exception {

	if (includePrefix) {
	  ttl.append(prefix, charEnc)
	}
	ttl.append(turtlizeInventory(), charEnc)


	// Cycle each configured collection:
	this.collections.keySet().each { u ->
	  CiteUrn urn = new CiteUrn(u)
	  if (debug > 3) {System.err.println "TURTLIZE " + u}
	  def src = getSourcePair(urn)
	  if (debug > 3) { System.err.println "examine " + src}

	  switch (src[0]) {
	  case "file":
	  try {
	    File f = new File("${this.baseDirectory}/${src[1]}")
	    if (src[1] ==~ /.+csv/) {
	      if (debug > 0) { System.err.println "Turtlize ${f} as csv"}
	      String ttlData 
	      try {
		ttlData = turtlizeCsv(f, u)
	      } catch (Exception e) {
		System.err.println "CollectionArchive:ttl with prefix: could not turtlize data in ${f}"
		System.err.println e
		throw new Exception("CollectionArchive:ttl: exception ${e}")
	      }
	      
	      if (debug > 2) {System.err.println "Turtlized ${f} successfully: appending data"}
					ttl.append(ttlData, charEnc)
				} else if (src[1] ==~ /.+tsv/) {
					if (debug > 0) { System.err.println "Turtlize ${f} as tsv"}
					ttl.append(turtlizeTsv(f, u), charEnc)
				} else {
					System.err.println "COULD NOT FIND PATTERN FOR " + src[1]
				}
			} catch (Exception e) {
				throw e
			}
			break

			default:
			System.err.println "CollectionArchive: failed to process ${src[0]}."
			System.err.println "Only local file sources currently implemented."
			break
		}
	}
	}
}
