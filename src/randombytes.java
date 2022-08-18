import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

public class randombytes {


    static void randombytes(int outIndex, int[] out, int outlen) {

        File file = new File("/dev/random");
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[outlen];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();

            for (int i = 0; i < bFile.length; i++) {
                int temp = bFile[i] & 0xff;
                out[outIndex+i]=temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}








