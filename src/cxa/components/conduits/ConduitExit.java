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

import cxa.components.StopSignalException;

/**
 * The conduit entry is used to receive data from a conduit.
 */
public interface ConduitExit<F> extends ConduitCommon {

    /**
     * Gets data from a conduit. This method blocks until method is available. If stop() is called on the conduit entrance.
     * This method will throw immediately a StopSignalException.
     * @return The data.
     * @throws cxa.components.StopSignalException
     */
    public F receive() throws StopSignalException;
}
