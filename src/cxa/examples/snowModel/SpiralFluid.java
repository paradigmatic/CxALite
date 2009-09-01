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
import java.util.Properties;
import static java.lang.Math.*;
/**
 *
 * @author falcone
 */
public class SpiralFluid implements Kernel {

    private double[][][] u;
    double uNorm;
    private int height;
    private int width;
    private String ID;
    private CxA cxa;
    private ConduitEntrance<double[][][]> velocity;
    private ConduitExit<Boolean> request;

    @Override
    public void initialize( String ID, CxA cxa ) {
        this.ID = ID;
        this.cxa = cxa;
        Properties p = cxa.properties();
        height = Integer.parseInt( p.getProperty( "height" ) );
        width = Integer.parseInt( p.getProperty( "width" ) );
        uNorm = 0.3;
        initializeU();
    }

    @Override
    public String ID() {
        return ID;
    }

    @Override
    public void mainLoop() throws StopSignalException {
        while( request.receive() ) {
            cxa.logger().fine( "Got request. Computing..." );
            velocity.send( u );
        }
    }

    @Override
    public void after() {
    }

    private void initializeU() {
        u = new double[ width ][ height ][ 2 ];
        for( int x = 0; x < width; x++ ) {
          for( int y = 0; y < height; y++ ) {
              double alpha = PI*0.5 + atan2( y-width/2, x-width/2);
              u[x][y][0] = cos(alpha)*uNorm;
              u[x][y][1] = sin(alpha)*uNorm;
          }
        }
    }
}
