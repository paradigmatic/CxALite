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

import cxa.CxA;
import cxa.components.StopSignalException;
import cxa.components.conduits.ConduitExit;
import cxa.components.kernel.Kernel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author falcone
 */
public class MultiConsumer implements Kernel {

    private String ID;
    private int numProd;
    private ConduitExit<Integer>[] in;
    private CxA cxa;

    public String ID() {
        return ID;
    }

    public void run() {
    }

    public void initialize( String ID, CxA cxa ) {
        this.ID = ID;
        this.cxa = cxa;
        numProd = new Integer( cxa.properties().getProperty( "num.producers" ) );
        in = new ConduitExit[ numProd ];
    }

    @Override
    public void mainLoop() throws StopSignalException {
        boolean stopped = false;
        int count = 0;
        while( !stopped ) {
            for( int i = 0; i < numProd; i++ ) {
                CxA.logger().fine( "Sub Model " + ID + ": Asking for value..." );
                int nextInt = in[i].receive();
                count++;
                CxA.logger().fine( "Sub Model " + ID + ": Received " + nextInt + " from exit " + i );
            }
            try {
                Thread.sleep( 50 );
            } catch ( InterruptedException ex ) {
                Logger.getLogger( MultiConsumer.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }

    }

    @Override
    public void after() {
    }

    public static void main( String[] args ) throws InterruptedException {
        int maxCount = 20;
        int numProd = 100;
        CxA cxa = new CxA( "MultiConsumer" );
        cxa.properties().setProperty( "maxCount", "" + maxCount );
        cxa.properties().setProperty( "num.producers", "" + numProd );
        cxa.addKernel( MultiConsumer.class, "consumer" );
        for( int i = 0; i < numProd; i++ ) {
            cxa.addKernel( RandomProducer.class, "producer" + i );
        }
        for( int i = 0; i < numProd; i++ ) {
            cxa.connect( "producer" + i + ".out").to( "consumer.in[" + i + "]").with( "conduit" + i );
        }
        cxa.execute();
    }
}
