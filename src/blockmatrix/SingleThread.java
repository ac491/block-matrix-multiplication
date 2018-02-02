
package blockmatrix;

/**
 *
 * @author Arko
 */
 public class SingleThread implements Runnable{

            double[][] matrix1, matrix2;

            SingleThread(double[][] m1, double[][] m2){
                matrix1=m1;
                matrix2=m2;
            }
            public static void matrixMultiplication(double[][] matrix1, double[][] matrix2, boolean printMatrix){

                int n=matrix1.length;
                double[][] resultMatrix = new double[n][n];

                for(int i=0; i<n; i++)for(int j=0; j<n; j++)for(int k=0; k<n; k++) resultMatrix[i][j]+=matrix1[i][k]*matrix2[k][j];

                BlockMatrixMultiplication.average(resultMatrix);
            }

            @Override
            public void run() {
                matrixMultiplication(matrix1, matrix2, false);
            }
        }