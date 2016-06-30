package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestPrefix extends GroovyTestCase {

  String schemaFileName = "schemas/CiteCollectionInventory.rng"

    @Test void testPrefixing() {

   String prefixString = """
@prefix cite:        <http://www.homermultitext.org/cite/rdf/> .
@prefix citedata:        <http://www.homermultitext.org/citedata/> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. 
@prefix  xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix olo:     <http://purl.org/ontology/olo/core#> .
@prefix dse:  <http://www.homermultitext.org/dse/rdf/> .
""".toString()

        String testTsvInventory = "testdata/collections.xml"
        File tsvInv = new File(testTsvInventory)

        String tsvDataDir = "testdata/csvs"
        File tsvDir = new File(tsvDataDir)


        CollectionArchive cc = new CollectionArchive(tsvInv, schemaFileName, tsvDir)

        File testOut = new File("testdata/testoutput/defaultNoPrefix.ttl")
        cc.ttl(testOut)
        
        File testOut2 = new File("testdata/testoutput/withPrefix.ttl")
        cc.ttl(testOut2, true)

        System.err.println "TTL in ${testOut} and ${testOut2}"

		assert prefixString.replaceAll(/\s/,"") == "NEED TO CHECK THIS PROGRAMMATICALY."
    }


}
