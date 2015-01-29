/********************************************************************
 * File Name:    InMemoryESNode.java
 *
 * Date Created: Jan 29, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
  
public class InMemoryESNode
{
  public InMemoryESNode(final String nodeName, final String clusterName)
  {
    this.nodeName = nodeName;
    this.clusterName = clusterName;
  }
  
  public void startNode() 
  {
    final Settings finalSettings = settingsBuilder().put(getNodeSettings())
                                                    .put("name", this.nodeName)
                                                    .build();
    
    this.node = nodeBuilder().settings(finalSettings).build();
    
    this.client = node.client();

    this.node.start();
  }

  public Client getClient()
  {
    return this.client;
  }
  
  public void stopNode()
  {
    this.node.close();
  }
  
  private Settings getNodeSettings() 
  {
    return ImmutableSettings
            .settingsBuilder()
            .put("cluster.name", this.clusterName)
            .put("index.number_of_shards", 2)
            .put("index.number_of_replicas", 1)
            .put("index.store.type", "ram")
            .put("http.enabled", false)
            .put("discovery.zen.multicast.enabled", false)
            .put("threadpool.bulk.queue_size", 200)
            .put("script.native.AcceptDistinctDocumentScriptFilter.type", "my.elasticsearch.distinctdocuments.search.AcceptDistinctDocumentScriptFilterFactory")
            .put("script.native.DistinctDocumentsSortScript.type", "my.elasticsearch.distinctdocuments.search.DistinctDocumentsSortScriptFactory")                
            .build();
  }
  
  // Private members
  private final String nodeName;
  private final String clusterName;
  private Node node;
  private Client client;
}

