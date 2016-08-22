package edu.holycross.shot.prestochango

/** A class enumerating possible implementations of
* a CITE Collection's data.
*/
public enum CiteDataSourceType {

  LOCAL_FILE ("locally stored delimited text file"),
  SQL_DB("SQL database")

  private String label

  private CiteDataSourceType(String label) {
    this.label = label
  }

  /** Gets a human-readable label for this value. */
  public String getLabel() {
    return label
  }
}
