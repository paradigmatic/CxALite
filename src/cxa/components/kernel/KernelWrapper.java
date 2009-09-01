/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.components.kernel;

import cxa.CxA;
import cxa.components.StopSignalException;
import cxa.components.conduits.Conduit;
import cxa.components.conduits.ConduitCommon;
import cxa.components.conduits.ConduitEntrance;
import cxa.components.conduits.ConduitExit;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author falcone
 */
public class KernelWrapper implements Runnable {

    private Kernel kernel;
    private CxA cxa;

    public KernelWrapper(Kernel kernel, CxA cxa) {
        this.cxa = cxa;
        this.kernel = kernel;
    }

    public String ID() {
        return kernel.ID();
    }

    public Kernel getKernel() {
        return kernel;
    }

    public void run() {
        try {
            kernel.mainLoop();
        } catch (StopSignalException ex) {
            propagateStopSignal();
        } finally {
            kernel.after();
            unregisterConduits();
            cxa.stopLatch().countDown();
        }
    }

    private void propagateStopSignal() {
        try {
            Field[] fields = kernel.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getType() == ConduitEntrance.class) {
                    ConduitEntrance e = (ConduitEntrance) f.get(kernel);
                    e.stop();

                } else if (f.getType() == ConduitEntrance[].class) {
                    ConduitEntrance[] eAry = (ConduitEntrance[]) f.get(kernel);
                    for (ConduitEntrance e : eAry) {
                        e.stop();
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(KernelWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(KernelWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void unregisterConduits() {
        try {
            Field[] fields = kernel.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Class type = f.getType();
                if (type == ConduitEntrance.class || type == ConduitExit.class) {
                    ConduitCommon cc = (ConduitCommon) f.get(kernel);
                    cc.unregister(kernel);
                } else if (type == ConduitEntrance[].class || type == ConduitExit[].class) {
                   ConduitCommon[] ccAry = (ConduitCommon[]) f.get(kernel);
                   for( ConduitCommon cc: ccAry ) {
                       cc.unregister(kernel);
                   }
                }
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(KernelWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(KernelWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//
}
