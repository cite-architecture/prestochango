package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test





class TestArchiveCollectionProperties extends GroovyTestCase {

    String schemaFileName = "schemas/CiteCollectionInventory.rng"


    String testInventory = "testdata/collections.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)


    @Test void testPropMetadata() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)

        CiteUrn collUrn = new CiteUrn("urn:cite:hmt:msA")

        // Siglum	Sequence	URN	RV	Label	CodexURN
        def expectedNames = ["URN", "Siglum", "Sequence", "RV", "Label", "CodexURN"]
        def propList =  cc.getProperties(collUrn)
        def actualNames = []
        propList.each {
          actualNames.add(it.propertyName)
        }
        assert actualNames == expectedNames


        // Check type and name of squence property
        CiteProperty seqProp = propList[2]
        assert seqProp.propertyType == CitePropertyType.NUM
        assert seqProp.propertyName == "Sequence"

    }
}
