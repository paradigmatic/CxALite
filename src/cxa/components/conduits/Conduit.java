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
import cxa.components.kernel.Kernel;

/**
 * Conduit interface
 * @author falcone
 */
public interface Conduit<E,F> extends ConduitEntrance<E>,ConduitExit<F> {

    /**
     * Provides the sender kernel
     * @return The sender kernel
     */
    public Kernel getSenderKernel();

    /**
     * Provides the receiver kernel
     * @return The receiver kernel
     */
    public Kernel getReceiverKernel();

    /**
     * Register the sender kernel to the conduit.
     * @param k The kernel that will send data.
     */
    public void registerSender(Kernel k);

    /**
     * Register the receiver kernel to the conduit.
     * @param k The kernel that will receive data.
     */
    public void registerReceiver(Kernel k);


    /**
     * Unregister a kernel from a conduit.
     * @param k The kernel to unregister
     */
    public void unregister(Kernel k);

    /**
     * Initialize a conduit. This method is automatically called by the CxA
     * after conduit creation.
     * @param id a unique name for the conduit.
     * @param cxa a reference to the CxA.
     */
    public void initialize( String id, CxA cxa );

}