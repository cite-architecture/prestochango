
# `prestochango`:  a library for managing a CITE Collection archive #


## CITE Collection archive
A CiteCollection archive is composed of

- a Collection inventory
- a Collection archive with stored stored in
    -  local files (`.tsv` or `.csv` formats)
    -  Google fusion tables

The inventory provides schema information about the data set.  The inventory does not have to configure every property given in the data set:  to exclude properties from `prestochango` processing, simply omit the property from the inventory's schema for that collection.


## Local files ##

Current assumptions:

-  file names indicate format:  for comma-separated value files, names should end in `.csv`;  for tab-delimited files, names should end in `.tsv`
- the first line of the file contains property names;  remaining lines contain data
- to configure a property in the inventory, the name of the property must match the name in the first line of the `tsv` file.  

## Google fusion tables ##

[SUPPORT NOT YET IMPLEMENTED]

