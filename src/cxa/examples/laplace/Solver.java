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


package cxa.examples.laplace;

import cxa.CxA;
import cxa.components.StopSignalException;
import cxa.components.conduits.ConduitEntrance;
import cxa.components.conduits.ConduitExit;
import cxa.components.kernel.Kernel;

/**
 *
 * @author falcone
 */
public class Solver implements Kernel {

    private int maxT;
    private int nx;
    private int ny;
    private double[][] domain;
    private double[][] nextDomain;
    private String ID;
    private CxA cxa;
    private ConduitEntrance<double[]> leftOut;
    private ConduitEntrance<double[]> rightOut;
    private ConduitExit<double[]> leftIn;
    private ConduitExit<double[]> rightIn;
    //private ConduitEntrance<double[][]> collector;

    public void initialize( String ID, CxA cxa ) {
        this.cxa = cxa;
        this.ID = ID;
        nx = new Integer( cxa.properties().getProperty( "width" ) ) + 2;
        ny = new Integer( cxa.properties().getProperty( "height" ) );
        maxT = new Integer( cxa.properties().getProperty( "total.time" ) );
        domain = new double[ nx ][ ny ];
        nextDomain = new double[ nx ][ ny ];
        boundaryConditions();
        computeDomain();
        update();
    }

    public String ID() {
        return ID;
    }

    @Override
    public void mainLoop() throws StopSignalException {
        CxA.logger().info( ID + ": starting computations." );
        for( int t = 0; t < maxT; t++ ) {
            CxA.logger().fine( ID + ": beginning iteration " + t );
            sendBorders();
            boundaryConditions();
            receiveBorders();
            computeDomain();
            update();
        }
        CxA.logger().info( ID + ": finished computations." );
    //collector.send(getDomain());

    }

    public void after() {
    }

    private void boundaryConditions() {
        for( int x = 0; x < nx; x++ ) {
            nextDomain[x][0] = 0.0;
            nextDomain[x][ny - 1] = 1.0;
        }
    }

    private void update() {
        double[][] tmp = domain;
        domain = nextDomain;
        nextDomain = tmp;
    }

    private void sendBorders() {
        leftOut.send( domain[1] );
        rightOut.send( domain[nx - 2] );
    }

    private void receiveBorders() throws StopSignalException {
        domain[0] = leftIn.receive();
        domain[nx - 1] = rightIn.receive();
    }

    private void computeDomain() {
        for( int x = 1; x < nx - 1; x++ ) {
            for( int y = 1; y < ny - 1; y++ ) {
                nextDomain[x][y] = 0.25 * ( domain[x - 1][y] + domain[x + 1][y] +
                                            domain[x][y - 1] + domain[x][y + 1] );
            }

        }
    }

    public double[][] getDomain() {
        double[][] result = new double[ nx - 2 ][ ny ];
        for( int x = 0; x < nx - 2; x++ ) {
            for( int y = 0; y < ny;
                 y++ ) {
                result[x][y] = domain[x + 1][y];
            }

        }
        return result;
    }
}
