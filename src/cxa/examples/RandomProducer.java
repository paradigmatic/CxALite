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
