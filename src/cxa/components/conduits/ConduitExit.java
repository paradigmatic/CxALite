package cxa.components.conduits;

import cxa.components.StopSignalException;

/**
 * The conduit entry is used to receive data from a conduit.
 */
public interface ConduitExit<F> extends ConduitCommon {

    /**
     * Gets data from a conduit. This method blocks until method is available. If stop() is called on the conduit entrance.
     * This method will throw immediately a StopSignalException.
     * @return The data.
     * @throws cxa.components.StopSignalException
     */
    public F receive() throws StopSignalException;
}
