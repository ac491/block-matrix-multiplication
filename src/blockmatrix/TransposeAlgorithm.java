/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockmatrix;

import java.util.concurrent.Callable;

/**
 *
 * @author Arko
 */
 public class TransposeAlgorithm implements Callable<Double>{

            TransposeAlgorithm(double[] vec1, double[] vec2){
                this.vec1=vec1; this.vec2=vec2;
            }
            double result;
            double[] vec1, vec2;

            @Override
            public Double call() {
                result=0;
                for(int i=0; i<vec1.length; i++) 
                    result+=vec1[i]*vec2[i];
                return result;
            }
        }