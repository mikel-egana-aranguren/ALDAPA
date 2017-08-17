Implementations
===============

## Overview

ALDAPA comprises a core that executes pipelines, and plugins that implement the steps of such pipelines. The plugins should implement the interfaces defined at `es.eurohelp.lod.aldapa.modification`, `es.eurohelp.lod.aldapa.storage`, and `es.eurohelp.lod.aldapa.transformation`. When a plugin is created, it should be registered in the configuration files (and obviously be available in the classpath).

## Plugin types

### Storage

Anything that can store and query RDF.

### Transformation

A program that can convert data to RDF.

### Modification

A program that is able to modify the RDF stored.

---
[Back to index](index.md)