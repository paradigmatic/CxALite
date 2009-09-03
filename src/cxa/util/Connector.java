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
