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


package cxa.components.conduits;

import cxa.CxA;
import cxa.components.kernel.Kernel;

/**
 * This interface gather common conduit methods.
 */
public interface ConduitCommon {

    /**
     * Initialize a conduit. This method is automatically called by the CxA
     * after conduit creation.
     * @param id a unique name for the conduit.
     * @param cxa a reference to the CxA.
     */
    public void initialize( String id, CxA cxa );

    /**
     * Gets the conduit name.
     * @return the conduit name
     */
    public String ID();

    /**
     * Register a kernel to the conduit.
     * @param k The kernel to register.
     */
    public void register(Kernel k);

    /**
     * Unregister a kernel from a conduit.
     * @param k The kernel to unregister
     */
    public void unregister(Kernel k);
}
