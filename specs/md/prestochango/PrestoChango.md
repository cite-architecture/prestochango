# Specification of the `prestochango` library#

`prestochango` is a library for managing a CITE Collection archive.

`prestochango` helps with:


- Constructing and validating the <a concordion:run="concordion" href="corpus/Corpus.html">archival corpus</a>



The inventory provides schema information about the data set. The inventory does not have to configure every column in the source data file as a property in the CITE model: to exclude properties from prestochango processing, simply omit the property from the inventory's schema for that collection.


## Local files



file names indicate format: for comma-separated value files, names should end in .csv; for tab-delimited files, names should end in .tsv
the first line of the file contains property names; remaining lines contain data
to configure a property in the inventory, the name of the property must match the name in the first line of the tsv file.

