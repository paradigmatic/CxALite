/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.extra.export;

import cxa.CxA;
import cxa.components.conduits.Conduit;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author falcone
 */
public class DOTFormat implements ExportFormat {

    private static String sanitize( String in ) {
        return in.replace( " ", "_");
    }

    @Override
    public void export( CxA cxa, PrintWriter pw ) throws IOException {
        pw.println( "digraph " + sanitize( cxa.ID() ) + " {" );
        pw.println( "  rankdir = LR;" );
        for( Conduit c : cxa.getConduits() ) {
            pw.print( "  " + sanitize( c.getSenderKernel().ID() ) );
            pw.print( " -> " +  sanitize( c.getReceiverKernel().ID() ) );
            pw.println( " [" + " label=\"" + sanitize( c.ID() ) + "\" ];");
        }
        pw.println("}");
    }
}
