/********************************************************************
 * File Name:    AcceptDistinctDocumentScriptFilterFactory.java
 *
 * Date Created: Jan 8, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.scripts.filters;

import java.util.Map;

import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
  
public final class AcceptDistinctDocumentScriptFilterFactory implements NativeScriptFactory
{
  public ExecutableScript newScript(final Map<String, Object> parameters)
  {
    return new AcceptDistinctDocumentScriptFilter(parameters);
  }
}

