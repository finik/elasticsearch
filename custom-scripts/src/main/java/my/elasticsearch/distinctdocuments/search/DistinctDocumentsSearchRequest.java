/********************************************************************
 * File Name:    DistinctDocumentsSearchRequest.java
 *
 * Date Created: Jan 19, 2015
 *
 * ------------------------------------------------------------------
 * 
 * Copyright @ 2015 ajeydudhe@gmail.com
 *
 *******************************************************************/

package my.elasticsearch.distinctdocuments.search;

import java.io.IOException;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
  
public class DistinctDocumentsSearchRequest extends ActionRequest<DistinctDocumentsSearchRequest> // TODO: Ajey - inherit directly from SearchRequest
{
  @Override
  public ActionRequestValidationException validate()
  {
    return null;
  }
  
  public SearchRequest getSearchRequest()
  {
    return searchRequest;
  }

  public void setSearchRequest(final SearchRequest searchRequest)
  {
    this.searchRequest = searchRequest;
  }

  @Override
  public void readFrom(final StreamInput in) throws IOException
  {
    super.readFrom(in);
    this.searchRequest.readFrom(in); // TODO: Ajey - null check
  }
  
  @Override
  public void writeTo(final StreamOutput out) throws IOException
  {
    super.writeTo(out);
    this.searchRequest.writeTo(out);
  }
  
  // Private members
  private SearchRequest searchRequest;
}

