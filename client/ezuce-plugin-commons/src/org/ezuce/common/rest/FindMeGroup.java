/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ezuce.common.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Razvan
 */
public class FindMeGroup implements Serializable
{
    private List<Ring> rings=new ArrayList<Ring>();
            
    public FindMeGroup(){
        
    }
    
    private FindMeGroup(List<Ring> ringList)
    {
        this.rings=ringList;
    }

    public List<Ring> getRings() {
        return rings;
    }

    public void setRings(List<Ring> rings) {
        this.rings = rings;
    }
    
    public void add(Ring ring) {
    	rings.add(ring);
    }
    
    
}
