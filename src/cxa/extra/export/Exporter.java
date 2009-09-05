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


package cxa.extra.export;

import cxa.CxA;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * The exporter class allows to export a cxa to a given text format defined by
 * the ExportFormat interface. It can write to PrintStream or Files. Usage example:
 *
 * <pre>new Exporter(new DOTFormat()).export(cxa).to( System.out );
 *
 */
public class Exporter {

    private final ExportFormat format;
    private CxA cxa;

    /**
     * Constructor for exporters
     * @param format The text format
     */
    public Exporter( ExportFormat format ) {
        this.format = format;
    }

    /**
     * Set the CxA to export.
     * @param cxa The cxa to export.
     * @return the exporter
     */
    public Exporter export( CxA cxa ) {
        this.cxa = cxa;
        return this;
    }

    /**
     * Export the set CxA to a PrintStream using the defined ExportFormat.
     * After export the flush method is automatically called.
     * @param ps the output stream
     * @throws java.io.IOException
     */
    public void to( PrintStream ps ) throws IOException {
        if( cxa == null ) {
            throw new IllegalStateException("No CxA source.");
        }
        format.export( cxa, ps );
        ps.flush();
    }

    /**
     * Exports the set CxA to a File using the defined format.
     * @param file
     * @throws java.io.IOException
     */
    public void to( File file ) throws IOException {
        PrintStream ps = new PrintStream( file );
        to( ps );
        ps.close();
    }

}
