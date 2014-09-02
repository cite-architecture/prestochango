package edu.holycross.shot.prestochango


import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

import org.apache.commons.io.FilenameUtils

/**
*/
class CiteCollection {


    /** Root directory of file system containing archival files.
    */
    File baseDirectory


    /** 
    */
    CiteCollection(File baseDir) 
    throws Exception {
        if (!baseDir.canRead()) {
            throw new Exception("CiteCollection: cannot read directory ${baseDir}")
        }
        this.baseDirectory = baseDir


        try {
            validateSchema()

        } catch (Exception invException) {
            throw invException
        }
    }



    /** Validates the XML serialization of the collection's schema
    * against the published schema for a CITE TextInventory.
    * @throws Exception if the XML does not validate.
    */
    void validateSchema() 
    throws Exception {
/*
        URL TextInvSchema = new URL("http://www.homermultitext.org/hmtschemas/TextInventory.rng")
        System.setProperty("javax.xml.validation.SchemaFactory:"+XMLConstants.RELAXNG_NS_URI,
    "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");

        def factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI)
        def schema = factory.newSchema(TextInvSchema)
        def validator = schema.newValidator()
        try {
            validator.validate(inventoryXml)
        } catch (Exception e) {
            throw e
        }
*/
    }

}
