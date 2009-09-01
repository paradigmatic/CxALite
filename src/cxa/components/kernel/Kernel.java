/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cxa.components.kernel;

import cxa.CxA;
import cxa.components.StopSignalException;

/**
 *
 * @author falcone
 */
public interface Kernel {
    
    public void initialize( String ID, CxA cxa );
    public String ID();
    public void mainLoop() throws StopSignalException;
    public void after();

    
}
