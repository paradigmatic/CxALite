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


package cxa.components.conduits;

import cxa.components.conduits.filters.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 * Produces filtered conduits. The filters list is passed to the factory by using
 * successive calls to addFillter() method. The produced conduits will be configured
 * with the filter list.
 */
public class FilteredConduitFactory implements ConduitFactory<FilteredConduit> {
    
    private List<Filter> filterList = new ArrayList<Filter>();

    /**
     * Adds a filter to the factory filter list.
     * @param f A new filter
     * @return A pointer to the current object (to allow method chaining).
     */
    public ConduitFactory<FilteredConduit> addFilter( Filter f ) {
        filterList.add(f);
        return this;
    }

    public FilteredConduit newInstance() {
        FilteredConduit fc = new FilteredConduit();
        for( Filter f: filterList ) {
            fc.addFilter(f);
        }
        return fc;
    }

}
