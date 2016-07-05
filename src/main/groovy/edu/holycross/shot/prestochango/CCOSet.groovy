package edu.holycross.shot.prestochango

import edu.harvard.chs.cite.CiteUrn

/** A class representing the implementation of a set of CITE Collection Objects
 */
class CCOSet {


  /** CITE URN for this set */
  public CiteUrn urn = null

  /** CITE URN for this first and last elements of this set; may be the same */
  public CiteUrn startUrn = null
  public CiteUrn endUrn = null

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
		
			//ccos.sort( { objA, objB -> objA.getSequence() <=> objB.getSequence() } as Comparator)*.key
			// We order first on dynamic property and then name property.
			def orderBySeq = new OrderBy([{ it.getSequence() }])
			def sortedCcos = ccos.sort(orderBySeq)
 

			this.ccos = ccos	

			// get boundary urns for easy access
			this.startUrn = ccos[0].urn
			this.endUrn = ccos[-1].urn

			//Construct URN out of first and last cco
			String tempUrnString = this.startUrn.toString()
			if (this.startUrn.toString() != this.endUrn.toString() ) {
				System.err.println(this.endUrn.getObjectWithoutCollection())
				tempUrnString += "-${this.endUrn.getObjectWithoutCollection()}"
			}
			this.urn = new CiteUrn(tempUrnString)
			

		}

  /** Return the number of objects in this set.
   * @returns BigInteger
   */
  	  public BigInteger countObjects(){
			BigInteger howMany = this.ccos.size()
	  }
  
}
