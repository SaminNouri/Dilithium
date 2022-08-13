import com.sun.source.tree.Scope;

import java.math.BigInteger;

public class Polynomial {

    static private Config config=new Config();;

    static private Reduce reduce=new Reduce();

    static private Ntt nttObject=new Ntt();

    static private Rounding rounding=new Rounding();

    static BigInteger allOne32=new BigInteger("4294967295");

    static BigInteger allOne64 = new BigInteger("18446744073709551615");

    static int allOne16=65535;






    /*************************************************
     * Name:        poly_reduce
     *
     * Description: Inplace reduction of all coefficients of polynomial to
     *              representative in [-6283009,6283007].
     *
     * Arguments:   - poly *a: pointer to input/output polynomial
     **************************************************/
    void poly_reduce(poly a,int aIndex) {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N; ++i)
            a.coeffs[i+aIndex] = reduce.reduce32(a.coeffs[i+aIndex]);

       // DBENCH_STOP(*tred);
    }

    /*************************************************
     * Name:        poly_caddq
     *
     * Description: For all coefficients of in/out polynomial add Q if
     *              coefficient is negative.
     *
     * Arguments:   - poly *a: pointer to input/output polynomial
     **************************************************/
    void poly_caddq(poly a,int aIndex) {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N; ++i)
            a.coeffs[i+aIndex] = reduce.caddq( a.coeffs[i+aIndex]);

       // DBENCH_STOP(*tred);
    }

    /*************************************************
     * Name:        poly_add
     *
     * Description: Add polynomials. No modular reduction is performed.
     *
     * Arguments:   - poly *c: pointer to output polynomial
     *              - const poly *a: pointer to first summand
     *              - const poly *b: pointer to second summand
     **************************************************/
    void poly_add(poly c,int cIndex, poly a,int aIndex,poly b, int bIndex)  {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N; ++i)
            c.coeffs[i+cIndex] = a.coeffs[i+aIndex] + b.coeffs[i+bIndex];

       // DBENCH_STOP(*tadd);
    }

    /*************************************************
     * Name:        poly_sub
     *
     * Description: Subtract polynomials. No modular reduction is
     *              performed.
     *
     * Arguments:   - poly *c: pointer to output polynomial
     *              - const poly *a: pointer to first input polynomial
     *              - const poly *b: pointer to second input polynomial to be
     *                               subtraced from first input polynomial
     **************************************************/
    void poly_sub(poly c,int cIndex, poly a,int aIndex,poly b, int bIndex) {
        int i;
       // DBENCH_START();

        for(i = 0; i < config.N; ++i)
            c.coeffs[i+cIndex] = a.coeffs[i+aIndex] - b.coeffs[i+bIndex];

       // DBENCH_STOP(*tadd);
    }

    /*************************************************
     * Name:        poly_shiftl
     *
     * Description: Multiply polynomial by 2^D without modular reduction. Assumes
     *              input coefficients to be less than 2^{31-D} in absolute value.
     *
     * Arguments:   - poly *a: pointer to input/output polynomial
     **************************************************/
    //todo: pay attention to shift!
    void poly_shiftl(poly a,int aIndex) {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N; ++i)
            a.coeffs[i+aIndex] <<= config.D;

       // DBENCH_STOP(*tmul);
    }

    /*************************************************
     * Name:        poly_ntt
     *
     * Description: Inplace forward NTT. Coefficients can grow by
     *              8*Q in absolute value.
     *
     * Arguments:   - poly *a: pointer to input/output polynomial
     **************************************************/
    void poly_ntt(poly a,int aIndex) {
       // DBENCH_START();

        nttObject.ntt(a.coeffs,aIndex);

       // DBENCH_STOP(*tmul);
    }

    /*************************************************
     * Name:        poly_invntt_tomont
     *
     * Description: Inplace inverse NTT and multiplication by 2^{32}.
     *              Input coefficients need to be less than Q in absolute
     *              value and output coefficients are again bounded by Q.
     *
     * Arguments:   - poly *a: pointer to input/output polynomial
     **************************************************/
    void poly_invntt_tomont(poly a,int aIndex) {
       // DBENCH_START();

        nttObject.invntt_tomont(a.coeffs,aIndex);

       // DBENCH_STOP(*tmul);
    }

    /*************************************************
     * Name:        poly_pointwise_montgomery
     *
     * Description: Pointwise multiplication of polynomials in NTT domain
     *              representation and multiplication of resulting polynomial
     *              by 2^{-32}.
     *
     * Arguments:   - poly *c: pointer to output polynomial
     *              - const poly *a: pointer to first input polynomial
     *              - const poly *b: pointer to second input polynomial
     **************************************************/

    //todo: fix long!
    void poly_pointwise_montgomery(poly c,int cIndex, poly a,int aIndex, poly b,int bIndex) {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N; ++i)
            c.coeffs[i+cIndex] = reduce.montgomery_reduce(new Long(a.coeffs[i+aIndex]) * b.coeffs[i+bIndex]);

       // DBENCH_STOP(*tmul);
    }

    /*************************************************
     * Name:        poly_power2round
     *
     * Description: For all coefficients c of the input polynomial,
     *              compute c0, c1 such that c mod Q = c1*2^D + c0
     *              with -2^{D-1} < c0 <= 2^{D-1}. Assumes coefficients to be
     *              standard representatives.
     *
     * Arguments:   - poly *a1: pointer to output polynomial with coefficients c1
     *              - poly *a0: pointer to output polynomial with coefficients c0
     *              - const poly *a: pointer to input polynomial
     **************************************************/
    void poly_power2round(poly a1,int a1Index, poly a0,int a0Index, poly a,int aIndex) {
        int i;
        //DBENCH_START();
        Pair p = null;

        for(i = 0; i < config.N; ++i)
        {
            p= rounding.power2round(a.coeffs[i+aIndex]);
            a0.coeffs[i+a0Index]=p.x;
            a1.coeffs[i+a1Index]=p.y;

        }



        //DBENCH_STOP(*tround);
    }

    /*************************************************
     * Name:        poly_decompose
     *
     * Description: For all coefficients c of the input polynomial,
     *              compute high and low bits c0, c1 such c mod Q = c1*ALPHA + c0
     *              with -ALPHA/2 < c0 <= ALPHA/2 except c1 = (Q-1)/ALPHA where we
     *              set c1 = 0 and -ALPHA/2 <= c0 = c mod Q - Q < 0.
     *              Assumes coefficients to be standard representatives.
     *
     * Arguments:   - poly *a1: pointer to output polynomial with coefficients c1
     *              - poly *a0: pointer to output polynomial with coefficients c0
     *              - const poly *a: pointer to input polynomial
     **************************************************/
    void poly_decompose(poly a1,int a1Index, poly a0,int a0Index,poly a,int aIndex) {
        int i;
       // DBENCH_START();
        Pair p=null;

        for(i = 0; i < config.N; ++i)
        {
            p = rounding.decompose( a.coeffs[i+aIndex]);
            a0.coeffs[i+a0Index]=p.x;
            a1.coeffs[i+a1Index]=p.y;
        }

       // DBENCH_STOP(*tround);
    }

/*************************************************
 * Name:        poly_make_hint
 *
 * Description: Compute hint polynomial. The coefficients of which indicate
 *              whether the low bits of the corresponding coefficient of
 *              the input polynomial overflow into the high bits.
 *
 * Arguments:   - poly *h: pointer to output hint polynomial
 *              - const poly *a0: pointer to low part of input polynomial
 *              - const poly *a1: pointer to high part of input polynomial
 *
 * Returns number of 1 bits.
 **************************************************/
    int poly_make_hint(poly h,int hIndex, poly a0,int a0Index,poly a1, int a1Index) {
        int i, s = 0;
        //DBENCH_START();

        for(i = 0; i < config.N; ++i) {
            h.coeffs[i+hIndex] = rounding.make_hint(a0.coeffs[i+a0Index], a1.coeffs[i+a1Index]);
            s += h.coeffs[i+hIndex];
        }

       // DBENCH_STOP(*tround);
        return s;
    }

    /*************************************************
     * Name:        poly_use_hint
     *
     * Description: Use hint polynomial to correct the high bits of a polynomial.
     *
     * Arguments:   - poly *b: pointer to output polynomial with corrected high bits
     *              - const poly *a: pointer to input polynomial
     *              - const poly *h: pointer to input hint polynomial
     **************************************************/
    void poly_use_hint(poly b,int bIndex, poly a,int aIndex, poly h, int hIndex) throws DilithiumModeException {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N; ++i)
            b.coeffs[i+bIndex] = rounding.use_hint(a.coeffs[i+aIndex], h.coeffs[i+hIndex]);

       // DBENCH_STOP(*tround);
    }

    /*************************************************
     * Name:        poly_chknorm
     *
     * Description: Check infinity norm of polynomial against given bound.
     *              Assumes input coefficients were reduced by reduce32().
     *
     * Arguments:   - const poly *a: pointer to polynomial
     *              - int32_t B: norm bound
     *
     * Returns 0 if norm is strictly smaller than B <= (Q-1)/8 and 1 otherwise.
     **************************************************/
    int poly_chknorm(poly a,int aIndex, int B) {
        int i;
        int t;
        //DBENCH_START();

        if(B > (config.Q-1)/8)
            return 1;

  /* It is ok to leak which coefficient violates the bound since
     the probability for each coefficient is independent of secret
     data but we must not leak the sign of the centralized representative. */
        for(i = 0; i < config.N; ++i) {
            /* Absolute value */
            t = a.coeffs[i+aIndex] >> 31;
            t = a.coeffs[i+aIndex] - (t & 2*a.coeffs[i+aIndex]);

            if(t >= B) {
               // DBENCH_STOP(*tsample);
                return 1;
            }
        }

        //DBENCH_STOP(*tsample);
        return 0;
    }

/*************************************************
 * Name:        rej_uniform
 *
 * Description: Sample uniformly random coefficients in [0, Q-1] by
 *              performing rejection sampling on array of random bytes.
 *
 * Arguments:   - int32_t *a: pointer to output array (allocated)
 *              - unsigned int len: number of coefficients to be sampled
 *              - const uint8_t *buf: array of random bytes
 *              - unsigned int buflen: length of array of random bytes
 *
 * Returns number of sampled coefficients. Can be smaller than len if not enough
 * random bytes were given.
 **************************************************/
//it was static before, I changed it because of config!
//can be problematic!
//buf was uint8_t!
//todo test this function!!!!
   static int rej_uniform(int[] a,int aIndex,
                                int len,
                                int[] buf,int bufIndex,
                                int buflen)
    {
        int ctr, pos;
        int t;
        Long allOne32=Polynomial.allOne32.longValue();
        //DBENCH_START();

        ctr=aIndex;
        pos=bufIndex;
        while(ctr < len && pos + 3 <= buflen) {
            t  = buf[pos++];
            Long temp1=new Long(buf[pos++])&allOne32;
            t |= temp1 << 8;
            temp1=new Long(buf[pos++])&allOne32;
            t |= temp1 << 16;
            t &= 0x7FFFFF;

            if(t < config.Q)
                a[ctr++] = t;
        }

        //DBENCH_STOP(*tsample);
        return ctr;
    }

/*************************************************
 * Name:        poly_uniform
 *
 * Description: Sample polynomial with uniformly random coefficients
 *              in [0,Q-1] by performing rejection sampling on the
 *              output stream of SHAKE256(seed|nonce) or AES256CTR(seed,nonce).
 *
 * Arguments:   - poly *a: pointer to output polynomial
 *              - const uint8_t seed[]: byte array with seed of length SEEDBYTES
 *              - uint16_t nonce: 2-byte nonce
 **************************************************/
public int POLY_UNIFORM_NBLOCKS= ((768 + config.STREAM128_BLOCKBYTES - 1)/config.STREAM128_BLOCKBYTES);

//a is a pointer!
    //nonce is unsigned 16 bit
    //seed,buf are unsigned 8 bit
    void poly_uniform(poly a,int aIndex,
                  int[] seed,int seedIndex,
                      int nonce)
    {
        int i, ctr, off;
        int buflen = POLY_UNIFORM_NBLOCKS*config.STREAM128_BLOCKBYTES;
        int[] buf=new int[POLY_UNIFORM_NBLOCKS*config.STREAM128_BLOCKBYTES + 2];
        int bufIndex=0;
        aes256ctr_ctx stateAES=null;
        KeccakState stateKeccak=null;


        if(config.isAESUsed==1){

            stateAES = new aes256ctr_ctx();
            aes256ctr.dilithium_aes256ctr_init(stateAES,seedIndex,seed,nonce);
            aes256ctr.aes256ctr_squeezeblocks(bufIndex,buf,POLY_UNIFORM_NBLOCKS,stateAES);

        }else {

            stateKeccak=new KeccakState();
            Fips202.shake128_init(stateKeccak);
            Fips202.shake128_squeezeblocks(buf,bufIndex,POLY_UNIFORM_NBLOCKS,stateKeccak);



        }


        ctr = rej_uniform(a.coeffs,aIndex, config.N, buf,bufIndex, buflen);

        while(ctr < config.N) {
            off = buflen % 3;
            for(i = 0; i < off; ++i)
                buf[i] = buf[buflen - off + i];

            if(config.isAESUsed==1){

                aes256ctr.aes256ctr_squeezeblocks(bufIndex+off,buf,1,stateAES);

            }else {

                Fips202.shake128_squeezeblocks(buf,bufIndex+off,1,stateKeccak);

            }

            buflen = config.STREAM128_BLOCKBYTES + off;
            ctr += rej_uniform(a.coeffs,aIndex + ctr, config.N - ctr, buf,bufIndex, buflen);
        }
    }

/*************************************************
 * Name:        rej_eta
 *
 * Description: Sample uniformly random coefficients in [-ETA, ETA] by
 *              performing rejection sampling on array of random bytes.
 *
 * Arguments:   - int32_t *a: pointer to output array (allocated)
 *              - unsigned int len: number of coefficients to be sampled
 *              - const uint8_t *buf: array of random bytes
 *              - unsigned int buflen: length of array of random bytes
 *
 * Returns number of sampled coefficients. Can be smaller than len if not enough
 * random bytes were given.
 **************************************************/

//buf is unsigned 8 bit
    static int rej_eta(int aIndex,int[] a,
                               int len,
                            int bufIndex,
                            int[] buf,
                                int buflen)
    {
        int ctr, pos;
        int t0, t1;
        //DBENCH_START();

        pos=bufIndex;
        ctr=aIndex;
        while(ctr < len && pos < buflen) {
            t0 = buf[pos] & 0x0F;
            t1 = buf[pos++] >> 4;

if(config.ETA == 2)
{

    if(t0 < 15) {
        t0 = t0 - (205*t0 >> 10)*5;
        a[ctr++] = 2 - t0;
    }
    if(t1 < 15 && ctr < len) {
        t1 = t1 - (205*t1 >> 10)*5;
        a[ctr++] = 2 - t1;
    }

}
else if (config.ETA == 4)
{

    if(t0 < 9)
        a[ctr++] = 4 - t0;
    if(t1 < 9 && ctr < len)
        a[ctr++] = 4 - t1;

}

        }

        //DBENCH_STOP(*tsample);
        return ctr;
    }

/*************************************************
 * Name:        poly_uniform_eta
 *
 * Description: Sample polynomial with uniformly random coefficients
 *              in [-ETA,ETA] by performing rejection sampling on the
 *              output stream from SHAKE256(seed|nonce) or AES256CTR(seed,nonce).
 *
 * Arguments:   - poly *a: pointer to output polynomial
 *              - const uint8_t seed[]: byte array with seed of length CRHBYTES
 *              - uint16_t nonce: 2-byte nonce
 **************************************************/


    void poly_uniform_eta(int aIndex,poly a,
                     int seedIndex, int[] seed,
                          int nonce)
    {
        int POLY_UNIFORM_ETA_NBLOCKS=0;

        if (config.ETA == 2)
        {

            POLY_UNIFORM_ETA_NBLOCKS= ((136 + config.STREAM256_BLOCKBYTES - 1)/config.STREAM256_BLOCKBYTES);


        }

        if (config.ETA == 4)
        {

            POLY_UNIFORM_ETA_NBLOCKS= ((227 + config.STREAM256_BLOCKBYTES - 1)/config.STREAM256_BLOCKBYTES);


        }





        int ctr;
        int buflen = POLY_UNIFORM_ETA_NBLOCKS*config.STREAM256_BLOCKBYTES;
        int[] buf=new int[POLY_UNIFORM_ETA_NBLOCKS* config.STREAM256_BLOCKBYTES];
        int bufIndex=0;



        aes256ctr_ctx stateAES=null;
        KeccakState stateKeccak=null;


        if(config.isAESUsed==1){

            stateAES = new aes256ctr_ctx();
            aes256ctr.dilithium_aes256ctr_init(stateAES,seedIndex,seed,nonce);
            aes256ctr.aes256ctr_squeezeblocks(bufIndex,buf,POLY_UNIFORM_ETA_NBLOCKS,stateAES);

        }else {

            stateKeccak=new KeccakState();
            Fips202.shake256_init(stateKeccak);
            Fips202.shake256_squeezeblocks(buf,bufIndex,POLY_UNIFORM_ETA_NBLOCKS,stateKeccak);



        }



        ctr = rej_eta(aIndex,a.coeffs, config.N, bufIndex,buf, buflen);

        while(ctr < config.N) {

            if(config.isAESUsed==1){

                aes256ctr.aes256ctr_squeezeblocks(bufIndex,buf,1,stateAES);

            }else {

                Fips202.shake256_squeezeblocks(buf,bufIndex,1,stateKeccak);

            }
            ctr += rej_eta(aIndex+ctr,a.coeffs , config.N - ctr, bufIndex,buf, config.STREAM256_BLOCKBYTES);
        }
    }

/*************************************************
 * Name:        poly_uniform_gamma1m1
 *
 * Description: Sample polynomial with uniformly random coefficients
 *              in [-(GAMMA1 - 1), GAMMA1] by unpacking output stream
 *              of SHAKE256(seed|nonce) or AES256CTR(seed,nonce).
 *
 * Arguments:   - poly *a: pointer to output polynomial
 *              - const uint8_t seed[]: byte array with seed of length CRHBYTES
 *              - uint16_t nonce: 16-bit nonce
 **************************************************/
public static int POLY_UNIFORM_GAMMA1_NBLOCKS= ((config.POLYZ_PACKEDBYTES + config.STREAM256_BLOCKBYTES - 1)/config.STREAM256_BLOCKBYTES);
    void poly_uniform_gamma1(int aIndex,poly a,
                         int seedIndex,int[] seed,
                             int nonce)
    {
        int[] buf=new int[POLY_UNIFORM_GAMMA1_NBLOCKS*config.STREAM256_BLOCKBYTES];
        int bufIndex=0;



        aes256ctr_ctx stateAES=null;
        KeccakState stateKeccak=null;


        if(config.isAESUsed==1){

            stateAES = new aes256ctr_ctx();
            aes256ctr.dilithium_aes256ctr_init(stateAES,seedIndex,seed,nonce);
            aes256ctr.aes256ctr_squeezeblocks(bufIndex,buf,POLY_UNIFORM_GAMMA1_NBLOCKS,stateAES);

        }else {

            stateKeccak=new KeccakState();
            Fips202.shake256_init(stateKeccak);
            Fips202.shake256_squeezeblocks(buf,bufIndex,POLY_UNIFORM_GAMMA1_NBLOCKS,stateKeccak);



        }

        //check again!
        polyeta_unpack(aIndex,a,bufIndex,buf);
    }

    /*************************************************
     * Name:        challenge
     *
     * Description: Implementation of H. Samples polynomial with TAU nonzero
     *              coefficients in {-1,1} using the output stream of
     *              SHAKE256(seed).
     *
     * Arguments:   - poly *c: pointer to output polynomial
     *              - const uint8_t mu[]: byte array containing seed of length SEEDBYTES
     **************************************************/


    void poly_challenge(int cIdex, poly c,int seedIndex, int[] seed) {
        int i, b, pos;
        BigInteger signs;
        int[] buf=new int[config.SHAKE256_RATE];
        int buIndex=0;
        KeccakState state=new KeccakState();

        Fips202.shake256_init(state);
        Fips202.shake256_absorb(state,seedIndex, seed, config.SEEDBYTES);
        Fips202.shake256_finalize(state);
        Fips202.shake256_squeezeblocks(buf,buIndex, 1, state);

        signs = new BigInteger("0");
        for(i = 0; i < 8; ++i)
        {
            signs=signs.or((new BigInteger(Integer.toString((buf[i] & 255) )).shiftLeft(8*i)).and(allOne64));
            signs=signs.and(allOne64);
        }
        pos = 8;

        for(i = 0; i < config.N; ++i)
            c.coeffs[i+cIdex] = 0;
        for(i = config.N-config.TAU; i < config.N; ++i) {
            do {
                if(pos >= config.SHAKE256_RATE) {
                    Fips202.shake256_squeezeblocks(buf,buIndex, 1, state);
                    pos = 0;
                }

                b = buf[pos++];
            } while(b > i);

            c.coeffs[i+cIdex] = c.coeffs[b+cIdex];
            c.coeffs[b+cIdex] = (new BigInteger("1").subtract(((signs.and(new BigInteger("1")))).multiply(new BigInteger("2")))).and(allOne32).intValue();
            signs =(signs.shiftRight(1)).and(allOne64);
        }
    }



    /*************************************************
     * Name:        polyeta_pack
     *
     * Description: Bit-pack polynomial with coefficients in [-ETA,ETA].
     *
     * Arguments:   - uint8_t *r: pointer to output byte array with at least
     *                            POLYETA_PACKEDBYTES bytes
     *              - const poly *a: pointer to input polynomial
     **************************************************/
    void polyeta_pack(int rIndex,int[] r, int aIndex,poly a) {
        int i;
        int[] t=new int[8];
        //DBENCH_START();



if (config.ETA == 2)
        {
            for(i = 0; i < config.N/8; ++i) {
                t[0] = (config.ETA - a.coeffs[8*i+0])&255;
                t[1] = (config.ETA - a.coeffs[8*i+1])&255 ;
                t[2] = (config.ETA - a.coeffs[8*i+2])&255 ;
                t[3] = (config.ETA - a.coeffs[8*i+3])&255 ;
                t[4] = (config.ETA - a.coeffs[8*i+4])&255 ;
                t[5] = (config.ETA - a.coeffs[8*i+5])&255 ;
                t[6] = (config.ETA - a.coeffs[8*i+6])&255 ;
                t[7] = (config.ETA - a.coeffs[8*i+7])&255 ;

                r[3*i+0]  = ((t[0] >> 0)&255) | ((t[1] << 3)&255)  | ((t[2] << 6)&255) ;
                r[3*i+1]  = ((t[2] >> 2)&255)  | ((t[3] << 1)&255)  | ((t[4] << 4)&255)  | ((t[5] << 7)&255) ;
                r[3*i+2]  = ((t[5] >> 1)&255)  | ((t[6] << 2)&255)  | ((t[7] << 5)&255) ;
            }

        }
  else if (config.ETA == 4)
        {
            for(i = 0; i < config.N/2; ++i) {
                t[0] = (config.ETA - a.coeffs[2*i+0])&255;
                t[1] = (config.ETA - a.coeffs[2*i+1])&255;
                r[i] = (t[0]&255) | ((t[1] << 4)&255) ;
            }
        }


        //DBENCH_STOP(*tpack);
    }

    /*************************************************
     * Name:        polyconfig.ETA_unpack
     *
     * Description: Unpack polynomial with coefficients in [-ETA,ETA].
     *
     * Arguments:   - poly *r: pointer to output polynomial
     *              - const uint8_t *a: byte array with bit-packed polynomial
     **************************************************/
    //no idea!
    void polyeta_unpack(int rIndex,poly r, int aIndex,int[] a) {
        int i;
        //DBENCH_START();

if (config.ETA == 2)
{

    for(i = 0; i < config.N/8; ++i) {
        r.coeffs[8*i+0] =  (a[3*i+0] >> 0) & 7;
        r.coeffs[8*i+1] =  (a[3*i+0] >> 3) & 7;
        r.coeffs[8*i+2] = ((a[3*i+0] >> 6) | (a[3*i+1] << 2)) & 7;
        r.coeffs[8*i+3] =  (a[3*i+1] >> 1) & 7;
        r.coeffs[8*i+4] =  (a[3*i+1] >> 4) & 7;
        r.coeffs[8*i+5] = ((a[3*i+1] >> 7) | (a[3*i+2] << 1)) & 7;
        r.coeffs[8*i+6] =  (a[3*i+2] >> 2) & 7;
        r.coeffs[8*i+7] =  (a[3*i+2] >> 5) & 7;

        r.coeffs[8*i+0] = config.ETA - r.coeffs[8*i+0];
        r.coeffs[8*i+1] = config.ETA - r.coeffs[8*i+1];
        r.coeffs[8*i+2] = config.ETA - r.coeffs[8*i+2];
        r.coeffs[8*i+3] = config.ETA - r.coeffs[8*i+3];
        r.coeffs[8*i+4] = config.ETA - r.coeffs[8*i+4];
        r.coeffs[8*i+5] = config.ETA - r.coeffs[8*i+5];
        r.coeffs[8*i+6] = config.ETA - r.coeffs[8*i+6];
        r.coeffs[8*i+7] = config.ETA - r.coeffs[8*i+7];
    }

}
else if (config.ETA == 4)
        {
            for(i = 0; i < config.N/2; ++i) {
                r.coeffs[2*i+0] = a[i] & 0x0F;
                r.coeffs[2*i+1] = a[i] >> 4;
                r.coeffs[2*i+0] = config.ETA - r.coeffs[2*i+0];
                r.coeffs[2*i+1] = config.ETA - r.coeffs[2*i+1];
            }
        }

        //DBENCH_STOP(*tpack);
    }

    /*************************************************
     * Name:        polyt1_pack
     *
     * Description: Bit-pack polynomial t1 with coefficients fitting in 10 bits.
     *              Input coefficients are assumed to be standard representatives.
     *
     * Arguments:   - uint8_t *r: pointer to output byte array with at least
     *                            POLYT1_PACKEDBYTES bytes
     *              - const poly *a: pointer to input polynomial
     **************************************************/
    void polyt1_pack(int rIndex,int[] r, poly a, int aIndex) {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N/4; ++i) {
            r[5*i+0] = (a.coeffs[4*i+0] >> 0)&255;
            r[5*i+1] = ((a.coeffs[4*i+0] >> 8)&255) | ((a.coeffs[4*i+1] << 2)&255);
            r[5*i+2] = ((a.coeffs[4*i+1] >> 6)&255)| ((a.coeffs[4*i+2] << 4)&255);
            r[5*i+3] =( (a.coeffs[4*i+2] >> 4) &255)| ((a.coeffs[4*i+3] << 6)&255);
            r[5*i+4] = (a.coeffs[4*i+3] >> 2)&255;
        }

       // DBENCH_STOP(*tpack);
    }

    /*************************************************
     * Name:        polyt1_unpack
     *
     * Description: Unpack polynomial t1 with 10-bit coefficients.
     *              Output coefficients are standard representatives.
     *
     * Arguments:   - poly *r: pointer to output polynomial
     *              - const uint8_t *a: byte array with bit-packed polynomial
     **************************************************/
    void polyt1_unpack(int rIndex,poly r,int aIndex, int[] a) {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N/4; ++i) {
            r.coeffs[4*i+0] = (int) (((a[5*i+0] >> 0) | (new Long(a[5*i+1]) << 8)) & 0x3FF);
            r.coeffs[4*i+1] = (int) (((a[5*i+1] >> 2) | (new Long(a[5*i+2]) << 6)) & 0x3FF);
            r.coeffs[4*i+2] = (int) (((a[5*i+2] >> 4) | (new Long(a[5*i+3]) << 4)) & 0x3FF);
            r.coeffs[4*i+3] = (int) (((a[5*i+3] >> 6) | (new Long(a[5*i+4]) << 2)) & 0x3FF);
        }

       // DBENCH_STOP(*tpack);
    }

    /*************************************************
     * Name:        polyt0_pack
     *
     * Description: Bit-pack polynomial t0 with coefficients in ]-2^{D-1}, 2^{D-1}].
     *
     * Arguments:   - uint8_t *r: pointer to output byte array with at least
     *                            POLYT0_PACKEDBYTES bytes
     *              - const poly *a: pointer to input polynomial
     **************************************************/
    void polyt0_pack(int[] r,int aIndex, poly a) {
        int i;
        Long[] t=new Long[8];
        Long allOne32=this.allOne32.longValue();
        //DBENCH_START();

        for(i = 0; i < config.N/8; ++i) {
            t[0] = allOne32 &((1 << (config.D-1)) - a.coeffs[aIndex+8*i+0]);
            t[1] = allOne32 &((1 << (config.D-1)) - a.coeffs[aIndex+8*i+1]);
            t[2] = allOne32 &((1 << (config.D-1)) - a.coeffs[aIndex+8*i+2]);
            t[3] = allOne32 &((1 << (config.D-1)) - a.coeffs[aIndex+8*i+3]);
            t[4] = allOne32 &((1 << (config.D-1)) - a.coeffs[aIndex+8*i+4]);
            t[5] = allOne32 &((1 << (config.D-1)) - a.coeffs[aIndex+8*i+5]);
            t[6] = allOne32 &((1 << (config.D-1)) - a.coeffs[aIndex+8*i+6]);
            t[7] = allOne32 &((1 << (config.D-1)) - a.coeffs[aIndex+8*i+7]);

            r[13*i+ 0]  = (int) (255&(  t[0]));
            r[13*i+ 1]  = (int) (255&(  t[0] >>  8));
            r[13*i+ 1] |=255&(  t[1] <<  5);
            r[13*i+ 2]  = (int) (255&(  t[1] >>  3));
            r[13*i+ 3]  = (int) (255&(  t[1] >> 11));
            r[13*i+ 3] |=255&(  t[2] <<  2);
            r[13*i+ 4]  = (int) (255&(  t[2] >>  6));
            r[13*i+ 4] |=255&(  t[3] <<  7);
            r[13*i+ 5]  = (int) (255&(  t[3] >>  1));
            r[13*i+ 6]  = (int) (255&(  t[3] >>  9));
            r[13*i+ 6] |=255&(  t[4] <<  4);
            r[13*i+ 7]  = (int) (255&(  t[4] >>  4));
            r[13*i+ 8]  = (int) (255&(  t[4] >> 12));
            r[13*i+ 8] |=255&(  t[5] <<  1);
            r[13*i+ 9]  = (int) (255&(  t[5] >>  7));
            r[13*i+ 9] |=255&(  t[6] <<  6);
            r[13*i+10]  = (int) (255&(  t[6] >>  2));
            r[13*i+11]  = (int) (255&(  t[6] >> 10));
            r[13*i+11] |=255&(  t[7] <<  3);
            r[13*i+12]  = (int) (255&(  t[7] >>  5));
        }

        //DBENCH_STOP(*tpack);
    }

    /*************************************************
     * Name:        polyt0_unpack
     *
     * Description: Unpack polynomial t0 with coefficients in ]-2^{D-1}, 2^{D-1}].
     *
     * Arguments:   - poly *r: pointer to output polynomial
     *              - const uint8_t *a: byte array with bit-packed polynomial
     **************************************************/
    void polyt0_unpack(int rIndex,poly r,int aIndex, int[] a) {
        int i;
        //DBENCH_START();

        for(i = 0; i < config.N/8; ++i) {
            r.coeffs[rIndex+8*i+0]  = a[13*i+0];
            r.coeffs[rIndex+8*i+0] |= new Long(a[13*i+1]) << 8;
            r.coeffs[rIndex+8*i+0] &= 0x1FFF;

            r.coeffs[rIndex+8*i+1]  = a[13*i+1] >> 5;
            r.coeffs[rIndex+8*i+1] |= new Long(a[13*i+2]) << 3;
            r.coeffs[rIndex+8*i+1] |= new Long(a[13*i+3]) << 11;
            r.coeffs[rIndex+8*i+1] &= 0x1FFF;

            r.coeffs[rIndex+8*i+2]  = a[13*i+3] >> 2;
            r.coeffs[rIndex+8*i+2] |= new Long(a[13*i+4] )<< 6;
            r.coeffs[rIndex+8*i+2] &= 0x1FFF;

            r.coeffs[rIndex+8*i+3]  = a[13*i+4] >> 7;
            r.coeffs[rIndex+8*i+3] |= new Long(a[13*i+5])<< 1;
            r.coeffs[rIndex+8*i+3] |= new Long(a[13*i+6]) << 9;
            r.coeffs[rIndex+8*i+3] &= 0x1FFF;

            r.coeffs[rIndex+8*i+4]  = a[13*i+6] >> 4;
            r.coeffs[rIndex+8*i+4] |= new Long(a[13*i+7]) << 4;
            r.coeffs[rIndex+8*i+4] |= new Long(a[13*i+8]) << 12;
            r.coeffs[rIndex+8*i+4] &= 0x1FFF;

            r.coeffs[rIndex+8*i+5]  = a[13*i+8] >> 1;
            r.coeffs[rIndex+8*i+5] |= new Long(a[13*i+9]) << 7;
            r.coeffs[rIndex+8*i+5] &= 0x1FFF;

            r.coeffs[rIndex+8*i+6]  = a[13*i+9] >> 6;
            r.coeffs[rIndex+8*i+6] |= new Long(a[13*i+10]) << 2;
            r.coeffs[rIndex+8*i+6] |= new Long(a[13*i+11]) << 10;
            r.coeffs[rIndex+8*i+6] &= 0x1FFF;

            r.coeffs[rIndex+8*i+7]  = a[13*i+11] >> 3;
            r.coeffs[rIndex+8*i+7] |= new Long(a[13*i+12]) << 5;
            r.coeffs[rIndex+8*i+7] &= 0x1FFF;

            r.coeffs[rIndex+8*i+0] = (1 << (config.D-1)) - r.coeffs[rIndex+8*i+0];
            r.coeffs[rIndex+8*i+1] = (1 << (config.D-1)) - r.coeffs[rIndex+8*i+1];
            r.coeffs[rIndex+8*i+2] = (1 << (config.D-1)) - r.coeffs[rIndex+8*i+2];
            r.coeffs[rIndex+8*i+3] = (1 << (config.D-1)) - r.coeffs[rIndex+8*i+3];
            r.coeffs[rIndex+8*i+4] = (1 << (config.D-1)) - r.coeffs[rIndex+8*i+4];
            r.coeffs[rIndex+8*i+5] = (1 << (config.D-1)) - r.coeffs[rIndex+8*i+5];
            r.coeffs[rIndex+8*i+6] = (1 << (config.D-1)) - r.coeffs[rIndex+8*i+6];
            r.coeffs[rIndex+8*i+7] = (1 << (config.D-1)) - r.coeffs[rIndex+8*i+7];
        }

        //DBENCH_STOP(*tpack);
    }

    /*************************************************
     * Name:        polyz_pack
     *
     * Description: Bit-pack polynomial with coefficients
     *              in [-(GAMMA1 - 1), GAMMA1].
     *
     * Arguments:   - uint8_t *r: pointer to output byte array with at least
     *                            POLYZ_PACKEDBYTES bytes
     *              - const poly *a: pointer to input polynomial
     **************************************************/
    void polyz_pack(int rIndex,int[] r, int aIndex,poly a) {
         int i;
        int[]  t=new int[4];
        int allOne32=this.allOne32.intValue();
        //DBENCH_START();

if (config.GAMMA1 == (1 << 17))
        {

            for(i = 0; i < config.N/4; ++i) {
                t[0] =allOne32 &( config.GAMMA1 - a.coeffs[aIndex+4*i+0]);
                t[1] =allOne32 &( config.GAMMA1 - a.coeffs[aIndex+4*i+1]);
                t[2] =allOne32 &( config.GAMMA1 - a.coeffs[aIndex+4*i+2]);
                t[3] =allOne32 &( config.GAMMA1 - a.coeffs[aIndex+4*i+3]);

                r[rIndex+9*i+0]  =255&( t[0]);
                r[rIndex+9*i+1]  =255&( t[0] >> 8);
                r[rIndex+9*i+2]  =255&( t[0] >> 16);
                r[rIndex+9*i+2] |=255&( t[1] << 2);
                r[rIndex+9*i+3]  =255&( t[1] >> 6);
                r[rIndex+9*i+4]  =255&( t[1] >> 14);
                r[rIndex+9*i+4] |=255&( t[2] << 4);
                r[rIndex+9*i+5]  =255&( t[2] >> 4);
                r[rIndex+9*i+6]  =255&( t[2] >> 12);
                r[rIndex+9*i+6] |=255&( t[3] << 6);
                r[rIndex+9*i+7]  =255&( t[3] >> 2);
                r[rIndex+9*i+8]  =255&( t[3] >> 10);
            }



        }
else if (config.GAMMA1 == (1 << 19))
{
    for(i = 0; i < config.N/2; ++i) {
        t[0] =allOne32 &(  config.GAMMA1 - a.coeffs[aIndex+2*i+0]);
        t[1] =allOne32 &(  config.GAMMA1 - a.coeffs[aIndex+2*i+1]);

        r[rIndex+5*i+0]   =255&(  t[0]);
        r[rIndex+5*i+1]   =255&(  t[0] >> 8);
        r[rIndex+5*i+2]   =255&(  t[0] >> 16);
        r[rIndex+5*i+2] |=255&(  t[1] << 4);
        r[rIndex+5*i+3]   =255&(  t[1] >> 4);
        r[rIndex+5*i+4]   =255&(  t[1] >> 12);
    }


}


        //DBENCH_STOP(*tpack);
    }

    /*************************************************
     * Name:        polyz_unpack
     *
     * Description: Unpack polynomial z with coefficients
     *              in [-(GAMMA1 - 1), GAMMA1].
     *
     * Arguments:   - poly *r: pointer to output polynomial
     *              - const uint8_t *a: byte array with bit-packed polynomial
     **************************************************/
    void polyz_unpack(int rIndex,poly r, int aIndex,int[] a) {
        int i;
       // DBENCH_START();

if (config.GAMMA1 == (1 << 17))
{
    for(i = 0; i < config.N/4; ++i) {
        r.coeffs[rIndex+4*i+0]  = a[aIndex+9*i+0];
        r.coeffs[rIndex+4*i+0] |= (uint32_t)a[aIndex+9*i+1] << 8;
        r.coeffs[rIndex+4*i+0] |= (uint32_t)a[aIndex+9*i+2] << 16;
        r.coeffs[rIndex+4*i+0] &= 0x3FFFF;

        r.coeffs[rIndex+4*i+1]  = a[aIndex+9*i+2] >> 2;
        r.coeffs[rIndex+4*i+1] |= (uint32_t)a[aIndex+9*i+3] << 6;
        r.coeffs[rIndex+4*i+1] |= (uint32_t)a[aIndex+9*i+4] << 14;
        r.coeffs[rIndex+4*i+1] &= 0x3FFFF;

        r.coeffs[rIndex+4*i+2]  = a[aIndex+9*i+4] >> 4;
        r.coeffs[rIndex+4*i+2] |= (uint32_t)a[aIndex+9*i+5] << 4;
        r.coeffs[rIndex+4*i+2] |= (uint32_t)a[aIndex+9*i+6] << 12;
        r.coeffs[rIndex+4*i+2] &= 0x3FFFF;

        r.coeffs[rIndex+4*i+3]  = a[aIndex+9*i+6] >> 6;
        r.coeffs[rIndex+4*i+3] |= (uint32_t)a[aIndex+9*i+7] << 2;
        r.coeffs[rIndex+4*i+3] |= (uint32_t)a[aIndex+9*i+8] << 10;
        r.coeffs[rIndex+4*i+3] &= 0x3FFFF;

        r.coeffs[rIndex+4*i+0] = config.GAMMA1 - r.coeffs[rIndex+4*i+0];
        r.coeffs[rIndex+4*i+1] = config.GAMMA1 - r.coeffs[rIndex+4*i+1];
        r.coeffs[rIndex+4*i+2] = config.GAMMA1 - r.coeffs[rIndex+4*i+2];
        r.coeffs[rIndex+4*i+3] = config.GAMMA1 - r.coeffs[rIndex+4*i+3];
    }



}
else if (config.GAMMA1 == (1 << 19))
        {

            for(i = 0; i < config.N/2; ++i) {
                r.coeffs[rIndex+2*i+0]  = a[aIndex+5*i+0];
                r.coeffs[rIndex+2*i+0] |= (uint32_t)a[aIndex+5*i+1] << 8;
                r.coeffs[rIndex+2*i+0] |= (uint32_t)a[aIndex+5*i+2] << 16;
                r.coeffs[rIndex+2*i+0] &= 0xFFFFF;

                r.coeffs[rIndex+2*i+1]  = a[aIndex+5*i+2] >> 4;
                r.coeffs[rIndex+2*i+1] |= (uint32_t)a[aIndex+5*i+3] << 4;
                r.coeffs[rIndex+2*i+1] |= (uint32_t)a[aIndex+5*i+4] << 12;
                r.coeffs[rIndex+2*i+0] &= 0xFFFFF;

                r.coeffs[rIndex+2*i+0] = config.GAMMA1 - r.coeffs[rIndex+2*i+0];
                r.coeffs[rIndex+2*i+1] = config.GAMMA1 - r.coeffs[2*i+1];
            }


        }

        //DBENCH_STOP(*tpack);
    }

    /*************************************************
     * Name:        polyw1_pack
     *
     * Description: Bit-pack polynomial w1 with coefficients in [0,15] or [0,43].
     *              Input coefficients are assumed to be standard representatives.
     *
     * Arguments:   - uint8_t *r: pointer to output byte array with at least
     *                            POLYW1_PACKEDBYTES bytes
     *              - const poly *a: pointer to input polynomial
     **************************************************/
    void polyw1_pack(int rIndex,uint8_t *r, poly a,int aIndex) {
        unsigned int i;
       // DBENCH_START();

#if GAMMA2 == (Q-1)/88
        for(i = 0; i < N/4; ++i) {
            r[rIndex+3*i+0]  = a.coeffs[aIndex+4*i+0];
            r[rIndex+3*i+0] |= a.coeffs[aIndex+4*i+1] << 6;
            r[rIndex+3*i+1]  = a.coeffs[aIndex+4*i+1] >> 2;
            r[rIndex+3*i+1] |= a.coeffs[aIndex+4*i+2] << 4;
            r[rIndex+3*i+2]  = a.coeffs[aIndex+4*i+2] >> 4;
            r[rIndex+3*i+2] |= a.coeffs[aIndex+4*i+3] << 2;
        }
#elif GAMMA2 == (Q-1)/32
        for(i = 0; i < N/2; ++i)
            r[rIndex+i] = a.coeffs[aIndex+2*i+0] | (a.coeffs[aIndex+2*i+1] << 4);
#endif

        //DBENCH_STOP(*tpack);
    }






}
