import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class aes256ctr {

    static Long allOne8 = 255L;

    static BigInteger allOne32=new BigInteger("4294967295");

    static BigInteger allOne64 = new BigInteger("18446744073709551615");

    static int allOne16=65535;






    static class PairBigInteger{
        public BigInteger x;
        public BigInteger y;

        public PairBigInteger(BigInteger x,BigInteger y) {
            this.y = y;
            this.x = x;
        }
    }

    //Tested
  static long br_dec32le(int srcIndex,int[] src)
    {
        return (long) src[srcIndex]
                | ((long)src[srcIndex+1] << 8)
                | ((long)src[srcIndex+2] << 16)
                | ((long)src[srcIndex+3] << 24);
    }
    //tested!
    static void br_range_dec32le(int vIndex, long[] v, int num,int srcIndex, int[] src)
    {

        while (num-- > 0) {
		v[vIndex++] = br_dec32le(srcIndex,src);
            srcIndex += 4;
        }
    }

    static long br_swap32(long x)
    {

        Long allOne32=aes256ctr.allOne32.longValue();

        Long temp=0x00FF00FFL & allOne32;
        Long temp1=0x00FF00FFL & allOne32;
        x = ( ((((x & temp)& allOne32)<< 8)&allOne32)
               | ((((x >> 8)&allOne32) & temp1)&allOne32)
                ) & allOne32;


        return (((x << 16)&allOne32) | ((x >> 16)&allOne32)&allOne32);
    }
    //tested
    static void br_enc32le(int dstIndex, int[] dst, long x)
    {
        dst[dstIndex] = (int)(x & allOne8);
        dst[dstIndex+1] = (int)((x >> 8)&allOne8);
        dst[dstIndex+2] = (int)((x >> 16) & allOne8);
        dst[dstIndex+3] = (int)((x >> 24) & allOne8);
    }
    //tested
    static void br_range_enc32le(int dstIndex,int[] dst,int vIndex, long[] v, int num)
    {
        while (num-- > 0) {
            br_enc32le(dstIndex,dst, v[vIndex++]);
            dstIndex += 4;
        }
    }
     //tested
    static void br_aes_ct64_bitslice_Sbox(BigInteger[] q)
    {


        BigInteger x0, x1, x2, x3, x4, x5, x6, x7;
        BigInteger y1, y2, y3, y4, y5, y6, y7, y8, y9;
        BigInteger y10, y11, y12, y13, y14, y15, y16, y17, y18, y19;
        BigInteger y20, y21;
        BigInteger z0, z1, z2, z3, z4, z5, z6, z7, z8, z9;
        BigInteger z10, z11, z12, z13, z14, z15, z16, z17;
        BigInteger t0, t1, t2, t3, t4, t5, t6, t7, t8, t9;
        BigInteger t10, t11, t12, t13, t14, t15, t16, t17, t18, t19;
        BigInteger t20, t21, t22, t23, t24, t25, t26, t27, t28, t29;
        BigInteger t30, t31, t32, t33, t34, t35, t36, t37, t38, t39;
        BigInteger t40, t41, t42, t43, t44, t45, t46, t47, t48, t49;
        BigInteger t50, t51, t52, t53, t54, t55, t56, t57, t58, t59;
        BigInteger t60, t61, t62, t63, t64, t65, t66, t67;
        BigInteger s0, s1, s2, s3, s4, s5, s6, s7;

        x0 = q[7];
        x1 = q[6];
        x2 = q[5];
        x3 = q[4];
        x4 = q[3];
        x5 = q[2];
        x6 = q[1];
        x7 = q[0];

        y14 =( x3.xor(x5)).and(allOne64);
        y13 =( x0 .xor( x6)).and(allOne64);
        y9 =( x0 .xor( x3)).and(allOne64);
        y8 =( x0 .xor( x5)).and(allOne64);
        t0 =( x1 .xor( x2)).and(allOne64);
        y1 =( t0 .xor( x7)).and(allOne64);
        y4 =( y1 .xor( x3)).and(allOne64);
        y12 =( y13 .xor( y14)).and(allOne64);
        y2 =( y1 .xor( x0)).and(allOne64);
        y5 =( y1 .xor( x6)).and(allOne64);
        y3 =( y5 .xor( y8)).and(allOne64);
        t1 =( x4 .xor( y12)).and(allOne64);
        y15 =( t1 .xor( x5)).and(allOne64);
        y20 =( t1 .xor( x1)).and(allOne64);
        y6 =( y15 .xor( x7)).and(allOne64);
        y10 =( y15 .xor( t0)).and(allOne64);
        y11 =( y20 .xor( y9)).and(allOne64);
        y7 =( x7 .xor( y11)).and(allOne64);
        y17 =( y10 .xor( y11)).and(allOne64);
        y19 =( y10 .xor( y8)).and(allOne64);
        y16 =( t0 .xor( y11)).and(allOne64);
        y21 =( y13 .xor( y16)).and(allOne64);
        y18 =( x0 .xor( y16)).and(allOne64);


        t2 =( y12 .and( y15)).and(allOne64);
        t3 =( y3 .and( y6)).and(allOne64);
        t4 =( t3 .xor( t2)).and(allOne64);
        t5 =( y4 .and( x7)).and(allOne64);
        t6 =( t5 .xor( t2)).and(allOne64);
        t7 =( y13 .and( y16)).and(allOne64);
        t8 =( y5 .and( y1)).and(allOne64);
        t9 =( t8 .xor( t7)).and(allOne64);
        t10 =( y2 .and( y7)).and(allOne64);
        t11 =( t10 .xor( t7)).and(allOne64);
        t12 =( y9 .and( y11)).and(allOne64);
        t13 =( y14 .and( y17)).and(allOne64);
        t14 =( t13 .xor( t12)).and(allOne64);
        t15 =( y8 .and( y10)).and(allOne64);
        t16 =( t15 .xor( t12)).and(allOne64);
        t17 =( t4 .xor( t14)).and(allOne64);
        t18 =( t6 .xor( t16)).and(allOne64);
        t19 =( t9 .xor( t14)).and(allOne64);
        t20 =( t11 .xor( t16)).and(allOne64);
        t21 =( t17 .xor( y20)).and(allOne64);
        t22 =( t18 .xor( y19)).and(allOne64);
        t23 =( t19 .xor( y21)).and(allOne64);
        t24 =( t20 .xor( y18)).and(allOne64);

        t25 =( t21 .xor( t22)).and(allOne64);
        t26 =( t21 .and( t23)).and(allOne64);
        t27 =( t24 .xor( t26)).and(allOne64);
        t28 =( t25 .and( t27)).and(allOne64);
        t29 =( t28 .xor( t22)).and(allOne64);
        t30 =( t23 .xor( t24)).and(allOne64);
        t31 =( t22 .xor( t26)).and(allOne64);
        t32 =( t31 .and( t30)).and(allOne64);
        t33 =( t32 .xor( t24)).and(allOne64);
        t34 =( t23 .xor( t33)).and(allOne64);
        t35 =( t27 .xor( t33)).and(allOne64);
        t36 =( t24 .and( t35)).and(allOne64);
        t37 =( t36 .xor( t34)).and(allOne64);
        t38 =( t27 .xor( t36)).and(allOne64);
        t39 =( t29 .and( t38)).and(allOne64);
        t40 =( t25 .xor( t39)).and(allOne64);

        t41 =( t40 .xor( t37)).and(allOne64);
        t42 =( t29 .xor( t33)).and(allOne64);
        t43 =( t29 .xor( t40)).and(allOne64);
        t44 =( t33 .xor( t37)).and(allOne64);
        t45 =( t42 .xor( t41)).and(allOne64);
        z0 =( t44 .and( y15)).and(allOne64);
        z1 =( t37 .and( y6)).and(allOne64);
        z2 =( t33 .and( x7)).and(allOne64);
        z3 =( t43 .and( y16)).and(allOne64);
        z4 =( t40 .and( y1)).and(allOne64);
        z5 =( t29 .and( y7)).and(allOne64);
        z6 =( t42 .and( y11)).and(allOne64);
        z7 =( t45 .and( y17)).and(allOne64);
        z8 =( t41 .and( y10)).and(allOne64);
        z9 =( t44 .and( y12)).and(allOne64);
        z10 =( t37 .and( y3)).and(allOne64);
        z11 =( t33 .and( y4)).and(allOne64);
        z12 =( t43 .and( y13)).and(allOne64);
        z13 =( t40 .and( y5)).and(allOne64);
        z14 =( t29 .and( y2)).and(allOne64);
        z15 =( t42 .and( y9)).and(allOne64);
        z16 =( t45 .and( y14)).and(allOne64);
        z17 =( t41 .and( y8)).and(allOne64);

        t46 =( z15 .xor( z16)).and(allOne64);
        t47 =( z10 .xor( z11)).and(allOne64);
        t48 =( z5 .xor( z13)).and(allOne64);
        t49 =( z9 .xor( z10)).and(allOne64);
        t50 =( z2 .xor( z12)).and(allOne64);
        t51 =( z2 .xor( z5)).and(allOne64);
        t52 =( z7 .xor( z8)).and(allOne64);
        t53 =( z0 .xor( z3)).and(allOne64);
        t54 =( z6 .xor( z7)).and(allOne64);
        t55 =( z16 .xor( z17)).and(allOne64);
        t56 =( z12 .xor( t48)).and(allOne64);
        t57 =( t50 .xor( t53)).and(allOne64);
        t58 =( z4 .xor( t46)).and(allOne64);
        t59 =( z3 .xor( t54)).and(allOne64);
        t60 =( t46 .xor( t57)).and(allOne64);
        t61 =( z14 .xor( t57)).and(allOne64);
        t62 =( t52 .xor( t58)).and(allOne64);
        t63 =( t49 .xor( t58)).and(allOne64);
        t64 =( z4 .xor( t59)).and(allOne64);
        t65 =( t61 .xor( t62)).and(allOne64);
        t66 =( z1 .xor( t63)).and(allOne64);
        s0 =( t59 .xor( t63)).and(allOne64);
        s6 =( t56 .xor(allOne64.subtract(t62))).and(allOne64);
        s7 =( t48 .xor(allOne64.subtract(t60))).and(allOne64);
        t67 =( t64 .xor( t65)).and(allOne64);
        s3 =( t53 .xor( t66)).and(allOne64);
        s4 =( t51 .xor( t66)).and(allOne64);
        s5 =( t47 .xor( t65)).and(allOne64);
        s1 =( t64 .xor(allOne64.subtract(s3))).and(allOne64);
        s2 =( t55 .xor(allOne64.subtract(t67))).and(allOne64);

        q[7] = s0;
        q[6] = s1;
        q[5] = s2;
        q[4] = s3;
        q[3] = s4;
        q[2] = s5;
        q[1] = s6;
        q[0] = s7;


    }

    //tested
   static PairBigInteger SWAPN(BigInteger cl,BigInteger ch,int s,BigInteger x,BigInteger y)   {

       // do {
            BigInteger a, b;
            a = (x);
            b = (y);
            (x) = (a.and(cl)).or ((b.and(cl)) .shiftLeft(s));
            x=x.and(allOne64);
            (y) = ((a.and(ch)).shiftRight(s)) .or (b.and(ch));
            y=y.and(allOne64);
       // } while (0);

        return new PairBigInteger(x,y);

    }
     //tested
    static PairBigInteger SWAP2(BigInteger x,BigInteger y)   {
        return SWAPN(new BigInteger("5555555555555555",16), new BigInteger("AAAAAAAAAAAAAAAA",16), 1, x, y);
    }

    //tested
    static PairBigInteger SWAP4(BigInteger x,BigInteger y) {
        return SWAPN(new BigInteger("3333333333333333",16), new BigInteger("CCCCCCCCCCCCCCCC",16),  2, x, y);
    }

    //tested
    static PairBigInteger SWAP8(BigInteger x,BigInteger y) {
        return SWAPN(new BigInteger("0F0F0F0F0F0F0F0F",16), new BigInteger("F0F0F0F0F0F0F0F0",16),  4, x, y);
    }
    //tested
   static void br_aes_ct64_ortho(BigInteger[] q,int qIndex)
    {


       PairBigInteger temp;


        temp=SWAP2(q[qIndex+0], q[qIndex+1]);
        q[qIndex+0]=temp.x;
        q[qIndex+1]=temp.y;
        temp=SWAP2(q[qIndex+2], q[qIndex+3]);
        q[qIndex+2]=temp.x;
        q[qIndex+3]=temp.y;
        temp=SWAP2(q[qIndex+4], q[qIndex+5]);
        q[qIndex+4]=temp.x;
        q[qIndex+5]=temp.y;
        temp=SWAP2(q[qIndex+6], q[qIndex+7]);
        q[qIndex+6]=temp.x;
        q[qIndex+7]=temp.y;
        temp=SWAP4(q[qIndex+0], q[qIndex+2]);
        q[qIndex+0]=temp.x;
        q[qIndex+2]=temp.y;
        temp=SWAP4(q[qIndex+1], q[qIndex+3]);
        q[qIndex+1]=temp.x;
        q[qIndex+3]=temp.y;
        temp=SWAP4(q[qIndex+4], q[qIndex+6]);
        q[qIndex+4]=temp.x;
        q[qIndex+6]=temp.y;
        temp=SWAP4(q[qIndex+5], q[qIndex+7]);
        q[qIndex+5]=temp.x;
        q[qIndex+7]=temp.y;
        temp=SWAP8(q[qIndex+0], q[qIndex+4]);
        q[qIndex+0]=temp.x;
        q[qIndex+4]=temp.y;
        temp=SWAP8(q[qIndex+1], q[qIndex+5]);
        q[qIndex+1]=temp.x;
        q[qIndex+5]=temp.y;
        temp=SWAP8(q[qIndex+2], q[qIndex+6]);
        q[qIndex+2]=temp.x;
        q[qIndex+6]=temp.y;
        temp=SWAP8(q[qIndex+3], q[qIndex+7]);
        q[qIndex+3]=temp.x;
        q[qIndex+7]=temp.y;

    }

   static void br_aes_ct64_interleave_in(int q0Index, BigInteger[] q0, int q1Index,BigInteger[] q1,int wIndex, long[] w)
    {
        BigInteger x0, x1, x2, x3;

        x0 = new BigInteger(Long.toString(w[wIndex]));
        x1 =  new BigInteger(Long.toString(w[wIndex+1]));
        x2 = new BigInteger(Long.toString(w[wIndex+2]));
        x3 = new BigInteger(Long.toString(w[wIndex+3]));
        x0 = (x0 .shiftLeft(16)).or(x0);
        x1 = (x1 .shiftLeft(16)).or(x1);
        x2 = (x2 .shiftLeft(16)).or(x2);
        x3 = (x3 .shiftLeft(16)).or(x3);
        BigInteger temp=new BigInteger("0000FFFF0000FFFF",16); //temp=(uint64_t)0x0000FFFF0000FFFF
        x0 = x0.and(temp);
        x1 = x1.and(temp);
        x2 = x2.and(temp);
        x3 = x3.and(temp);
        x0 = (x0 .shiftLeft(8)).or(x0);
        x1 = (x1 .shiftLeft(8)).or(x1);
        x2 = (x2 .shiftLeft(8)).or(x2);
        x3 = (x3 .shiftLeft(8)).or(x3);
        BigInteger temp1=new BigInteger("00FF00FF00FF00FF",16); //temp=(uint64_t)0x00FF00FF00FF00FF
        x0 = x0.and(temp1);
        x1 = x1.and(temp1);
        x2 = x2.and(temp1);
        x3 = x3.and(temp1);
	    q0[q0Index] = x0.or (x2 .shiftLeft(8));
	    q1[q1Index] = x1.or(x3 .shiftLeft(8));
    }

    static void br_aes_ct64_interleave_out(int wIndex, long[] w, BigInteger q0, BigInteger q1)
    {
        BigInteger x0, x1, x2, x3;
        BigInteger temp=new BigInteger("00FF00FF00FF00FF",16).and(allOne64);

        x0 = (q0.and(temp)).and(allOne64);
        x1 = (q1.and(temp)).and(allOne64);
        x2 = (((q0.shiftRight(8)).and(allOne64)).and(temp)).and(allOne64);
        x3 =( ((q1.shiftRight(8)).and(allOne64)).and(temp)).and(allOne64);


        x0 = (((x0.shiftRight(8)).and(allOne64)).or(x0)).and(allOne64);
        x1 = (((x1.shiftRight(8)).and(allOne64)).or(x1)).and(allOne64);
        x2 = (((x2.shiftRight(8)).and(allOne64)).or(x2)).and(allOne64);
        x3 = (((x3.shiftRight(8)).and(allOne64)).or(x3)).and(allOne64);

        BigInteger temp1=new BigInteger("0000FFFF0000FFFF",16).and(allOne64); //temp1=(uint64_t)0x0000FFFF0000FFFF;
        x0 =(x0.and(temp1)).and(allOne64);
        x1 =(x1.and(temp1)).and(allOne64);
        x2 =(x2.and(temp1)).and(allOne64);
        x3 =(x3.and(temp1)).and(allOne64);
        //problematic




        w[wIndex+0] = (long)x0.and(allOne32).longValue() | (long)(x0.shiftRight(16)).and(allOne32).longValue() ;
        w[wIndex+1] = (long)x1.and(allOne32).longValue() | (long)(x1.shiftRight(16)).and(allOne32).longValue() ;
        w[wIndex+2] = (long)x2.and(allOne32).longValue() | (long)(x2.shiftRight(16)).and(allOne32).longValue() ;
        w[wIndex+3] = (long)x3.and(allOne32).longValue() | (long)(x3.shiftRight(16)).and(allOne32).longValue() ;
    }

    static int[] Rcon = {
            0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36
    };


    //tested
    static long sub_word(long x)
    {
        BigInteger[] q=new BigInteger[8];

        for(int i=0; i<8; i++){
            q[i]=new BigInteger("0");
        }
        q[0] = new BigInteger(Long.toString(x));
        br_aes_ct64_ortho(q,0);
        br_aes_ct64_bitslice_Sbox(q);
        br_aes_ct64_ortho(q,0);
        return (q[0].and(allOne32)).longValue();
    }
    //tested
     static void br_aes_ct64_keysched(int comp_skeyIndex,BigInteger[] comp_skey,int keyIndex, int[] key)
    {
        int i, j, k, nk, nkf;
        long tmp;
        long[] skey=new long[60];
        int skeyIndex=0;
        Long allOnes32=allOne32.longValue();

        int key_len = 32;

        nk = (int)(key_len >> 2);
        nkf = (int)((14 + 1) << 2);
        br_range_dec32le(skeyIndex,skey, (key_len >> 2),keyIndex, key);
        tmp = skey[(key_len >> 2) - 1] & allOnes32;
        for (i = nk, j = 0, k = 0; i < nkf; i ++) {
            if (j == 0) {
                tmp = (((tmp << 24)&allOnes32) | ((tmp >>> 8)&allOnes32)) & allOnes32;
                Long tempLong= (long) Rcon[k];
                tmp = sub_word(tmp) ^ ( tempLong);
            } else if (nk > 6 && j == 4) {
               tmp = sub_word(tmp);
            }
            tmp ^= skey[i - nk];
          skey[i] = tmp;
          if (++ j == nk) {
              j = 0;
              k ++;
          }
        }


        BigInteger[] q=new BigInteger[8];
        for (i = 0, j = 0; i < nkf; i += 4, j += 2) {
            br_aes_ct64_interleave_in(0,q, 4,q, skeyIndex + i,skey);
            q[1] = q[0];
            q[2] = q[0];
            q[3] = q[0];
            q[5] = q[4];
            q[6] = q[4];
            q[7] = q[4];
            br_aes_ct64_ortho(q,0);
            BigInteger t1=new BigInteger("1111111111111111",16); //t1=0x1111111111111111
            BigInteger t2=new BigInteger("2222222222222222",16); //t2=0x2222222222222222
            BigInteger t3=new BigInteger("4444444444444444",16); //t3=0x4444444444444444
            BigInteger t4=new BigInteger("8888888888888888",16); //t4=0x8888888888888888
            comp_skey[comp_skeyIndex+j + 0] =
                    ( (q[0].and(t1))
                            .or (q[1].and(t2))
                            .or (q[2].and(t3))
                            .or (q[3].and(t4))).and(allOne64);
            comp_skey[comp_skeyIndex+j + 1] =((q[4].and(t1))
                    .or (q[5].and(t2))
                    .or (q[6].and(t3))
                    .or (q[7].and(t4))).and(allOne64);
        }
    }

   static void br_aes_ct64_skey_expand(int skeyIndex,BigInteger[] skey,int comp_skeyIndex, BigInteger[] comp_skey)
    {

        BigInteger t0=new BigInteger("1229782938247303441"); //t0=0x1111111111111111
        BigInteger t1=new BigInteger("2459565876494606882"); //t1=0x2222222222222222
        BigInteger t2=new BigInteger("4919131752989213764"); //t2=0x4444444444444444
        BigInteger t3=new BigInteger("9838263505978427528"); //t3=0x8888888888888888

        int u, v, n;

        n = (14 + 1) << 1;
        for (u = 0, v = 0; u < n; u ++, v += 4) {
            BigInteger x0, x1, x2, x3;

            x0 = x1 = x2 = x3 = comp_skey[comp_skeyIndex+u];
            x0 =x0.and(t0);
            x1 =x1.and(t1);
            x2 =x2.and(t2);
            x3 =x3.and(t3);
            x1 =x1.shiftRight(1);
            x2 =x2.shiftRight(2);
            x3 =x3.shiftRight(3);
            skey[skeyIndex+v + 0] = (x0.shiftLeft(4)) .subtract(x0);
            skey[skeyIndex+v + 1] = (x1.shiftLeft(4)) .subtract(x1);
            skey[skeyIndex+v + 2] = (x2.shiftLeft(4)) .subtract(x2);
            skey[skeyIndex+v + 3] = (x3.shiftLeft(4)) .subtract(x3);
        }
    }

    static void add_round_key(int qIndex,BigInteger[] q,int skIndex, BigInteger[] sk)
    {

       /* System.out.println("input:"+Arrays.toString(q));
        System.out.println("sk5:"+(sk[5]));
        System.out.println("q5:"+(q[5]));
        System.out.println("sk5:"+(sk[5]));
        System.out.println("q5:"+(q[5]));*/

        q[0+qIndex] =(q[0+qIndex].xor(sk[0+skIndex])).and(allOne64);
        q[1+qIndex] =(q[1+qIndex].xor(sk[1+skIndex])).and(allOne64);
        q[2+qIndex] =(q[2+qIndex].xor(sk[2+skIndex])).and(allOne64);
        q[3+qIndex] =(q[3+qIndex].xor(sk[3+skIndex])).and(allOne64);
        q[4+qIndex] =(q[4+qIndex].xor(sk[4+skIndex])).and(allOne64);
        q[5+qIndex] =(q[5+qIndex].xor(sk[5+skIndex])).and(allOne64);
        q[6+qIndex] =(q[6+qIndex].xor(sk[6+skIndex])).and(allOne64);
        q[7+qIndex] =(q[7+qIndex].xor(sk[7+skIndex])).and(allOne64);


        //System.out.println("output:"+Arrays.toString(q));
        //System.out.println("q[7]:"+q[7]);
       // System.out.println("test2:"+Arrays.toString(q));
    }

  static void shift_rows(BigInteger[] q)
    {
        int i;


        BigInteger t0=new BigInteger("000000000000FFFF",16);
        BigInteger t1=new BigInteger("00000000FFF00000",16);
        BigInteger t2=new BigInteger("00000000000F0000",16);
        BigInteger t3=new BigInteger("0000FF0000000000",16);
        BigInteger t4=new BigInteger("000000FF00000000",16);
        BigInteger t5=new BigInteger("F000000000000000",16);
        BigInteger t6=new BigInteger("0FFF000000000000",16);


        for (i = 0; i < 8; i ++) {
            BigInteger x;

            x = q[i];
            q[i] = (x.and(t0))
                    .or ((x.and(t1)).shiftRight(4))
                    .or ((x.and(t2)).shiftLeft(12))
                    .or ((x.and(t3)).shiftRight(8))
                    .or ((x.and(t4)).shiftLeft(8))
                    .or ((x.and(t5)).shiftRight(12))
                    .or ((x.and(t6)).shiftLeft(4));
        }
    }

     static BigInteger rotr32(BigInteger x)
    {
        return (x.shiftLeft(32)) .or (x.shiftRight(32));
    }

    static void mix_columns(BigInteger[] q)
    {
        BigInteger q0, q1, q2, q3, q4, q5, q6, q7;
        BigInteger r0, r1, r2, r3, r4, r5, r6, r7;

        q0 = q[0];
        q1 = q[1];
        q2 = q[2];
        q3 = q[3];
        q4 = q[4];
        q5 = q[5];
        q6 = q[6];
        q7 = q[7];
        r0 = (allOne64.and(q0 .shiftRight(16))) .or (allOne64.and(q0 .shiftLeft(48)));
        r1 = (allOne64.and(q1 .shiftRight(16) )) .or (allOne64.and(q1 .shiftLeft(48)));
        r2 = (allOne64.and(q2 .shiftRight(16) )) .or (allOne64.and(q2 .shiftLeft(48) ));
        r3 = (allOne64.and(q3 .shiftRight(16) )) .or (allOne64.and(q3 .shiftLeft(48) ));
        r4 = (allOne64.and(q4 .shiftRight(16) )) .or (allOne64.and(q4 .shiftLeft(48) ));
        r5 = (allOne64.and(q5 .shiftRight(16) )) .or (allOne64.and(q5 .shiftLeft(48) ));
        r6 = (allOne64.and(q6 .shiftRight(16) )) .or (allOne64.and(q6 .shiftLeft(48) ));
        r7 = (allOne64.and(q7 .shiftRight(16) )) .or (allOne64.and(q7 .shiftLeft(48) ));

        q[0] = xor64bit(xor64bit(xor64bit(q7,r7),r0),rotr32(xor64bit(q0,r0)));
        q[1] = xor64bit(xor64bit(xor64bit(xor64bit(xor64bit(q0,r0),q7),r7),r1),rotr32(xor64bit(q1,r1)));
        q[2] = xor64bit(xor64bit(xor64bit(q1,r1),r2),rotr32(xor64bit(q2,r2)));
        q[3] = xor64bit(xor64bit(xor64bit(xor64bit(xor64bit(q2,r2),q7),r7),r3),rotr32(xor64bit(q3,r3)));
        q[4] = xor64bit(xor64bit(xor64bit(xor64bit(xor64bit(q3,r3),q7),r7),r4),rotr32(xor64bit(q4,r4)));
        q[5] = xor64bit(xor64bit(xor64bit(q4,r4),r5),rotr32(xor64bit(q5,r5)));
        q[6] = xor64bit(xor64bit(xor64bit(q5,r5),r6),rotr32(xor64bit(q6,r6)));
        q[7] = xor64bit(xor64bit(xor64bit(q6,r6),r7),rotr32(xor64bit(q7,r7)));
    }


    static BigInteger xor64bit(BigInteger a,BigInteger b){

       return (a.xor(b)).and(allOne64);

    }

   static void inc4_be(long[] x,int xIndex)
    {
        x[xIndex]= br_swap32(x[xIndex])+4;
        x[xIndex] = br_swap32(x[xIndex]);
    }

    static void memcpy(int wIndex,long[] w,int ivwIndex, long[] ivw,int length){
        for(int i=0; i<length;i++){
            if(i+wIndex>=w.length || i+ivwIndex>=ivw.length)
                break;
            w[i+wIndex]=ivw[i+ivwIndex];
        }
    }


    //tested!
    static void aes_ctr4x(int outIndex, int[] out,int ivwIndex, long[] ivw,int sk_expIndex, BigInteger[] sk_exp)
    {
        long[] w=new long[16];
        int wIndex=0;
        BigInteger[] q=new BigInteger[8];
        int qIndex=0;
        int i;
        //w.length=ivw.length=16
        memcpy(wIndex,w,ivwIndex, ivw, w.length);
        for (i = 0; i < 4; i++) {
            br_aes_ct64_interleave_in(i+qIndex,q,i+4+qIndex,q, wIndex + (i << 2),w);
        }
        br_aes_ct64_ortho(q,qIndex);

        //System.out.println("q:"+Arrays.toString(q));
        //magas

        add_round_key(qIndex,q,sk_expIndex, sk_exp);

        for (i = 1; i < 14; i++) {
          br_aes_ct64_bitslice_Sbox(q);


          shift_rows(q);

             //System.out.println("qqq:"+Arrays.toString(q));

            mix_columns(q);

            //System.out.println("qqqqq:"+Arrays.toString(q)+" "+q[7]);


            add_round_key(qIndex,q,  (i << 3)+sk_expIndex,sk_exp);

            //System.out.println("qqqqq:"+Arrays.toString(q));

            //break;

        }


       br_aes_ct64_bitslice_Sbox(q);


        shift_rows(q);
        add_round_key(qIndex,q, sk_expIndex + 112,sk_exp);

        br_aes_ct64_ortho(q,qIndex);

       for (i = 0; i < 4; i ++) {
            br_aes_ct64_interleave_out(wIndex + (i << 2),w,q[qIndex+i],q[qIndex+i+4]);
        }




       br_range_enc32le(outIndex,out,wIndex, w, 16);



        inc4_be(ivw,ivwIndex+3);
        inc4_be(ivw,ivwIndex+7);
        inc4_be(ivw,ivwIndex+11);
        inc4_be(ivw,ivwIndex+15);



    }
    //tested
    static void br_aes_ct64_ctr_init(int sk_expIndex, BigInteger[] sk_exp,int keyIndex, int[] key)
    {
        BigInteger[] skey=new BigInteger[30];
        int skeyIndex=0;

        br_aes_ct64_keysched(skeyIndex,skey,keyIndex, key);
        br_aes_ct64_skey_expand(sk_expIndex,sk_exp,skeyIndex, skey);
    }
//tested
    static void aes256ctr_init(aes256ctr_ctx s,int keyIndex, int[] key, int nonceIndex,int[] nonce)
    {

        br_aes_ct64_ctr_init(s.sk_expIndex,s.sk_exp,keyIndex, key);

        br_range_dec32le(s.ivwIndex,s.ivw, 3, nonceIndex,nonce);

        int sizeOfUint32_t=4;
        memcpy(s.ivwIndex +  4, s.ivw, s.ivwIndex,s.ivw, 3 * sizeOfUint32_t);
        memcpy(s.ivwIndex +  8, s.ivw,s.ivwIndex,s.ivw, 3 * sizeOfUint32_t);
        memcpy(s.ivwIndex + 12, s.ivw,s.ivwIndex,s.ivw, 3 * sizeOfUint32_t);


        s.ivw[ 3] = br_swap32(0);
        s.ivw[ 7] = br_swap32(1);
        s.ivw[11] = br_swap32(2);
        s.ivw[15] = br_swap32(3);



    }

    static void aes256ctr_squeezeblocks(int outIndex,int[] out, int nblocks, aes256ctr_ctx s)
    {
        while (nblocks > 0) {
            aes_ctr4x(outIndex,out,s.ivwIndex, s.ivw,s.sk_expIndex, s.sk_exp);
            //System.out.println(Arrays.toString(out));
            outIndex += 64;
            nblocks--;
        }
    }

    public static void main(String args[])
    {
        int[] buf=new int[100];
        int SEEDBYTES= 2;
        int[] key =new int[32];
        for(int i=0;i<32;i++)
            key[i]=1;
        int[] nonce =new int[12];
        for(int i=0;i<12;i++)
            nonce[i]=1;
        aes256ctr_ctx ae=new aes256ctr_ctx();
        aes256ctr_init(ae,0,key,0,nonce);

        aes256ctr_squeezeblocks(0,buf,1,ae );

       /// List<BigInteger> a= {18446744073692708606, 18446744073692708606, 16843009, 16843009, 16843009, 18446744073709551615, 18446744073709551615, 0};


        //BigInteger[] a={new BigInteger("1"),new BigInteger("2"),new BigInteger("3"),new BigInteger("4"),new BigInteger("5"),new BigInteger("6"),new BigInteger("7"),new BigInteger("8")};
        //Long t=sub_word(56778398L);
       // System.out.println(t);




    }



    static void dilithium_aes256ctr_init(aes256ctr_ctx state, int keyIndex,
                              int[] key,
                                  int nonce)
    {

        int allOne8=255;
        int[] expnonce=new int[12];
        expnonce[0] = nonce & allOne8;
        expnonce[1] = (nonce >> 8) & allOne8;
        aes256ctr_init(state,keyIndex,key,0, expnonce);
    }



}
