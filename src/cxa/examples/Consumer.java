/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class Consumer implements Kernel {

    private String ID;
    private ConduitExit<Integer> in;
    private CxA cxa;

    public String ID() {
        return ID;
    }

    public void initialize(String ID, CxA cxa) {
        this.ID = ID;
        this.cxa = cxa;
    }

    public void mainLoop() throws StopSignalException {
        boolean stopped = false;
        int count = 0;
        while (!stopped) {
            CxA.logger().fine("Sub Model " + ID + ": Asking for value...");
            int nextInt = in.receive();
            count++;
            CxA.logger().fine("Sub Model " + ID + ": Received " + nextInt);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void after() { }
}
