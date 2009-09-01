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


package cxa.examples.snowModel;

import cxa.CxA;
import cxa.components.StopSignalException;
import cxa.components.conduits.ConduitEntrance;
import cxa.components.conduits.ConduitExit;
import cxa.components.kernel.Kernel;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import static java.lang.Math.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author falcone
 */
public class AdvectionDiffusion implements Kernel {

    private int[][] domain;
    private int[][] nextDomain;
    private double[][][] u;
    private int height;
    private int width;
    private int source;
    private int maxT;
    private String outFileName;
    /* CxA  fields */
    private String ID;
    private CxA cxa;
    private ConduitExit<double[][][]> velocity;
    private ConduitEntrance<Boolean> request;

    @Override
    public void initialize( String ID, CxA cxa ) {
        this.ID = ID;
        this.cxa = cxa;
        Properties p = cxa.properties();
        height = Integer.parseInt( p.getProperty( "height" ) );
        width = Integer.parseInt( p.getProperty( "width" ) );
        source = Integer.parseInt( p.getProperty( "source" ) );
        maxT = Integer.parseInt( p.getProperty( "maxT" ) );
        outFileName = p.getProperty( "outFileName" );
        domain = new int[ width ][ height ];
    }

    @Override
    public String ID() {
        return ID;
    }

    @Override
    public void mainLoop() throws StopSignalException {
        for( int t=0; t< maxT; t++ ) {
            cxa.logger().fine( "Iteration: " + t);
            request.send( true );
            u = velocity.receive();
            nextDomain = new int[ width ][ height ];
            boundaries();
            update();
            domain = nextDomain;
        }
        request.stop();
    }

    @Override
    public void after() {
        try {
            cxa.logger().fine( "Saving data" );
            dumpMatrix( outFileName );
        } catch ( IOException ex ) {
            Logger.getLogger( AdvectionDiffusion.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    private void boundaries() {
        for( int x = 0; x < width; x++ ) {
            domain[x][0] = 0;
            domain[x][height - 1] = 0;
        }
        for( int y = 0; y < height; y++ ) {
            domain[0][y] = 0;
            domain[width - 1][y] = 0;
        }
        domain[3*width/4][height/4] = source;
    }

    private static int[] getDisp( double[] u ) {
        int[] disp = new int[2];
        if( random() < abs( u[0] ) ) {
            disp[0] = (int) signum( u[0] );
        }
        if( random() < abs( u[1] ) ) {
            disp[1] = (int) signum( u[1] );
        }
        return disp;
    }

    private void update() {
        for( int x = 0; x < width; x++ ) {
            for( int y = 0; y < height; y++ ) {
                for( int i=0; i<domain[x][y]; i++ ) {
                    int[] disp = getDisp( u[x][y] );
                    nextDomain[ x+disp[0] ][ y+disp[1] ]++;
                }
            }
        }
    }

    public void dumpMatrix( String Filename ) throws IOException {
        PrintWriter pw = new PrintWriter( new FileWriter( Filename ) );
        for( int[] col: domain ) {
            for( int i: col ) {
                pw.print( i + " " );
            }
            pw.println();
        }
        pw.close();
    }

}
