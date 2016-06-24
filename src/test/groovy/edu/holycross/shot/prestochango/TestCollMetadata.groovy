package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestCollMetadata extends GroovyTestCase {

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
        assert cc.getPropNameList(collUrn) == expectedNames

        def typeList = cc.getPropTypeList(collUrn)
        assert typeList[2] == "number" // Sequence property

        def labelList = cc.getPropLabelList(collUrn)
        assert labelList[2] == "Sequence"

    }
}
