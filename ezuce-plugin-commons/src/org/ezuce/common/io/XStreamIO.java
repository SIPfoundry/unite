package org.ezuce.common.io;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public abstract class XStreamIO {
	private XStream xstream = null;
	
	public XStreamIO() {
        xstream = new XStream(new DomDriver());
        xstream.setClassLoader(getClass().getClassLoader());
        setAliases();
	}	

    protected Object parseXml(String xml) throws Exception {
        if (xml == null) {
            return null;
        }        
        return xstream.fromXML(xml.replaceAll( "&([^;]+(?!(?:\\w|;)))", "&amp;$1"));
    }
    
    protected String toXml(Object objectToParse) {
    	return xstream.toXML(objectToParse);
    }
    
    public void setAlias(String alias, Class type) {
    	xstream.alias(alias, type);
    }
    
    public void setAliasField(String alias, Class type, String field) {
    	xstream.aliasField(alias, type, field);
    }
    
    public void setAttributeFor(String attribute, Class type) {
    	xstream.useAttributeFor(attribute, type);
    }
    
	public abstract void setAliases();
	public abstract Object parse(String xml)throws Exception;
}
