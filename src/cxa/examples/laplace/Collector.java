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
import cxa.components.StopSignalException;
import cxa.components.conduits.ConduitExit;
import cxa.components.kernel.Kernel;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.*;

/**
 *
 * @author falcone
 */
public class Collector implements Kernel {

    private CxA cxa;
    private String id;
    private int numProd;
    private ConduitExit<double[][]>[] in;
    private int height;
    private int width;
    private int stripeWidth;
    private double[][] domain;
    private double maxValue;
    private String filename;

    public void initialize(String ID, CxA cxa) {
        this.cxa = cxa;
        id = ID;
        numProd = cxa.properties().getInt("num.workers");
        height = cxa.properties().getInt("height");
        width = cxa.properties().getInt("total.width");
        stripeWidth = width / numProd;
        domain = new double[height][width];
        maxValue = 0;
        filename = cxa.properties().getString("out.file");
        in = new ConduitExit[numProd];
    }

    public String ID() {
        return id;
    }

    @Override
    public void mainLoop() throws StopSignalException {
        try{
        for (int i = 0; i < numProd; i++) {
                int offset = stripeWidth * i;
                double[][] stripe = in[i].receive();
                for (int j = 0; j < stripeWidth; j++) {
                    updateMax(stripe[j]);
                    domain[j + offset] = stripe[j];
                }
                CxA.logger().info(id +" collected results from solver " + i);
            }
            writeImage();
            CxA.logger().info(id +" just writed the image.");
        } catch (IOException ex) {
            CxA.logger().severe(id + ": trouble while saving file. " + ex);
        }
    }

    private void updateMax(double[] d) {
        for (int i = 0; i < d.length; i++) {
            maxValue = max(maxValue, d[i]);
        }
    }

    private void writeImage() throws IOException {
        final int GREY_LEVELS = 256;
        PrintWriter pw = new PrintWriter(new File(filename));
        pw.println("P2");
        pw.println(width + " " + height);
        pw.println(GREY_LEVELS);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pgmVal = (int) ceil(domain[x][y] * GREY_LEVELS / maxValue );
                pgmVal = max(0, pgmVal);
                pw.print(pgmVal + " ");
            }
            pw.println();
        }
        pw.close();
    }
}
