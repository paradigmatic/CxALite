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

import java.util.HashMap;

/**
 *
 * @author falcone
 */
public class Properties {

    private HashMap<String,String> strings = new HashMap<String, String>();
    private HashMap<String,Integer> ints = new HashMap<String, Integer>();
    private HashMap<String,Double> doubles = new HashMap<String, Double>();
    private HashMap<String,Object> objects = new HashMap<String, Object>();

    public Integer putInt( String key, Integer value ) {
        return ints.put( key, value );
    }

    public Integer getInt( String key ) {
        return ints.get( key );
    }

    public String putString( String key, String value ) {
        return strings.put( key, value );
    }

    public String getString( String key ) {
        return strings.get( key );
    }

    public Double putDouble( String key, Double value ) {
        return doubles.put( key, value );
    }

    public Double getDouble( String key ) {
        return doubles.get( key );
    }

    public Object putObject( String key, Object value ) {
        return objects.put( key, value );
    }

    public Object getObject( String key ) {
        return objects.get( key );
    }

}
