/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cxa.examples;

import cxa.components.conduits.filters.Filter;

/**
 *
 * @author falcone
 */
public class NopFilter implements Filter {
    
    private Filter outputFilter;

    public void setOutput(Filter f) {
       outputFilter = f;
    }

    public void filter(Object inData) {
        outputFilter.filter(inData);
    }
}
