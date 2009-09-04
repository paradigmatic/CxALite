/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cxa.util.export;

import cxa.CxA;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author falcone
 */
public interface ExportFormat {

    public void export( CxA cxa, PrintWriter pw ) throws IOException;

}
