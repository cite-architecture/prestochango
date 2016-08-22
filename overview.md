---
layout: page
title: Overview of the prestochango library
---


The main objects that a programmer might directly manipulate are:

- the `CollectionArchive`
- individual `CiteCollection` objects
- individual `CiteProperty` objects



Conceptually, the `CollectionArchive` manages a group of CITE Collections.  In the 2.x series of `prestochango`, Collections are managed using XML inventories documenting collections and their metadata, and delimited text files with the Collection's data.  You therefore construct an archive with a combination of an XML inventory file, and a base directory where delimited text files are stored.

When the `CollectionArchive` is created, it reads the inventory and constructs three maps.  Each map has an entry for each collection in the archive, and is keyed by the collection's URN value (as a String).  The three maps are:

- `collections`.  A map of `CiteCollection` objects
- `dataSources`.  A map of `CiteDataSource` objects.  (`CiteDataSource` is an interface; in version 2.x, the only implementation of this interface is the `LocalFileSource` class)
- `dcMeta`.  A map of property-value maps.

The most complex object in the library is the `CiteCollection` object. It contains all the information about a Collection's structure, but no actual data records.  This information includes a list of `CiteProperty` objects.  All `CiteProperty` objects have a property name, a label, and a `CitePropertyType`.  They may, optionally, have further information:

- a single ("universal") value to apply to all instances of the property
- a controlled vocabulary list for properties of type `CitePropertyType.STRING`
- a pair of RDF verbs defining the relation of a URN-type property to the object it belongs to

The `CiteDataSource` interface defines a method that returns an array of records for a given collection (e.g., by reading a delimited text file, for `LocalFileSource` objects).

The property-value maps in `dcMeta` are simple mappings of Dublin Core element names to vaues.  The three values required in the inventory schema currently used with `prestochango` are `title`, `description` and `rights`.
