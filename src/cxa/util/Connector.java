/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.util;

import cxa.CxA;
import cxa.components.conduits.Conduit;
import cxa.components.conduits.ConduitFactory;

/**
 *
 * @author falcone
 */
public class Connector {

    private CxA cxa;
    private String from;
    private String to;
    private Conduit conduit;
    private String conduitID;

    public Connector( CxA cxa ) {
        this.cxa = cxa;
    }

    public Connector from( String from ) {
        this.from = from;
        return this;
    }

    public Connector to( String to ) {
        this.to = to;
        tryConnection();
        return this;
    }

    public Connector with( String conduitID ) {
        this.conduitID = conduitID;
        tryConnection();

        return this;
    }

    public Connector with( ConduitFactory conduitFactory, String conduitID ) {
        return with( conduitFactory.newInstance(), conduitID );
    }

    public Connector with( Conduit conduit, String conduitID ) {
        this.conduit = conduit;
        this.conduitID = conduitID;
        tryConnection();

        return this;
    }

    private void tryConnection() {
        if ( to != null && conduitID != null ) {
            if ( conduit == null ) {
                cxa.connect( from, to, conduitID );
            } else {
                cxa.connect( from, to, conduit, conduitID );
            }
        }
    }
}
