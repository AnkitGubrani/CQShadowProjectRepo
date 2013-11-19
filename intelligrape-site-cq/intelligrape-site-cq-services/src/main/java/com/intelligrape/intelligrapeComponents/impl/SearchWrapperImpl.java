package com.intelligrape.intelligrapeComponents.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.intelligrape.intelligrapeComponents.SearchWrapper;

@Component(label="SearchWrapper" ,description="Search Wrapper implementation" ,immediate = true , metatype = true , enabled = true)
@Service(SearchWrapper.class)
public class SearchWrapperImpl implements SearchWrapper{
	
	@Reference 
	QueryBuilder queryBuilder;

	public Query createQuery(Session session) {
		// TODO Auto-generated method stub
		return queryBuilder.createQuery(session);
	}

	public Query createQuery(PredicateGroup predicateGroup, Session session) {
		// TODO Auto-generated method stub
		return queryBuilder.createQuery(predicateGroup, session);
	}

	public Query loadQuery(String arg0, Session session) throws RepositoryException, IOException {
		// TODO Auto-generated method stub
		return queryBuilder.loadQuery(arg0, session);
	}

	public void storeQuery(Query query, String arg1, boolean arg2, Session session) {
		// TODO Auto-generated method stub
		try {
			queryBuilder.storeQuery(query, arg1, arg2, session);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SearchResult getResults(String searchText, String searchInPath ,SlingHttpServletRequest slingRequest) {
		// TODO Auto-generated method stub
		Session session = slingRequest.getResourceResolver().adaptTo(Session.class);
		
	    Map<String ,String> predicateGrpMap = new HashMap<String ,String>();
	    predicateGrpMap.put("type","cq:Page");
	    predicateGrpMap.put("path",searchInPath);
	    // * is added in the end of Searchtext to implement partial search
	    predicateGrpMap.put("fulltext",searchText+"*");
	    
	    Query query = createQuery(PredicateGroup.create(predicateGrpMap), session);	    
		return query.getResult();
	}
	
}
