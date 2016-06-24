package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing prestochange CiteCollection class.
*/
class TestCsv extends GroovyTestCase {
    String schemaFileName = "schemas/CiteCollectionInventory.rng"

    String testInventory = "testdata/image-collection.xml"
    File inv = new File(testInventory)

    String dataDir = "testdata/csvs"
    File dir = new File(dataDir)




    @Test void testInvContents() {
        File outputDir = new File("testdata/testoutput")
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }

        File testOut = new File(outputDir, "image-collection-out.ttl")
        testOut.text = ""


        CollectionArchive cc = new CollectionArchive(inv, schemaFileName, dir)
        cc.ttl(testOut)
        System.err.println "TTL in ${testOut}"
    }

}
