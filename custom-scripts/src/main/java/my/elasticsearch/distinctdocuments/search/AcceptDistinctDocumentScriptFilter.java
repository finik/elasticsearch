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

package my.elasticsearch.distinctdocuments.search;

import java.util.List;
import java.util.Map;
import my.elasticsearch.distinctdocuments.DistinctDocumentsSentry;
import my.elasticsearch.scripts.filters.AbstractSearchScriptFilter;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.slf4j.Slf4jESLoggerFactory;
  
public final class AcceptDistinctDocumentScriptFilter extends AbstractSearchScriptFilter
{
  @SuppressWarnings("unchecked")
  protected AcceptDistinctDocumentScriptFilter(final Map<String, Object> parameters)
  {
    super(parameters);
    
    this.distinctDocumentsSentry = new DistinctDocumentsSentry((List<String>) parameters.get(PRIMARY_KEYS_PARAMETER));
  }

  @Override
  protected boolean evaluate()
  {
    try
    {
      return this.distinctDocumentsSentry.isDistinct(doc());
    }
    catch(RuntimeException e)
    {
      LOGGER.error("An error occurred while determining duplicate document", e);
      throw e;
    }
  }
  
  // Private members
  private final DistinctDocumentsSentry distinctDocumentsSentry;
  private final static String PRIMARY_KEYS_PARAMETER = "primaryKeys";
  private final static ESLogger LOGGER = Slf4jESLoggerFactory.getLogger("AcceptDistinctDocumentScriptFilter");  
}

