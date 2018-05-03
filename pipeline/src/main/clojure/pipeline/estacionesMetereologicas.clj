(ns pipeline.estacionesMetereologicas
    (:require [grafter.tabular :refer [_ add-column add-columns apply-columns
                              build-lookup-table column-names columns
                              derive-column drop-rows graph-fn grep make-dataset
                              mapc melt move-first-row-to-header read-dataset
                              read-datasets rows swap swap take-rows
                              test-dataset test-dataset ]]
            [grafter.pipeline :refer [declare-pipeline]]
            [grafter.rdf :as rdf]
            [grafter.rdf.io :as io]
           ; [grafter.rdf :refer [xsd:dateTime]]
            [grafter.vocabularies.qb :refer :all]
            [grafter.rdf.protocols :refer [->Quad]]
            [grafter.rdf.protocols :refer [ITripleWriteable]]
            [grafter.rdf.templater :refer [graph]]
            [grafter.rdf.formats :refer [rdf-nquads rdf-turtle]]
            [grafter.vocabularies.qb :refer :all]
            [grafter.pipeline :refer [declare-pipeline]]
            [grafter.vocabularies.rdf :refer :all]
            [pipeline.transform :refer :all]       
            [pipeline.prefix :refer :all]
            [pipeline.prefix :refer :all]
            [clojure.string :as str]
              )
     )

	(def make-graph
 (graph-fn [{:keys [
   location Name Description Province Town Address
   CoordenatesX CoordenatesY Latitude Longitude
    ]
             :as row }]
           ;Nombre de la 
           (graph (base-graph "AirQuality") 
             
               [location
                 [rdf:a "http://schema.org/Place"]
                 ["http://www.w3.org/2003/01/geo/wgs84_pos#long" (row "Longitude")]
                 ["http://www.w3.org/2003/01/geo/wgs84_pos#lat"  (row "Latitude")]
                 ["http://schema.org/address" (langSp (str (row "Address")))] 
                 ["http://schema.org/addressLocality" (langSp (str (row "Town"))) ]
                 ["http://schema.org/addressRegion" (langSp (str (row "Province"))) ]
                 ;["http://schema.org/description" (langSp (str (row "Description"))) ]
                ]
               
             ))) 


(defn convert-data
  [data-file]
  (-> (read-dataset data-file)
    ;Creamos el dataset de los datos a cargar
  (make-dataset ["Name" "Description" "Province" "Town" "Address"
                "CoordenatesX" "CoordenatesY" "Latitude" "Longitude"])
    ;Borra la primera fila correspondiente a los nombres de las columnas
  (drop-rows 1)
   (mapc {
   "Name" removeSymbols
   "CoordenatesX" parseValue
   "CoordenatesY" parseValue
   "Latitude"  parseValue
   "Longitude" parseValue
   "Address" removeSymbols
   "Province" removeSymbols
   "Town" removeSymbols

          })
    (derive-column :location [:Name] base-Location)   
      ))

(defn convert-data-to-graph
  [dataset]
  (-> dataset convert-data make-graph missing-data-filter))


(declare-pipeline convert-data-to-graph [Dataset -> (Seq Statement)]
                  {dataset "The data file to convert into a graph."})

;Convierte una IStatement en una statement Sesame
(defn convertidor [is]
  (map io/IStatement->sesame-statement (convert-data-to-graph is)))