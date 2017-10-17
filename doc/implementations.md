Implementations
===============

## Overview

ALDAPA comprises a core that executes pipelines, and plugins that implement the steps of such pipelines. The plugins should implement the interfaces, and extend the abstract classes, defined at `es.eurohelp.lod.aldapa.modification`, `es.eurohelp.lod.aldapa.storage`, and `es.eurohelp.lod.aldapa.transformation` (The interfaces describe what the plugins should do, and the abstract classes how they should be created). To deploy a plugin, it should be registered in the configuration files (and obviously be available in the classpath).

## Plugin types

### Storage

Anything that can store and query RDF.

### Transformation

A program that can convert data to RDF.

### Modification

A program that is able to modify the RDF stored.

---
[Back to index](index.md)