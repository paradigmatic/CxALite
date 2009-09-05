/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.extra.export;

import cxa.CxA;
import cxa.components.conduits.Conduit;
import java.io.IOException;
import java.io.PrintStream;
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
    public void export( CxA cxa, PrintStream ps ) throws IOException {
        ps.println( "digraph " + sanitize( cxa.ID() ) + " {" );
        ps.println( "  rankdir = LR;" );
        for( Conduit c : cxa.getConduits() ) {
            ps.print( "  " + sanitize( c.getSenderKernel().ID() ) );
            ps.print( " -> " +  sanitize( c.getReceiverKernel().ID() ) );
            ps.println( " [" + " label=\"" + sanitize( c.ID() ) + "\" ];");
        }
        ps.println("}");
    }
}
