# The CITE Collection archive #


An CITE Collection archive corpus is made up of a set of delimited text files, and an inventory documenting the structure and citation form of each collection.

@openex@
### Example ###


We can validate <a href="../../../resources/test/data/archive1/testcapabilities.xml" concordion:set="#inv = setHref(#HREF)">this TextInventory file</a> against 
<a href="../../../resources/test/data/schemas/CiteCollectionInventory.rng" concordion:set="#schema = setHref(#HREF)">this schema</a>, and use the
files in <a href="../../../resources/test/data/archive1/tsvs" concordion:set="#archive = setHref(#HREF)">this root directory</a> to <strong concordion:assertTrue="shouldMakeArchive(#inv,#schema,#archive)">construct a CITE Collection archive</strong>.

@closeex@

