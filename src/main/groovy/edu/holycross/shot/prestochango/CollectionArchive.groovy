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
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
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

      this.dataSources = configureDataSources(inv, baseDir)

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
    this.dataSources = configureDataSources(inv, baseDir)

    if (debug > 0) { System.err.println "Configuration map = " + this.collections} 
  }


  /** Maps URNs for each CITE Collection in the archive to
   * a CiteDataSource object.
   * @param inv File with a CITE inventory for the archive.
   * @param baseDir Root directory of locally stored files.
   * @returns A map of URN values (as Strings) to CiteDataSource objects.
   */
  LinkedHashMap configureDataSources(File inv, File baseDir) {
    groovy.util.Node root 
    try {
      root = new XmlParser().parse(inv)
    } catch (Exception e) {
      throw new Exception("CollectionArchive: unable to parse inventory file ${f}")
    }
    
    def configuredSources = [:]
    root[cite.citeCollection].each { c ->
      if (debug > 0) { System.err.println "Configure data source for " + c.'@urn'}
      configuredSources.putAt("${c.'@urn'}", configureDataSource(c, baseDir))
    }
    return configuredSources
  }



  /** Instantiates a CiteDataSource object for a CITE Collection.
   * @param c Parsed XML node for a single collection.
   * @param baseDir Root directory of locally stored files.
   * @returns A CiteDataSource object.
   */
  CiteDataSource configureDataSource(groovy.util.Node c, File baseDir) {
    CiteDataSource cds
    c[cite.source].each { src ->
      switch (src.'@type') {
      case "file":
      File f = new File(baseDir,src.'@value')
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

  /** For all extensions recognized in this archive,
   * maps the abbreviated identifier to a full URI.
   * @param f File with a CITE inventory for the archive.
   * @returns A map of abbreviations (Strings) to URI Strings.
   */
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


  /** Maps URNs for each CITE Collection in the archive to
   * a property-value map for Dublin Core data.
   * @param inv File with a CITE inventory for the archive.
   * @returns A map of URN values (as Strings) to LinkedHashMaps
   * with keys of "title", "description" and "rights".
   */
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


  /** For a given CITE collection, instantiates a key-value
   * map for Dublin Core metadata.
   * @param c Parsed XML node for a single collection.
   * @returns A map of Dublin Core element names to String values.
   */
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
  

  /** Maps URNs for each CITE Collection in the archive to
   * a CiteCollection object.
   * @param inv File with a CITE inventory for the archive.
   * @returns A map of URN values (as Strings) to CiteCollection objects.
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


  /** Finds CitePropertyType value corresponding
   * to attribute values allowed by schema for
   * CITE Collection inventory.
   * @param s Value of attribute.
   * @returns Corresponding CitePropertyType.
   */
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


  /** Gathers the list of CITE extensions appearing
   * in a CITE Collection inventory for a given
   * collection.
   * @param c Parsed XML node for a single collection.
   * @returns A List of extension identifiers in
   * abbreviated form.
   */
  ArrayList collectExtensions(groovy.util.Node c ) {
    ArrayList extensions = []
    c[cite.extendedBy].each { ce ->
      extensions << "${ce.'@extension'}"
    }
    return extensions
  }


  /** Gathers the list of CITE properties
   * in a CITE Collection inventory for a given
   * collection.
   * @param Parsed XML node for a single collection.
   * @returns A list of CiteProperty objects.
   */
  ArrayList collectProperties(groovy.util.Node c ) {
    ArrayList propertyList = []
    c[cite.citeProperty].each { cp ->
      CiteProperty citeProperty
            
      String propName = "${cp.'@name'}"
      String propLabel = "${cp.'@label'}"

      
      String rdfAbbr = ""
      String rdfFull = ""
      String inverseAbbr = ""
      String inverseFull = ""
      cp[cite.indexRelation].each { idx ->
	rdfAbbr = idx.'@rdfAbbr'
	rdfFull = idx.'@rdfFull'
	if (idx.'@inverseAbbr') {
	  inverseAbbr = idx.'@inverseAbbr'
	  inverseFull = idx.'@inverseFull'
	}
      }
      RdfVerb rdf = null
      
      if ((rdfAbbr != "")
	  && (rdfFull != "")
	  &&  (inverseAbbr != "")
	  &&  (inverseFull != "")
	 ) {
	rdf = new RdfVerb(rdfAbbr,rdfFull,inverseAbbr,inverseFull)
      } else if ((rdfAbbr != "")
		 && (rdfFull != "")) {
	rdf = new RdfVerb(rdfAbbr,rdfFull)
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

      if (cp.'@universalValue' != null) {
	//println "INITIALIZING UVAL: " + cp.'@universalValue'
	//println cp.attributes()
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
      throw new Exception("NO PROPERTY FOR  #" + c.'@label' + "# in " + collUrn + " w properties " + collProps)
    } else {
      if (debug > 0) { System.err.println "Using labelling property " + labelProp }
    }

    CiteProperty orderedByProp = findPropertyByName(collProps, orderingPropName)
    
    return new CiteCollection(collUrn, descr, idProp, labelProp, orderedByProp, nsAbbr, nsFull, collProps, extensions)
  }


  
  /** Gets the CiteCollection object for
   * a collection identified by URN.
   * @param urn URN of the collection to find.
   * @returns A CiteCollection object.
   */
  CiteCollection getCollection(CiteUrn urn) {
    return collections[urn.toString()]
  }

  /** Gets a list of all collections in archive.
   * @returns A list of CiteCollection objects.
   */
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


  /** Gets a list of properties for a collection 
   * identified by URN.
   * @param urn The Collection in question.
   * @returns A list of CiteProperty objects.
   * @throws Exception if urn is not a configured collection.
   */
  ArrayList getProperties(CiteUrn urn)
  throws Exception {
    return this.collections[urn.toString()].collProperties
  }
  
  /** Gets a description of a collection
   * identified by URN.
   * @param urn The Collection in question.
   * @returns A human-readable string.
   * @throws Exception if urn is not a configured collection.
   */
  String getDescription(CiteUrn urn)
  throws Exception {
    return this.collections[urn.toString()].description
  }

  /** Gets the CITE namespace abbreviation of a collection
   * identified by URN.
   * @param urn The Collection in question.
   * @returns CITE namespace, in abbreviated form.
   * @throws Exception if urn is not a configured collection.
   */
  String getNsAbbr(CiteUrn urn) {
    return this.collections[urn.toString()].nsAbbr
  }


  /** Gets the CITE namespace of a collection
   * identified by URN, as a full URI.
   * @param urn The Collection in question.
   * @returns CITE namespace URI.
   * @throws Exception if urn is not a configured collection.
   */
  String getNsFull(CiteUrn urn)
  throws Exception {
    return this.collections[urn.toString()].nsFull
  }
  
  /** Gets a list of controlled vocabulary values
   * for a given property in a given collection. The 
   * property must be of type CitePropertyType.STRING.
   * An empty list means that any string value is allowed.
   * @param urn The Collection containing the property.
   * @param propertyName String name of the property to find.
   * @returns List of zero or more allowed values for the property.
   * @throws Exception if urn is not a configured collection, or
   * if propertyName does not exist in that collection.
   */
  ArrayList getVocabulary(CiteUrn urn, String propertyName)
  throws Exception {
    return this.collections[urn.toString()].getVocabList(propertyName)
  }

  /** Finds the value of the rdfVerb for a given property
   * in a given collection.
   * @param urn The Collection containing the property.
   * @param propertyName String name of the property to find.
   * @returns An RdfVerb object, or null if none configured.
   * @throws Exception if urn is not a configured collection or
   * if propertyName does not exist in that collection.
   */
  RdfVerb getRdfVerb(CiteUrn urn, String propertyName) 
  throws Exception {
    return this.collections[urn.toString()].getRdf(propertyName)
  }

  /** For a given property in a given collection,
   * finds the single value to apply to all instances
   * of that property, if one has been defined.
   * @param urn The Collection in question.
   * @param propertyName Name of the property.
   * @returns An object of appropriate class for the
   * property's CitePropertyType, or null if a "universal value"
   * has not been configured.
   * @throws Exception if urn is not a configured collection or
   * if propertyName does not exist in that collection.
   */
  Object getSingleValue(CiteUrn collectionUrn, String propertyName)
  throws Exception {
    CiteCollection cc = this.collections[collectionUrn.toString()]
    Object singleVal =  cc.getSingleValue(propertyName)
    println "CollectionArchive: Single val for ${propertyName}: " + singleVal + " (${singleVal.getClass()})"
    println "is value ${singleVal} null? ${singleVal == null}"
    return singleVal
  }

  
  // ORDERING
  CiteProperty getOrderedByProperty(CiteUrn urn) {
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

  String getUriForExtension(String extensAbbr) 
  throws Exception {
    try {
      return this.extensionsMap[extensAbbr]

    } catch (Exception e) {
      throw new Exception("CollectionArchive:  no extension ${extensAbbr} configured.")
    }
  }





  String turtleizeArchive() {
    StringBuilder ttl = new StringBuilder()
    // 0. Handle ttl'ing prefix data
    ttl.append(prefix)
    
    // 1. Done once for whole archive:
    // expansion of extension abbr -> URI
    ttl.append(ttlExtensionMap())

    // 2. Ttlze each collection
    collections.each { k,v ->
      ttl.append(turtleizeCollection(v))
    }
    return ttl.toString()
  }


  String ttlExtensionMap() {
    StringBuilder ttl = new StringBuilder()
    this.extensionsMap.each { k, v ->
      ttl.append("<${v}> rdf:type cite:CiteExtension .\n")
      ttl.append("<${v}> cite:abbreviatedBy ${k} .\n")
    }
    return ttl.toString()
  }



  /** Composes TTL describing structure of a given property in
   * a given CITE Collection.
   * @param prop The property to describe.
   * @param cc The Collection containing the property.
   * @returns A String with TTL statements.
   */
  String ttlPropertyStructure(CiteProperty prop, CiteCollection cc) {
    StringBuilder ttl = new StringBuilder()

    String urnStr = "<${cc.urn}>"
    String propUri = "citedata:${cc.urn.getCollection()}_${prop.propertyName}"
    
    ttl.append( urnStr + " cite:collProperty  " + propUri + " .\n")
    ttl.append( propUri + " rdf:type rdf:Property .\n")
    ttl.append( propUri + " cite:propType " + prop.typeAsRdfString() + " .\n")

    return ttl.toString()
  }


  /** Composes TTL describing structure of a given CITE Collection.
   * @param cc The Collection to describe.
   * @returns A String with TTL statements.
   */
  String ttlCollectionStructure(CiteCollection cc) {
    StringBuilder ttl = new StringBuilder()
    
    String urnStr = "<${cc.urn}>"
    ttl.append(urnStr + " rdfs:label " + '"' + cc.description  + '" . \n')

    String canonicalPropStr = "citedata:${cc.urn.getCollection()}_${cc.canonicalIdProp.propertyName}"
    ttl.append(urnStr + " cite:idPropName " + canonicalPropStr + " . \n")

    String labelPropStr =  "citedata:${cc.urn.getCollection()}_${cc.labelProp.propertyName.replaceAll(/[\n\t\s]+/,' ')}"
    ttl.append(urnStr + " cite:labelPropName " + labelPropStr + " . \n")

    cc.collProperties.each { prop ->
      ttl.append(ttlPropertyStructure(prop, cc))
    }
    
    return ttl.toString()
  }
  
  String turtleizeCollection(CiteCollection cc) {
    StringBuilder ttl = new StringBuilder()

    //1. turtleize collection structure
    ttl.append(ttlCollectionStructure(cc))

    // check for optional ordering and extensions
    String urnStr = "<${cc.urn}>"
    
    // ordering
    if (cc.isOrderedCollection) {
      String orderingPropStr = "citedata:${cc.urn.getCollection()}_${cc.orderedByProp.propertyName}"
      
      ttl.append(urnStr + " cite:ordered " + '"true" .\n')
      ttl.append(urnStr + " cite:orderingPropName " + orderingPropStr + " . \n")

    } else {
      ttl.append(urnStr + " cite:ordered " + '"false" .\n') 
    }
    
    // extensions supported:
    ttl.append(ttlExtensionMap())

    
    //2. turtleize data array    
    LocalFileSource lfs = this.dataSources[cc.urn.toString()]
    ttl.append(turtleizeDataArray(lfs.getRecordArray(), cc))
    return ttl.toString()
  }

  String turtleizeDataArray(ArrayList records,CiteCollection cc)
  throws Exception {
    StringBuilder ttl = new StringBuilder()
    def header = records[0] as ArrayList
    def data = records[1..records.size() -1]
    data.each { r ->
      ttl.append(turtleizeOneRow(r as ArrayList,header, cc))
    }
    return ttl.toString()
  }


  /** Composes a TTL representation of of a single instance of a CITE property 
   * with data value.
   * @param propValue String representation of the property's value.
   * @param prop The CiteProperty this value instantiates.
   * @param colName Name of column in data array.
   * @param objectUrn URN of the object this data value belongs to.
   * @returns A String of TTL.
   */
  String turtleizeProperty(String propValue, CiteProperty prop, String colName, CiteUrn objectUrn) {
    StringBuilder propertyTtl = new StringBuilder()

    // Compose subject-verb-object statements
    String subject = "<${objectUrn}>"
    String verb = "citedata:${objectUrn.getCollection()}_${colName}"
    String objectString = prop.asRdfString(propValue)
    // CHECK CONTROL VOCAB ON STRINGS

    propertyTtl.append("${subject} ${verb} ${objectString} .\n")

    // Check for RDF verbs on property
    if (prop.rdfPair != null) {
      propertyTtl.append(ttlRdfVerb(subject, prop.rdfPair, objectString))
    }
    return propertyTtl.toString()
  }

  String ttlRdfVerb (String subjectString, RdfVerb verbs, String objectString) {
    StringBuilder ttl = new StringBuilder()
    ttl.append(subjectString + " " + verbs.abbr + " " + objectString + " .\n")
    if (verbs.inverseAbbr != null) {
      ttl.append(objectString + " " + verbs.inverseAbbr + " " + subjectString + " .\n")
    }
    return ttl.toString()
  }

  CiteUrn findCanonicalUrn(ArrayList cols, ArrayList header, String canonicalName){
    CiteUrn canonical = null
    cols.eachWithIndex { column, idx ->
      if (header.indexOf(canonicalName) == idx) {
	try {
	  canonical = new CiteUrn(column)
	} catch (Exception e) {
	  System.err.println "Could not form URN for canonical identifier " + column
	}
      }
    }
    if (canonical == null) {
      throw new Exception("No canonical ID find in " + header)
    } else {
      return canonical
    }
  }

  String findLabelString(ArrayList cols, ArrayList header, String labelPropName){
    String label = null
    cols.eachWithIndex { column, idx ->
      if (header.indexOf(labelPropName) == idx) {
	label = column
      }
    }
    if (label == null) {
      throw new Exception("No label string founnd in " + header)
    } else {
      return label
    }
  }

  String turtleizeUniversalValues(CiteUrn urn, CiteCollection coll) {
    StringBuilder ttl = new StringBuilder()
    coll.collProperties.each { p ->
      try {
	Object thingie = getSingleValue(coll.urn, p.propertyName)
	//	if ((thingie instanceof java.lang.String) &&  (thingie.size() == 0)) {
	//} else {
	println "Is the thing null? ${thingie == null}"
	if (thingie != null) {
	  println "Because ${p} -> " + thingie + " ... "

	  String subject = "<${urn}>"
	  String verb = "citedata:${urn.getCollection()}_${p.propertyName}"
	  String objectString = p.asRdfString("${thingie}")

	  propertyTtl.append("${subject} ${verb} ${objectString} .\n")
	  println "Appended to " + propertyTtl

	}
      } catch (Exception e) {
	//println "No single value for " + p
      }
    }
    return ttl.toString()
  }
  
  String turtleizeOneRow(ArrayList cols, ArrayList header, CiteCollection cc)
  throws Exception {
    StringBuilder rowTtl = new StringBuilder()
    // required Canonical ID:
    CiteUrn objectUrn = findCanonicalUrn(cols, header, cc.canonicalIdProp.propertyName)
    rowTtl.append(turtleizeCanonicalRelation(objectUrn, cc.urn))
    // required human-readable label:
    String objectLabel = findLabelString(cols, header, cc.labelProp.propertyName)
    rowTtl.append("<${objectUrn}> rdfs:label " + '"' + objectLabel + '" .\n')
    // 
    // ADD UNIVERSAL VALUE PROPERTIES
    // accessible from cc object
    rowTtl.append(turtleizeUniversalValues(objectUrn, cc))

    
    // All properties with variable values:
    cols.eachWithIndex { column, idx ->
      try {
	CiteProperty columnProp =  cc.collProperties.find {it.propertyName == header[idx]}
	rowTtl.append(turtleizeProperty(column, columnProp, header[idx], objectUrn))
	
      } catch (Exception e) {
	System.err.println "CollectionArchive: no property " + header[idx] + " for collection ${cc}"
	throw e
      }
    }
    return rowTtl.toString()
  }

  String turtleizeCanonicalRelation(CiteUrn objUrn,CiteUrn collUrn) {
    StringBuilder oneRow = new StringBuilder()
    oneRow.append("<${objUrn}> cite:belongsTo <${collUrn}> .\n")
    oneRow.append("<${collUrn}> cite:possesses <${objUrn}> .\n")

    if (objUrn.hasVersion()){
      oneRow.append("<${objUrn}> cite:isVersionOf <${objUrn.reduceToObject()}> .\n")
      oneRow.append("<${objUrn.reduceToObject()}> cite:hasVersion <${objUrn}> .\n")
      oneRow.append("<${objUrn.reduceToObject()}> cite:belongsTo <${collUrn}> .\n")
      oneRow.append("<${collUrn}> cite:possesses <${objUrn.reduceToObject()}> .\n")
    }
    return oneRow.toString()
  }
}



//////////////////////////////////////////////////////////////////////////////////
/// GET EXAMPLES OF TTL FROM QUARRY BELOW:  ///////////////////////////////////////




 //////////////////////////////////////////////////////////////////////////////////
 ////////////////////////////  RDFING UNIVERSAL VALUE  ////////////////////////////
  /*

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
  */


  //////////////////////////////////////////////////////////////////////
  ////////////OLO TTL FOR ORDERING: ///////////////////////////////////
  

  	  /*
	String addOloCsvData(File f, CiteUrn collUrn) {

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
		}
		return replyBuff.toString()
	}

 */

