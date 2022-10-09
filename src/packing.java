import java.util.Arrays;

public class packing {


private static Config config=new Config();

    /*************************************************
     * Name:        pack_pk
     *
     * Description: Bit-pack public key pk = (rho, t1).
     *
     * Arguments:   - uint8_t pk[]: output byte array
     *              - const uint8_t rho[]: byte array containing rho
     *              - const polyveck *t1: pointer to vector t1
     **************************************************/
    static void pack_pk(int pkIndex,int[] pk,
             int rhoIndex,int[] rho,
             polyveck t1)
    {
        int i;

        for(i = 0; i < config.SEEDBYTES; ++i)
            pk[i+pkIndex] = rho[i+rhoIndex];
        pkIndex += config.SEEDBYTES;

        for(i = 0; i < config.K; ++i)
            Polynomial.polyt1_pack(pkIndex + i*config.POLYT1_PACKEDBYTES,pk, 0,t1.vec[i]);
    }

    /*************************************************
     * Name:        unpack_pk
     *
     * Description: Unpack public key pk = (rho, t1).
     *
     * Arguments:   - const uint8_t rho[]: output byte array for rho
     *              - const polyveck *t1: pointer to output vector t1
     *              - uint8_t pk[]: byte array containing bit-packed pk
     **************************************************/
    static void unpack_pk(int rhoIndex,int[] rho,
                   polyveck t1,
               int pkIndex,int[] pk)
    {
         int i;

        for(i = 0; i < config.SEEDBYTES; ++i)
            rho[i+rhoIndex] = pk[i+pkIndex];
        pkIndex += config.SEEDBYTES;

        for(i = 0; i < config.K; ++i)
            Polynomial.polyt1_unpack(0,t1.vec[i], pkIndex + i*config.POLYT1_PACKEDBYTES,pk);
    }

    /*************************************************
     * Name:        pack_sk
     *
     * Description: Bit-pack secret key sk = (rho, tr, key, t0, s1, s2).
     *
     * Arguments:   - uint8_t sk[]: output byte array
     *              - const uint8_t rho[]: byte array containing rho
     *              - const uint8_t tr[]: byte array containing tr
     *              - const uint8_t key[]: byte array containing key
     *              - const polyveck *t0: pointer to vector t0
     *              - const polyvecl *s1: pointer to vector s1
     *              - const polyveck *s2: pointer to vector s2
     **************************************************/
    static void pack_sk(int skIndex,int[] sk,
             int rhoIndex,int[] rho,
             int trIndex,int[] tr,
             int keyIndex,int[] key,
             polyveck t0,
             polyvecl s1,
             polyveck s2)
    {
         int i;

        for(i = 0; i < config.SEEDBYTES; ++i)
            sk[skIndex+i] = rho[rhoIndex+i];
        skIndex += config.SEEDBYTES;

        for(i = 0; i < config.SEEDBYTES; ++i)
            sk[skIndex+i] = key[keyIndex+i];
        skIndex += config.SEEDBYTES;

        for(i = 0; i < config.SEEDBYTES; ++i)
            sk[skIndex+i] = tr[trIndex+i];
        skIndex += config.SEEDBYTES;

       for(i = 0; i < config.L; ++i) {

           //System.out.println("kuft:"+Arrays.toString(s1.vec[i].coeffs));
           Polynomial.polyeta_pack(skIndex + i * config.POLYETA_PACKEDBYTES, sk, 0, s1.vec[i]);

       }
        skIndex += config.L*config.POLYETA_PACKEDBYTES;

        for(i = 0; i < config.K; ++i)
            Polynomial.polyeta_pack(skIndex + i*config.POLYETA_PACKEDBYTES,sk,0, s2.vec[i]);
        skIndex += config.K*config.POLYETA_PACKEDBYTES;

        for(i = 0; i < config.K; ++i)
            Polynomial.polyt0_pack(skIndex + i*config.POLYT0_PACKEDBYTES,sk,0, t0.vec[i]);
    }

    /*************************************************
     * Name:        unpack_sk
     *
     * Description: Unpack secret key sk = (rho, tr, key, t0, s1, s2).
     *
     * Arguments:   - const uint8_t rho[]: output byte array for rho
     *              - const uint8_t tr[]: output byte array for tr
     *              - const uint8_t key[]: output byte array for key
     *              - const polyveck *t0: pointer to output vector t0
     *              - const polyvecl *s1: pointer to output vector s1
     *              - const polyveck *s2: pointer to output vector s2
     *              - uint8_t sk[]: byte array containing bit-packed sk
     **************************************************/
    static void unpack_sk(int rhoIndex,int[] rho,
                   int trIndex,int[] tr,
                   int keyIndex,int[] key,
                   polyveck t0,
                   polyvecl s1,
                   polyveck s2,
               int skIndex,int[] sk)
    {
         int i;

        for(i = 0; i < config.SEEDBYTES; ++i)
            rho[rhoIndex+i] = sk[skIndex+i];
        skIndex += config.SEEDBYTES;

        for(i = 0; i < config.SEEDBYTES; ++i)
            key[keyIndex+i] = sk[skIndex+i];
        skIndex += config.SEEDBYTES;

        for(i = 0; i < config.SEEDBYTES; ++i)
            tr[trIndex+i] = sk[skIndex+i];
        skIndex += config.SEEDBYTES;

        for(i=0; i < config.L; ++i)
            Polynomial.polyeta_unpack(0,s1.vec[i], skIndex + i*config.POLYETA_PACKEDBYTES,sk);
        skIndex += config.L*config.POLYETA_PACKEDBYTES;

        for(i=0; i < config.K; ++i)
            Polynomial.polyeta_unpack(0,s2.vec[i], skIndex + i*config.POLYETA_PACKEDBYTES,sk);
        skIndex += config.K*config.POLYETA_PACKEDBYTES;

        for(i=0; i < config.K; ++i)
            Polynomial.polyt0_unpack(0,t0.vec[i], skIndex + i*config.POLYT0_PACKEDBYTES,sk);
    }

    /*************************************************
     * Name:        pack_sig
     *
     * Description: Bit-pack signature sig = (c, z, h).
     *
     * Arguments:   - uint8_t sig[]: output byte array
     *              - const uint8_t *c: pointer to challenge hash length config.SEEDBYTES
     *              - const polyvecl *z: pointer to vector z
     *              - const polyveck *h: pointer to hint vector h
     **************************************************/
    static void pack_sig(int sigIndex,int[] sig,
              int cIndex,int[] c,
              polyvecl z,
              polyveck h)
    {
         int i, j, k;

        for(i=0; i < config.SEEDBYTES; ++i)
            sig[i+sigIndex] = c[i+cIndex];
        sigIndex += config.SEEDBYTES;

        for(i = 0; i < config.L; ++i)
            Polynomial.polyz_pack(sigIndex + i*config.POLYZ_PACKEDBYTES,sig,0, z.vec[i]);
        sigIndex += config.L*config.POLYZ_PACKEDBYTES;

        /* Encode h */
        for(i = 0; i < config.OMEGA + config.K; ++i)
            sig[sigIndex+i] = 0;

        k = 0;
        for(i = 0; i < config.K; ++i) {
            for(j = 0; j < config.N; ++j)
                if(h.vec[i].coeffs[j] != 0)
                    sig[sigIndex+k++] = j;

            sig[sigIndex+config.OMEGA + i] = k;
        }
    }

    /*************************************************
     * Name:        unpack_sig
     *
     * Description: Unpack signature sig = (c, z, h).
     *
     * Arguments:   - uint8_t *c: pointer to output challenge hash
     *              - polyvecl *z: pointer to output vector z
     *              - polyveck *h: pointer to output hint vector h
     *              - const uint8_t sig[]: byte array containing
     *                bit-packed signature
     *
     * Returns 1 in case of malformed signature; otherwise 0.
     **************************************************/
    static int unpack_sig(int cIndex,int[] c,
                   polyvecl z,
                   polyveck h,
               int sigIndex,int[] sig) {
        int i, j, k;

        for (i = 0; i < config.SEEDBYTES; ++i)
            c[cIndex+i] = sig[sigIndex+i];
        sigIndex += config.SEEDBYTES;

        for (i = 0; i < config.L; ++i)
            Polynomial.polyz_unpack(0,z.vec[i], sigIndex + i * config.POLYZ_PACKEDBYTES,sig);
        sigIndex += config.L * config.POLYZ_PACKEDBYTES;

        /* Decode h */
        k = 0;
        for (i = 0; i < config.K; ++i) {
            for (j = 0; j < config.N; ++j)
                h.vec[i].coeffs[j] = 0;

            if (sig[sigIndex+config.OMEGA + i] < k || sig[sigIndex+config.OMEGA + i] > config.OMEGA)
                return 1;

            for (j = k; j < sig[sigIndex+config.OMEGA + i]; ++j) {
                /* Coefficients are ordered for strong unforgeability */
                if (j > k && sig[sigIndex+j] <= sig[sigIndex+j - 1])return 1;
                h.vec[i].coeffs[sig[sigIndex+j]] = 1;
            }

            k = sig[sigIndex+config.OMEGA + i];
        }

        /* Extra indices are zero for strong unforgeability */
        for (j = k; j < config.OMEGA; ++j)
            if (sig[sigIndex+j]==1)
                return 1;

        return 0;
    }







}
