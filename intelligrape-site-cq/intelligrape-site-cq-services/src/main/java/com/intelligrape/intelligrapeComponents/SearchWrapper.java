package com.intelligrape.intelligrapeComponents;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

public interface SearchWrapper {

	public Query createQuery(Session session);

	public Query createQuery(PredicateGroup predicateGroup, Session session);

	public Query loadQuery(String arg0, Session session) throws RepositoryException, IOException ;

	public void storeQuery(Query query, String arg1, boolean arg2, Session arg3);
	
	public SearchResult getResults(String searchText ,String searchInPath ,SlingHttpServletRequest slingRequest);
}
