package net.mv.meuespaco.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class CustomCache implements Cache<Long, AuthorizationInfo> {

	private static final CacheManager cacheManager = createCache();
	
	private static CacheManager createCache()
	{
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder() 
				.withCache("preConfigured",
							CacheConfigurationBuilder
								.newCacheConfigurationBuilder(Long.class, AuthorizationInfo.class, ResourcePoolsBuilder.heap(10))) 
				.build(); 
		cacheManager.init(); 
		
		return cacheManager;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	@Override
	public void clear() throws CacheException {
		getCache().clear();
	}

	private org.ehcache.Cache<Long, AuthorizationInfo> getCache() {
		return cacheManager.getCache("preConfigured", Long.class, AuthorizationInfo.class);
	}

	@Override
	public AuthorizationInfo get(Long codigo) throws CacheException {
		return getCache().get(codigo);
	}

	@Override
	public Set<Long> keys() {
		
		Set<Long> keys = new HashSet<Long>();
		
		while (getCache().iterator().hasNext())
		{
			keys.add(getCache().iterator().next().getKey());
		}
		
		return keys;
	}
		

	@Override
	public AuthorizationInfo put(Long codigo, AuthorizationInfo info) throws CacheException {
		getCache().put(codigo, info);
		
		return info;
	}

	@Override
	public AuthorizationInfo remove(Long codigo) throws CacheException {
		AuthorizationInfo info = getCache().get(codigo);
		
		getCache().remove(codigo, info);
		
		return info;
	}

	@Override
	public int size() {
		int elements = 0;
		
		while (getCache().iterator().hasNext())
		{
			elements++;
		}
		
		return elements;
	}

	@Override
	public Collection<AuthorizationInfo> values() {
		Set<AuthorizationInfo> values = new HashSet<AuthorizationInfo>();
		
		while (getCache().iterator().hasNext())
		{
			values.add(getCache().iterator().next().getValue());
		}
		
		return values;
	}
}
