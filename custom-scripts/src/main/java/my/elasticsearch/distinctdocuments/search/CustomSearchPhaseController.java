/********************************************************************
 * File Name:    CustomSearchPhaseController.java
 *
 * Date Created: Jan 20, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.cache.recycler.CacheRecycler;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.common.util.concurrent.AtomicArray;
import org.elasticsearch.common.util.concurrent.AtomicArray.Entry;
import org.elasticsearch.search.controller.SearchPhaseController;
import org.elasticsearch.search.query.QuerySearchResultProvider;
  
public class CustomSearchPhaseController extends SearchPhaseController
{
  @Inject
  public CustomSearchPhaseController(final Settings settings, final CacheRecycler cacheRecycler, final BigArrays bigArrays)
  {
    super(settings, cacheRecycler, bigArrays);
  }
  
  @Override
  public ScoreDoc[] sortDocs(boolean ignoreFrom, AtomicArray<? extends QuerySearchResultProvider> resultsArr) throws IOException
  {
    // TODO: Ajey - optimizations for single shard or result from single shard
    final HashSet<BytesRef> existingDocumentKeys = new HashSet<>();
    for (Entry<? extends QuerySearchResultProvider> entry : resultsArr.asList())
    {
      final int nShardIndex = entry.index;
      final QuerySearchResultProvider provider = entry.value;
      final TopFieldDocs docs = (TopFieldDocs) provider.queryResult().topDocs();
            
      final List<ScoreDoc> uniqueDocs = new ArrayList<>(docs.scoreDocs.length);
      for (ScoreDoc scoreDoc : docs.scoreDocs)
      {
        final FieldDoc fieldDoc = (FieldDoc) scoreDoc;
        logger.info("fields {} : {}", fieldDoc.fields, fieldDoc.fields[0].getClass());
        
        final BytesRef field = (BytesRef)fieldDoc.fields[fieldDoc.fields.length - 1]; // TODO: Ajey - remove this last sort field as we have added it for store primaryKey
        if (existingDocumentKeys.add(field))
        {
          uniqueDocs.add(scoreDoc);
        }
        else
        {
          if(logger.isInfoEnabled())
          {
            logger.info("[Shard_{}] Ignored duplicate document = [{}]", nShardIndex, field.utf8ToString());
          }
        }
      }
      
      /*
       * Reduce the totalHits by no. of duplicate documents we have removed here.
       * However, this will still not give accurate totalHits. The totalHits returned
       * by each shard includes the documents which match the filter but were removed because
       * of pagination from & size parameters. There is no way to identify if such documents were
       * also present on other shard. But still we can use the totalHits as an approximate count.
       */
      docs.totalHits = docs.totalHits - (docs.scoreDocs.length - uniqueDocs.size()); 
      docs.scoreDocs = uniqueDocs.toArray(new ScoreDoc[uniqueDocs.size()]);
    }
    
    return super.sortDocs(ignoreFrom, resultsArr);
  }
}

