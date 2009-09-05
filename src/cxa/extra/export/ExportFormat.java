/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cxa.extra.export;

import cxa.CxA;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author falcone
 */
public interface ExportFormat {

    public void export( CxA cxa, PrintStream ps ) throws IOException;

}
