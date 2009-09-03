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

/**
 * A conduit entrance is used to send data (or the stop signal) to a conduit.
 */
public interface ConduitEntrance<E> extends ConduitCommon {

    /**
     * Sends data to a conduit. This methods returns as soon as the data are in
     * the conduit. However, the reception can occur later in time.
     * @param data The data to send.
     */
    public void send(E data);

    /**
     * Propagates a stop signal to a conduit. This stop signal will generate
     * an StopSignalException. The next time the receiving kernel will try to
     * receive data from the conduit, the exception will be thrown. Further, if
     * it was blocked in the receive method, the stop() will release it.
     */
    public void stop();
}
