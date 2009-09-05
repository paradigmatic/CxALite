/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cxa.extra.export;

import cxa.CxA;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * @author falcone
 */
public class Exporter {

    private final ExportFormat format;
    private CxA cxa;

    public Exporter( ExportFormat format ) {
        this.format = format;
    }

    public Exporter export( CxA cxa ) {
        this.cxa = cxa;
        return this;
    }

    public void to( PrintWriter pw ) throws IOException {
        if( cxa == null ) {
            throw new IllegalStateException("No CxA source.");
        }
        format.export( cxa, pw );
        pw.flush();
    }

    public void to( File file ) throws IOException {
        PrintWriter pw = new PrintWriter( file );
        to( pw );
        pw.close();
    }

    public void to( PrintStream ps ) throws IOException {
        PrintWriter pw = new PrintWriter( ps );
        to( pw );
    }
}