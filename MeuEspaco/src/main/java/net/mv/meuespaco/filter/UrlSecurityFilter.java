package net.mv.meuespaco.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;

import net.mv.meuespaco.service.impl.PropertiesLoad;

@WebFilter("/*")
public class UrlSecurityFilter implements Filter {

	private PropertiesLoad props = new PropertiesLoad(); 
	
	public UrlSecurityFilter() {	}
	
	public UrlSecurityFilter(PropertiesLoad props) 
	{
		this.props = props;
	}
	
	public void validate(ServletRequest req, ServletResponse resp) throws IOException, URISyntaxException
	{
		String urlBase = ((HttpServletRequest) req).getRequestURL().toString();
		String query = ((HttpServletRequest) req).getQueryString();
		
		UriBuilder uriBuilder = UriBuilder.fromUri(urlBase);
		uriBuilder.replaceQuery(query);
		
		URL url = uriBuilder.build().toURL();
		
		if (!this.isSecure(url))
		{
			this.redirect(url, resp);
		}
	}

	public void redirect(URL url, ServletResponse resp) throws IOException, URISyntaxException 
	{
		((HttpServletResponse) resp).sendRedirect(this.securedUrl(url));
	}

	public String securedUrl(URL url) throws URISyntaxException 
	{
		if (null == props)
		{
			return "";
		}
		
		UriBuilder uriBuilder = UriBuilder.fromUri(new URI(props.getProperty("server")));
		
		uriBuilder.path(url.getPath());
		uriBuilder.replaceQuery(url.getQuery());
		
		String secured = uriBuilder.build().toString();
		
		return secured;
	}

	public boolean isSecure(URL url) {
		return url.getProtocol().equals("https");
	}

	@Override
	public void destroy() 
	{
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException 
	{
		try 
		{
			this.validate(req, response);
			chain.doFilter(req, response);
	
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException 
	{
		
	}

}
