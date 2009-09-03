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
package cxa.examples.laplace;

import cxa.CxA;
import cxa.util.Properties;

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
        prop.putInt("total.time", totalTime );
        prop.putInt("width", width);
        prop.putInt("height", height);
        prop.putInt("total.width", totalWidth);
        prop.putInt("num.workers", numOfSolvers);
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
