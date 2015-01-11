Steps to use the custom script:
- Copy the generated jar custom-scripts-0.0.1-SNAPSHOT to elasticsearch directory e.g. /usr/share/elasticsearch/lib
- Add following line to the elasticsearch.yml
	
	# custom script filter
	script.native.AcceptDistinctDocumentScriptFilter.type: my.elasticsearch.scripts.filters.AcceptDistinctDocumentScriptFilterFactory
	
- Add this script filter to the search and pass the primary keys as input e.g. assuming the type is myindex and we need to search for file hash with
  primary keys as file_name & file_folder (file_id is already in filter) 

	GET myindex/files_in_environment/_search
	{
	  "size" : 5,
	  "query" : {
	    "filtered" : {
	      "query" : {
	        "match_all" : { }
	      },
	      "filter" : {
	        "and" : {
	          "filters" : [ {
	            "term" : {
	              "file_hash" : "some_file_hash_value_to_search"
	            }
	          }, {
	            "script" : {
	              "script" : "AcceptDistinctDocumentScriptFilter",
	              "params" : {
	                "primaryKeys" : ["file_name", "file_folder"]
	              },
	              "lang" : "native"
	            }
	          } ]
	        }
	      }
	    }
	  }
	}
