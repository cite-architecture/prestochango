package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestLocalFileImpl extends GroovyTestCase {


 @Test void testCsvFile() {
   File shortFile = new File("testdata/csvs/just-one.csv")
   LocalFileImplementation lfi = new LocalFileImplementation(shortFile)
   def records = lfi.getRecordArray()
   assert records.size() == 2
   def headers = ["Image", "Label", "Rights", "OtherUrn", "testBoolean", "Sequence"]
   assert records[0] == headers
 }

 @Test void testTabFile() {
   File shortFile = new File("testdata/tsvs/just-one.tsv")
   LocalFileImplementation lfi = new LocalFileImplementation(shortFile)
   def records = lfi.getRecordArray()
   assert records.size() == 2
   def headers = ["Image", "Label", "Rights", "OtherUrn", "testBoolean", "Sequence"]
   assert records[0] == headers
 }


}
