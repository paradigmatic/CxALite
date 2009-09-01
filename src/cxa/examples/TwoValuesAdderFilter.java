/*  
 * This file is part of CxALite
 *
 *  Facade is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Facade is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  (c) 2009, University of Geneva (Jean-Luc Falcone), jean-luc.falcone@unige.ch
 *
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
