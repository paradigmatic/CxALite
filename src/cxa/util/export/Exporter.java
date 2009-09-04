/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cxa.util.export;

import cxa.CxA;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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
    }

    public void to( File file ) throws IOException {
        to( new PrintWriter( file ) );
    }
}
