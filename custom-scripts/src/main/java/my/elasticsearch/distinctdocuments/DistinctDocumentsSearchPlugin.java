/********************************************************************
 * File Name:    DistinctDocumentsSearchPlugin.java
 *
 * Date Created: Jan 14, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments;

import my.elasticsearch.distinctdocuments.search.DistinctDocumentsSearchClientAction;
import my.elasticsearch.distinctdocuments.search.DistinctDocumentsSearchTransportAction;

import org.elasticsearch.action.ActionModule;
import org.elasticsearch.plugins.AbstractPlugin;
  
public class DistinctDocumentsSearchPlugin extends AbstractPlugin
{
  @Override
  public String description()
  {
    return "Plugin to return distinct documents in a search request.";
  }

  @Override
  public String name()
  {
    return "distinct-documents-search";
  }
  
  public void onModule(final ActionModule module) 
  {
    module.registerAction(DistinctDocumentsSearchClientAction.INSTANCE, DistinctDocumentsSearchTransportAction.class);
  }
}

