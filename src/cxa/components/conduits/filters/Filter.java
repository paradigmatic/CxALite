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


package cxa.components.conduits.filters;

/**
 * Filter interface. Filters are chained, when the process data, they send
 * the result to an output filter (set using the setOutput method).
 */
public interface Filter {

    /**
     * Sets the output filter. After processing the result will be sent to
     * the next filter.
     * @param f The next filter.
     */
    public void setOutput( Filter f );

    /**
     * Filters the data and send the result to the next filter.
     * @param inData The incoming data.
     */
    public void filter( Object inData );
}
