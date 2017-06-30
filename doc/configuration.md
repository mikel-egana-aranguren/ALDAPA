Configuration
=============

## File structure

File structure shoudl remain:

'''
ALDAPA_CONFIG_FILE: configuration/aldapa-default-configuration.yml
TRIPLE_STORE_CONFIG_FILE: configuration/triple-store-default-configuration.yml
#CONVERTER_CONFIG_FILE
#LINK_DISCOVERY_CONFIG_FILE
#QUALITY_CONFIG_FILE 
#PUBLISHER_CONFIG_FILE
#URI_ENGINE_CONFIG_FILE  
'''

'''
# THIS CONFIGURATION
name: default
comment: A configuration suitable for most cases
author: Mikel Egana Aranguren

# ALDAPA URIs
INTERNAL_BASE: "urn:aldapa:" # (Quoted due to colons)
PROJECT: ${INTERNAL_BASE}:project:+PROJECT_NAME+

# DOMAIN URIs
BASE: http://opendata.eurohelp.es/

# DOMAIN METADATA URIs
DCAT_CATALOG: ${BASE}catalog/+CATALOG_NAME+
DCAT_DATASET: ${BASE}dataset/+DATASET_NAME+
DCAT_DISTRIBUTION: ${BASE}distribution/+DISTRIBUTION_NAME+

# DOMAIN DATA URIs
RESOURCE: ${BASE}resource/+RESOURCE_NAME+
PROPERTY: ${BASE}property/+PROPERTY_NAME+
'''

'''
# THIS CONFIGURATION
name: InMemoryRDF4JForTests
comment: in memory triple store that will be dumped when the program finishes
author: Mikel Egana Aranguren

# Plugin with actual code for accesing the Triple Store
pluginClassName: es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore
    
    
#- url: http://localhost:9999
#- user: admin 
#- password: admin
'''