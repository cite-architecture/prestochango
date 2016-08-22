package edu.holycross.shot.prestochango


class RdfVerb  {


  String full = null
  String abbr = null
  String inverseFull = null
  String inverseAbbr = null

  RdfVerb( String abbreviated,String fullUri) {
    this.abbr = abbreviated
    this.full = fullUri
  }


  RdfVerb( String abbreviated,String fullUri, String inverseAbbr, String inverseFullUri) {
    this.abbr = abbreviated
    this.full = fullUri
    this.inverseAbbr = inverseAbbr
    this.inverseFull = inverseFullUri
  }

}
