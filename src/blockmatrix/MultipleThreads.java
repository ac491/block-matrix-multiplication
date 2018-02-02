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
 public class MultipleThreads implements Callable<Double>{
     
            int r,c;
            double result;
            double[][] vec1, vec2;

            MultipleThreads(int r, int c, double[][] vec1, double[][] vec2){
                this.r=r; 
                this.c=c; 
                this.vec1=vec1; 
                this.vec2=vec2;
            }
           
            @Override
            public Double call() {
                result=0;
                for(int i=0; i<vec1.length; i++) 
                    result+=vec1[r][i]*vec2[i][c];
                return result;
            }
        }