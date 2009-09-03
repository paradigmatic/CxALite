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
