public class PolynomialVector {

    private static Config config=new Config();

    /*************************************************
     * Name:        expand_mat
     *
     * Description: Impementation of ExpandA. Generates matrix A with uniformly
     *              random coefficients a_{i,j} by performing rejection
     *              sampling on the output stream of SHAKE128(rho|j|i)
     *              or AES256CTR(rho,j|i).
     *
     * Arguments:   - polyvecl mat[K]: output matrix
     *              - const uint8_t rho[]: byte array containing seed rho
     **************************************************/
    //warming! we have supposed that the pointer to mat[i+matIndex].vec[j] is 0
    static void polyvec_matrix_expand(int matIndex,polyvecl[] mat,int rhoIndex, int[] rho) {
        int i, j;

        for(i = 0; i < config.K; ++i)
            for(j = 0; j < config.L; ++j)
                Polynomial.poly_uniform(mat[i+matIndex].vec[j],0, rho,rhoIndex, (i << 8) + j);
    }
    //warning
    static void polyvec_matrix_pointwise_montgomery(polyveck t, polyvecl[] mat, polyvecl v) {
        int i;

        for(i = 0; i < config.K; ++i)
            polyvecl_pointwise_acc_montgomery(t.vec[i],0,mat[i], v);
    }

/**************************************************************/
/************ Vectors of polynomials of length L **************/
    /**************************************************************/
   //warning!
    static void polyvecl_uniform_eta(polyvecl v,int seedIndex, int[] seed, int nonce) {
        int i;

        for(i = 0; i < config.L; ++i)
            Polynomial.poly_uniform_eta(0,v.vec[i],seedIndex, seed, nonce++);
    }
    //warning!
    static void polyvecl_uniform_gamma1(polyvecl v, int seedIndex, int[] seed, int nonce) {
        int i;

        for(i = 0; i < config.L; ++i)
            Polynomial.poly_uniform_gamma1(0,v.vec[i],seedIndex, seed, config.L*nonce + i);
    }
   //warning!
    static void polyvecl_reduce(polyvecl v) {
        int i;

        for(i = 0; i < config.L; ++i)
            Polynomial.poly_reduce(v.vec[i],0);
    }

    /*************************************************
     * Name:        polyvecl_add
     *
     * Description: Add vectors of polynomials of length L.
     *              No modular reduction is performed.
     *
     * Arguments:   - polyvecl *w: pointer to output vector
     *              - const polyvecl *u: pointer to first summand
     *              - const polyvecl *v: pointer to second summand
     **************************************************/
    //warning!
    static void polyvecl_add(polyvecl w, polyvecl u, polyvecl v) {
        int i;

        for(i = 0; i < config.L; ++i)
            Polynomial.poly_add(w.vec[i], 0,u.vec[i],0, v.vec[i],0);
    }

    /*************************************************
     * Name:        polyvecl_ntt
     *
     * Description: Forward NTT of all polynomials in vector of length L. Output
     *              coefficients can be up to 16*Q larger than input coefficients.
     *
     * Arguments:   - polyvecl *v: pointer to input/output vector
     **************************************************/
    //warning!
    static void polyvecl_ntt(polyvecl v) {
        int i;

        for(i = 0; i < config.L; ++i)
            Polynomial.poly_ntt(v.vec[i],0);
    }
    //warning!
    static void polyvecl_invntt_tomont(polyvecl v) {
        int i;

        for(i = 0; i < config.L; ++i)
            Polynomial.poly_invntt_tomont(v.vec[i],0);
    }
    //warning!
    static void polyvecl_pointwise_poly_montgomery(polyvecl r, poly a,int aIndex, polyvecl v) {
        int i;

        for(i = 0; i < config.L; ++i)
            Polynomial.poly_pointwise_montgomery(r.vec[i],0, a,aIndex, v.vec[i],0);
    }

    /*************************************************
     * Name:        polyvecl_pointwise_acc_montgomery
     *
     * Description: Pointwise multiply vectors of polynomials of length L, multiply
     *              resulting vector by 2^{-32} and add (accumulate) polynomials
     *              in it. Input/output vectors are in NTT domain representation.
     *
     * Arguments:   - poly *w: output polynomial
     *              - const polyvecl *u: pointer to first input vector
     *              - const polyvecl *v: pointer to second input vector
     **************************************************/
    //warning!
    static void polyvecl_pointwise_acc_montgomery(poly w,int wIndex,
                                       polyvecl u,
                                       polyvecl v)
    {
        int i;
        poly t=new poly();
        int tIndex=0;

        Polynomial.poly_pointwise_montgomery(w,wIndex, u.vec[0],0, v.vec[0],0);
        for(i = 1; i < config.L; ++i) {
            Polynomial.poly_pointwise_montgomery(t,tIndex, u.vec[i],0, v.vec[i],0);
            Polynomial.poly_add(w,wIndex, w,wIndex, t,tIndex);
        }
    }

    /*************************************************
     * Name:        polyvecl_chknorm
     *
     * Description: Check infinity norm of polynomials in vector of length L.
     *              Assumes input polyvecl to be reduced by polyvecl_reduce().
     *
     * Arguments:   - const polyvecl *v: pointer to vector
     *              - int32_t B: norm bound
     *
     * Returns 0 if norm of all polynomials is strictly smaller than B <= (Q-1)/8
     * and 1 otherwise.
     **************************************************/
    //warning!
    int polyvecl_chknorm(polyvecl v, int bound)  {
        int i;

        for(i = 0; i < config.L; ++i)
            if(Polynomial.poly_chknorm(v.vec[i],0, bound)==1)
               return 1;

        return 0;
    }

/**************************************************************/
/************ Vectors of polynomials of length K **************/
    /**************************************************************/
    //warning!
    static void polyveck_uniform_eta(polyveck v, int seedIndex,int[] seed, int nonce) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_uniform_eta(0,v.vec[i],seedIndex, seed, nonce++);
    }

    /*************************************************
     * Name:        polyveck_reduce
     *
     * Description: Reduce coefficients of polynomials in vector of length K
     *              to representatives in [-6283009,6283007].
     *
     * Arguments:   - polyveck *v: pointer to input/output vector
     **************************************************/
    //warning!
    static void polyveck_reduce(polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_reduce(v.vec[i],0);
    }

    /*************************************************
     * Name:        polyveck_caddq
     *
     * Description: For all coefficients of polynomials in vector of length K
     *              add Q if coefficient is negative.
     *
     * Arguments:   - polyveck *v: pointer to input/output vector
     **************************************************/
    static void polyveck_caddq(polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_caddq(v.vec[i],0);
    }

    /*************************************************
     * Name:        polyveck_add
     *
     * Description: Add vectors of polynomials of length K.
     *              No modular reduction is performed.
     *
     * Arguments:   - polyveck *w: pointer to output vector
     *              - const polyveck *u: pointer to first summand
     *              - const polyveck *v: pointer to second summand
     **************************************************/
    //warning!
    static void polyveck_add(polyveck w, polyveck u, polyveck v) {
       int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_add(w.vec[i],0, u.vec[i],0, v.vec[i],0);
    }

    /*************************************************
     * Name:        polyveck_sub
     *
     * Description: Subtract vectors of polynomials of length K.
     *              No modular reduction is performed.
     *
     * Arguments:   - polyveck *w: pointer to output vector
     *              - const polyveck *u: pointer to first input vector
     *              - const polyveck *v: pointer to second input vector to be
     *                                   subtracted from first input vector
     **************************************************/
    //warning!
    static void polyveck_sub(polyveck w, polyveck u, polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_sub(w.vec[i],0, u.vec[i],0, v.vec[i],0);
    }

    /*************************************************
     * Name:        polyveck_shiftl
     *
     * Description: Multiply vector of polynomials of Length K by 2^D without modular
     *              reduction. Assumes input coefficients to be less than 2^{31-D}.
     *
     * Arguments:   - polyveck *v: pointer to input/output vector
     **************************************************/
    //warning
    static void polyveck_shiftl(polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_shiftl(v.vec[i],0);
    }

    /*************************************************
     * Name:        polyveck_ntt
     *
     * Description: Forward NTT of all polynomials in vector of length K. Output
     *              coefficients can be up to 16*Q larger than input coefficients.
     *
     * Arguments:   - polyveck *v: pointer to input/output vector
     **************************************************/
    //warning
    static void polyveck_ntt(polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_ntt(v.vec[i],0);
    }

    /*************************************************
     * Name:        polyveck_invntt_tomont
     *
     * Description: Inverse NTT and multiplication by 2^{32} of polynomials
     *              in vector of length K. Input coefficients need to be less
     *              than 2*Q.
     *
     * Arguments:   - polyveck *v: pointer to input/output vector
     **************************************************/
    //warning
    static void polyveck_invntt_tomont(polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_invntt_tomont(v.vec[i],0);
    }
    //warning!
    static void polyveck_pointwise_poly_montgomery(polyveck r, poly a,int aIndex, polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_pointwise_montgomery(r.vec[i],0, a,aIndex, v.vec[i],0);
    }


    /*************************************************
     * Name:        polyveck_chknorm
     *
     * Description: Check infinity norm of polynomials in vector of length K.
     *              Assumes input polyveck to be reduced by polyveck_reduce().
     *
     * Arguments:   - const polyveck *v: pointer to vector
     *              - int32_t B: norm bound
     *
     * Returns 0 if norm of all polynomials are strictly smaller than B <= (Q-1)/8
     * and 1 otherwise.
     **************************************************/
    //warning!
    int polyveck_chknorm(polyveck v, int bound) {
        int i;

        for(i = 0; i < config.K; ++i)
            if(Polynomial.poly_chknorm(v.vec[i],0, bound)==1)
                return 1;

        return 0;
    }

    /*************************************************
     * Name:        polyveck_power2round
     *
     * Description: For all coefficients a of polynomials in vector of length K,
     *              compute a0, a1 such that a mod^+ Q = a1*2^D + a0
     *              with -2^{D-1} < a0 <= 2^{D-1}. Assumes coefficients to be
     *              standard representatives.
     *
     * Arguments:   - polyveck *v1: pointer to output vector of polynomials with
     *                              coefficients a1
     *              - polyveck *v0: pointer to output vector of polynomials with
     *                              coefficients a0
     *              - const polyveck *v: pointer to input vector
     **************************************************/
    //warning!
    static void polyveck_power2round(polyveck v1, polyveck v0, polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_power2round(v1.vec[i],0, v0.vec[i],0, v.vec[i],0);
    }

    /*************************************************
     * Name:        polyveck_decompose
     *
     * Description: For all coefficients a of polynomials in vector of length K,
     *              compute high and low bits a0, a1 such a mod^+ Q = a1*ALPHA + a0
     *              with -ALPHA/2 < a0 <= ALPHA/2 except a1 = (Q-1)/ALPHA where we
     *              set a1 = 0 and -ALPHA/2 <= a0 = a mod Q - Q < 0.
     *              Assumes coefficients to be standard representatives.
     *
     * Arguments:   - polyveck *v1: pointer to output vector of polynomials with
     *                              coefficients a1
     *              - polyveck *v0: pointer to output vector of polynomials with
     *                              coefficients a0
     *              - const polyveck *v: pointer to input vector
     **************************************************/
    //warning!
    static void polyveck_decompose(polyveck v1, polyveck v0, polyveck v) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_decompose(v1.vec[i],0, v0.vec[i],0, v.vec[i],0);
    }

/*************************************************
 * Name:        polyveck_make_hint
 *
 * Description: Compute hint vector.
 *
 * Arguments:   - polyveck *h: pointer to output vector
 *              - const polyveck *v0: pointer to low part of input vector
 *              - const polyveck *v1: pointer to high part of input vector
 *
 * Returns number of 1 bits.
 **************************************************/
    int polyveck_make_hint(polyveck h,
                                polyveck v0,
                                polyveck v1)
    {
       int i, s = 0;

        for(i = 0; i < config.K; ++i)
            s += Polynomial.poly_make_hint(h.vec[i],0, v0.vec[i],0, v1.vec[i],0);

        return s;
    }

    /*************************************************
     * Name:        polyveck_use_hint
     *
     * Description: Use hint vector to correct the high bits of input vector.
     *
     * Arguments:   - polyveck *w: pointer to output vector of polynomials with
     *                             corrected high bits
     *              - const polyveck *u: pointer to input vector
     *              - const polyveck *h: pointer to input hint vector
     **************************************************/
    //warning!
    static void polyveck_use_hint(polyveck w, polyveck u, polyveck h) throws DilithiumModeException {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.poly_use_hint(w.vec[i],0, u.vec[i],0, h.vec[i],0);
    }

    static void polyveck_pack_w1(int[] r,int rIndex, polyveck w1) {
        int i;

        for(i = 0; i < config.K; ++i)
            Polynomial.polyw1_pack(rIndex+i*config.POLYW1_PACKEDBYTES,r, w1.vec[i],0);
    }







}
