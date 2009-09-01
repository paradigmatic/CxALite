package cxa.components.conduits;

/**
 * Factory used to instanciate conduits during CxA initialization.
 */
public interface ConduitFactory<E extends Conduit> {

    /**
     * Produces a new conduit instance.
     * @return A newly instanciated conduit.
     */
    public E newInstance();
    
}
