# Working with inventory data #



`prestochango` creates an object model of the information that is serialized in XML as a CITE Collections inventory file.
This includes information about extensions to CITE Collections recognized in this inventory, and about each inventoried Collection.


## Information about CITE Exentions ##



You can determine the number of Extensions, and for each Extension:

- its abbreviated name
- its full RDF verb


## Information about inventoried Collections ##

Every Collection belongs to a CITE namespace.    In CITE URNs, the namespace is identified with an abbreviated identifier.  For any namespace in the inventory, you can determine what its abbreviated identifier, and what full URI that corresponds to.

@openex@
### Examples ###

TBA

@closeex@

 You can determine the number of Collections in the repository, and for each Collection:

- the name and type of each its properties
- if a property has a constrained set of values, a list of allowed values
- the property for its canonical identifier (always a CITE URN)
- the property with its standard human-readable label (in RDF serializations, expressed with `rdf:label`)
- whether the Collection is ordered
- what type of data source it is stored in
- the name of the data source

@openex@

### Examples ###


Given the test data in <a href="../../../resources/test/data/archive1/testcapabilities.xml" concordion:set="#inv = setHref(#HREF)">this TextInventory file</a> (validated against 
<a href="../../../resources/test/data/schemas/CiteCollectionInventory.rng" concordion:set="#schema = setHref(#HREF)">this schema</a>), and a reference to <a href="." concordion:set="#archive = setHref(#HREF)">any directory</a>, we can determine that:

-  it inventories <strong concordion:assertEquals="shouldFindNumberOfCollections(#inv,#schema,#archive)">1</strong> Collection 
- the URN for that collection is <strong concordion:assertEquals="shouldFindOneUrn(#inv,#schema,#archive)">urn:cite:hmt:u4</strong>

In that inventory, collection <strong concordion:set="#urn">urn:cite:hmt:u4</strong> 

- is <strong concordion:assertTrue="isOrdered(#inv,#schema,#archive,#urn)">an ordered collection</strong>
- it is ordered by the <strong concordion:assertEquals="isOrderedBy(#inv,#schema,#archive,#urn)">Sequence</strong> property
- the property for its canonical identifier is named <strong concordion:assertEquals="findCanonicalProperty(#inv,#schema,#archive,#urn)">URN</strong>
- the property for its standard human-readable label is named <strong concordion:assertEquals="findLabelProperty(#inv,#schema,#archive,#urn)">Label</strong>

The complete list of its properties is:

<table concordion:verifyRows="#propname : shouldFindPropertyList(#inv,#schema,#archive,#urn)">
        <tr><th concordion:assertEquals="#propname">Property names</th></tr>
        <tr><td>CodexURN</td></tr>
        <tr><td>Label</td></tr>
                <tr><td>RV</td></tr>
                        <tr><td>Sequence</td></tr>
                                <tr><td>URN</td></tr>
    </table>


Their types are:
<table concordion:execute="#result = getTypeForName(#inv,#schema,#archive,#urn,#prop)">
<tr><th concordion:set="#prop">Name</th><th concordion:assertEquals="#result">Type</th></tr>
<tr><td>CodexURN</td><td>citeurn</td></tr>
<tr><td>Label</td><td>string</td></tr>
<tr><td>RV</td><td>string</td></tr>
<tr><td>Sequence</td><td>number</td></tr>
<tr><td>URN</td><td>citeurn</td></tr>

</table>


The <strong concordion:set="#rv">RV</strong> property has an authority list of allowed values.  They are


<table concordion:verifyRows="#val : findValueList(#inv,#schema,#archive,#urn,#rv)">
        <tr><th concordion:assertEquals="#val">Allowed values</th></tr>
<tr><td>recto</td></tr>
<tr><td>verso</td></tr>
</table>
@closeex@




## Information about indexed relations ##


When a property in a CITE Collection is a URN type, the inventory may optionally indicate the nature of the relationship between the cited object and the object in this collection.  The relationship is expressed as a pair of RDF verbs.


@openex@

### Examples ###

@closeex@

