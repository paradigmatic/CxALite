package cxa.extra.multiscale;

/**
 * Defines space and temporal scales.
 */
public class Scale {


    /**
     * Defines convenience units. To use them, multiply the value by the prefix
     * (MILLI, KILO, etc.) and by the unit (METER, DAY, etc.), Examples
     * <pre>
     * 10*NANO*METER
     * 4*DAY
     * 100*KILO*HOURS;
     * </pre>
     */
    public class Units {

        public static final double NANO= 1e-9;
        public static final double MICRO= 1e-6;
        public static final double MILLI= 1e-3;
        public static final double CENTI= 1e-2;
        public static final double DECI= 1e-1;

        public static final double DECA= 1e1;
        public static final double HECTO= 1e2;
        public static final double KILO= 1e3;
        public static final double MEGA= 1e6;
        public static final double GIGA= 1e6;

        public static final double SECOND = 1;
        public static final double MINUTE = 60 * SECOND;
        public static final double HOUR = 60 * MINUTE;
        public static final double DAY = 24 * HOUR;

        public static final double METER = 1.0;
    }

    /**
     * Space resolution.
     */
    public final double deltaX;
    /**
     * Spatial size (whole computation domain lenght).
     */
    public final double L;
    /**
     * Time resolution.
     */
    public final double deltaT;
    /**
     * Time size (whole computation length).
     */
    public final double T;

    /**
     * Sets computation scale. You can pass a value of 0 to indicate the absence
     * of scale parameter. ie a static spatial system will a a deltaT and T equal
     * to 0. For a multidimensional system, pass the finest deltaX and the biggest L.
     * @param deltaX space resolution in meters
     * @param L space size in meters
     * @param deltaT time resolution in seconds
     * @param T time size in seconds.
     */
    public Scale( double deltaX, double L, double deltaT, double T ) {
        this.deltaX = deltaX;
        this.L = L;
        this.deltaT = deltaT;
        this.T = T;
    }

    /**
     * Returns the discrete system size.
     * @return the discrete size in sites.
     */
    public int discreteSize() {
        return (int) Math.floor( L / deltaX ) + 1;
    }


    /**
     * Returns the discrete system duration.
     * @return the discrete duration in iterations.
     */
    public int discreteDuration() {
        return (int) Math.floor( L / deltaX ) + 1;
    }

}
