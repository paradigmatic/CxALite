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
import cxa.components.conduits.BasicConduitFactory;
import java.util.Properties;

/**
 *
 * @author falcone
 */
public class SnowModel {

    private static void setProperties( CxA cxa ) {
        Properties p = cxa.properties();
        p.setProperty( "width", "100" );
        p.setProperty( "height", "100" );
        p.setProperty( "source", "20" );
        p.setProperty( "maxT", "500" );
        p.setProperty( "outFileName", "snow.dat" );
    }

    public static void main( String[] args ) throws InterruptedException {

        CxA cxa = new CxA( "Snow Model" );
       
        setProperties( cxa );

        cxa.setDefaultConduitFactory( new BasicConduitFactory() );

        cxa.addKernel( AdvectionDiffusion.class, "snow" );
        cxa.addKernel( SpiralFluid.class, "fluid" );

        cxa.connect( "fluid.velocity" ).to( "snow.velocity" ).with("velocity" );
        cxa.connect( "snow.request" ).to("fluid.request").with("request");

        cxa.execute();


    }
}
