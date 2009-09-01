package cxa.components.conduits;

import cxa.CxA;
import cxa.components.kernel.Kernel;

/**
 * This interface gather common conduit methods.
 */
public interface ConduitCommon {

    /**
     * Initialize a conduit. This method is automatically called by the CxA
     * after conduit creation.
     * @param id a unique name for the conduit.
     * @param cxa a reference to the CxA.
     */
    public void initialize( String id, CxA cxa );

    /**
     * Gets the conduit name.
     * @return the conduit name
     */
    public String ID();

    /**
     * Register a kernel to the conduit.
     * @param k The kernel to register.
     */
    public void register(Kernel k);

    /**
     * Unregister a kernel from a conduit.
     * @param k The kernel to unregister
     */
    public void unregister(Kernel k);
}
