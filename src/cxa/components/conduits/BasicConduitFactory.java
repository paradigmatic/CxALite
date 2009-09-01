package cxa.components.conduits;

/**
 * Builds a Basic Conduit
 * @author falcone
 */
public class BasicConduitFactory implements ConduitFactory<BasicConduit> {

    public BasicConduit newInstance() {
        return new BasicConduit();
    }

}
