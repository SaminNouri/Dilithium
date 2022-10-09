import java.util.Arrays;

public class Sign {




    private static Config config = new Config();


    /*************************************************
     * Name:        crypto_sign_keypair
     *
     * Description: Generates public and private key.
     *
     * Arguments:   - uint8_t *pk: pointer to output public key (allocated
     *                             array of config.CRYPTO_PUBLICKEYBYTES bytes)
     *              - uint8_t *sk: pointer to output private key (allocated
     *                             array of CRYPTO_SECRETKEYBYTES bytes)
     *
     * Returns 0 (success)
     **************************************************/
    //tested
    static int crypto_sign_keypair(int pkIndex, int[] pk, int skIndex, int[] sk) {


        int[] seedbuf = new int[2 * config.SEEDBYTES + config.CRHBYTES];
        int seedbuf_Index = 0;
        int[] tr = new int[config.SEEDBYTES];
        int trIndex = 0;
        int rho, rhoprime, key;
        polyvecl[] mat = new polyvecl[config.K];
        for (int i=0;i<config.K;i++){
            mat[i]=new polyvecl();
        }
        int matIndex = 0;
        polyvecl s1 = new polyvecl(), s1hat = new polyvecl();
        polyveck s2 = new polyveck(), t1 = new polyveck(), t0 = new polyveck();

        /* Get randomness for rho, rhoprime and key */
        randombytes.randombytes(0, seedbuf, config.SEEDBYTES);
        /*for(int i=0;i<config.SEEDBYTES;i++){
            seedbuf[i]=sb[i];
        }*/




        Fips202.shake256(seedbuf_Index, seedbuf, 2 * config.SEEDBYTES + config.CRHBYTES, 0, seedbuf, config.SEEDBYTES);
        rho = seedbuf_Index;
        rhoprime = rho + config.SEEDBYTES;
        key = rhoprime + config.CRHBYTES;


        /* Expand matrix */
        PolynomialVector.polyvec_matrix_expand(matIndex, mat, rho, seedbuf);

        /* Sample short vectors s1 and s2 */
       PolynomialVector.polyvecl_uniform_eta(s1, rhoprime, seedbuf, 0);
       PolynomialVector.polyveck_uniform_eta(s2, rhoprime, seedbuf, config.L);


        /* Matrix-vector multiplication */
        //s1hat = s1; doesn't work in c
       for (int j=0;j<config.L;j++){

           for (int i=0;i<config.N;i++){
               s1hat.vec[j].coeffs[i]=s1.vec[j].coeffs[i];
           }

       }


        PolynomialVector.polyvecl_ntt(s1hat);
        PolynomialVector.polyvec_matrix_pointwise_montgomery(t1, mat, s1hat);
        PolynomialVector.polyveck_reduce(t1);
        PolynomialVector.polyveck_invntt_tomont(t1);


        /* Add error vector s2 */
        PolynomialVector.polyveck_add(t1, t1, s2);


        /* Extract t1 and write public key */
        PolynomialVector.polyveck_caddq(t1);
        PolynomialVector.polyveck_power2round(t1, t0, t1);


        packing.pack_pk(pkIndex,pk,rho,seedbuf, t1);

        /* Compute H(rho, t1) and write secret key */



        Fips202.shake256(trIndex, tr, config.SEEDBYTES, pkIndex, pk, config.CRYPTO_PUBLICKEYBYTES);
        packing.pack_sk(skIndex,sk, rho,seedbuf,trIndex, tr,key,seedbuf, t0, s1, s2);

        return 0;
    }

    /*************************************************
     * Name:        crypto_sign_signature
     *
     * Description: Computes signature.
     *
     * Arguments:   - uint8_t *sig:   pointer to output signature (of length CRYPTO_BYTES)
     *              - size_t *siglen: pointer to output length of signature
     *              - uint8_t *m:     pointer to message to be signed
     *              - size_t mlen:    length of message
     *              - uint8_t *sk:    pointer to bit-packed secret key
     *
     * Returns 0 (success)
     **************************************************/
    //tested
    static int crypto_sign_signature(int sigIndex, int[] sig,
                              SIZE siglen,
                              int mIndex, int[] m,
                              int mlen,
                              int skIndex, int[] sk) {
        int n;
        int[] seedbuf = new int[3 * config.SEEDBYTES + 2 * config.CRHBYTES];
        int seedbuf_Index = 0;
        int rho, tr, key, mu, rhoprime;
        int nonce = 0;
        polyvecl[] mat = new polyvecl[config.K];
        for (int i=0;i<config.K;i++){
            mat[i]=new polyvecl();
        }
        int matIndex = 0;
        polyvecl s1 = new polyvecl(), y = new polyvecl(), z = new polyvecl();
        polyveck t0 = new polyveck(), s2 = new polyveck(), w1 = new polyveck(), w0 = new polyveck(), h = new polyveck();
        poly cp = new poly();
        int cpIndex = 0;
        KeccakState state = new KeccakState();

        rho = seedbuf_Index;
        tr = rho + config.SEEDBYTES;
        key = tr + config.SEEDBYTES;
        mu = key + config.SEEDBYTES;
        rhoprime = mu + config.CRHBYTES;
        packing.unpack_sk(rho,seedbuf, tr,seedbuf, key,seedbuf, t0, s1, s2,skIndex, sk);

        /* Compute CRH(tr, msg) */
        Fips202.shake256_init(state);
        Fips202.shake256_absorb(state, tr, seedbuf, config.SEEDBYTES);
        Fips202.shake256_absorb(state, mIndex, m, mlen);
        Fips202.shake256_finalize(state);
        Fips202.shake256_squeeze(seedbuf, mu, config.CRHBYTES, state);

        if (config.DILITHIUM_RANDOMIZED_SIGNING == 1)
            randombytes.randombytes(rhoprime, seedbuf, config.CRHBYTES);
        else
            Fips202.shake256(rhoprime, seedbuf, config.CRHBYTES, key, seedbuf, config.SEEDBYTES + config.CRHBYTES);


        /* Expand matrix and transform vectors */
        PolynomialVector.polyvec_matrix_expand(matIndex, mat, rho, seedbuf);
        PolynomialVector.polyvecl_ntt(s1);
        PolynomialVector.polyveck_ntt(s2);
        PolynomialVector.polyveck_ntt(t0);

        rej:
        while (true) {
            /* Sample intermediate vector y */
            PolynomialVector.polyvecl_uniform_gamma1(y, rhoprime, seedbuf, nonce++);

            /* Matrix-vector multiplication */
            //z = y;
            for (int j=0;j<config.L;j++){

                for (int i=0;i<config.N;i++){
                    z.vec[j].coeffs[i]=y.vec[j].coeffs[i];
                }

            }
            PolynomialVector.polyvecl_ntt(z);
            PolynomialVector.polyvec_matrix_pointwise_montgomery(w1, mat, z);
            PolynomialVector.polyveck_reduce(w1);
            PolynomialVector.polyveck_invntt_tomont(w1);

            /* Decompose w and call the random oracle */
            PolynomialVector.polyveck_caddq(w1);
            PolynomialVector.polyveck_decompose(w1, w0, w1);
            PolynomialVector.polyveck_pack_w1(sig, sigIndex, w1);

            Fips202.shake256_init(state);
            Fips202.shake256_absorb(state, mu, seedbuf, config.CRHBYTES);
            Fips202.shake256_absorb(state, sigIndex, sig, config.K * config.POLYW1_PACKEDBYTES);
            Fips202.shake256_finalize(state);
            Fips202.shake256_squeeze(sig, sigIndex, config.SEEDBYTES, state);
            Polynomial.poly_challenge(cpIndex, cp, sigIndex, sig);
            Polynomial.poly_ntt(cp, cpIndex);

            /* Compute z, reject if it reveals secret */
            PolynomialVector.polyvecl_pointwise_poly_montgomery(z, cp, cpIndex, s1);
            PolynomialVector.polyvecl_invntt_tomont(z);
            PolynomialVector.polyvecl_add(z, z, y);
            PolynomialVector.polyvecl_reduce(z);




        if (PolynomialVector.polyvecl_chknorm(z, config.GAMMA1 - config.BETA) == 1)
            continue rej;

        /* Check that subtracting cs2 does not change high bits of w and low bits
         * do not reveal secret information */
        PolynomialVector.polyveck_pointwise_poly_montgomery(h, cp, cpIndex, s2);
        PolynomialVector.polyveck_invntt_tomont(h);
        PolynomialVector.polyveck_sub(w0, w0, h);
        PolynomialVector.polyveck_reduce(w0);
        if (PolynomialVector.polyveck_chknorm(w0, config.GAMMA2 - config.BETA) == 1)
            continue rej;

        /* Compute hints for w1 */
        PolynomialVector.polyveck_pointwise_poly_montgomery(h, cp, cpIndex, t0);
        PolynomialVector.polyveck_invntt_tomont(h);
        PolynomialVector.polyveck_reduce(h);
        if (PolynomialVector.polyveck_chknorm(h, config.GAMMA2) == 1)
            continue rej;

        PolynomialVector.polyveck_add(w0, w0, h);
        n = PolynomialVector.polyveck_make_hint(h, w0, w1);
        if (n > config.OMEGA)
            continue rej;

        /* Write signature */
        packing.pack_sig(sigIndex,sig,sigIndex, sig, z, h);
        siglen.size = config.CRYPTO_BYTES;
        return 0;


    }

    }


    /*************************************************
     * Name:        crypto_sign
     *
     * Description: Compute signed message.
     *
     * Arguments:   - uint8_t *sm: pointer to output signed message (allocated
     *                             array with CRYPTO_BYTES + mlen bytes),
     *                             can be equal to m
     *              - size_t *smlen: pointer to output length of signed
     *                               message
     *              - const uint8_t *m: pointer to message to be signed
     *              - size_t mlen: length of message
     *              - const uint8_t *sk: pointer to bit-packed secret key
     *
     * Returns 0 (success)
     **************************************************/
    //tested
    static int crypto_sign(int smIndex, int[] sm,
                           SIZE smlen,
                           int mIndex, int[] m,
                           int mlen,
                           int skIndex, int[] sk)
    {
        int i;

        for(i = 0; i < mlen; ++i)
            sm[config.CRYPTO_BYTES + mlen - 1 - i+smIndex] = m[mlen - 1 - i+mIndex];
        crypto_sign_signature(smIndex, sm, ((smlen)), smIndex + config.CRYPTO_BYTES,sm, mlen,skIndex, sk);
        smlen.size += mlen;
        return 0;
    }

    /*************************************************
     * Name:        crypto_sign_verify
     *
     * Description: Verifies signature.
     *
     * Arguments:   - uint8_t *m: pointer to input signature
     *              - size_t siglen: length of signature
     *              - const uint8_t *m: pointer to message
     *              - size_t mlen: length of message
     *              - const uint8_t *pk: pointer to bit-packed public key
     *
     * Returns 0 if signature could be verified correctly and -1 otherwise
     **************************************************/
    static int crypto_sign_verify(int sigIndex,int[] sig,
                           int siglen,
                       int mIndex,int[] m,
                           int mlen,
                       int pkIndex,int[] pk) throws DilithiumModeException {
        int i;
        int[] buf=new int[config.K*config.POLYW1_PACKEDBYTES];
        int bufIndex=0;
        int[] rho=new int[config.SEEDBYTES];
        int rhoIndex=0;
        int[] mu=new int[config.CRHBYTES];
        int muIndex=0;
        int[] c=new int[config.SEEDBYTES];
        int cIndex=0;
        int[] c2=new int[config.SEEDBYTES];
        int c2Index=0;
        poly cp=new poly();
        int cpIndex=0;
        polyvecl[] mat=new polyvecl[config.K];
        for (int j=0;j<config.K;j++){
            mat[j]=new polyvecl();
        }
        int matIndex=0;
        polyvecl z=new polyvecl();
        polyveck t1=new polyveck(), w1=new polyveck(), h=new polyveck();
        KeccakState state=new KeccakState();

        if(siglen != config.CRYPTO_BYTES)
            return -1;

        packing.unpack_pk(rhoIndex,rho, t1,pkIndex, pk);
        if(packing.unpack_sig(cIndex,c, z, h,sigIndex, sig)==1)
           return -1;
        if(PolynomialVector.polyvecl_chknorm(z, config.GAMMA1 - config.BETA)==1)
           return -1;




        /* Compute CRH(H(rho, t1), msg) */
        Fips202.shake256(muIndex,mu, config.SEEDBYTES,pkIndex, pk, config.CRYPTO_PUBLICKEYBYTES);
        Fips202.shake256_init(state);
        Fips202.shake256_absorb(state,muIndex, mu, config.SEEDBYTES);

        Fips202.shake256_absorb(state,mIndex, m, mlen);
        Fips202.shake256_finalize(state);
        Fips202.shake256_squeeze(mu,muIndex, config.CRHBYTES, state);





        /* Matrix-vector multiplication; compute Az - c2^dt1 */
        Polynomial.poly_challenge(cpIndex,cp,cIndex, c);
        PolynomialVector.polyvec_matrix_expand(matIndex,mat,rhoIndex, rho);


        PolynomialVector.polyvecl_ntt(z);
        PolynomialVector.polyvec_matrix_pointwise_montgomery(w1, mat, z);



        Polynomial.poly_ntt(cp,cpIndex);
        PolynomialVector.polyveck_shiftl(t1);
        PolynomialVector.polyveck_ntt(t1);
        PolynomialVector.polyveck_pointwise_poly_montgomery(t1, cp,cpIndex, t1);


        PolynomialVector.polyveck_sub(w1, w1, t1);
        PolynomialVector.polyveck_reduce(w1);
        PolynomialVector.polyveck_invntt_tomont(w1);



        /* Reconstruct w1 */
        PolynomialVector.polyveck_caddq(w1);
        PolynomialVector.polyveck_use_hint(w1, w1, h);
        PolynomialVector.polyveck_pack_w1(buf,bufIndex, w1);




        /* Call random oracle and verify challenge */
        Fips202.shake256_init(state);
        Fips202.shake256_absorb(state,muIndex, mu, config.CRHBYTES);
        Fips202.shake256_absorb(state,bufIndex, buf, config.K*config.POLYW1_PACKEDBYTES);
        Fips202.shake256_finalize(state);
        Fips202.shake256_squeeze(c2,c2Index, config.SEEDBYTES, state);






        for(i = 0; i < config.SEEDBYTES; ++i)
            if(c[i+cIndex] != c2[i+c2Index])
                return -1;

        return 0;
    }

    /*************************************************
     * Name:        crypto_sign_open
     *
     * Description: Verify signed message.
     *
     * Arguments:   - uint8_t *m: pointer to output message (allocated
     *                            array with smlen bytes), can be equal to sm
     *              - size_t *mlen: pointer to output length of message
     *              - const uint8_t *sm: pointer to signed message
     *              - size_t smlen: length of signed message
     *              - const uint8_t *pk: pointer to bit-packed public key
     *
     * Returns 0 if signed message could be verified correctly and -1 otherwise
     **************************************************/
    static int crypto_sign_open(int mIndex,int[] m,
                         SIZE mlen,
                     int smIndex,int[] sm,
                         int smlen,
                     int pkIndex,int[] pk) throws DilithiumModeException {
        int i;


        if(smlen < config.CRYPTO_BYTES)
        {
            /* Signature verification failed */
            mlen.size = -1;
            for(i = 0; i < smlen; ++i)
                m[i+mIndex] = 0;

            return -1;
        }
        System.out.println("smIndex:"+smIndex);
        mlen.size = smlen - config.CRYPTO_BYTES;
        int n=crypto_sign_verify(smIndex,sm, (config.CRYPTO_BYTES),smIndex + config.CRYPTO_BYTES,sm, mlen.size,pkIndex, pk);
        if(n==-1)
        {
            System.out.println("here2");
            /* Signature verification failed */
            mlen.size = -1;
            for(i = 0; i < mlen.size; ++i)
                m[i+mIndex] = 0;

            return -1;
        }
        else {

            System.out.println("maraz");
        /* All good, copy msg, return 0 */
        for(i = 0; i < mlen.size; ++i)
             m[i+mIndex] = sm[smIndex+config.CRYPTO_BYTES + i];
        return 0;
    }

    }










}
