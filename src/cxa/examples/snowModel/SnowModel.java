/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.examples.snowModel;

import cxa.CxA;
import cxa.components.conduits.BasicConduitFactory;
import cxa.util.Properties;

/**
 *
 * @author falcone
 */
public class SnowModel {

    private static void setProperties( CxA cxa ) {
        Properties p = cxa.properties();
        p.putInt( "width", 100 );
        p.putInt( "height", 100 );
        p.putInt( "source", 20 );
        p.putInt( "maxT", 500 );
        p.putString( "outFileName", "snow.dat" );
    }

    public static void main( String[] args ) throws InterruptedException {

        CxA cxa = new CxA( "Snow Model" );
       
        setProperties( cxa );

        cxa.addKernel( AdvectionDiffusion.class, "snow" );
        cxa.addKernel( SpiralFluid.class, "fluid" );

        cxa.connect( "fluid.velocity" ).to( "snow.velocity" ).with("velocity" );
        cxa.connect( "snow.request" ).to("fluid.request").with("request");

        cxa.execute();


    }
}