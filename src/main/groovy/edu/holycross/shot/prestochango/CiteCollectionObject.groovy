package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn

/** A class representing the implementation of a set of CITE Collection Objects
 */
class CiteCollectionObject {


  /** CITE URN for this set */
  public Cite2Urn urn = null

  /** CITE Collection to which this object belongs **/
  public CiteCollection collection = null

  /** URN of the previous object in an ordered collection; may be null */
  public Cite2Urn prevUrn = null

  /** URN of the next object in an ordered collection; may be null */
  public Cite2Urn nextUrn = null

  /** Map of property names and values
	*
  **/
  public def objectProperties  = [:]

  /** Constructor for CiteCollectionObject
   * @param urn A Cite URN identifying the collection
   * @param collection A CiteCollection
   * @param objectProperties a map of property-name <--> value
   */
    CiteCollectionObject(
		Cite2Urn urn,
		CiteCollection collection,
		Map objectProperties )

    throws Exception {

		// check properties for correct types
		String tempType = ""
		float f
		objectProperties.each { key, value ->
			tempType = collection.getPropertyType(key)
			switch(tempType){
				case "CITE2_URN":
					Cite2Urn tcite
					try {
						if (value.contains(":cite:")){
							CiteUrn c1urn = new CiteUrn(value)
							tcite = new Cite2Urn(c1urn)
						} else {
					    tcite = new Cite2Urn(value)
						}
					} catch (Exception e) {
						throw new Exception("CiteCollectionObject: Could not turn '${value}' into a CITE2 URN. " + e)
					}

				break;

				case "CTS_URN":
					try {
					    CtsUrn tcts = new CtsUrn(value)
					} catch (Exception e) {
						throw new Exception("CiteCollectionObject: Could not turn '${value}' into a CTS URN. " + e)
					}
				break;

				case "NUM":
					try {
						  f = Float.valueOf(value.trim()).floatValue();
						}
						catch (NumberFormatException nfe)
						{
						  throw new Exception("CiteCollectionObject: Could not conver '${value} to a number. " + nfe.getMessage());
						}
				break;

				case "BOOLEAN":
					if ((value != "true") && (value != "false")){
						  throw new Exception("CiteCollectionObject: '${value} was supposed to be either 'true' or 'false'. " )
					}

				break;
        case "STRING":
				break;

				default:
          throw new Exception("CiteCollectionObject: ${tempType} is not a recognized data-type for a CITE object.")

				break;
			}
		}

		this.urn = urn
		this.collection = collection
		this.objectProperties = objectProperties

	}

  /** Constructor for CiteCollectionObject
   * @param urn A Cite URN identifying the collection
   * @param collection A CiteCollection
   * @param properties a map of property-name <--> value
   * @param prevUrn previous urn in an ordered collection; may be null
   * @param nextUrn next urn in an ordered collection; may be null
   */
    CiteCollectionObject(
		Cite2Urn urn,
		CiteCollection collection,
		Map objectProperties,
		Cite2Urn prevUrn,
		Cite2Urn nextUrn )
    throws Exception {

		if (collection.isOrderedCollection == false){
			throw new Exception("CiteCollectionObject: You can't have prev and next on an unordered collection.")
		}


		// check properties for correct types
		String tempType = ""
		float f
			objectProperties.each { key, value ->
			tempType = collection.getPropertyType(key)
			switch(tempType){
				case "CITE2_URN":
				  Cite2Urn tcite
					try {
						if (value.contains(":cite:")){
						  CiteUrn c1urn = new CiteUrn(value)
							tcite = new Cite2Urn(c1urn)
						} else {
					    tcite = new Cite2Urn(value)
						}
					} catch (Exception e) {
						throw new Exception("CiteCollectionObject: Could not turn '${value}' into a CITE URN. " + e)
					}

				break;

				case "CTS_URN":
					try {
					    CtsUrn tcts = new CtsUrn(value)
					} catch (Exception e) {
						throw new Exception("CiteCollectionObject: Could not turn '${value}' into a CTS URN. " + e)
					}
				break;

				case "NUM":
					try {
						  f = Float.valueOf(value.trim()).floatValue();
						}
						catch (NumberFormatException nfe)
						{
						  throw new Exception("CiteCollectionObject: Could not conver '${value} to a number. " + nfe.getMessage());
						}
				break;

				case "BOOLEAN":
					if ((value != "true") && (value != "false")){
						  throw new Exception("CiteCollectionObject: '${value} was supposed to be either 'true' or 'false'. " )
					}

				break;
        case "STRING":
				break;

				default:
          throw new Exception("CiteCollectionObject: ${tempType} is not a recognized data-type for a CITE object.")

				break;
			}
		}


		this.urn = urn
		this.collection = collection
		this.objectProperties = objectProperties
		this.prevUrn = prevUrn
		this.nextUrn = nextUrn

	}

  /** Returns an ArrayList of property names
   * @returns ArrayList of property names
   */
  ArrayList getPropertyNames() {
		return this.collection.getPropertyNames()
  }

  Object getPropertyValue(String pn) {
	  	String pt = this.getPropertyType(pn)
		def returnVal

		switch(pt){
			case "cite2urn":
				returnVal = new Cite2Urn(this.objectProperties[pn])
			break;

			case "ctsurn":
				returnVal =  new CtsUrn(this.objectProperties[pn])
			break;

			case "boolean":
				if (this.objectProperties[pn] == "true") { returnVal =  true } else { returnVal =  false }
			break;

			case "number":
				returnVal = new BigDecimal(this.objectProperties[pn])
			break;

			default:
				returnVal =  this.objectProperties[pn]
			break;
		}
		return returnVal
  }

  String getPropertyType(String pn) {
	  	String tt = ""
		this.collection.collProperties.each { p ->
			if (p.propertyName == pn){ tt = p.propertyType }
		}
		return tt
  }

  BigDecimal getSequence()
    throws Exception {
		if (this.collection.isOrderedCollection != true){
			throw new Exception( "CITE Collection Object: ${this.collection.urn} is not an ordered collection, but you asked for sequence on an object.")
		}

		String orderedByProp = collection.orderedByProp.propertyName
		return new BigDecimal(this.objectProperties[orderedByProp])

  }

}
