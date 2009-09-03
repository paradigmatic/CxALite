/*  
 * This file is part of CxALite
 *
 *  CxALite is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CxALite is distributed in the hope that it will be useful,
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
