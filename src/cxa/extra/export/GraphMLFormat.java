/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.extra.export;

import cxa.CxA;
import cxa.components.conduits.Conduit;
import cxa.components.kernel.Kernel;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import nu.xom.*;

/**
 *
 * @author falcone
 */
public class GraphMLFormat implements ExportFormat {

    private static final String NS = "http://graphml.graphdrawing.org/xmlns"; //XML Namespace

    @Override
    public void export( CxA cxa, PrintStream ps ) throws IOException {

        Element graphml = new Element( "graphml", NS );
        graphml.addNamespaceDeclaration( "xsi", "http://www.w3.org/2001/XMLSchema-instance");
        graphml.addAttribute( new Attribute( "xsi:schemaLocation", "http://www.w3.org/2001/XMLSchema-instance","http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd" ) );
        Element graph = new Element( "graph", NS );
        graph.addAttribute( new Attribute( "id", cxa.ID() ) );
        graph.addAttribute( new Attribute( "edgedefault", "directed" ) );
        graphml.appendChild( graph );
        for( Kernel k : cxa.getKernels() ) {
            Element node = new Element( "node", NS );
            node.addAttribute( new Attribute( "id", k.ID() ) );
            graph.appendChild( node );
        }
        for( Conduit c : cxa.getConduits() ) {
            Element edge = new Element( "edge", NS );
            edge.addAttribute( new Attribute( "id", c.ID() ) );
            edge.addAttribute( new Attribute( "source", c.getSenderKernel().ID() ) );
            edge.addAttribute( new Attribute( "target", c.getReceiverKernel().ID() ) );
            graph.appendChild( edge );
        }
        Document doc = new Document( graphml );
        Serializer serializer = new Serializer( new BufferedOutputStream( ps), "UTF8" );
        serializer.setIndent( 2 );
        serializer.setMaxLength( 80 );
        serializer.write( doc );

    }
}
