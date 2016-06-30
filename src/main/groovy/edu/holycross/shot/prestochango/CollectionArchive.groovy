package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import au.com.bytecode.opencsv.CSVReader

import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

import org.apache.commons.io.FilenameUtils

/** A CollectionArchive consists of an inventory and a data repository.
*/
class CollectionArchive {

  Integer SCREAM = 3
  Integer DEBUGMSG = 2
  Integer WARN = 1
  public Integer debug = 1

  /** CITE Collection inventory serialized in XML to a File. */
  File inventory

  /** Root directory of file system containing archival files.  */
  File baseDirectory

  /** Hash map with key information from the inventory file for
   * easy access without having to navigate complex XML. */
  LinkedHashMap citeConfig

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
	  this.inventory = inv
	  if (debug > 0) { System.err.println "constructing CA from inventory ${inv} with baseDir ${baseDir}"}

	  try {
		  validateInventory(schemaFileName)
	  } catch (Exception invException) {
		  throw invException
	  }
	  this.citeConfig = configureFromFile()

	  if (debug > 0) { System.err.println "Configuration map = " + this.citeConfig} 

  } catch (Exception e) {
	  throw e
  }
  }


  CollectionArchive(File inv, File schemaFile, File baseDir) 
  throws Exception {
    if (!baseDir.canRead()) {
      throw new Exception("CollectionArchive: cannot read directory ${baseDir}")
    }
    this.baseDirectory = baseDir
    this.inventory = inv
    if (debug > 0) { System.err.println "constructing CA from inventory ${inv} with baseDir ${baseDir}"}
        
    try {
      validateInventory(schemaFile)
    } catch (Exception invException) {
      throw invException
    }
    this.citeConfig = configureFromFile()
    
    if (debug > 0) { System.err.println "Configuration map = " + this.citeConfig} 
  }


  /** Constructor for CollectionArchive using Google Tables for data storage.
   *  @param inv Inventory file.
   */
  CollectionArchive(File inv, String schemaFileName) 
  throws Exception {
    this.inventory = inv
    try {
      validateInventory(schemaFileName)
    } catch (Exception invException) {
      throw invException
    }
    this.citeConfig = configureFromFile()
  }

  /** Validates the XML serialization of the collection's schema
   * against the published schema for a CITE TextInventory.
   * @throws Exception if the XML does not validate.
   */
  void validateInventory(String schemaFileName) 
  throws Exception {
    try {
      File schemaFile = new File(schemaFileName)
      validateInventory(schemaFile)
    } catch (Exception e) {
      throw e
    }
  }

  void validateInventory(File schemaFile) 
  throws Exception {
    System.setProperty("javax.xml.validation.SchemaFactory:"+XMLConstants.RELAXNG_NS_URI,
		       "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");
    def factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI)
    def schema = factory.newSchema(schemaFile)
    def validator = schema.newValidator()
    try {
      validator.validate(this.inventory)
    } catch (Exception e) {
      throw e
    }
  }


  /**  Creates a map of the configuration data 
   * in this collection's XML capabilities file.
   * @returns A map of configuration data or null
   * if the inventory file could not be parsed.
   */
  LinkedHashMap configureFromFile() {
    return configureFromFile(this.inventory)
  }
    
  /** Creates a map of the configuration data 
   * in an XML capabilities file.
   * @param f The XML capabilities file.
   * @returns A map of configuration data.
   * @throws Exception if the file could not be parsed.
   */
  LinkedHashMap configureFromFile(File f) {
	  def root 
	  try {
		  root = new XmlParser().parse(f)
	  } catch (Exception e) {
		  throw new Exception("CollectionArchive: unable to parse inventory file ${f}")
	  }



	  root[cite.extensions][cite.extension].each { extension ->
		  System.err.println "Extension; ${extension}"
		  this.extensionsMap[extension.'@rdfType'] = extension.'@rdfUri'
	  }
	  this.extensionsMap.each { System.err.println it }
	  if (debug > 0) { System.err.println "Extension Map:" }
	  if (debug > 0) { System.err.println this.extensionsMap }

	  def configuredCollections = [:]
	  root[cite.citeCollection].each { c ->
		  String title = c.'@urn'
		  c[dc.description].each {
			  title = it.text()
		  }


		  String sourceType = ""
		  String source = ""
		  c[cite.source].each { src ->
			  sourceType = "${src.'@type'}"
			  source = "${src.'@value'}"
		  }


		  def propertyList = []
		  c[cite.citeProperty].each { cp ->
			  def prop = [:]
			  prop['name'] = "${cp.'@name'}"
			  prop['label'] = "${cp.'@label'}"
			  prop['type'] = "${cp.'@type'}"
			  prop['universalValue'] = "${cp.'@universalValue'}"
			  prop['rdfverb'] = ""
			  prop['inverseverb'] = ""


			  // can only be zero or one of these:
			  cp[cite.indexRelation].each { idx ->
				  prop['rdfverb'] = idx.'@rdfverb'
				  if (idx.'@inverseverb') {
					  prop['inverseverb'] = idx.'@inverseverb'
				  }
			  }

			  def valList = []
			  cp[cite.valueList][cite.value].each {
				  valList.add("${it.text()}")
			  }
			  prop['valueList'] = valList

			  propertyList.add(prop)
		  } 

		  def seq = ""
		  if (c.orderedBy) {
			  seq = "${c.orderedBy[0].'@property'}"
		  }
		  String groupProp = null
		  if (c.'@groupProperty') {
			  groupProp = c.'@groupProperty'


		  }


		  def citeExtensions = []
		  c[cite.extendedBy].each { ce ->
			  citeExtensions << "${ce.'@extension'}"
		  }

		  def collData = [
		  "title" : title,
		  "canonicalId" : "${c.'@canonicalId'}",
		  "labelProp" : "${c.'@label'}",

		  "groupProperty" : groupProp,
		  "nsabbr" : "${c[cite.namespaceMapping][0].'@abbr'}", 
		  "nsfull" :"${c[cite.namespaceMapping][0].'@uri'}",
		  "orderedBy" : seq,
		  "citeExtensions" : citeExtensions,
		  "properties" : propertyList,
		  "sourceType" : sourceType,
		  "source" : source
		  ]
		  configuredCollections.putAt("${c.'@urn'}",collData)
	  }
	  return configuredCollections
  }



  String getRdfTypeForExtension(String extensAbbr) 
  throws Exception {
    try {
      return this.extensionsMap[extensAbbr]
      
    } catch (Exception e) {
      throw new Exception("CollectionArchive:  no extension ${extensAbbr} configured.")
    }
  }


  /** Finds the list of enumerated values allowed for a property.
   * @param urn A CiteUrn identifying the collection.
   * @param propertyName Name of the property in question.
   * @returns 
   */
  ArrayList getValueList(CiteUrn urn, String propertyName) {
    def config =  this.citeConfig[urn.toString()]

    def vals = []
    if (config) {
      config['properties'].each { p ->
	if (debug > WARN) {
	  System.err.println "CollectionArchive:getValueList: examine property " + p
	}

	if (p['name'] == propertyName) {
	  vals = p['valueList']
	}
      }
    }
    return vals
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
      def config =  this.citeConfig[urn.toString()]
      return config['citeExtensions']
    } catch (Exception e) {
      throw new Exception("CollectionArchive:getExtensionList: no collection ${urn} configured.")
    }
  }
  

  /** Finds name of property with URN identifier
   * for objects in a collection.
   * @param urn The Collection in question.
   * @returns Name of the property.
   * @throws Exception if urn is not a configured collection.
   */
  String getCanonicalIdProperty(CiteUrn urn) 
  throws Exception {
    try {
      def config =  this.citeConfig[urn.toString()]
      return config['canonicalId']
    } catch (Exception e) {
      throw new Exception("CollectionArchive:getCanonicalIdProperty: no collection ${urn} configured.")
    }
  }


  /** Finds the value of the rdfVerb for a given property.
   * @param urn The Collection in question.
   * @param propertyName Name of the property.
   * @returns The rdfVerb string, or null if none configured.
   * @throws Exception if urn is not a configured collection or
   * if propertyName does not exist in that collection.
   */
  String getRdfVerb(CiteUrn urn, String propertyName) 
  throws Exception {
    String rdfVerb = null
    def config
    try {
      config =  this.citeConfig[urn.toString()]
    } catch (Exception e) {
      throw new Exception("CollectionArchive:getCanonicalIdProperty: no collection ${urn} configured.")
    }
    
    boolean propertyFound
    config['properties'].each { p ->
      if (debug > 3) {
	System.err.println "getRdfVerb: cf property ${p['name']} and ${propertyName}"
      }
      if (p['name'] == propertyName) {
	rdfVerb =  p['rdfverb']
	propertyFound = true
	if (debug > 3) {
	  System.err.println "FOUND IT. ${propertyFound}"
	}
      }
    }
    if (propertyFound) {
      return rdfVerb
    } else {
      throw new Exception("CollectionArchive:getRdfVerb: no property ${propertyName} in collection ${urn}")
    }
  }



  /** Finds name of property with labelling
   * information usable in rdf:label description.
   * @param urn The Collection in question.
   * @returns Name of the property.
   * @throws Exception if urn is not a configured collection.
   */
  String getLabelProperty(CiteUrn urn) 
  throws Exception {
    try {
      def config =  this.citeConfig[urn.toString()]
      return config['labelProp']
    } catch (Exception e) {
      throw new Exception("CollectionArchive:getLabelProperty: no collection ${urn} configured.")
    }
  }



  /** Finds name of property with long description
   * of the Collection.
   * @param urn The Collection in question.
   * @returns Name of the property.
   * @throws Exception if urn is not a configured collection.
   */
  String getTitle(CiteUrn urn) 
  throws Exception {
    try {
      def config =  this.citeConfig[urn.toString()]
      return config['title']
    } catch (Exception e) {
      throw new Exception("CollectionArchive:getLabelProperty: no collection ${urn} configured.")
    }
  }



  /** Finds a single pairing of source type and source name
   * for a Collection.  Currently, only 'file' source type is
   * implemented, and source name should be a local file name.
   * @param urn The Collection in question.
   * @returns An ArrayList containing the two items.
   */
  ArrayList getSourcePair(CiteUrn urn) {
        def config = this.citeConfig[urn.toString()]
        def pair = [config['sourceType'], config['source']]
        return pair
    }


    String getDCMetadata() {
    }


    String getOrderedByProperty(CiteUrn urn) {
      if (debug > 5) {
	System.err.println "Get ordered prop for " + urn
      }
        def config =  this.citeConfig[urn.toString()]
        return config['orderedBy']
    }


    String getClassName(CiteUrn urn) {
        def config =  this.citeConfig[urn.toString()]
        return config['className']
    }

    boolean isOrdered(CiteUrn urn) {
        def config =  this.citeConfig[urn.toString()]
        return (config['orderedBy']?.size() > 0)
    }



  //  What the heck is this supposed to mean?
  boolean isGrouped(CiteUrn urn) {
    def config =  this.citeConfig[urn.toString()]
    return (config['groupedBy']?.size() > 0)
  }


    // do we really want the whole collection in one NS?
    def getNs() {
        def invroot = new XmlParser().parse(this.inventory)

    }





    ArrayList getCollectionList() {
        def collectionList = []
        def invroot = new XmlParser().parse(this.inventory)
        invroot[cite.citeCollection].each { cc ->
            def nsMap = cc[cite.namespaceMapping][0]
            collectionList.add("${cc.'@uri'}")
        }
        return collectionList
    }


  
  /* get ordered list of prop names */
  ArrayList getPropNameList(CiteUrn collectionUrn) {
    return getPropNameList(collectionUrn.toString())
  }


  ArrayList getPropNameList(String collectionUrn) {
    def config =  this.citeConfig[collectionUrn]
    def propList = []

    if (config) {
      config['properties'].each { p ->
	propList.add(p['name'])
      }
    }
    return propList
  }




  ArrayList getPropLabelList(CiteUrn collectionUrn) {
        return getPropLabelList(collectionUrn.toString())
    }

    ArrayList getPropLabelList(String collectionUrn) {
        def config =  this.citeConfig[collectionUrn]
        def propList = []
        config['properties'].each { p ->
            propList.add(p['label'])
        }
        return propList
    }



ArrayList getPropTypeList(CiteUrn collectionUrn) {
        return getPropTypeList(collectionUrn.toString())
    }

ArrayList getPropTypeList(String collectionUrn) {
        def config =  this.citeConfig[collectionUrn]
        def propList = []
        config['properties'].each { p ->
            propList.add(p['type'])
        }
        return propList
    }



  /**  Writes an RDF description, in TTL format, of the data about
   * a collection expressed by the Collection Inventory.
   * @returns A String composed of TTL statements.
   * @throws Exception if inventory is misconfigured.
   */
String turtlizeInventory() 
throws Exception {
StringBuffer ttl = new StringBuffer()

def invroot = new XmlParser().parse(this.inventory)

	invroot[cite.extensions][cite.extension].each { extension ->

		String extensionUri = extension.'@rdfUri'
		String extensionType = extension.'@rdfType'

			System.err.println "CollectionArchive: examine exension ${extensionUri}"
			System.err.println "It has type ${extensionType}"
			
		ttl.append("<${extensionUri}> rdf:type cite:CiteExtension .\n")
		ttl.append("<${extensionUri}> cite:abbreviatedBy ${extensionType} .\n")
	}

invroot[cite.citeCollection].each { cc ->
	if (!cc.'@urn') {
		System.err.println "CollectionArchive:  cannot turtlieze collection with no URN!"
		System.err.println "Parsed record was " + cc
		throw new Exception("No urn defined for collection.")
	}
	CiteUrn urn = new CiteUrn(cc.'@urn')
	String labelProperty = getLabelProperty(urn)
	def nsMap = cc[cite.namespaceMapping][0]
	//System.err.println "nsMap.'@uri' = "
	// System.err.println nsMap.'@uri'

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
		}
	}

	if (cc[cite.orderedBy]) {
		ttl.append("<urn:cite:${nsMap.'@abbr'}:${urn.getCollection()}>  cite:orderedBy citedata:${urn.getCollection()}_${cc[cite.orderedBy][0].'@property'} .\n")
	}
	ttl.append("\n")


	/* document configured properties: */
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

}
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

	  if (!ordered) {
	    oneRow.append("<${column}> cite:ordered " + '"false" .\n')
	  }

	  if (debug > 3) { System.err.println "appended to oneRow, now ${oneRow}" }
	} catch (Exception e) {
	  System.err.println "turtlizeOneRow: unable to make urn from ${column}"
	}
      }
    }

    def collConf = this.citeConfig["${collUrn}"]

    if (collConf == null) {
      throw new Exception("CollectionArchive:turtlizeOneRow: no configuration for collection ${collUrn}")
    }

	/*
    if (debug > 0) {
      System.err.println "Config for ${collUrn} is " + collConf
    }
	*/
        

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
	      break

	      case "number":
	      oneRow.append("<${urn}> citedata:${urn.getCollection()}_${headingIndex[i]} ${c} .\n")

	      if (getRdfVerb(collUrn, headingIndex[i])) {
			oneRow.append( "<${urn}> ${getRdfVerb(collUrn, headingIndex[i])} ${c} .\n")
	      }

	      break

	      case "markdown":              
		  case "geojson":
	      case "string":
	      oneRow.append("<${urn}> citedata:${urn.getCollection()}_${headingIndex[i]} " + '"' + c + '" .\n')
	      if (getRdfVerb(collUrn, headingIndex[i])) {
		oneRow.append( "<${urn}> ${getRdfVerb(collUrn, headingIndex[i])} " + '"' + c + '" .\n')
	      }

	      break
                
	      case "citeurn":
	      case "citeimg":
	      case "ctsurn":
	      oneRow.append("<${urn}> citedata:${urn.getCollection()}_${headingIndex[i]} <${c}> .\n")

	      if (getRdfVerb(collUrn, headingIndex[i])) {
		oneRow.append( "<${urn}> ${getRdfVerb(collUrn, headingIndex[i])} <${c}> .\n")
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
        String canonical = getCanonicalIdProperty(collUrn)
        String label = getLabelProperty(collUrn)
        String orderProp = getOrderedByProperty(collUrn)

        def headingIndex = [:]
        def seqs = [:]

        /* First, cycle though file to construct a map of sequence numbers to URNs: */
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
        /* then, use the sequences index to generate prev-next 
        statements for each object in the list: */
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
        }
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

    Reader wrapper = new InputStreamReader(new FileInputStream(f), "utf-8");
    CSVReader reader = new CSVReader(wrapper)
    reader.readAll().each { cols ->
      if (debug >= SCREAM) {System.err.println "READ COLS " + cols + " of size " + cols.size()}
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
        }
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
    if (debug > WARN) {
      System.err.println "Trying to turtilize entire archive ..."
    }
    try {
      ttl(outFile, false)
    } catch (Exception e) {
      throw e
    }
    if (debug > WARN) {
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
    this.citeConfig.keySet().each { u ->
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

	  if (debug > WARN) {System.err.println "Turtlized ${f} successfully: appending data"}
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
