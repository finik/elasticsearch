/********************************************************************
 * File Name:    DistinctDocumentsSentry.java
 *
 * Date Created: Jan 25, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.slf4j.Slf4jESLoggerFactory;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.search.lookup.DocLookup;
  
public class DistinctDocumentsSentry
{
  public DistinctDocumentsSentry(final List<String> primarytKeys)
  {
    this.primaryKeys = new ArrayList<String>(primarytKeys);
    //this.primaryKeys.add("_type"); // internally add _type if single query spans multiple types. Can be avoided if we do not want to treat records from different types as distinct if primary keys are matched.
    
    LOGGER.info("primaryKeys {}", this.primaryKeys);
  }
  
  public boolean isDistinct(final DocLookup docLookup)
  {
    final String key = generateKey(docLookup);

    final boolean isDistinct = distinctDocumentsSeen.add(key);
    
    if( ! isDistinct && LOGGER.isInfoEnabled() )
    {
      LOGGER.info("Duplicate document seen [{}]", key);
    }
    
    return isDistinct;
  }
  
  public String generateKey(final DocLookup document)
  {    
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
          key.append(SEPARATOR); // We will always have key starting with a separator which is fine than testing for first value to avoid separator 
          key.append(value); // TODO: Ajey - test for numeric, date and types other than string.
        }
      }
    }    
    
    return key.toString();
  }
  
  private final Set<String> distinctDocumentsSeen = new HashSet<>(1024); 
  private final List<String> primaryKeys;
  
  private final static String SEPARATOR = "#";
  private final static ESLogger LOGGER = Slf4jESLoggerFactory.getLogger("DistinctDocumentsSentry");  
}

