/********************************************************************
 * File Name:    DistinctDocumentsSearchTransportAction.java
 *
 * Date Created: Jan 20, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments.search;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.TransportSearchAction;
import org.elasticsearch.action.search.type.TransportSearchCountAction;
import org.elasticsearch.action.search.type.TransportSearchDfsQueryAndFetchAction;
import org.elasticsearch.action.search.type.TransportSearchDfsQueryThenFetchAction;
import org.elasticsearch.action.search.type.TransportSearchQueryAndFetchAction;
import org.elasticsearch.action.search.type.TransportSearchScanAction;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.TransportAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;
  
public class DistinctDocumentsSearchTransportAction extends TransportAction<DistinctDocumentsSearchRequest, SearchResponse>
{
  @Inject
  public DistinctDocumentsSearchTransportAction(Settings settings, ThreadPool threadPool,
      TransportService transportService, ClusterService clusterService,
      TransportSearchDfsQueryThenFetchAction dfsQueryThenFetchAction,
      CustomTransportSearchQueryThenFetchAction queryThenFetchAction,
      TransportSearchDfsQueryAndFetchAction dfsQueryAndFetchAction,
      TransportSearchQueryAndFetchAction queryAndFetchAction,
      TransportSearchScanAction scanAction,
      TransportSearchCountAction countAction,
      ActionFilters actionFilters)
  {
    super(settings, DistinctDocumentsSearchClientAction.NAME, threadPool, actionFilters);
    
    this.transportSearchAction = new TransportSearchAction(settings, threadPool, transportService, clusterService, dfsQueryThenFetchAction, queryThenFetchAction, dfsQueryAndFetchAction, queryAndFetchAction, scanAction, countAction, actionFilters);
  }
  
  @Override
  protected void doExecute(final DistinctDocumentsSearchRequest searchRequest, final ActionListener<SearchResponse> listener)
  {
    this.transportSearchAction.execute(searchRequest.getSearchRequest(), listener);
  }
  
  // Private members
  final TransportSearchAction transportSearchAction;
}

