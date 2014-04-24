package org.ezuce.unitemedia.context;

import org.jitsi.util.StringUtils;

public class RefKey {
	private String name;
	private String filter;	
	
	public RefKey(String name, String filter) {
		this.name = name;
		this.filter = filter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
		 
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + name.hashCode();
        hash = hash * 31 + (filter == null ? 0 : filter.hashCode());
        return hash;
    }

	@Override
	public boolean equals(Object ref) {
		return StringUtils.isEquals(filter, ((RefKey)ref).getFilter()) && StringUtils.isEquals(name, ((RefKey)ref).getName());
	}
	
}
