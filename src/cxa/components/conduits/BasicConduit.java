package cxa.components.conduits;

import cxa.CxA;
import cxa.components.StopSignalException;
import cxa.components.kernel.Kernel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Basic conduit implementation. Stores the data received from the sending kernel
 * in a queue.
 */
public class BasicConduit<E> implements Conduit<E,E> {

    private final LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<E>();
    private final AtomicInteger numOfConnections = new AtomicInteger(0);
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private String ID;
    private CxA cxa;
    
    public void initialize(String id, CxA cxa) {
        this.ID = id;
        this.cxa = cxa;
    }

    public void send(E data) {
        queue.offer(data);
        CxA.logger().finest( "Conduit " + ID + ": data sent.");
    }

    public E receive() throws StopSignalException {
        int t=0;
        while (true) { //TODO: avoid busy waiting...
            t++;
            E e = queue.poll();
            if (e != null) {
                CxA.logger().finest( "Conduit " + ID + ": data to be received.");
                return e;
            } else {
                if (stopped.get()) {
                    CxA.logger().finest( "Conduit " + ID + ": sending stop signal.");
                    throw new StopSignalException();
                }
            }
        }
    }

    public void register(Kernel k) {
        CxA.logger().config("Conduit " + ID + ": Registering kernel " + k.ID() );
        numOfConnections.incrementAndGet();
    }

    public void unregister(Kernel k) {
       CxA.logger().config("Conduit " + ID + ": UnRegistering kernel " + k.ID() );
        numOfConnections.decrementAndGet();
        if (numOfConnections.get() == 0) {
            queue.clear();
        }
    }

    public String ID() {
        return ID;
    }

    public void stop() {
        CxA.logger().info("Conduit " + ID + ": Received stop signal." );
        stopped.set(true);
    }


}
