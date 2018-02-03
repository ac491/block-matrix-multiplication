# block-matrix-multiplication

I have multiplied a 2000*2000 random matrix in four senarios to check for the most efficient case:

1. the matrix is multiplied in main(). It takes a lot of time (~2 minutes) to multiply a 2000 *2000 matrix.
2. In a single thread to show if the operation becomes slow due to other running processes, but it also takes a lot of time.
3. when multiplied in multiple threads, it took approx. 102 seconds
4. when i used a transpose algorithm which multiplies two vectors with O(n) complexity, it just takes 11 second to multiply.
