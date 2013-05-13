public class Strassen{


   
    public static int[][] sMult( int [][]a, int [][]b){
    int n = a.length;
    int [][] c = new int [n][n];

    if ( n == 1){
        c[0][0] = a[0][0]*b[0][0];
        return c;
    }
    int [][] a11 = new int [n/2][n/2];
    int [][] a12 = new int [n/2][n/2];
    int [][] a21 = new int [n/2][n/2];
    int [][] a22 = new int [n/2][n/2];
    int [][] b11 = new int [n/2][n/2];
    int [][] b12 = new int [n/2][n/2];
    int [][] b21 = new int [n/2][n/2];
    int [][] b22 = new int [n/2][n/2];

    int m = (n/2)-1;
    n = a.length-1;
    a11 = copy ( a , a11 , 0,  0);
    a12 = copy ( a , a12 , m+1 , 0 );
    a21 = copy ( a , a21 , 0 , m+1 );
    a22 = copy ( a , a22 , m+1 , m+1);
   
    b11 = copy ( b , b11 , 0 , 0 );
    b12 = copy ( b , b12 , m+1 , 0);
    b21 = copy ( b , b21 , 0 , m+1 );
    b22 = copy ( b , b22 , m+1 , m+1);

   
    int [][] s1 = subMatrix (b12, b22);
    int [][] s2 = addMatrix (a11, a12);
    int [][] s3 = addMatrix (a21, a22);
    int [][] s4 = subMatrix (b21, b11);
    int [][] s5 = addMatrix (a11, a22);
    int [][] s6 = addMatrix (b11, b22);
    int [][] s7 = subMatrix (a12, a22);
    int [][] s8 = addMatrix (b21, b22);
    int [][] s9 = subMatrix (a11, a21);
    int [][] s10= addMatrix (b11, b12);

    int [][] p1 = sMult (a11, s1);
    int [][] p2 = sMult (s2,  b22);
    int [][] p3 = sMult (s3,  b11);
    int [][] p4 = sMult (a22, s4);   
    int [][] p5 = sMult (s5,  s6);
    int [][] p6 = sMult (s7,  s8);
    int [][] p7 = sMult (s9,  s10);

    int [][] g1 = addMatrix(p5,p4);
    int [][] g3 = addMatrix(p5,p1);
   
    int [][] c11 = addMatrix(subMatrix(g1,p2),p6);
    int [][] c12 = addMatrix(p1,p2);
    int [][] c21 = addMatrix(p3,p4);
    int [][] c22 = subMatrix(subMatrix(g3,p3),p7);
   
    c = combine(c11,c12,c21,c22);
   
    return c;               
    }   


    public static int [][] copy ( int [][] parent, int [][] sub, int sRow, int sCol){
	for ( int i = 0, i2 = sCol; i < sub.length; i++, i2++){
	    for ( int j = 0, j2 = sRow; j< sub.length; j++, j2++){
		sub[i][j] = parent[i2][j2];
	    }
	}
	return sub;
    }
    
    public static int[][] addMatrix ( int [][] a , int [] [] b){
	
	int [][]d = new int [a.length][a.length];
	for ( int i = 0; i < a.length; i++){
	    for ( int j = 0; j < a.length; j++){
		d[i][j] = a[i][j] + b[i][j];
	    }
	}
	return d;
    }
    
    public static int[][] subMatrix ( int [][] a , int [] [] b){
    int [][]d = new int [a.length][a.length];
    for ( int i = 0; i < a.length; i++){
        for ( int j = 0; j < a.length; j++){
        d[i][j] = a[i][j] - b[i][j];
        }
    }
    return d;
    }
    public static int [][] combine ( int[][] c11 , int [][]c12 , int [][] c21, int [][] c22){
    int n = (c11.length)*2 ;
    int [][]d = new int [n][n];
    for ( int i = 0, i2 = 0 ; i < c11.length; i++, i2++){
        for ( int j = 0, j2 = 0 ; j < c11.length; j++, j2++){
        d[i2][j2] = c11[i][j];
	}
    }
    for ( int l = 0, l2 = 0; l< c12.length; l++, l2++){
        for ( int m =0, m2 = c12.length; m <c12.length ; m++,m2++){
        d[l2][m2] = c12[l][m];
        }
    }
    for ( int o =0, o2 = c21.length; o < c21.length; o++, o2++){
        for ( int p = 0, p2 = 0; p < c21.length; p++, p2++){
        d[o2][p2] = c21[o][p];
        }
    }
    for ( int q =0, q2 = c22.length; q < c22.length; q++, q2++){
        for ( int r = 0, r2 = c22.length ; r < c22.length ; r++, r2++){
        d[q2][r2] = c22[q][r];
        }
    }
    return d;
    }
}