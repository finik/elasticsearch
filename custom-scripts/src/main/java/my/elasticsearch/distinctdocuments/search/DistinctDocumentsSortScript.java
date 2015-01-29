/********************************************************************
 * File Name:    DistinctDocumentsSortScript.java
 *
 * Date Created: Jan 25, 2015
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

import org.elasticsearch.script.AbstractSearchScript;
  
public class DistinctDocumentsSortScript extends AbstractSearchScript
{
  @SuppressWarnings("unchecked")
  public DistinctDocumentsSortScript(final Map<String, Object> parameters)
  {
    this.distinctDocumentsSentry = new DistinctDocumentsSentry((List<String>) parameters.get(PRIMARY_KEYS_PARAMETER));
  }
  
  @Override
  public Object run()
  {
    return this.distinctDocumentsSentry.generateKey(doc());
  }
  
  private final DistinctDocumentsSentry distinctDocumentsSentry;
  
  private final static String PRIMARY_KEYS_PARAMETER = "primaryKeys";
  //private final static ESLogger LOGGER = Slf4jESLoggerFactory.getLogger("DistinctDocumentsSortScript");  
}

