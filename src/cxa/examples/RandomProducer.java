/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.examples;

import cxa.CxA;
import cxa.components.StopSignalException;
import cxa.components.conduits.ConduitEntrance;
import cxa.components.conduits.FilteredConduitFactory;
import cxa.components.kernel.Kernel;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author falcone
 */
public class RandomProducer implements Kernel {

    private String ID;
    private int maxCount;
    private ConduitEntrance<Integer> out;
    private CxA cxa;
    private Random RNG;

    public String ID() {
        return ID;
    }

    public void run() {
        try {
            for (int count = 0; count < maxCount; count++) {
                int nextInt = RNG.nextInt(20);
                CxA.logger().fine("SubModel " + ID + ": sending " + nextInt);
                out.send(nextInt);

                Thread.sleep(50);
            }
            CxA.logger().info("SubModel " + ID + ": sending stop signal");
            out.stop();
        } catch (InterruptedException ex) {
            Logger.getLogger(RandomProducer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.unregister(this);
            cxa.stopLatch().countDown();
        }
    }

    public void initialize(String ID, CxA cxa) {
        this.ID = ID;
        this.cxa = cxa;
        maxCount = cxa.properties().getInt("maxCount");
        RNG = new Random();
    }

    public void mainLoop() throws StopSignalException {
        for (int count = 0; count < maxCount; count++) {
            int nextInt = RNG.nextInt(20);
            CxA.logger().fine("SubModel " + ID + ": sending " + nextInt);
            out.send(nextInt);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(RandomProducer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        CxA.logger().info("SubModel " + ID + ": sending stop signal");
        out.stop();
    }

    public void after() {
    }

     public static void main(String[] args) throws InterruptedException {
        int maxCount = 11;
        CxA cxa = new CxA("Random Producer");
        cxa.properties().putInt("maxCount", 11);
        FilteredConduitFactory factory = new FilteredConduitFactory();
        factory.addFilter(new TwoValuesAdderFilter());
        cxa.addKernel(Consumer.class, "consumer");
        cxa.addKernel(RandomProducer.class, "producer");
        cxa.connect("producer.out").to( "consumer.in").with( factory, "conduit");
        cxa.execute();
    }
}
