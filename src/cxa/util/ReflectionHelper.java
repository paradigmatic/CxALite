/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cxa.util;

import cxa.CxA;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author falcone
 */
public class ReflectionHelper {

     private static <T> Field findField( Object object, String name, Class<T> klass ) throws NoSuchFieldException {
        Field f = object.getClass().getDeclaredField( name );
        f.setAccessible( true );
        if ( f.getType() == klass ) {
            return f;
        } else {
            return null;
        }
    }

    public static <T> T getField( Object object, String name, Class<T> klass ) {
        try {
            Field f = findField( object, name, klass );
            try {
                return (T) f.get( object );
            } catch ( IllegalArgumentException ex ) {
                Logger.getAnonymousLogger().log( Level.SEVERE, null, ex );
            } catch ( IllegalAccessException ex ) {
                Logger.getLogger( CxA.class.getName()).log( Level.SEVERE, null, ex );
            }
        } catch ( NoSuchFieldException ex ) {
            Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
        } catch ( SecurityException ex ) {
            Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return null;

    }

    public static <T> void setField( Object object, String field, Class<T> klass, Object value ) {
        try {
            Field f = findField( object, field, klass );
            try {
                f.set( object, value );
            } catch ( IllegalArgumentException ex ) {
                Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
            } catch ( IllegalAccessException ex ) {
                Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
            }
        } catch ( NoSuchFieldException ex ) {
            Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
            Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, "########> " + object.toString() );

        }
    }

}
