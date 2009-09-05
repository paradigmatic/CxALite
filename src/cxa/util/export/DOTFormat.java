/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.util.export;

import cxa.CxA;
import cxa.components.conduits.Conduit;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author falcone
 */
public class DOTFormat implements ExportFormat {

    @Override
    public void export( CxA cxa, PrintWriter pw ) throws IOException {
        pw.println( "digraph " + cxa.ID() + " {" );
        for( Conduit c : cxa.getConduits() ) {
            pw.println( "  " + c.getSenderKernel().ID() + " -> "
                    + c.getReceiverKernel().ID() + ";" );
        }
        pw.println("}");
    }
}
