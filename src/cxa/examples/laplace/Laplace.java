/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cxa.examples.laplace;

import cxa.CxA;
import java.util.Properties;

/**
 *
 * @author falcone
 */
public class Laplace {

    public static void main(String[] args) throws InterruptedException {

        int totalWidth = Integer.parseInt(args[0]);
        int height = totalWidth;
        int numOfSolvers = Integer.parseInt(args[1]);
        int totalTime = Integer.parseInt(args[2]);
        //int totalWidth = 120;
        //int numOfSolvers = 2;
        int width = totalWidth / numOfSolvers;

        CxA.logger().setLevel(java.util.logging.Level.FINE);
        CxA cxa = new CxA("Laplace");

        Properties prop = cxa.properties();
        prop.setProperty("total.time", ""+totalTime );
        prop.setProperty("width", "" + width);
        prop.setProperty("height", "" + height);
        prop.setProperty("total.width", "" + totalWidth);
        prop.setProperty("num.workers", "" + numOfSolvers);
        //prop.setProperty("out.file", "laplace.pgm");

        for (int i = 0; i < numOfSolvers; i++) {
            cxa.addKernel(Solver.class, "Solver" + i);
        }
        //cxa.addKernel(Collector.class, "Collector");

        for (int i = 0; i < numOfSolvers; i++) {
            int j = (i + 1) % numOfSolvers;
            cxa.connect("Solver" + i + ".leftOut")
                       .to( "Solver" + j + ".rightIn")
                       .with("Conduit" + i + "-" + j);
            cxa.connect("Solver" + j + ".rightOut")
                       .to("Solver" + i + ".leftIn")
                       .with("Conduit" + j + "-" + i);
            /*cxa.connect("Solver"+i+".collector",
                        "Collector.in["+i+"]",
                        "ConduitCollector"+i);*/
        }
        cxa.execute();
    }
}
