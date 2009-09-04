/*  
 * This file is part of CxALite
 *
 *  CxALite is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CxALite is distributed in the hope that it will be useful,
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

import cxa.CxA;
import cxa.components.StopSignalException;
import cxa.components.conduits.filters.Filter;
import cxa.components.kernel.Kernel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * This conduits works like a basic conduit except that data is passed through a
 * series of filters.
 * @author falcone
 */
public class FilteredConduit<E, F> implements Conduit<E, F> {


    private class EndFilter implements Filter {

        public void setOutput(Filter f) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void filter(Object inData) {
            outgoing.add((F) inData);
        }
    }
    
    private class DataPump extends Thread {
        @Override
        public void run() {
            CxA.logger().fine( "Conduit " + ID + " datapump starting.");
            try {
                while( true ) {
                    E e = incoming.take();
                    filters.get(0).filter( e );
                }
            } catch (InterruptedException ex) {
               CxA.logger().fine( "Conduit " + ID + " interupted."); 
            }
        }   
    }
    
    private final LinkedBlockingQueue<E> incoming = new LinkedBlockingQueue<E>();
    private final LinkedBlockingQueue<F> outgoing = new LinkedBlockingQueue<F>();
    private final List<Filter> filters = new ArrayList<Filter>();
    private Filter lastAddedFilter = null;
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private String ID;
    private CxA cxa;
    private DataPump pump = new DataPump();
    private Kernel sender;
    private Kernel receiver;

    public void initialize(String id, CxA cxa) {
        this.ID = id;
        this.cxa = cxa;
    }

    /**
     * Adds a filter to the filter list. New filter are added at the end, i.e.
     * the filter will be applied after previously added filters.
     * @param f The new filter
     */
    public void addFilter(Filter f) {
        filters.add(f);
        if (lastAddedFilter != null) {
            lastAddedFilter.setOutput(f);
        }
        lastAddedFilter = f;
    }

    public void send(E data) {
        incoming.offer(data);
        CxA.logger().fine("Conduit " + ID + ": data sent.");
    }

    public F receive() throws StopSignalException {
        int t = 0;
        while (true) { //TODO: avoid busy waiting...
            t++;
            F f = outgoing.poll();
            if (f != null) {
                CxA.logger().fine("Conduit " + ID + ": data to be received.");
                return f;
            } else {
                if (stopped.get()) {
                    CxA.logger().fine("Conduit " + ID + ": sending stop signal.");
                    throw new StopSignalException();
                }
            }
        }
    }


     public synchronized void unregister(Kernel k) {
       CxA.logger().config("Conduit " + ID + ": UnRegistering kernel " + k.ID() );
       if( k == sender ) {
           sender = null;
       } else if( k == receiver ) {
           receiver = null;
       } else {
           throw new IllegalStateException( "Kernel " + k.ID() + " was never registred in conduit " + k.ID() );
       }
       if( sender == null && receiver == null ) {
            pump.interrupt();
            incoming.clear();
            outgoing.clear();
          }
    }
    
    @Override
    public Kernel getSenderKernel() {
        return sender;
    }

    @Override
    public Kernel getReceiverKernel() {
        return receiver;
    }

    @Override
    public void registerSender( Kernel k ) {
        CxA.logger().config("Conduit " + ID + ": registering sender kernel " + k.ID() );
        if( sender == null) {
            sender = k;

        } else {
            throw new IllegalStateException("The sender kernel is already registered in conduit " + ID );
        }
        Filter f = new EndFilter();
        addFilter( f );
    }

    @Override
    public void registerReceiver( Kernel k ) {
        CxA.logger().config("Conduit " + ID + ": registering receiver kernel " + k.ID() );
        if( receiver == null) {
            receiver = k;
        } else {
            throw new IllegalStateException("The receiver kernel is already registered in conduit " + ID );
        }
    }

    public String ID() {
        return ID;
    }

    public void stop() {
        CxA.logger().info("Conduit " + ID + ": Received stop signal.");
        stopped.set(true);
    }

 
}
