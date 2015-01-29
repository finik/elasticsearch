/********************************************************************
 * File Name:    DistinctDocumentsSearchIntegrationTests.java
 *
 * Date Created: Jan 20, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import my.elasticsearch.distinctdocuments.search.DistinctDocumentsSearchRequestBuilder;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Before;
import org.junit.Test;

public class DistinctDocumentsSearchIntegrationTests
{
  @Test
  public void testDistinctDocumentSearch_noSort_noFilter()
  {
    final DistinctDocumentsSearchRequestBuilder builder = new DistinctDocumentsSearchRequestBuilder(this.esNode.getClient());
    builder.setPrimaryKeys("someField1", "someField2");
    
    final SearchResponse response = builder.execute().actionGet();
    logger.info("Results = {}", response.toString());

    assertEquals(10, response.getHits().getTotalHits());

    validateDistinctDocuments(response);
  }

  @Test
  public void testDistinctDocumentSearch_noSort_WithFilter()
  {
    final DistinctDocumentsSearchRequestBuilder builder = new DistinctDocumentsSearchRequestBuilder(this.esNode.getClient());
    builder.setPrimaryKeys("someField1", "someField2");
    
    builder.setFilter(FilterBuilders.termsFilter("someField1", "a1", "a3", "a5"));
    
    final SearchResponse response = builder.execute().actionGet();
    logger.info("Results = {}", response.toString());

    assertEquals(3, response.getHits().getTotalHits());

    validateDistinctDocuments(response);
  }

  @Test
  public void testDistinctDocumentSearch_noSort_noFilter_withPagination()
  {
    final DistinctDocumentsSearchRequestBuilder builder = new DistinctDocumentsSearchRequestBuilder(this.esNode.getClient());
    builder.setPrimaryKeys("someField1", "someField2");
    
    builder.builder().setFrom(3)
                     .setSize(2);
    
    final SearchResponse response = builder.execute().actionGet();
    logger.info("Results = {}", response.toString());

    // Following condition cannot be handled because each shard will ignore the documents which do not meet pagination criteria.
    // There is no way to adjust the totalHits since we won;t know if the ignored documents are also present on other shards.
    //assertEquals(10, response.getHits().getTotalHits());

    validateDistinctDocuments(response);
  }

  private void validateDistinctDocuments(final SearchResponse response)
  {
    final Set<String> distinctValues = new HashSet<>();
    for (SearchHit hit : response.getHits())
    {
      logger.info("[Shard_{}] [{}]", hit.getShard().getShardId(), hit.getSourceAsString());
      
      final String fieldValue = (String) hit.getSource().get("someField1");
      assertTrue("Value is not unique " + fieldValue, distinctValues.add(fieldValue));
    }
  }

  @Before
  public void startNode() // TODO: Ajey - This will get called before each test which should be avoided !!!
  {
    this.esNode = new InMemoryESNode("distinctDocTest", "distinctDocTest");
    this.esNode.startNode();

    populateData();    
  }
  
  public void stopNode()
  {
    this.esNode.stopNode();
  }
  
  private void populateData()
  {
    // Populate data
    for (int nIndexDup = 0; nIndexDup < 4; nIndexDup++)
    {
      for (int nIndex = 0; nIndex < 10; nIndex++)
      {
        IndexRequest indexRequest = new IndexRequest().index("test").type("test").id("test_" + nIndexDup + "_" + nIndex)
            .source("{someField1: \"a" + nIndex + "\", someField2: \"b" + nIndex + "\"}")
            .refresh(true);
    
        this.esNode.getClient().index(indexRequest).actionGet();          
      }
    }
  }  
  
  // Private members
  private InMemoryESNode esNode;
  
  private final static ESLogger logger = ESLoggerFactory.getLogger("DistinctDocumentsSearchIntegrationTests");
}

