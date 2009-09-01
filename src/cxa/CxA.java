package cxa;

import cxa.components.conduits.BasicConduitFactory;
import cxa.components.conduits.Conduit;
import cxa.components.conduits.ConduitEntrance;
import cxa.components.conduits.ConduitExit;
import cxa.components.conduits.ConduitFactory;
import cxa.components.kernel.Kernel;
import cxa.components.kernel.KernelWrapper;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static <T> Field findField( Object object, String name, Class<T> klass ) throws NoSuchFieldException {
        Field f = object.getClass().getDeclaredField( name );
        f.setAccessible( true );
        if ( f.getType() == klass ) {
            return f;
        } else {
            return null;
        }
    }

    private static <T> T getField( Object object, String name, Class<T> klass ) {
        try {
            Field f = findField( object, name, klass );
            try {
                return (T) f.get( object );
            } catch ( IllegalArgumentException ex ) {
                Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
            } catch ( IllegalAccessException ex ) {
                Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
            }
        } catch ( NoSuchFieldException ex ) {
            Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
        } catch ( SecurityException ex ) {
            Logger.getLogger( CxA.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return null;

    }

    private static <T> void setField( Object object, String field, Class<T> klass, Object value ) {
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
                    cxa.connect( from, to, ID );
                } else {
                    cxa.connect( from, to, conduit, conduitID );
                }
            }
        }
    }
    private Properties props;
    private ConduitFactory defaultFactory = new BasicConduitFactory();
    private Map<String, KernelWrapper> kernels;
    private CountDownLatch stopLatch;
    private final String ID;

    /**
     * Default constructor for instanciating a CxA
     * @param ID The CxA name (used in loggout).
     */
    public CxA(
            String ID ) {
        this.ID = ID;
        props =
                new Properties();
        kernels =
                new HashMap<String, KernelWrapper>();
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
        try { //TODO: check add keys
            Kernel k = (Kernel) klass.newInstance();
            k.initialize( id, this );
            KernelWrapper kw = new KernelWrapper( k, this );
            kernels.put( id, kw );
        } catch ( InstantiationException ex ) { //TODO: check exceptions
            LOGGER.severe( ex.toString() );
        } catch ( IllegalAccessException ex ) {
            LOGGER.severe( ex.toString() );
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
    private void connect( String fromStr, String toStr, String conduitID ) {
        connect( fromStr, toStr, defaultFactory.newInstance(), conduitID );
    }

    /**
     * Connects two kernel ports together using a specified conduit factory.
     * @param fromStr The port of the sending kernel.
     * @param toStr The port of the receiving kernel.
     * @param conFac A conduit factory.
     * @param conduitID An unique name for the conduit.
     */
    private void connect( String fromStr, String toStr, Conduit conduit, String conduitID ) {
        conduit.initialize( conduitID, this );
        /* Attaching the to kernel */
        Port fromPort = new Port( fromStr );
        Kernel fromKernel = kernels.get( fromPort.kernel ).getKernel(); //TODO: check if exists
        if ( fromPort.isArray ) {
            ConduitEntrance[] entrances = getField( fromKernel, fromPort.field, ConduitEntrance[].class ); //TODO: check if not already connected
            entrances[fromPort.index] = conduit;
        } else {


            setField( fromKernel, fromPort.field, ConduitEntrance.class, conduit ); //TODO: check if not already connected
        }
        conduit.register( fromKernel ); //TODO: cĥeck if registering was OK.
        /* Attaching the to kernel */
        Port toPort = new Port( toStr );
        Kernel toKernel = kernels.get( toPort.kernel ).getKernel(); //TODO: check if exists
        if ( toPort.isArray ) {
            ConduitExit[] exits = getField( toKernel, toPort.field, ConduitExit[].class ); //TODO: check if not already connected
            exits[toPort.index] = conduit;
        } else {
            setField( toKernel, toPort.field, ConduitExit.class, conduit ); //TODO: check if not already connected
        }
        conduit.register( toKernel ); //TODO: cĥeck if registering was OK.
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
}
