package edu.holycross.shot.prestochango

/** A class enumerating possible types of
* a CITE Property.
*/
public enum CitePropertyType {


  STRING ("string value"),
  NUM ("numeric value"),
  BOOLEAN ("boolean value"),
  MARKDOWN ("markdown string"),
  CITE_URN ("CITE Object URN"),
  CTS_URN ("CTS URN")

  private String label

  private CitePropertyType(String label) {
    this.label = label
  }

  /** Gets a human-readable label for this value. */
  public String getLabel() {
    return label
  }
}
