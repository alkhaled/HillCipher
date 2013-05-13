import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/*
method tree

                   invert matrix
		         |
		 ScalarMatrixMult 
                 /              \
	  inverse(scalar)      adjoint(matrix)
	   /                      \
      determinant                 determinant

 */

public class HillCipher{

   static int mod = 256; // set the modulus to equal the size of the ascii character set

    public static void main ( String [] args) throws IOException{
	
	String knownCiphertext = args[0];
	String knownCleartext  = args[1];
	String ciphertext      = args[2];

	int keySize = 9;
	int [][] knownCipher = readAsArray(knownCiphertext, keySize);
	int [][] knownClear  = readAsArray(knownCleartext , keySize);
	int [][] cipher      = readAsArray(ciphertext     , 1274);
    	

	int [][] invertedClear = invertMatrix(knownClear);

	// key = invertedcleartext * knownCiphertext
	int [][] invKey = invertMatrix(mult(invertedClear, knownCipher));

	// decrypted text = unknownCipher * inverted key
	int [][] ans = mult(cipher, invKey);
	print(ans);

    }


    public static int [][] invertMatrix ( int [][] matrix){
	// inverse(m) = (inverse(determinant)*m) * adjoint(m)
	//                   scalar                  matrix
	int invdet = modInverse(determinant(matrix));
	return scalarMatrixMult(adjoint(matrix), invdet); // scalarMatrixMult is modular so we dont have to worry about modding the result.
	
    }
   
    public static int[][] scalarMatrixMult (int [][] matrix, int x){

	for( int i = 0; i < matrix.length; i++){

	    for ( int j = 0; j < matrix[0].length; j++){
		matrix[i][j] = mod((matrix[i][j]*x));
	    }
	}
	return matrix;
    }


    /*returns the adjoint of the matrix
      the adjoint of a matrix if the matrix where for every i,j pair
      the i,j entry in the adjoint is just the determinant of the original matrix
      with the ith and jth rows removed. ( the values also alternate being positive and negative)
      finally the resulting matrix is transposed.
    */
    public static int[][] adjoint ( int [][] matrix){
	int rowlength = matrix.length;
	int collength = matrix[0].length;
	int [][] newMatrix;

	int [][] adj = new int [rowlength][collength];

	for (int r = 0; r < rowlength; r++){
	    for ( int c = 0; c < collength; c++){
		newMatrix = removeRowCol(matrix,c,r);// switch the row and col to be deleted so that the matrix is transposed
		adj[r][c] += (int)Math.pow(-1, r + c) * determinant(newMatrix); 
	    }

	}
	
	return adj;
    
    }

    // returns the inverse of x mod 256
    public static int modInverse (int x){
	for ( int i = 0 ; i < mod; i++){
	    if ( mod(x*i) == 1)
		return i;
	}
	System.out.println("ERROR: NO INVERSE FOUND");
	return -1;
    }
    

    // returns the determinant of a matrix % mod    
    public static int determinant ( int [][] matrix){
	
	// if the matrix has a single row the determinant is just the first element
	if (matrix.length == 1) 
	    return mod(matrix[0][0]);

	

	// if the matrix is 2X2 the detereminant is ad - bc 
	//   Det |a b|  = ad - bc
	//       |c d|
	
	if (matrix.length == 2 && matrix[0].length ==2)
	    return (mod(((matrix[0][0] * matrix[1][1])  -
			 (matrix[0][1] * matrix[1][0]))));
		     

	// for all other matrices pick a row and col
	// let M(i,j) = our original matrix with row i and col j removed)
	// then the determinant is the Sum for j [0:colsize] of  ((-1) ^ (i+j)) + matrix[i][j]*M[i,j]

	int determinant = 0;
	int [][] minor;
	for (int row = 0; row < matrix.length; row++){
	    minor = removeRowCol(matrix, row, 0);
	    determinant += (int)Math.pow(-1, row)*matrix[row][0]*determinant(minor);
	}
	    

	return mod(determinant);
	
    }

    // removes the specified row and col from the matrix and returns the new matrix
    public static int [][] removeRowCol ( int [][] matrix, int row , int col){
	
	int oldRowLength = matrix.length;
	int oldColLength = matrix[0].length;
	int [][] newMatrix = new int[oldRowLength -1][oldColLength -1];
	
	// indices into the new matrix
	int newr = 0;
	int newc = 0;

	for (int r = 0; newr < oldRowLength -1 ; r++, newr++){	    
	   
	    if (r == row) r++; // skip the desired row
	    newc = 0;
	    
	    for (int c = 0; newc < oldColLength -1; c++, newc++){
	       	
		if ( c == col) c++;// skip the desired col	
		newMatrix[newr][newc] = matrix[r][c];

	    }
	}	

	return newMatrix;
    }


    public static int[][] readAsArray (String filename, int rows) throws IOException{

	File f              = new File(filename);
	FileInputStream fis = new FileInputStream(filename);
	
	int collength = (int)f.length()/rows;
	
	System.out.println("Reading: " + filename + "Into matrix with: \n" +
			   "Rows: " + rows+ "\n"+
			   "Cols: " + collength + "\n");
	
	int [][] matrix = new int [rows][collength];
	for ( int i = 0; i < rows; i++){
	    
	    for ( int j = 0; j < collength; j++){
		matrix[i][j] = fis.read();
	    }
	    
	}
	return matrix;
    }
    
    // returns the matrix multiplication of a*b
    public static int[][] mult(int[][] a, int[][] b) {
	
	if (a[0].length !=b.length) System.out.print("ERROR: ROWS DONT MATCH COLS FOR MATRIX MULT");
	int [][] result = new int [a.length][b[0].length];
	// multiply the matrices
	for ( int i = 0; i < a.length; i++){
	    for ( int j = 0; j< b[0].length; j++){
		for ( int k = 0; k < b.length; k++){
		    result[i][j] += a[i][k] *b[k][j];
		}
	    }
	}
	
	// take the result matrix and mod it.
	for ( int i = 0; i < result.length; i++){
	    for ( int j = 0;j < result[0].length; j++){
		result[i][j]=mod(result[i][j]);
	    }
	}
	
	return result;		
    }
    

    // this mod method fixes the problem with java's mod of negative numbers
    public static int mod ( int x){
	if ( x>= 0) 
	    return x%mod;
	else 
	    return( (x%mod) + mod);
    }


    //prints out the final cleartext
    public static void print( int [][] matrix){
	for (int i = 0; i < matrix.length; i++){
	    for ( int j = 0 ; j < matrix[0].length ; j++){
		System.out.print ((char)matrix[i][j]);
	    }
	}
    }
    // prints out the contents of a matrix formatted 
    public static void printf ( int [][] matrix){
	for (int i = 0; i < matrix.length; i++){
	    for ( int j = 0 ; j < matrix[0].length ; j++){
		System.out.print (" " + matrix[i][j]);
	    }
	    System.out.println();
	    }
    }
}