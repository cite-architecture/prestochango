package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
*/
class TestLocalDataSource extends GroovyTestCase {


 @Test void testCsvFile() {
   File shortFile = new File("testdata/csvs/just-one.csv")
   LocalFileSource lfs = new LocalFileSource(shortFile)
   def records = lfs.getRecordArray()
   assert records.size() == 2
   def headers = ["Image", "Label", "Rights", "OtherUrn", "testBoolean", "Sequence"]
   assert records[0] == headers
 }

 @Test void testTabFile() {
   File shortFile = new File("testdata/tsvs/just-one.tsv")
   LocalFileSource lfs = new LocalFileSource(shortFile)
   def records = lfs.getRecordArray()
   assert records.size() == 2
   def headers = ["Image", "Label", "Rights", "OtherUrn", "testBoolean", "Sequence"]
   assert records[0] == headers
 }

 @Test void testBadFilenameExtension() {
   File shortFile = new File("testdata/tsvs/just-one.txt")
	 assert shouldFail{
	   LocalFileSource lfs = new LocalFileSource(shortFile)
	   def records = lfs.getRecordArray()
	   assert records.size() == 2
	   def headers = ["Image", "Label", "Rights", "OtherUrn", "testBoolean", "Sequence"]
	   assert records[0] == headers
	 }
 }


}
