package cxa.components.conduits;

/**
 * A conduit entrance is used to send data (or the stop signal) to a conduit.
 */
public interface ConduitEntrance<E> extends ConduitCommon {

    /**
     * Sends data to a conduit. This methods returns as soon as the data are in
     * the conduit. However, the reception can occur later in time.
     * @param data The data to send.
     */
    public void send(E data);

    /**
     * Propagates a stop signal to a conduit. This stop signal will generate
     * an StopSignalException. The next time the receiving kernel will try to
     * receive data from the conduit, the exception will be thrown. Further, if
     * it was blocked in the receive method, the stop() will release it.
     */
    public void stop();
}
