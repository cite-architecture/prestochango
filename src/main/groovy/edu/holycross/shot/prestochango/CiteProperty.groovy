package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn
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
  String singleValue = ""
  
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

    if ((propType != CitePropertyType.CITE_URN) && (propType != CitePropertyType.CTS_URN))  {
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
      throw new Exception("Single value note defined for property ${propertyName}")
    }
    switch (this.propertyType) {
    case (CitePropertyType.CITE_URN):
    try {
      return new CiteUrn(this.singleValue)
    } catch(Exception e) {
      System.err.println "Single value '" + this.singleValue + "' is not a valid CITE URN"
      throw e
    }
    break


    case (CitePropertyType.CTS_URN):
    try {
      return new CtsUrn(this.singleValue)
    } catch(Exception e) {
      System.err.println this.singleValue + " is not a valid CTS URN"
      throw e
    }
    break

    case (CitePropertyType.MARKDOWN):
    case (CitePropertyType.STRING):
    return this.singleValue
    break

    case (CitePropertyType.NUM):
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator(',');
    symbols.setDecimalSeparator('.');
    String pattern = "#,##0.0#";
    DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
    decimalFormat.setParseBigDecimal(true);
    // parse the string
    return (BigDecimal) decimalFormat.parse(this.singleValue)
    break


    case (CitePropertyType.BOOLEAN):
    return this.singleValue.toBoolean()
    break
    
    }
  }
  
  /** Overrides default. */
  String toString() {
    return("${this.propertyName} (${this.propertyType})")
  }

}
