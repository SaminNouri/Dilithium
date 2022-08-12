import java.math.BigInteger;

public class Test {

    static void store64(int[] x, int beginIndex, BigInteger u) {
        for (int i = beginIndex; i < beginIndex + 8; i++) {
            BigInteger temp = u.shiftRight(8 * i);
            temp = temp.and(new BigInteger("255"));
            x[i] = temp.intValue();
            System.out.println(Integer.toBinaryString(x[i]));

        }
    }

    static BigInteger load64(int[] x, int beginIndex) {
        BigInteger r = new BigInteger("0");
        BigInteger xx;
        for (int i = beginIndex; i < beginIndex + 8; i++) {
            xx = new BigInteger(String.valueOf(x[i]));
            r = r.or(xx.shiftLeft(8 * i));
        }
        return r;
    }

    static long br_dec32le(int[] src)
    {
        return (long) src[0]
                | ((long)src[1] << 8)
                | ((long)src[2] << 16)
                | ((long)src[3] << 24);
    }


    public static void main(String args[])
    {
        int[] buf=new int[168];
        int SEEDBYTES= 2;
        int[] seed ={
                (byte)1, (byte)2
        } ;
       // BigInteger b=new BigInteger("1024000000");
        //System.out.println(b.toString(2));
       // store64(buf,0,b);
       // System.out.println(load64(buf,0));

        int[] seed1 ={
                1,2,3,23
        } ;

        long p=br_dec32le(seed1);
        System.out.println(Long.toBinaryString(p));

        BigInteger b1 = new BigInteger("2");
        int exponent = 64;
        BigInteger ones = b1.pow(exponent);
        ones=ones.subtract(new BigInteger("1"));
        //BigInteger a=new BigInteger("000000000000FFFF",16);

        System.out.println("a:"+ones);

        System.out.println(0x00FF00FFL);


    }
}
