/********************************************************************
 * File Name:    DistinctDocumentsSearchRequestBuilder.java
 *
 * Date Created: Jan 20, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments.search;

import java.util.Arrays;
import java.util.List;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
  
public class DistinctDocumentsSearchRequestBuilder extends ActionRequestBuilder<DistinctDocumentsSearchRequest, SearchResponse, DistinctDocumentsSearchRequestBuilder, Client>
{
  public DistinctDocumentsSearchRequestBuilder(final Client client)
  {
    super(client, new DistinctDocumentsSearchRequest());
    
    this.searchRequestBuilder = new SearchRequestBuilder(client);
  }

  public SearchRequestBuilder builder()
  {
    return this.searchRequestBuilder;
  }
  
  public void setPrimaryKeys(final String...primaryKeys)
  {
    this.primaryKeys = Arrays.asList(primaryKeys);
  }
  
  public void setFilter(final FilterBuilder filterBuilder) // TODO: Ajey - Add support for taking QueryBuilder as input
  {
    this.filterBuilder = filterBuilder;
  }
  
  @Override
  protected void doExecute(final ActionListener<SearchResponse> listener)
  {
    if(this.primaryKeys == null || this.primaryKeys.size() == 0)
    {
      throw new IllegalStateException("No primary keys provided for determining distinct documents.");
    }
    
    this.searchRequestBuilder.setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), 
                                                                   getFilterBuilder()));
        
    // TODO: Ajey - Need to clone to handle execute() getting called multiple times.
    this.searchRequestBuilder.addSort(SortBuilders.scriptSort("DistinctDocumentsSortScript", "string")
                                                  .lang("native")
                                                  .order(SortOrder.ASC)
                                                  .param("primaryKeys", this.primaryKeys));
    
    logger.info("Distinct Document Search Request: {}", this.searchRequestBuilder);
    
    this.request.setSearchRequest(this.searchRequestBuilder.request());
    
    this.client.execute(DistinctDocumentsSearchClientAction.INSTANCE, request(), listener);
  }
  
  private FilterBuilder getFilterBuilder()
  {
    if(this.filterBuilder == null)
    {
      return getDistinctDocumentScriptFilter();
    }
    
    return FilterBuilders.andFilter(this.filterBuilder, getDistinctDocumentScriptFilter());
  }

  private FilterBuilder getDistinctDocumentScriptFilter()
  {
    return FilterBuilders.scriptFilter("AcceptDistinctDocumentScriptFilter")
                         .lang("native")
                         .addParam("primaryKeys", this.primaryKeys);
  }
  
  // Private members
  private final SearchRequestBuilder searchRequestBuilder;
  private List<String> primaryKeys;
  private FilterBuilder filterBuilder;
  private final static ESLogger logger = ESLoggerFactory.getLogger("DistinctDocumentsSearchRequestBuilder");
}

