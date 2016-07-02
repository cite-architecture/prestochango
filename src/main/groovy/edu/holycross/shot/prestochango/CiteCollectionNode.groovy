package edu.holycross.shot.prestochango

import groovy.json.*
import edu.harvard.chs.cite.CiteUrn


/** Class representing a CollectionNode (one or more collection objects) in the Cite collection model.
 */
class CiteCollectionNode {


  /** Machine-actionable URN for the node. */
  CiteUrn nodeUrn
  /** Human-readable label for the node*/
  String nodeLabel


  /** Human-readable label for bibliographic components **/
  String groupLabel
  String workLabel
  String versionLabel
  String exemplarLabel

  
  /** Possibly null URN for preceding leaf node. */
  CiteUrn prevUrn = null
  /** Possibly null URN for following leaf node. */
  CiteUrn nextUrn = null
  /** Ordered list of collObject objects. */
  /** N.b. a collObject is a map **/
  ArrayList collObjects
  
  
  /** Constructor for an OHCO2 citable node requiring all member properties. 
   * @param urn Urn for the node.
   * @param label Human-readable label.
   * @param prev URN for preceding node, or null if no preceding node.
   * @param next URN for following node, or null if no following node.
   * @param collObjects Non-null arraylist of collObject objects.
   * @throws Exception if urn, label or txt is empty; or if a non-null
   * value for prev or next is not a valid URN.
   */
  CiteCollectionNode(CiteUrn urn, String label, CiteUrn prev, CiteUrn next, ArrayList collObjects)
  throws Exception {
    if (urn == null) {
      throw new Exception("CiteCollectionNode: URN for node cannot be null.")
    } else {
      this.nodeUrn = urn
    }
    if ((label == null) || (label.size() < 1)) {
      throw new Exception("CiteCollectionNode: text content of node cannot be null.")
    } else {
      this.nodeLabel = label
    }

    
    if ((collObjects == null) || (collObjects.size() < 1)) {
      throw new Exception("CiteCollectionNode: data-contents of node cannot be null.")
    } else {
      this.collObjects = collObjects 
    }
    
    this.prevUrn = prev
    this.nextUrn = next

  }


  /**
   * Overrides default toString() method.
   * @returns Description, URN and text content of this node.
   */
  String toString() {
	String tempString = "" 
	leafNodes.each{ 
		tempString += "${it}\r"
	}

    return "${nodeLabel} (${nodeUrn}): ${tempString}"
  }


	/**
	* Outputs a pretty JSON representation of the Ohco2Node
	* @returns String.
	*/
  String toJson() {
		return new JsonBuilder(this).toPrettyString()
  }

  String toXml() {
    return "<cite:node xmlns:cts='http://chs.harvard.edu/xmlns/cite' urn='" + this.nodeUrn +  "'>${this.textContent}</cite:node>"
  }

 
}
