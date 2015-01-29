/********************************************************************
 * File Name:    DistinctDocumentsSortScriptFactory.java
 *
 * Date Created: Jan 25, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments.search;

import java.util.Map;

import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
  
public class DistinctDocumentsSortScriptFactory implements NativeScriptFactory
{
  @Override
  public ExecutableScript newScript(Map<String, Object> parameters)
  {
    return new DistinctDocumentsSortScript(parameters);
  }
}

