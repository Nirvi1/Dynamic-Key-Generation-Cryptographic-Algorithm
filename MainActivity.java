package com.example.nirvi.cryptography;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView tv;
    String s;

    public static final String alpha = "abcdefghijklmnopqrstuvwxyz";
    int inputText[][]  =   new int[16][16];
    int key[]   =   new int[4];
    int quo[][] =   new int[16][16];
    int transpose[][]   =   new int[4][4];

    int[][] p1;
    StringBuilder cipher    =   new StringBuilder(16);
    String bytes;
    int alpha_position;
    char ch;
    StringBuilder ci    =   new StringBuilder(16);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.hello);
        s = "bye";
        int i=0,j,k,p=0,h=0;
        for(k=0;k<4;k++) {
         for (j = 0; j < 4; j++) {

             if (i<s.length())
             {
                 char r=s.charAt(i);

                 inputText[k][j] = (int)r;

             i++;

            }
             else
                 inputText[k][j]=0;

             Log.d("input text",String.valueOf(inputText[k][j]));
         }

     }

        generateKey();
        //only second and third rows
        leftRotateByOne();
        //only first and last rows
        //ascending();
        transpose();
        addKey();
        applyModulus26();
        generateCipher();
        decrypt();
    }
    void decrypt()
    {
       p1=new int[4][4];
        int k=0;
        String quo=cipher.substring(20,cipher.length());
        int t;
        cipher.delete(20, cipher.length());
        String cc=cipher.substring(0, 16);
        cipher.delete(0, 16);

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                t =  Integer.parseInt(String.valueOf(quo.charAt(k)))*26 + alpha.indexOf(cc.charAt(k)) - Integer.parseInt(String.valueOf(cipher.charAt(j)));
                p1[i][j]=t;
                k++;
            }
        }
        
        for (int c = 0; c < 4; c++)
            for( int d = 0 ; d < 4 ; d++ )
                transpose[d][c] = p1[c][d];
        for (int c = 0; c < 4; c++)
            for( int d = 0 ; d < 4 ; d++ ) {
                p1[c][d] = transpose[c][d];
            }
        int init,result;
        for (int j = 3; j >0; j--) {
            init    =   p1[1][j];
            result  =   p1[2][j];
            p1[2][j]    =   p1[2][j-1];
            p1[2][j-1]  =   result;
            p1[1][j] = p1[1][j-1];
            p1[1][j-1] = init1;
        }
        
        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
               tv.append(String.valueOf((char)p1[i][j]));
            }
        }

    }
    
    void generateCipher()
    {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                ch = alpha.charAt(inputText[i][j]);
                cipher.append(ch);

            }
        }
        for (int i = 0; i < 4; ++i)
        {
            cipher.append(key[i]);
        }
        for (int i = 0; i < 4; ++i)
        {
            for(int j=0;j<4;++j)
            cipher.append(quo[i][j]);

        }
    }
void generateKey()
{
    int v=0,c=0,d=0,sp=0;
    for(int i=0;i<s.length();i++) {
        if (s.charAt(i) == 'a' || s.charAt(i) == 'e' || s.charAt(i) == 'i' || s.charAt(i) == 'o' || s.charAt(i) == 'u' || s.charAt(i) == 'A' || s.charAt(i) == 'E' || s.charAt(i) == 'I' || s.charAt(i) == 'O' || s.charAt(i) == 'U')
            ++v;
        else if ((s.charAt(i) >= 'a' && s.charAt(i) <= 'z') || (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z'))
            ++c;
        else if (s.charAt(i) >= '0' && c <= '9'|| s.charAt(i) == ' ')
            ++d;
        else
            sp++;

    }
    v   =   s.length()-v;
    c   =   s.length()-c;

    d   =  s.length()-d;

    sp  = s.length()-sp;
    int xor1,xor2,xor3,xor4;
    xor1    =   v^c;
    xor2    =   c^d;
    xor3    =   d^sp;
    xor4    =   sp^v;
    int k   =   0;

    key[k]  =   xor1%9;
    key[++k]    =   xor2%9;
    key[++k]    =   xor3%9;
    key[++k]    =   xor4%9;

}
    
    void leftRotateByOne() {
        int j;
        int first,second;
        first = inputText[1][0];
            for (j = 0; j < 3; j++) {
                first=inputText[1][j];
                second=inputText[2][j];
                inputText[2][j]=inputText[2][j+1];
                inputText[2][j+1]=second;
                inputText[1][j] = inputText[1][j + 1];
                inputText[1][j+1] = first;
            }

    }
    
    void transpose()
    {
        for (int c = 0; c < 4; c++)
            for( int d = 0 ; d < 4 ; d++ )
                transpose[d][c] = inputText[c][d];
        for (int c = 0; c < 4; c++)
            for( int d = 0 ; d < 4 ; d++ ) {
                inputText[c][d] = transpose[c][d];
               // Log.d("aftert", String.valueOf(in[c][d]));
            }
    }

    void addKey()
    {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                inputText[i][j] = (inputText[i][j]+key[j]);
            }
        }
    }
    
    void applyModulus26() {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                quo[i][j]= (inputText[i][j] / 26);
                if (inputText[i][j] >= 26) {
                    inputText[i][j] =(inputText[i][j] % 26);
                }
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
