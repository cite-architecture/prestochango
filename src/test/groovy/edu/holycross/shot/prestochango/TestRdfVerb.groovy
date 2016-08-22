package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestRdfVerb extends GroovyTestCase {




	@Test void testConstructor1() {
    RdfVerb rdf = new RdfVerb
    ( "dse:illustratedBy",
    "http://www.homermultitext.org/dse/rdf/illustratedBy"
    )
    assert rdf.abbr == "dse:illustratedBy"
		assert rdf.full == "http://www.homermultitext.org/dse/rdf/illustratedBy"

	}

  	@Test void testConstructor2() {
      RdfVerb rdf = new RdfVerb( "dse:illustratedBy","http://www.homermultitext.org/dse/rdf/illustratedBy/illustratedBy","dse:illustrates","http://www.homermultitext.org/dse/rdf/illustratedBy/illustrates")
      assert rdf.abbr == "dse:illustratedBy"
  		assert rdf.inverseAbbr == "dse:illustrates"

  	}

}
