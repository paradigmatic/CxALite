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
package cxa;

import cxa.components.conduits.BasicConduitFactory;
import cxa.components.conduits.Conduit;
import cxa.components.conduits.ConduitEntrance;
import cxa.components.conduits.ConduitExit;
import cxa.components.conduits.ConduitFactory;
import cxa.components.kernel.Kernel;
import cxa.components.kernel.KernelWrapper;
import cxa.util.Connector;
import cxa.util.Properties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cxa.util.ReflectionHelper.*;

/**
 * The CxA class represents a whole complex automaton. It contains all components
 * (kernel and conduits) and offer access to common resources (properties, logger, etc.)
 */
public class CxA {

    private static final Logger LOGGER = Logger.getLogger( "CxA" );

    /* Sets default logger options */


    static {
        Level l = Level.FINE;
        LOGGER.setUseParentHandlers( false );
        Handler h = new ConsoleHandler();
        h.setLevel( l );
        LOGGER.addHandler( h );
        LOGGER.setLevel( l );
    }


    /* Define ports (entries and exits of conduits) */
    private class Port {

        public final String kernel;
        public final String field;
        public final boolean isArray;
        public final int index;
        private final Pattern SIMPLE_PORT = Pattern.compile( "(\\w+)\\.(\\w+)" );
        private final Pattern ARRAY_PORT = Pattern.compile( "(\\w+)\\.(\\w+)\\[(\\d+)\\]" );

        public Port( String description ) {
            Matcher arrayMatcher = ARRAY_PORT.matcher( description );
            if ( arrayMatcher.matches() ) {
                kernel = arrayMatcher.group( 1 );
                field = arrayMatcher.group( 2 );
                isArray = true;
                index = Integer.parseInt( arrayMatcher.group( 3 ) );
            } else {
                Matcher simpleMatcher = SIMPLE_PORT.matcher( description );
                if ( simpleMatcher.matches() ) {
                    kernel = simpleMatcher.group( 1 );
                    field = simpleMatcher.group( 2 );
                    isArray = false;
                    index = -1;
                } else {
                    throw new IllegalArgumentException( "The port description is not valid: " + description );
                }
            }
        }
    }
    private Properties props;
    private ConduitFactory defaultFactory = new BasicConduitFactory();
    private Map<String, KernelWrapper> kernels;
    private Map<String, Conduit> conduits;
    private CountDownLatch stopLatch;
    private final String ID;

    /**
     * Default constructor for instanciating a CxA
     * @param ID The CxA name (used in loggout).
     */
    public CxA( String ID ) {
        this.ID = ID;
        props =
                new Properties();
        kernels =
                new HashMap<String, KernelWrapper>();
        conduits = new HashMap<String, Conduit>();
    }

    /**
     * Provides a reference to the CxA logger.
     * @return the CxA logger
     */
    public static Logger logger() {
        return LOGGER;
    }

    /**
     * Provides a reference to the CxA properties
     * @return the CxA properties
     */
    public Properties properties() {
        return props;
    }

    /**
     * Return the CxA ID
     * @return the CxA ID
     */
    public String ID() {
        return ID;
    }

    /**
     * Provides a reference to the CxA stop latch.
     * @return the CxA stop latch.
     */
    public CountDownLatch stopLatch() {
        return stopLatch;
    }

    /**
     * Allows the user to set a default conduit factory.
     * @param factory
     */
    public void setDefaultConduitFactory( ConduitFactory factory ) {
        defaultFactory = factory;
    }

    /**
     * Adds a kernel to the CxA.
     * @param klass The kernel class
     * @param id An unique name for the kernel.
     */
    public void addKernel( Class klass, String id ) {
        final String constructorExceptionMessage = "Class: "
                + klass.getCanonicalName()
                + " must have a public constructor with no arguments.";
        try {
            if( kernels.containsKey( id ) ) {
                throw new IllegalArgumentException(" A kernel was already declared with ID: " + id );
            }
            Kernel k = (Kernel) klass.newInstance();
            k.initialize( id, this );
            KernelWrapper kw = new KernelWrapper( k, this );
            kernels.put( id, kw );
        } catch ( InstantiationException ex ) {
            throw new IllegalArgumentException( constructorExceptionMessage );
        } catch ( IllegalAccessException ex ) {
            throw new IllegalArgumentException( constructorExceptionMessage );
        }

    }

    public Connector connect( String fromStr ) {
        return new Connector( this ).from( fromStr );
    }

    /**
     * Connects two kernel ports together using a conduit built using the
     * default conduit factory.
     * @param fromStr The port of the sending kernel.
     * @param toStr The port of the receiving kernel.
     * @param conduitID An unique name for the conduit.
     * @see setDefaultConduitFactory()
     */
    public void connect( String fromStr, String toStr, String conduitID ) {
        connect( fromStr, toStr, defaultFactory.newInstance(), conduitID );
    }

    /**
     * Connects two kernel ports together using a specified conduit factory.
     * @param fromStr The port of the sending kernel.
     * @param toStr The port of the receiving kernel.
     * @param conFac A conduit factory.
     * @param conduitID An unique name for the conduit.
     */
    public void connect( String fromStr, String toStr, Conduit conduit, String conduitID ) {
        conduit.initialize( conduitID, this );
        Kernel fromKernel = assignConduit( fromStr, conduit, ConduitEntrance.class, ConduitEntrance[].class );
        conduit.registerSender( fromKernel );
        /* Attaching the to kernel */
        Port toPort = new Port( toStr );
        Kernel toKernel = assignConduit( toStr, conduit, ConduitExit.class, ConduitExit[].class );
        conduit.registerReceiver( toKernel );
        if( conduits.containsKey( conduitID ) ) {
            throw new IllegalArgumentException("A conduit with ID: " + conduitID + " was already declared");
        }
        conduits.put( conduitID, conduit );
    }


    private <T,A> Kernel assignConduit( String portString, Conduit conduit, Class<T> klass, Class<A> arrayKlass ) throws IllegalStateException {
        Port port = new Port( portString );
        Kernel kernel = getKernel( port.kernel );
        if ( port.isArray ) {
            Object[] conds = (Object[]) getField( kernel, port.field, arrayKlass );
            checkAlreadyAssigned( conds[port.index], portString );
            conds[port.index] = conduit;
        } else {
            checkAlreadyAssigned( getField( kernel, port.field, klass ), portString );
            setField( kernel, port.field, klass, conduit );
        }
        return kernel;
    }

    private void checkAlreadyAssigned( Object o, String portStr ) throws IllegalStateException {
        if (  o != null ) {
            throw new IllegalStateException( "Already assigned: " + portStr );
        }
    }

    private Kernel getKernel( String ID ) {
        if( ! kernels.containsKey( ID ) ) {
            throw new IllegalStateException("Kernel with ID: " + ID + " waqs not registred." );
        }
        return kernels.get( ID ).getKernel();
    }

    /**
     * Executes the CxA. Stops when all the kernel stop.
     * @throws java.lang.InterruptedException
     */
    public void execute() throws InterruptedException {
        //TODO: check if all ports are registred for all conduits.
        stopLatch = new CountDownLatch( kernels.size() );
        LOGGER.info( "Starting CxA..." );
        for( KernelWrapper kw : kernels.values() ) {
            ( new Thread( kw ) ).start();
        }
        stopLatch.await();
        LOGGER.info( "CxA terminated." );
    }

    /**
     * Provides a lists containing all the declared conduits.
     * @return The conduit list
     */
    public List<Conduit> getConduits() {
        return new ArrayList<Conduit>( conduits.values() );
    }

    /**
     * Provides a list containing all the declared kernels.
     * @return The kernel list
     */
    public List<Kernel> getKernels() {
        List<Kernel> kerns = new ArrayList<Kernel>();
        for( KernelWrapper k : kernels.values() ) {
            kerns.add( k.getKernel() );
        }
        return kerns;
    }
}

