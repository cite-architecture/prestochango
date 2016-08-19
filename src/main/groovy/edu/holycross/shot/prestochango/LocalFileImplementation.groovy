package edu.holycross.shot.prestochango

import edu.holycross.shot.safecsv.SafeCsvReader

class LocalFileImplementation implements CollectionImplementation {

    File delimitedFile
    ImplementationType implType


    LocalFileImplementation(File f) {
      delimitedFile = f
      implType = ImplementationType.LOCAL_FILE
    }

    ArrayList getRecordArray(boolean asUnicode) {
      def records = []
      delimitedFile.eachLine {

      }
      return delimitedFile.getText()
   }


   String toString() {
     return "Collection implemented in local file " + delimitedFile
   }
}
