/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.remote;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.Util;

/**
 *
 * @author falcone
 */
public class RemoteCxA implements Receiver {

    private List<String> ids = new ArrayList<String>();
    private boolean FIRST = false;
    private final Map<String, Address> directory = new HashMap<String, Address>();
    private Channel channel;

    public RemoteCxA() {
    }

    public void start() {
        try {
            channel = new JChannel();
            channel.connect("FOOGroup");
            if (channel.getAddress().equals(channel.getView().getMembers().firstElement())) {
                FIRST = true;
            }
            channel.setReceiver(this);
            while( channel.getView().getMembers().size() < ids.size() ) {
                System.out.println("Waiting for every guy  to join the party.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RemoteCxA.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            channel.getState(null, 0);
            for( String id: ids ) {
                if( directory.get(id).equals( channel.getAddress() ) ) {
                    System.out.println("My ID is: " + id );
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RemoteCxA.class.getName()).log(Level.SEVERE, null, ex);
            }
            channel.disconnect();
            channel.close();
        } catch (ChannelException ex) {
            Logger.getLogger(RemoteCxA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addID(String id) {
        ids.add(id);
    }

    public void receive(Message msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public byte[] getState() {
        System.out.println(channel.getName() + ": get state called.");

        synchronized (directory) {
            byte[] state = null;
            try {
                state = Util.objectToByteBuffer(directory);
            } catch (Exception ex) {
                Logger.getLogger(RemoteCxA.class.getName()).log(Level.SEVERE, null, ex);
            }
            return state;
        }

    }

    public void setState(byte[] state) {
        System.out.println(channel.getName() + ": set state called.");
        synchronized (directory) {
            Map<String,Address> new_dir = null;
            try {
                new_dir = (Map<String,Address>) Util.objectFromByteBuffer(state);
            } catch (Exception ex) {
                Logger.getLogger(RemoteCxA.class.getName()).log(Level.SEVERE, null, ex);
            }
            directory.clear();
            directory.putAll(new_dir);
        }

    }

    public void viewAccepted(View view) {
        if (FIRST) {
            final int size = view.getMembers().size();
            if (size == ids.size()) {
                System.out.println("Everybody is here");
                computeState();
            } else if (size > ids.size()) {
                System.out.println("Too many puppies...");
            }
        }
    }

    public void suspect(Address suspected_mbr) {
        
    }

    public void block() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        final RemoteCxA rcxa = new RemoteCxA();
        rcxa.addID("foo");
        rcxa.addID("bar");
        rcxa.addID("baz");
        rcxa.addID("foo2");
        rcxa.addID("bar2");
        rcxa.addID("baz2");
        rcxa.start();
    }

    private void computeState() {
        Vector<Address> addr = channel.getView().getMembers();
        for (int i = 0; i < ids.size(); i++) {
            directory.put(ids.get(i), addr.get(i));
        }
    }
}
