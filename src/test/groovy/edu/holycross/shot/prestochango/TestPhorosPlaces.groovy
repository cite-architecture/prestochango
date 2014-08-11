package edu.holycross.shot.prestochango

import static org.junit.Assert.*
import org.junit.Test



/**
*/
class TestPhorosPlaces extends GroovyTestCase {


  File inv = new File( "/Users/nsmith/repos/git/phoros/collections/placeinv.xml")
  String schemaUrl = "http://www.homermultitext.org/hmtschemas/collections/CiteCollectionService.rng"
  File baseDir = new File( "/Users/nsmith/repos/git/phoros/collections")

  File data = new File( baseDir, "places.csv")

  @Test void testInterface() {
    CollectionArchive ca = new CollectionArchive(inv, schemaUrl, baseDir)
    println    ca.turtlizeCsv(data, "urn:cite:phoros:places")
  }

}
