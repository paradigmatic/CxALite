package cxa.extra.properties;

import cxa.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.*;
s
/**
 * Allows to read cxa properties from XML.
 */
/**
 * Allows to read cxa properties from XML.
 */
public class XMLReader {

    public static void readProperties( Properties properties, InputStream source ) throws IOException {
        try {
            Document doc = new Builder().build( source );
            Nodes props = doc.query( "/cxa/properties/val" );
            for( int i=0; i < props.size(); i++ ) {
                Element p = (Element) props.get( i );
                String name = p.getAttributeValue( "name" );
                String value = p.getAttributeValue( "value" );
                setProperty( name, value, properties );
            }
        } catch ( ValidityException ex ) {
            Logger.getLogger( XMLReader.class.getName() ).log( Level.SEVERE, null, ex );
        } catch ( ParsingException ex ) {
            Logger.getLogger( XMLReader.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }


    private static void setProperty( String name, String value, Properties properties ) {
        properties.putString( name, value );
    }

    public static void readProperties( Properties properties, File file ) throws IOException {
        readProperties( properties, new FileInputStream( file ));
    }

    public static void main( String[] args ) throws IOException {
        Properties prop = new Properties();
        XMLReader.readProperties( prop, new File( "/home/falcone/prg/CxALite/resources/properties_example.xml") );
        System.out.println( prop.getString( "size") );
        System.out.println( prop.getString( "factor") );
        System.out.println( prop.getString( "fileName") );
    }

}
