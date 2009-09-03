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
public class TwoValuesAdderFilter implements Filter {
    
    private Filter outFilter;
    private int precedingValue;
    private boolean availableValue = false;

    public void setOutput(Filter f) {
        outFilter = f;
    }

    public void filter(Object inData) {
        int newValue = (Integer) inData;
        if( availableValue ) {
            Integer i = newValue + precedingValue;
            outFilter.filter( i );
            availableValue = false;
        } else {
            precedingValue = newValue;
            availableValue = true;
        }
    }
    
}
