https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html


format: DEFAULT, EXCEL, MYSQL, RFC4180, TDF

charset

Not all the values are allowed, and only a few of them can be configured 

Not allowed
withAllowMissingColumnNames(): Missing column names are not allowed
withIgnoreEmptyLines(): empty lines are not allowed

Compulsory:
withFirstRecordAsHeader() the CSV must include headers in the first record
withIgnoreHeaderCase(): ignores case and URLifies column name

Configurable (optional):
withDelimiter(char delimiter)
withEscape(Character escape)
withQuote(char quoteChar)
withRecordSeparator(char recordSeparator)

http://graphdb.ontotext.com/free/loading-data-using-ontorefine.html#viewing-tabular-data-as-rdf