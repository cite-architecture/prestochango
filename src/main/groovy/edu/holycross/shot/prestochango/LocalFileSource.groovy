package edu.holycross.shot.prestochango

import edu.holycross.shot.safecsv.SafeCsvReader
import org.apache.commons.io.FilenameUtils

class LocalFileSource implements CiteDataSource {

    File delimitedFile
    CiteDataSourceType implType

    String fileExtension

    LocalFileSource(File f) {
      delimitedFile = f
      implType = CiteDataSourceType.LOCAL_FILE
    }

    /** Reads delimited file and returns an array of records,
    * including initial row of strings with identifying property labels.
    */
    ArrayList getRecordArray() {
      switch (FilenameUtils.getExtension(delimitedFile.toString())) {
      case "csv":
      return getCsvAsArray()
      break

      case "tsv":
      return getTabAsArray()
      break

      default:
      throw new Exception("Unsupported filename extension in " + delimitedString)
      break
      }

   }

   ArrayList getCsvAsArray() {
     SafeCsvReader srcReader = new SafeCsvReader(delimitedFile)
     return srcReader.readAll()
   }

  ArrayList getTabAsArray() {
    def records = []
    delimitedFile.eachLine {
      records.add(it.split(/\t/))
    }
    return records
   }


   String toString() {
     return "Collection implemented in local file " + delimitedFile
   }
}
