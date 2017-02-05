package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


/** A class representing the implementation of a CITE Collection
 * in the context of a specific CITE Collection service.
 * Abbreviations for extensions can be resolved with reference to the
 * inventory documenting the CITE Collection Service where this collection
 * is implemented.
 */
class CiteProperty {



  /** Identifying name of property. */
  String propertyName

  /** Type of property.  String value must be
   * one of the set citeTypes. */
  CitePropertyType propertyType

  /** A human-readable name for this property.  */
  String label

  RdfVerb rdfPair = null


  /** Possible null set defining a controlled vocabulary list for a
   * string property.  A null valueSet means that any value is allowed.
   */
  Set valueSet = []

  // string expression of a single value of any type,
  // converted to type defined in propertyType on retreival
  String singleValue = null

  /** Constructor with three required values.
   * @param propName Name of the property.
   * @param propType One of the allowed values for property type.
   * @param propLabel Human-readable label for the property.
   */
  CiteProperty (String propName, CitePropertyType propType, String propLabel) {
      this.propertyName = propName
      this.propertyType = propType
      this.label = propLabel
  }

  CiteProperty (String propName, CitePropertyType propType, String propLabel, RdfVerb rdfVerb) {
    this.propertyName = propName
    this.propertyType = propType
    this.label = propLabel
    this.rdfPair = rdfVerb

    if ((propType != CitePropertyType.CITE2_URN) && (propType != CitePropertyType.CTS_URN))  {
      throw new Exception("Cannot create property ${propName} of type ${propType}: RDF relations only apply to URN values")
    }
  }


  /**
   * Constructor with controlled vocabulary list for a string property.
   * @param propName Name of the property.
   * @param propLabel Human-readable label for the property.
   * @param propValues Set of allowed values for this string property.
   */
  CiteProperty (String propName, String propLabel, Set propValues) {
    this.propertyType = CitePropertyType.STRING
    this.propertyName = propName
    this.label = propLabel
    this.valueSet = propValues
  }


  /** Gets set of controlled vocabulary for a string object.
   * If there are no restrictions on values, the Set will be empty.
   * @returns A (possibly empty) set of string values.
   * @throws Exception if the CiteProperty is not a string type property.
   */
  Set getVocabulary()
  throws Exception {
    if (this.propertyType != CitePropertyType.STRING) {
      throw new Exception("CiteProperty: cannot get controlled vocabulary on object of type ${this.propertyType}")
    } else {
      return this.valueSet
    }
  }




  // Creates appropriate type of object from the "universal value" string.
  Object getSingleValue() {
    if (this.singleValue  == null) {
      throw new Exception("CiteProperty: single value not defined for property ${propertyName}")
    } else {
      //println "CiteProperty ${propertyName}: single value is " + this.singleValue + " (null? ${this.singleValue == null})"
      //println "It's a " + singleValue.getClass() + " with size " + singleValue.size()
    }

    Object singleVal = null
    switch (this.propertyType) {
    case (CitePropertyType.CITE2_URN):

    try {
			if (this.singleValue.contains(":cite:")){
				CiteUrn c1urn = new CiteUrn(this.singleValue)
				singleVal = new Cite2Urn(c1urn)
			} else {
	      singleVal = new Cite2Urn(this.singleValue)
			}
    } catch(Exception e) {
      System.err.println "CiteProperty, ${propertyName}: single value '" + this.singleValue + "' is not a valid CITE or CITE2 URN. ${e}"
      throw e
    }
    break


    case (CitePropertyType.CTS_URN):
    try {
      singleVal = new CtsUrn(this.singleValue)
    } catch(Exception e) {
      System.err.println "CiteProperty, ${propertyName}: single value '" + this.singleValue + "' is not a valid CTS URN"
      throw e
    }
    break

    case (CitePropertyType.MARKDOWN):
    case (CitePropertyType.STRING):
    singleVal = this.singleValue
    break

    case (CitePropertyType.NUM):
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(',');
    symbols.setDecimalSeparator('.');
    String pattern = "#,##0.0#";
    DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
    decimalFormat.setParseBigDecimal(true);
    // parse the string
    singleVal = (BigDecimal) decimalFormat.parse(this.singleValue)
    break


    case (CitePropertyType.BOOLEAN):
    singleVal = this.singleValue.toBoolean()
    break
    }
    if (singleVal == null) {
      throw new Exception("CiteProperty: single value not defined for property ${propertyName}")
    } else {
      return singleVal
    }
  }

  /** Overrides default. */
  String toString() {
    return("${this.propertyName} (${this.propertyType})")
  }


  /** Formats a string representation of this property's
   * CITE type for use in TTL statements.
   * @returns A String formatted for use in TTL.
   */
  String typeAsRdfString() {
    String rdf = ""
    switch(propertyType) {

    case (CitePropertyType.CITE2_URN):
    rdf = "cite:Cite2Urn"
    break

    case (CitePropertyType.CTS_URN):
    rdf = "cite:CtsUrn"
    break

    case (CitePropertyType.STRING):
    rdf = "cite:String"
    break
    case (CitePropertyType.MARKDOWN):
    rdf = "cite:Markdown"
    break
    case (CitePropertyType.NUM):
    rdf = "cite:Numeric"
    break
    case (CitePropertyType.BOOLEAN):
    rdf = "cite:Boolean"
    break

    default:
    throw new Exception("CiteProperty: unrecognized type " + propertyType)
    break
    }
    return rdf
  }

  /** Formats a string representation of a value for
   * this property for use in TTL statements.
   * @param propValue String representation of a
   * value for this property.
   * @returns A String formatted for use in TTL.
   */
  String asRdfString(String propValue) {

    String objectString = null
    switch (this.propertyType) {

    case CitePropertyType.BOOLEAN:
    if (propValue == "true") {
      objectString = "true"
    } else {
      objectString = "false"
    }
    break

    case CitePropertyType.STRING:
    objectString = '"' + propValue + '"'
    break

    case CitePropertyType.NUM:
    objectString = propValue
    break

    case CitePropertyType.MARKDOWN:
    // triple quote?
    objectString = '"' + propValue + '"'
    break


    case CitePropertyType.CITE2_URN:

    try {
			Cite2Urn urn
			if (propValue.contains(":cite:")){
				CiteUrn c1urn = new CiteUrn(propValue)
				urn = new Cite2Urn(c1urn)
			} else {
	      urn = new Cite2Urn(propValue)
			}

			if (urn.hasExtendedRef()){
				objectString = '<' + urn.encodeSubref() + '>'
			} else {
				objectString = '<' + urn.toString() + '>'
			}

    } catch (Exception e) {
      System.err.println("CiteProperty: invalid value for CITE URN " + propValue)
      throw e
    }
    break

    case CitePropertyType.CTS_URN:
    try {
      CtsUrn urn = new CtsUrn(propValue)
    } catch (Exception e) {
      System.err.println("CiteProperty: invalid value for CTS URN " + propValue)
      throw e
    }
		CtsUrn ctsurn = new CtsUrn(propValue)
		if (ctsurn.hasSubref()){
       objectString = '<' + ctsurn.encodeSubref() + '>'
		} else {
       objectString = '<' + ctsurn.toString() + '>'
		}
    break

    default :
    System.err.println "UNRECOGNIZED TYPE:" + propertyType
    throw new Exception("CiteProperty: unrecognized type " + propertyType)
    break
    }
    if (objectString == null) {
      throw new Exception("CiteProperty ${propertyName}/${propertyType} not expressed as RDF")
    } else {
      //println "CiteProperty ${propertyName} returning RDF  " + objectString
      return objectString
      }
  }

}
