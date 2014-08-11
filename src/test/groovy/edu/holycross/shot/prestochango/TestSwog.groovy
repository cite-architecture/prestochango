package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestSwog extends GroovyTestCase {


   String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"

    @Test void testTsv() {

        String toughCollection = "urn:cite:swog:autolycus"

        String testTsvInventory = "/Users/nsmith/repos/bitbucket/swog/collections/swogcollections.xml"
        File tsvInv = new File(testTsvInventory)
        String tsvDataDir ="/Users/nsmith/repos/bitbucket/swog/collections"
        File tsvDir = new File(tsvDataDir)

        CollectionArchive cc = new CollectionArchive(tsvInv, schemaUrl, tsvDir)

        def conf = cc.citeConfig[toughCollection]
        System.err.println "Properties are " + conf['properties']

        File testOut = new File("testdata/testoutput/swogOut.ttl")
        cc.ttl(testOut)

        //System.err.println "TTL in ${testOut}"
    }

}
