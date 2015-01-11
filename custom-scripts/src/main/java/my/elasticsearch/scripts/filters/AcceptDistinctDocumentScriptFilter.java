/********************************************************************
 * File Name:    AcceptDistinctDocumentScriptFilter.java
 *
 * Date Created: Jan 8, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.scripts.filters;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.slf4j.Slf4jESLoggerFactory;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.search.lookup.DocLookup;
  
public final class AcceptDistinctDocumentScriptFilter extends AbstractSearchScriptFilter
{
  @SuppressWarnings("unchecked")
  protected AcceptDistinctDocumentScriptFilter(final Map<String, Object> parameters)
  {
    super(parameters);
    
    this.primaryKeys = (List<String>) parameters.get(PRIMARY_KEYS_PARAMETER);
    this.primaryKeys.add("_type"); // Adding type internally to the key as we can query multiple types at the same time.
    
    LOGGER.error("### primaryKeys = {}", this.primaryKeys); // TODO: Ajey - Change log level
  }

  @Override
  protected boolean evaluate()
  {
    try
    {
      final String key = generateKey();

      final boolean documentAddedForFirstTime = distinctDocuments.add(key);
      
      if(!documentAddedForFirstTime)
      {
        // TODO: Ajey - Change log level
        LOGGER.error("###: Ignored duplicate document with key [{}]", key);
      }
      
      return documentAddedForFirstTime;
    }
    catch(RuntimeException e)
    {
      LOGGER.error("An error occurred while determining duplicate document", e);
      throw e;
    }
  }
  
  // Private methods
  private String generateKey()
  {
    final DocLookup document = doc(); // doc will return the index values. Assuming that for uniqueness the properties used do not have multiple terms and are in required case e.g. lower case already. 

    final StringBuilder key = new StringBuilder();    
    for (String property : this.primaryKeys)
    {
      if(! document.containsKey(property) )
      {
        key.append(SEPARATOR); // Append the separator even if we don't have a value
        continue;
      }
      
      final ScriptDocValues scriptValues = (ScriptDocValues) document.get(property);
      if(scriptValues != null && !scriptValues.isEmpty())
      {
        for (Object value : scriptValues.getValues())
        {
          key.append(SEPARATOR); 
          key.append(value);
        }
      }
    }    
    
    return key.toString();
  }

  // Private members
  private final Set<String> distinctDocuments = new HashSet<>(1024); 
  private final List<String> primaryKeys;
  
  private final static String SEPARATOR = "#";
  private final static String PRIMARY_KEYS_PARAMETER = "primaryKeys";
  private final static ESLogger LOGGER = Slf4jESLoggerFactory.getLogger("AcceptDistinctDocumentScriptFilter");  
}

