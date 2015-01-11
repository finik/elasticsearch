/********************************************************************
 * File Name:    AbstractSearchScriptFilter.java
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

import org.elasticsearch.script.AbstractSearchScript;
  
public abstract class AbstractSearchScriptFilter extends AbstractSearchScript
{
  protected AbstractSearchScriptFilter(final Map<String, Object> parameters)
  {
    this.parameters = parameters;
  }
  
  @Override
  public Object run()
  {
    return evaluate();
  }
  
  // Protected methods
  protected abstract boolean evaluate();
  
  // Protected members
  protected final Map<String, Object> parameters;
}

