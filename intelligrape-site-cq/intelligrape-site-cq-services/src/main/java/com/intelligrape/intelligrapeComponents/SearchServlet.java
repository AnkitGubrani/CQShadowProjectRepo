package com.intelligrape.intelligrapeComponents;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@SlingServlet(methods={"GET"},
			  paths={"/search/service/search.html"},
			  generateComponent=false
)
@Component(description="My Search service servlet" ,enabled = true ,immediate = true ,metatype = true)
@Properties({
	@Property(name = "search.vendor" ,value = "Intelligrape")
})
public class SearchServlet extends SlingSafeMethodsServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Reference
	SearchWrapper searchWrapper;
	
	@Reference
	SlingRepository repository;
	
	@Activate
	protected void activate(ComponentContext context)
	{
		//System.out.println("Activating the search servlet");
	}
	
	@Override
	protected void doGet(SlingHttpServletRequest request ,SlingHttpServletResponse response) throws ServletException, IOException 
	{
		String searchtext =(String)request.getParameter("searchtext");
		String searchInPath = (String) request.getParameter("searchinpath");

		Session session = request.getResourceResolver().adaptTo(Session.class);

		Map<String, String> predicateGrpMap = new HashMap<String, String>();

		predicateGrpMap.put("type", "cq:Page");
		predicateGrpMap.put("path", "/content");
		predicateGrpMap.put("fulltext", searchtext);

		System.out.println("map="+predicateGrpMap);

		Query query = searchWrapper.createQuery(PredicateGroup.create(predicateGrpMap) , session);

		query.setStart(0);
		query.setHitsPerPage(20);

		SearchResult result = query.getResult();

		System.out.println("Result =="+result.getTotalMatches());

		for(Hit hit : result.getHits())
		{
			try {
				System.out.println(""+hit.getPath());
				Resource resource = request.getResourceResolver().getResource(hit.getPath());

				System.out.println("hit resource ="+resource);

			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}