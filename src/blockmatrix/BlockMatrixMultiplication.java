
package blockmatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Arko
 */
    public class BlockMatrixMultiplication{
     static double[][] A;
     static double[][] B;
        
        
    public static void getA() throws IOException{
       BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter dimensions of matrix A:(SQUARE MATRIX N*N)");
        int n=sc.nextInt();
        A=new double[n][n];
        
        System.out.println("Enter matrix A:");
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                A[i][j]=Integer.parseInt(br.readLine());
            }
        }
    }
    public static void getB() throws IOException{
       BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter dimensions of matrix B:(SQUARE MATRIX N*N)");
        int n=sc.nextInt();
        B=new double[n][n];
        
        System.out.println("Enter matrix B:");
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                B[i][j]=Integer.parseInt(br.readLine());
            }
        }
    }

        public static double[][] randomSquareMatrix(int n){   //TO GENERATE A RANDOM 2000*2000 MATRIX
            double[][] mat = new double[n][n];
            
            Random rand = new Random();
            for(int i=0; i<n; i++) 
                for(int j=0; j<n; j++) 
                    mat[i][j]=rand.nextInt(10);
            
            return mat;
        }
        
        public static void printSquareMat(double[][] matrix){
            int n=matrix.length;
            for(int i=0; i<n; i++){ 
                for(int j=0; j<n; j++) 
                   System.out.print(matrix[i][j]+" "); 
              System.out.print("\n");
            }
            System.out.print("\n");
        }

        public static void average(double[][] matrix)
        {
            int n=matrix.length;
            double sum=0;
            for(int i=0; i<n; i++) 
                for(int j=0; j<n; j++) 
                    sum+=matrix[i][j];

            System.out.println("Average of all Elements of Matrix : "+(sum/(n*n)));
        }

        public static void matrixMultiplication(double[][] matrix1, double[][] matrix2, boolean printMatrix){

            int n=matrix1.length;
            double[][] resultMatrix = new double[n][n];

            double startTime = System.currentTimeMillis();

            for(int i=0; i<n; i++)
                for(int j=0; j<n; j++)
                    for(int k=0; k<n; k++) 
                        resultMatrix[i][j]+=matrix1[i][k]*matrix2[k][j];


            if (printMatrix && n<=5)
                printSquareMat(resultMatrix);
//                for(int i=0; i<n; i++){
//                    for(int j=0; j<n; j++)
//                        System.out.print(resultMatrix[i][j]+" ");
//                    System.out.print("\n"); }

            System.out.print("\n");
            System.out.println(((System.currentTimeMillis()-startTime)/1000)+" seconds for matrix of size "+n+" in main thread.");
            average(resultMatrix);
        }

        public static void matrixMultiplicationSingleThread(double[][] m1, double[][] m2)
        {
            int n=m1.length;
            double startTime = System.currentTimeMillis();
            Thread t = new Thread(new SingleThread(m1,m2));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("\n");
            System.out.println(((System.currentTimeMillis()-startTime)/1000)+" seconds for matrix of size "+n+" in external Thread.");

        }

        public static void matrixMultiplicationTranspose(double[][] matrix1, double[][] matrix2, boolean printMatrix) throws InterruptedException, ExecutionException{

            int n=matrix1.length;
            double[][] resultMatrix=new double[n][n];
            double tmp;
            ExecutorService exe = Executors.newFixedThreadPool(2);
            Future<Double>[][] result = new Future[n][n];
            double startTime = System.currentTimeMillis();
            for(int i=0; i<n; i++)
            {
                for(int j=0; j<=i; j++)
                {
                    tmp=matrix2[i][j];          //taking the transpose of matrix 2
                    matrix2[i][j]=matrix2[j][i];
                    matrix2[j][i]=tmp;
                }
            }

            for(int i=0; i<n; i++)
            {
                for(int j=0; j<n; j++)
                {
                    result[i][j] = exe.submit(new TransposeAlgorithm(matrix1[i],matrix2[j]));
                }
            }

            exe.shutdown();
            exe.awaitTermination(1, TimeUnit.DAYS);

            for(int i=0; i<n; i++)
            {
                for(int j=0; j<n; j++)
                {
                    resultMatrix[i][j] = result[i][j].get(); //get the result from the future
                }
            }
            for(int i=0; i<n; i++)
            {
                for(int j=0; j<=i; j++)
                {
                    tmp=matrix2[i][j];
                    matrix2[i][j]=matrix2[j][i];      //taking back the transpose
                    matrix2[j][i]=tmp;
                }
            }
            if (printMatrix && n<=5)
                printSquareMat(resultMatrix);
//                for(int i=0; i<n; i++){
//                    for(int j=0; j<n; j++)
//                        System.out.print(resultMatrix[i][j]+" ");
//                    System.out.print("\n"); }

            System.out.print("\n");
            System.out.println(((System.currentTimeMillis()-startTime)/1000)+" seconds for matrix of size "+n+" multithreaded with transpose algorithm for parallel multiplication.");
            average(resultMatrix);
        }

        public static void matrixMultiplicationParallel(double[][] matrix1, double[][] matrix2, boolean printMatrix) throws InterruptedException, ExecutionException{

            int n=matrix1.length;
            double[][] resultMatrix=new double[n][n];
            double tmp;
            ExecutorService exe = Executors.newFixedThreadPool(2);
            Future<Double>[][] result = new Future[n][n];
            double startTime = System.currentTimeMillis();


            for(int i=0; i<n; i++)
            {
                for(int j=0; j<n; j++)
                {
                    result[i][j] = exe.submit(new MultipleThreads(i,j,matrix1,matrix2));
                }
            }

            exe.shutdown();

            exe.awaitTermination(1, TimeUnit.DAYS);


            for(int i=0; i<n; i++)
            {
                for(int j=0; j<n; j++)
                {
                    resultMatrix[i][j] = result[i][j].get();   
                }
            }

            if (printMatrix && n<=5)
                printSquareMat(resultMatrix);
//                for(int i=0; i<n; i++){
//                    for(int j=0; j<n; j++) 
//                        System.out.print(resultMatrix[i][j]+" ");
//                    System.out.print("\n"); }

            System.out.print("\n");
            System.out.println(((System.currentTimeMillis()-startTime)/1000)+" seconds for matrix of size "+n+" multithreaded with multiple threads running in parallel.");
            average(resultMatrix);
        }
        
        public static void main(String[] args) throws IOException {
            
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            
          System.out.println("BLOCK MATRIX MULTIPLIER:\n");
          System.out.println("SELECT YOUR CHOICE:\n1.ENTER TWO MATRICES AND CHECK THIER PRODUCT\n2.CHECK EFFECIENCY FOR MULTIPLICATION OF 2000*2000 MATRIX");
          int ch=Integer.parseInt(br.readLine());
          switch(ch){
              case 1: getA(); 
                      getB();
                      matrixMultiplication(A,B,true);
                      matrixMultiplicationSingleThread(A,B);
                  try {
                    matrixMultiplicationTranspose(A,B, true);
                  } catch (InterruptedException | ExecutionException e) {
                     e.printStackTrace();
                  }
                  try {
                      matrixMultiplicationParallel(A,B, true);
                  } catch (InterruptedException | ExecutionException e){  
                     e.printStackTrace();
                 }
                  break;
              
            case 2: 
             double[][] A1 = randomSquareMatrix(2000);
             double[][] B1 = randomSquareMatrix(2000);

             matrixMultiplication(A1,B1,true);
             matrixMultiplicationSingleThread(A1,B1);
              try {
                   matrixMultiplicationTranspose(A1,B1, true);
              } catch (InterruptedException | ExecutionException e) {
                  e.printStackTrace();
              }
              try {
                matrixMultiplicationParallel(A1,B1, true);
              } catch (InterruptedException | ExecutionException e){  
                e.printStackTrace();
            }
        }
      }

      
    }

