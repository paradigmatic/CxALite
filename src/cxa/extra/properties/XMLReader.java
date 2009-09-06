package cxa.extra.properties;

import cxa.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import nu.xom.*;

/**
 * Allows to read cxa properties from XML.
 */
/**
 * Allows to read cxa properties from XML.
 */
public class XMLReader {

    private static String INT_REGEXP = "^[-+]?[0-9]*$";
    private static String DOUBLE_REGEXP = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";

    private static void setProperty( String name, String value, Properties properties ) {
        if ( value.matches( INT_REGEXP ) ) {
            properties.putInt( name, new Integer( value ) );
        } else if ( value.matches( DOUBLE_REGEXP ) ) {
            properties.putDouble( name, new Double( value ) );
        } else {
            properties.putString( name, value );
        }
    }

    public static void readProperties( Properties properties, InputStream source ) throws IOException {
        try {
            Document doc = new Builder().build( source );
            Nodes props = doc.query( "/cxa/properties/val" );
            for( int i = 0; i < props.size(); i++ ) {
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

    public static void readProperties( Properties properties, File file ) throws IOException {
        readProperties( properties, new FileInputStream( file ) );
    }

    public static void main( String[] args ) throws IOException {
        Properties prop = new Properties();
        XMLReader.readProperties( prop, new File( "/home/falcone/prg/CxALite/resources/properties_example.xml" ) );
        System.out.println( prop.getInt( "size" ) );
        System.out.println( prop.getDouble( "factor" ) );
        System.out.println( prop.getString( "fileName" ) );
    }
}
