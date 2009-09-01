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
