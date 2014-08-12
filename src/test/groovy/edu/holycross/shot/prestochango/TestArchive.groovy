package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestArchive extends GroovyTestCase {

    String schemaFileName = "schemas/CiteCollectionInventory.rng"

    String testInventory = "testdata/testcapabilities.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/tsvs"
    File dir = new File(dataDir)

    File testOut = new File("testdata/testoutput")

    /** Tests parameter requirements for constructor
    * with valid CTS TextInventory and readable text archive.
    */
    @Test void testConstructor() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)
        assert cc

        File fake = new File("fake-inv-file")

        shouldFail {
            CollectionArchive failedCollection = new CollectionArchive(fake, schemaFileName, dir)
        }

        shouldFail {
            CollectionArchive failedCollection = new CollectionArchive(inv, schemaFileName, fake)
        }

    }

    @Test void testListColls() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)
        def expectedList = ["urn:cite:hmt:u4"]
        assert cc.getCollectionList() == expectedList
    }



    @Test void testObjModel() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)
        def conf  = cc.configureFromFile()
        // 1 collection configured:
        assert conf.keySet().size() == 1
    }
    

    @Test void testPropertyRetrieval() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)
        assert cc

//        CiteUrn tstId = new CiteUrn("urn:cite:hmt:u4.13r")
        CiteUrn tstId = new CiteUrn("urn:cite:hmt:u4")
        String expectedName = "URN"
        assert cc.getCanonicalIdProperty(tstId) == expectedName

        assert cc.isOrdered(tstId) == true
        assert cc.isGrouped(tstId) == false

    }

    @Test void testPropValList() {
        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)
        assert cc

        CiteUrn collUrn = new CiteUrn("urn:cite:hmt:u4")
        String propName = "RV"
        def expectedList = ['recto', 'verso']


        def valList = cc.getValueList(collUrn, propName)
        assert valList.size() == 2
        assert valList == expectedList

    }

}
