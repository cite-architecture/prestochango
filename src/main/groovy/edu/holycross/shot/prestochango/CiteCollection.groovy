package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

/** A class representing the implementation of a CITE Collection
 * in the context of a specific CITE Collection service.
 * Abbreviations for extensions can be resolved with reference to the
 * inventory documenting the CITE Collection Service where this collection
 * is implemented.
 */
class CiteCollection {


  /** CITE Collection URN for this collection */
   public CiteUrn urn = null

  /** Name of the property with object's URN. */
  public CiteProperty canonicalIdProp = null

  /** Name of the property with a human-readable label.*/
  public CiteProperty labelProp = null

  /** Name of the property that holds sequencing info for an ordered collection **/
  public CiteProperty orderedByProp = null

  /** Is ths an ordered collection **/
  public Boolean isOrderedCollection = false

  /** Abbreviated form of CITE namespace used in URNs.*/
  public String nsAbbr = null

  /** URI uniquely identifying the CITE namespace. */
  public String nsFull = null

  
  /** Possibly empty list of extensions applicable to this 
   * Collection.  These are identified by abbreviations that 
   * can resolved with reference to the inventory of the Collection
   * Service where this Collection is instantiated. */
  public ArrayList extendedBy = []

  // all properties
  public ArrayList collProperties = []

  
  /** Constructor for CollectionArchive using local file storage.
   * @param urn A Cite URN identifying the collection
   * @param canonicalIdProp A string naming the property that contains each object's CITE URN, its unique identifier
   * @param labelProp A string naming the property that contains labeling info for each object
   * @param orderedProp May be null. A string naming the property that contains sequence info for an ordered collection
   * @param extendedBy May be null. An array list of Strings naming extensions
   * @param properties May be null. An ArrayList of CollectionProperty objects must have at least two.
   */
    CiteCollection(
		CiteUrn urn, 
		CiteProperty canonicalIdProp, 
		CiteProperty labelProp, 
		CiteProperty orderedByProp, 
		String nsAbbr, 
		String nsFull, 
		ArrayList collProperties,
		ArrayList extendedBy ) 
    throws Exception {

        try {
            
		  if ( (urn == null) ) {
			  throw new Exception("CiteCollection constructor: collUrn parameter cannot be null.")
		  }

		  this.urn = urn 

		  if ( (collProperties == null) || (collProperties.size() < 2) ) {
			  throw new Exception("CiteCollection constructor: there must be at least two properties identified in the ArrayList param 'properties'.")
		  }

		  this.collProperties = collProperties 

		  if ( canonicalIdProp == null ) {
			  throw new Exception("CiteCollection constructor: String param canonicalIdProp cannot be null or empty.")
		  }

		  this.canonicalIdProp = canonicalIdProp

		  if ( (nsAbbr == null) || (nsAbbr == "") ) {
			  throw new Exception("CiteCollection constructor: String param nsAbbr cannot be null or empty.")
		  }

		  this.nsAbbr = nsAbbr

		  if ( (nsFull == null) || (nsFull == "") ) {
			  throw new Exception("CiteCollection constructor: String param nsFull cannot be null or empty.")
		  }

		  this.nsFull = nsFull

		  if ( (labelProp == null) || (labelProp == "") ) {
			  throw new Exception("CiteCollection constructor: String param idProp cannot be null or empty.")
		  }

		  this.labelProp = labelProp


		  if (( orderedByProp != null) && (orderedByProp.propertyName != "")){
			    if (orderedByProp.propertyType != "number"){
					throw new Exception("CiteCollection constructor: OrderedBy property must have type of 'numbver'.")
				}
				this.orderedByProp = orderedByProp
				this.isOrderedCollection = true	
		  } else {
				this.orderedByProp = null
				this.isOrderedCollection = false
		  }

        } catch (Exception ccException) {
            throw ccException
        }
    }



  /** Evaluates configuration of this collection.
   * @returns True if configuration is valid.
   */
  boolean isValid() {
    // check that nsFull is a URI object...
    return(urn != null
	   && (canonicalIdProp != null)
	   && (labelProp != null)
	   && (nsAbbr == urn.getNs())
	   && (nsFull != null)
	   && (collProperties.size() > 1) // we need at least two properties
	  )
  }

  /** Returns an ArrayList of property names
   * @returns ArrayList of property names
   */
  ArrayList getPropertyNames() {
		def pn = []
		this.collProperties.each { p ->
			pn << p.propertyName
		}
		return pn
  }

  
  /** Returns an string identifying the type of a given property
	* @param String property name
   * @returns String
   */
  String getPropertyType(String pn) {
	  	String tt = ""
		this.collProperties.each { p ->
			if (p.propertyName == pn){ tt = p.propertyType }
		}
		return tt
  }


  public String getNsFull() {
    return this.nsFull
  }
  
}
