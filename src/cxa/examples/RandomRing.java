/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.examples;

import cxa.CxA;
import cxa.components.StopSignalException;
import cxa.components.conduits.BasicConduitFactory;
import cxa.components.conduits.Conduit;
import cxa.components.conduits.ConduitEntrance;
import cxa.components.conduits.ConduitExit;
import cxa.components.conduits.ConduitFactory;
import cxa.components.kernel.Kernel;
import java.util.Random;
import java.util.logging.Level;

/**
 *
 * @author falcone
 */
public class RandomRing implements Kernel {

    private String ID;
    private CxA cxa;
    private ConduitExit<Integer> in;
    private ConduitEntrance<Integer> out;
    private Random RNG;
    private int maxInt;

    public String ID() {
        return ID;
    }


    public void initialize( String ID, CxA cxa ) {
        this.ID = ID;
        this.cxa = cxa;
        maxInt = cxa.properties().getInt( "maxInt" );
        RNG = new Random();
    }


    @Override
    public void mainLoop() throws StopSignalException {
        int t = 0;
        boolean stopped = false;
        while( !stopped ) {
            int myValue = RNG.nextInt( maxInt );
            out.send( myValue );
            int otherValue = in.receive();
            t++;
            if ( otherValue == myValue ) {
                out.stop();
                System.out.println( "FOUND THE SAME NUMBER AFTER " + t + " ITERATIONS..." );
            }
        }

    }

    public static void main( String[] args ) throws InterruptedException {
        int maxInt = 1000;
        int numOfKernels = 4;
        CxA cxa = new CxA( "RandomRing" );
        CxA.logger().setLevel( Level.INFO );
        cxa.properties().putInt("maxInt", maxInt );
        for( int i = 0; i < numOfKernels; i++ ) {
            cxa.addKernel( RandomRing.class, "Node" + i );
        }

        ConduitFactory factory = new BasicConduitFactory();
        for( int i = 0; i < numOfKernels; i++ ) {
            String from = "Node" + ( i - 1 + numOfKernels ) % numOfKernels + ".out";
            String to = "Node" + i + ".in";
            cxa.connect( from ).with( factory, "Conduit" + i).to( to );
        }
        cxa.execute();
    }

}
