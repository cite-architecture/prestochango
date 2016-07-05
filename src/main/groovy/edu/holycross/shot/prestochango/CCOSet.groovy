package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

/** A class representing the implementation of a set of CITE Collection Objects
 */
class CCOSet {


  /** CITE URN for this set */
  public CiteUrn urn = null

  /** CITE Collection to which this object belongs **/
  public CiteCollection collection = null

  /** ArrayList of one or more CiteCollectionObjects 
	* One object for uordered collection, more than one for ranges in an ordered collection
  **/
  public ArrayList ccos = []

  /** Constructor for CCOS, a CiteCollectionObjectSet
   * @param CiteCollection collection A CiteCollection
   * @param ArrayList ccos an arrayList of CiteCollectionObjects
   */
    CCOSet(
		CiteCollection collection,
		ArrayList ccos ) throws Exception {


			// Test that this is an ordered collection
			if (collection.isOrderedCollection){
				this.collection = collection
			} else {
				throw new Exception("CiteCollectionObjectSet: Collection ${collection.urn} is not an ordered collection.")
			}

			//Test ccos for unique URNs
			def testUrns = ccos.collect { it.urn.toString() } as Set 
			if (testUrns.size() != ccos.size()) {
				throw new Exception("CiteCollectionObjectSet: Set of Cite Collection Objects must have unique URNs.")
			}

			//Test ccos for unique sequence numbers
			def seqList = []
			ccos.each { cco ->
						seqList << cco.getSequence()
			}
			def uniquedSeqList = seqList as Set

			if (uniquedSeqList.size() != ccos.size()) {
				throw new Exception("CiteCollectionObjectSet: Set of Cite Collection Objects must have unique sequenceNumbers.")
			}
		
			ccos.sort( { objA, objB -> objA.getSequence() <=> objB.getSequence() } as Comparator)*.key

			this.ccos = ccos	

		}
  
}