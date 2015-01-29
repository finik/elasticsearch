/********************************************************************
 * File Name:    CustomTransportSearchQueryThenFetchAction.java
 *
 * Date Created: Jan 20, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments.search;

import org.elasticsearch.action.search.type.TransportSearchQueryThenFetchAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.search.action.SearchServiceTransportAction;
import org.elasticsearch.threadpool.ThreadPool;
  
public class CustomTransportSearchQueryThenFetchAction extends TransportSearchQueryThenFetchAction
{
  @Inject
  public CustomTransportSearchQueryThenFetchAction(Settings settings, ThreadPool threadPool, ClusterService clusterService,
      SearchServiceTransportAction searchService, CustomSearchPhaseController searchPhaseController)
  {
    super(settings, threadPool, clusterService, searchService, searchPhaseController);
  }
}

