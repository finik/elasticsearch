/********************************************************************
 * File Name:    DistinctDocumentsSearchClientAction.java
 *
 * Date Created: Jan 20, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments.search;

import org.elasticsearch.action.ClientAction;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
  
public class DistinctDocumentsSearchClientAction extends ClientAction<DistinctDocumentsSearchRequest, SearchResponse, DistinctDocumentsSearchRequestBuilder>
{
  public static final DistinctDocumentsSearchClientAction INSTANCE = new DistinctDocumentsSearchClientAction();
  public static final String NAME = "my.elasticsearch.plugins.search.distinctdocuments.search.action";
  
  protected DistinctDocumentsSearchClientAction()
  {
    super(NAME);
  }

  @Override
  public DistinctDocumentsSearchRequestBuilder newRequestBuilder(final Client client)
  {
    return new DistinctDocumentsSearchRequestBuilder(client);
  }

  @Override
  public SearchResponse newResponse()
  {
    return new SearchResponse();
  }
}

