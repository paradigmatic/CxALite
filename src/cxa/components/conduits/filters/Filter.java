package cxa.components.conduits.filters;

/**
 * Filter interface. Filters are chained, when the process data, they send
 * the result to an output filter (set using the setOutput method).
 */
public interface Filter {

    /**
     * Sets the output filter. After processing the result will be sent to
     * the next filter.
     * @param f The next filter.
     */
    public void setOutput( Filter f );

    /**
     * Filters the data and send the result to the next filter.
     * @param inData The incoming data.
     */
    public void filter( Object inData );
}
