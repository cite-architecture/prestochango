# The CITE Collection archive #


A CITE Collection archive corpus is made up of a set of delimited text files, and an inventory documenting the structure and citation form of each collection.

It is an error if the inventory file, schema file or directory of data files does not exist.


@openex@
### Example: successfully creating a repository ###



We can validate <a href="../../../resources/test/data/archive1/testcapabilities.xml" concordion:set="#inv = setHref(#HREF)">this TextInventory file</a> against 
<a href="../../../resources/test/data/schemas/CiteCollectionInventory.rng" concordion:set="#schema = setHref(#HREF)">this schema</a>, and use the
files in <a href="../../../resources/test/data/archive1/tsvs" concordion:set="#archive = setHref(#HREF)">this root directory</a> to <strong concordion:assertTrue="shouldMakeArchive(#inv,#schema,#archive)">construct a CITE Collection archive</strong>.

@closeex@


@openex@


### Examples: failures ###

`prestochango` fails  to construct a CITE Collection archive if we use <strong concordion:set="#bogusvalue">invalid references</strong>, for

- <strong concordion:assertFalse="shouldMakeArchive(#bogusvalue,#schema,#archive)">the inventory file</strong>
- <strong concordion:assertFalse="shouldMakeArchive(#inv,#schema,#bogusarchive)">the directory of archival files</strong>
- <strong concordion:assertFalse="shouldMakeArchive(#inv,#bogusvalue,#archive)">the inventory schema</strong>


@closeex@

