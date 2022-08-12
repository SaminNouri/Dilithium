public class TSHAKE256 {

    private long[] s=new long[25];
    private int pos=0;

       int  NROUNDS= 24;
      static long ROL(long a,int  offset) {

        return ((a << offset) ^ (a >> (64-offset)));
    }

    /*************************************************
     * Name:        load64
     *
     * Description: Load 8 bytes into uint64_t in little-endian order
     *
     * Arguments:   - const uint8_t *x: pointer to input byte array
     *
     * Returns the loaded 64-bit unsigned integer
     **************************************************/
    static long load64(byte[] x) {
        int i;
        long r = 0L;

        for(i=0;i<8;i++)
            r |= (long)x[i] << 8*i;

        return r;
    }

    /*************************************************
     * Name:        store64
     *
     * Description: Store a 64-bit integer to array of 8 bytes in little-endian order
     *
     * Arguments:   - uint8_t *x: pointer to the output byte array (allocated)
     *              - uint64_t u: input 64-bit unsigned integer
     **************************************************/
    static void store64(byte[] x, long u) {
        int i;

        for(i=0;i<8;i++)
            x[i] = (byte) (u >>> 8*i);
    }

    /* Keccak round constants */
      static long[] KeccakF_RoundConstants= {
                0x0000000000000001l, 0x0000000000008082l,
                0x800000000000808Al, 0x8000000080008000l,
                0x000000000000808Bl, 0x0000000080000001l,
                0x8000000080008081l, 0x8000000000008009l,
                0x000000000000008Al, 0x0000000000000088l,
                0x0000000080008009l, 0x000000008000000Al,
                0x000000008000808Bl, 0x800000000000008Bl,
                0x8000000000008089l, 0x8000000000008003l,
                0x8000000000008002l, 0x8000000000000080l,
                0x000000000000800Al, 0x800000008000000Al,
                0x8000000080008081l, 0x8000000000008080l,
                0x0000000080000001l, 0x8000000080008008l
    };

    /*************************************************
     * Name:        KeccakF1600_StatePermute
     *
     * Description: The Keccak F1600 Permutation
     *
     * Arguments:   - uint64_t *state: pointer to input/output Keccak state
     **************************************************/
    static void KeccakF1600_StatePermute(long[] A)
    {
        int round;

        long Aba, Abe, Abi, Abo, Abu;
        long Aga, Age, Agi, Ago, Agu;
        long Aka, Ake, Aki, Ako, Aku;
        long Ama, Ame, Ami, Amo, Amu;
        long Asa, Ase, Asi, Aso, Asu;
        long BCa, BCe, BCi, BCo, BCu;
        long Da, De, Di, Do, Du;
        long Eba, Ebe, Ebi, Ebo, Ebu;
        long Ega, Ege, Egi, Ego, Egu;
        long Eka, Eke, Eki, Eko, Eku;
        long Ema, Eme, Emi, Emo, Emu;
        long Esa, Ese, Esi, Eso, Esu;

        //copyFromA(A, A)
        Aba = A[ 0];
        Abe = A[ 1];
        Abi = A[ 2];
        Abo = A[ 3];
        Abu = A[ 4];
        Aga = A[ 5];
        Age = A[ 6];
        Agi = A[ 7];
        Ago = A[ 8];
        Agu = A[ 9];
        Aka = A[10];
        Ake = A[11];
        Aki = A[12];
        Ako = A[13];
        Aku = A[14];
        Ama = A[15];
        Ame = A[16];
        Ami = A[17];
        Amo = A[18];
        Amu = A[19];
        Asa = A[20];
        Ase = A[21];
        Asi = A[22];
        Aso = A[23];
        Asu = A[24];

        for(round = 0; round < 24; round += 2) {
            //    prepareTheta
            BCa = Aba^Aga^Aka^Ama^Asa;
            BCe = Abe^Age^Ake^Ame^Ase;
            BCi = Abi^Agi^Aki^Ami^Asi;
            BCo = Abo^Ago^Ako^Amo^Aso;
            BCu = Abu^Agu^Aku^Amu^Asu;

            //thetaRhoPiChiIotaPrepareTheta(round, A, E)
            Da = BCu^ROL(BCe, 1);
            De = BCa^ROL(BCi, 1);
            Di = BCe^ROL(BCo, 1);
            Do = BCi^ROL(BCu, 1);
            Du = BCo^ROL(BCa, 1);

            Aba ^= Da;
            BCa = Aba;
            Age ^= De;
            BCe = ROL(Age, 44);
            Aki ^= Di;
            BCi = ROL(Aki, 43);
            Amo ^= Do;
            BCo = ROL(Amo, 21);
            Asu ^= Du;
            BCu = ROL(Asu, 14);
            Eba =   BCa ^((~BCe)&  BCi );
            Eba ^= (long)KeccakF_RoundConstants[round];
            Ebe =   BCe ^((~BCi)&  BCo );
            Ebi =   BCi ^((~BCo)&  BCu );
            Ebo =   BCo ^((~BCu)&  BCa );
            Ebu =   BCu ^((~BCa)&  BCe );

            Abo ^= Do;
            BCa = ROL(Abo, 28);
            Agu ^= Du;
            BCe = ROL(Agu, 20);
            Aka ^= Da;
            BCi = ROL(Aka,  3);
            Ame ^= De;
            BCo = ROL(Ame, 45);
            Asi ^= Di;
            BCu = ROL(Asi, 61);
            Ega =   BCa ^((~BCe)&  BCi );
            Ege =   BCe ^((~BCi)&  BCo );
            Egi =   BCi ^((~BCo)&  BCu );
            Ego =   BCo ^((~BCu)&  BCa );
            Egu =   BCu ^((~BCa)&  BCe );

            Abe ^= De;
            BCa = ROL(Abe,  1);
            Agi ^= Di;
            BCe = ROL(Agi,  6);
            Ako ^= Do;
            BCi = ROL(Ako, 25);
            Amu ^= Du;
            BCo = ROL(Amu,  8);
            Asa ^= Da;
            BCu = ROL(Asa, 18);
            Eka =   BCa ^((~BCe)&  BCi );
            Eke =   BCe ^((~BCi)&  BCo );
            Eki =   BCi ^((~BCo)&  BCu );
            Eko =   BCo ^((~BCu)&  BCa );
            Eku =   BCu ^((~BCa)&  BCe );

            Abu ^= Du;
            BCa = ROL(Abu, 27);
            Aga ^= Da;
            BCe = ROL(Aga, 36);
            Ake ^= De;
            BCi = ROL(Ake, 10);
            Ami ^= Di;
            BCo = ROL(Ami, 15);
            Aso ^= Do;
            BCu = ROL(Aso, 56);
            Ema =   BCa ^((~BCe)&  BCi );
            Eme =   BCe ^((~BCi)&  BCo );
            Emi =   BCi ^((~BCo)&  BCu );
            Emo =   BCo ^((~BCu)&  BCa );
            Emu =   BCu ^((~BCa)&  BCe );

            Abi ^= Di;
            BCa = ROL(Abi, 62);
            Ago ^= Do;
            BCe = ROL(Ago, 55);
            Aku ^= Du;
            BCi = ROL(Aku, 39);
            Ama ^= Da;
            BCo = ROL(Ama, 41);
            Ase ^= De;
            BCu = ROL(Ase,  2);
            Esa =   BCa ^((~BCe)&  BCi );
            Ese =   BCe ^((~BCi)&  BCo );
            Esi =   BCi ^((~BCo)&  BCu );
            Eso =   BCo ^((~BCu)&  BCa );
            Esu =   BCu ^((~BCa)&  BCe );

            //    prepareTheta
            BCa = Eba^Ega^Eka^Ema^Esa;
            BCe = Ebe^Ege^Eke^Eme^Ese;
            BCi = Ebi^Egi^Eki^Emi^Esi;
            BCo = Ebo^Ego^Eko^Emo^Eso;
            BCu = Ebu^Egu^Eku^Emu^Esu;

            //thetaRhoPiChiIotaPrepareTheta(round+1, E, A)
            Da = BCu^ROL(BCe, 1);
            De = BCa^ROL(BCi, 1);
            Di = BCe^ROL(BCo, 1);
            Do = BCi^ROL(BCu, 1);
            Du = BCo^ROL(BCa, 1);

            Eba ^= Da;
            BCa = Eba;
            Ege ^= De;
            BCe = ROL(Ege, 44);
            Eki ^= Di;
            BCi = ROL(Eki, 43);
            Emo ^= Do;
            BCo = ROL(Emo, 21);
            Esu ^= Du;
            BCu = ROL(Esu, 14);
            Aba =   BCa ^((~BCe)&  BCi );
            Aba ^= (long)KeccakF_RoundConstants[round+1];
            Abe =   BCe ^((~BCi)&  BCo );
            Abi =   BCi ^((~BCo)&  BCu );
            Abo =   BCo ^((~BCu)&  BCa );
            Abu =   BCu ^((~BCa)&  BCe );

            Ebo ^= Do;
            BCa = ROL(Ebo, 28);
            Egu ^= Du;
            BCe = ROL(Egu, 20);
            Eka ^= Da;
            BCi = ROL(Eka, 3);
            Eme ^= De;
            BCo = ROL(Eme, 45);
            Esi ^= Di;
            BCu = ROL(Esi, 61);
            Aga =   BCa ^((~BCe)&  BCi );
            Age =   BCe ^((~BCi)&  BCo );
            Agi =   BCi ^((~BCo)&  BCu );
            Ago =   BCo ^((~BCu)&  BCa );
            Agu =   BCu ^((~BCa)&  BCe );

            Ebe ^= De;
            BCa = ROL(Ebe, 1);
            Egi ^= Di;
            BCe = ROL(Egi, 6);
            Eko ^= Do;
            BCi = ROL(Eko, 25);
            Emu ^= Du;
            BCo = ROL(Emu, 8);
            Esa ^= Da;
            BCu = ROL(Esa, 18);
            Aka =   BCa ^((~BCe)&  BCi );
            Ake =   BCe ^((~BCi)&  BCo );
            Aki =   BCi ^((~BCo)&  BCu );
            Ako =   BCo ^((~BCu)&  BCa );
            Aku =   BCu ^((~BCa)&  BCe );

            Ebu ^= Du;
            BCa = ROL(Ebu, 27);
            Ega ^= Da;
            BCe = ROL(Ega, 36);
            Eke ^= De;
            BCi = ROL(Eke, 10);
            Emi ^= Di;
            BCo = ROL(Emi, 15);
            Eso ^= Do;
            BCu = ROL(Eso, 56);
            Ama =   BCa ^((~BCe)&  BCi );
            Ame =   BCe ^((~BCi)&  BCo );
            Ami =   BCi ^((~BCo)&  BCu );
            Amo =   BCo ^((~BCu)&  BCa );
            Amu =   BCu ^((~BCa)&  BCe );

            Ebi ^= Di;
            BCa = ROL(Ebi, 62);
            Ego ^= Do;
            BCe = ROL(Ego, 55);
            Eku ^= Du;
            BCi = ROL(Eku, 39);
            Ema ^= Da;
            BCo = ROL(Ema, 41);
            Ese ^= De;
            BCu = ROL(Ese, 2);
            Asa =   BCa ^((~BCe)&  BCi );
            Ase =   BCe ^((~BCi)&  BCo );
            Asi =   BCi ^((~BCo)&  BCu );
            Aso =   BCo ^((~BCu)&  BCa );
            Asu =   BCu ^((~BCa)&  BCe );
        }

        //copyToA(A, A)
        A[ 0] = Aba;
        A[ 1] = Abe;
        A[ 2] = Abi;
        A[ 3] = Abo;
        A[ 4] = Abu;
        A[ 5] = Aga;
        A[ 6] = Age;
        A[ 7] = Agi;
        A[ 8] = Ago;
        A[ 9] = Agu;
        A[10] = Aka;
        A[11] = Ake;
        A[12] = Aki;
        A[13] = Ako;
        A[14] = Aku;
        A[15] = Ama;
        A[16] = Ame;
        A[17] = Ami;
        A[18] = Amo;
        A[19] = Amu;
        A[20] = Asa;
        A[21] = Ase;
        A[22] = Asi;
        A[23] = Aso;
        A[24] = Asu;
    }

    /*************************************************
     * Name:        keccak_init
     *
     * Description: Initializes the Keccak state.
     *
     * Arguments:   - uint64_t *s: pointer to Keccak state
     **************************************************/
    void keccak_init()
    {
        int i;
        for(i=0;i<25;i++)
            this.s[i] = 0;
    }

/*************************************************
 * Name:        keccak_absorb
 *
 * Description: Absorb step of Keccak; incremental.
 *
 * Arguments:   - uint64_t *s: pointer to Keccak state
 *              - unsigned int pos: position in current block to be absorbed
 *              - unsigned int r: rate in bytes (e.g., 168 for SHAKE128)
 *              - const uint8_t *in: pointer to input to be absorbed into s
 *              - size_t inlen: length of input in bytes
 *
 * Returns new position pos in current block
 **************************************************/
    int keccak_absorb(int r,byte[] srcin, int in, int inlen)
    {
        int i;

        while(pos+inlen >= r) {
            for(i=pos;i<r;i++)
                KeccakF_RoundConstants[i/8] ^= (long)srcin[in] << 8*(i%8);
                in++;
            inlen -= r-pos;
            KeccakF1600_StatePermute(s);
            pos = 0;
        }

        for(i=pos;i<pos+inlen;i++)
            s[i/8] ^= (long) srcin[in] << 8*(i%8);
            in++;

        return i;
    }

    /*************************************************
     * Name:        keccak_finalize
     *
     * Description: Finalize absorb step.
     *
     * Arguments:   - uint64_t *s: pointer to Keccak state
     *              - unsigned int pos: position in current block to be absorbed
     *              - unsigned int r: rate in bytes (e.g., 168 for SHAKE128)
     *              - uint8_t p: domain separation byte
     **************************************************/
    void keccak_finalize(int r,int p)
    {
        s[pos/8] ^= (long)p << 8*(pos%8);
        s[r/8-1] ^=  Integer.toUnsignedLong(0x80) << 56;
        pos=136;
    }

/*************************************************
 * Name:        keccak_squeeze
 *
 * Description: Squeeze step of Keccak. Squeezes arbitratrily many bytes.
 *              Modifies the state. Can be called multiple times to keep
 *              squeezing, i.e., is incremental.
 *
 * Arguments:   - uint8_t *out: pointer to output
 *              - size_t outlen: number of bytes to be squeezed (written to out)
 *              - uint64_t *s: pointer to input/output Keccak state
 *              - unsigned int pos: number of bytes in current block already squeezed
 *              - unsigned int r: rate in bytes (e.g., 168 for SHAKE128)
 *
 * Returns new position pos in current block
 **************************************************/
  /*  static unsigned int keccak_squeeze(uint8_t *out,
                                       size_t outlen,
                                       uint64_t s[25],
                                       unsigned int pos,
                                       unsigned int r)
    {
        unsigned int i;

        while(outlen) {
            if(pos == r) {
                KeccakF1600_StatePermute(s);
                pos = 0;
            }
            for(i=pos;i < r && i < pos+outlen; i++)
      *out++ = s[i/8] >> 8*(i%8);
            outlen -= i-pos;
            pos = i;
        }

        return pos;
    }


    static void keccak_absorb_once(uint64_t s[25],
                                   unsigned int r,
                               const uint8_t *in,
                                   size_t inlen,
                                   uint8_t p)
    {
        unsigned int i;

        for(i=0;i<25;i++)
            s[i] = 0;

        while(inlen >= r) {
            for(i=0;i<r/8;i++)
                s[i] ^= load64(in+8*i);
            in += r;
            inlen -= r;
            KeccakF1600_StatePermute(s);
        }

        for(i=0;i<inlen;i++)
            s[i/8] ^= (uint64_t)in[i] << 8*(i%8);

        s[i/8] ^= (uint64_t)p << 8*(i%8);
        s[(r-1)/8] ^= 1ULL << 63;
    }

    static void keccak_squeezeblocks(uint8_t *out,
                                     size_t nblocks,
                                     uint64_t s[25],
                                     unsigned int r)
    {
        unsigned int i;

        while(nblocks) {
            KeccakF1600_StatePermute(s);
            for(i=0;i<r/8;i++)
                store64(out+8*i, s[i]);
            out += r;
            nblocks -= 1;
        }
    }*/

}
