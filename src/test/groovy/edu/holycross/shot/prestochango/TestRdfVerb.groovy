package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

import static org.junit.Assert.*
import org.junit.Test




/** Class testing output of ttl from prestochango's CollectionArchive class.
 */
class TestRdfVerb extends GroovyTestCase {


	// Ordered colections MUST have a sequence value
	@Test void testConstructor1() {
    RdfVerb rdf = new RdfVerb("http://www.homermultitext.org/cite/rdf/citeimage", "citeimg")
    assert rdf.abbr == "citeimg"
		assert rdf.uri == "http://www.homermultitext.org/cite/rdf/citeimage"
	}


}
