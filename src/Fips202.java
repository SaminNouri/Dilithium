
I Love You SaminJoon :)

import java.math.BigInteger;
import java.util.Arrays;
import java.lang.Math;

public class Fips202 {

	static int NROUNDS = 24;
	static BigInteger allOne64 = new BigInteger("18446744073709551615");
	static BigInteger allOne32 = new BigInteger("4294967295");

	static final int SHAKE128_RATE = 168;
	static final int SHAKE256_RATE = 136;
	static final int SHA3_256_RATE = 136;
	static final int SHA3_512_RATE = 72;
//    static BigInteger allOne64 = new BigInteger("18446744073709551615");

	public static void main(String[] args) {

	}

	/*
	 * Based on the public domain implementation in crypto_hash/keccakc512/simple/
	 * from http://bench.cr.yp.to/supercop.html by Ronny Van Keer and the public
	 * domain "TweetFips202" implementation from https://twitter.com/tweetfips202 by
	 * Gilles Van Assche, Daniel J. Bernstein, and Peter Schwabe
	 */

	/*
	 * #include <stddef.h> #include <stdint.h> #include "fips202.h"
	 *
	 *
	 *
	 * #define NROUNDS 24 #define ROL(a, offset) ((a << offset) ^ (a >>
	 * (64-offset)))
	 */

	static BigInteger ROL(BigInteger a, int offset) {
		BigInteger temp1 = a.shiftLeft(offset);
		temp1 = temp1.and(allOne64);
		BigInteger temp2 = a.shiftRight(64 - offset);
		return temp1.or(temp2);
	}

	/*************************************************
	 * Name: load64
	 *
	 * Description: Load 8 bytes into uint64_t in little-endian order
	 *
	 * Arguments: - const byte *x: pointer to input byte array
	 *
	 * Returns the loaded 64-bit unsigned integer
	 **************************************************/
	// tested!
	static BigInteger load64(int[] x, int beginIndex) {
		BigInteger r = new BigInteger("0");
		BigInteger xx;
		for (int i = 0; i < 8; i++) {
			xx = new BigInteger(String.valueOf(x[beginIndex + i]));
			xx = xx.shiftLeft(8 * i);
			xx = xx.and(allOne64);
			r = r.or(xx);
		}
		return r;
	}

	//

	/***********
	 * ************************************** Name: store64
	 *
	 * Description: Store a 64-bit integer to array of 8 bytes in little-endian
	 * order
	 *
	 * Arguments: - byte *x: pointer to the output byte array (allocated) - uint64_t
	 * u: input 64-bit unsigned integer
	 **************************************************/

	// Tested!
	static void store64(int[] x, int beginIndex, BigInteger u) {
		for (int i = 0; i < 8; i++) {
			BigInteger temp = u.shiftRight(8 * i);
			temp = temp.and(new BigInteger("255"));
			x[beginIndex + i] = temp.intValue();
		}
	}

	private static final String[] KeccakF_RoundConstantsLong = { "0000000000000001", "0000000000008082",
			"800000000000808A", "8000000080008000", "000000000000808B", "0000000080000001", "8000000080008081",
			"8000000000008009", "000000000000008A", "0000000000000088", "0000000080008009", "000000008000000A",
			"000000008000808B", "800000000000008B", "8000000000008089", "8000000000008003", "8000000000008002",
			"8000000000000080", "000000000000800A", "800000008000000A", "8000000080008081", "8000000000008080",
			"0000000080000001", "8000000080008008" };

	static final BigInteger[] KeccakF_RoundConstants = { new BigInteger(KeccakF_RoundConstantsLong[0], 16),
			new BigInteger(KeccakF_RoundConstantsLong[1], 16), new BigInteger(KeccakF_RoundConstantsLong[2], 16),
			new BigInteger(KeccakF_RoundConstantsLong[3], 16), new BigInteger(KeccakF_RoundConstantsLong[4], 16),
			new BigInteger(KeccakF_RoundConstantsLong[5], 16), new BigInteger(KeccakF_RoundConstantsLong[6], 16),
			new BigInteger(KeccakF_RoundConstantsLong[7], 16), new BigInteger(KeccakF_RoundConstantsLong[8], 16),
			new BigInteger(KeccakF_RoundConstantsLong[9], 16), new BigInteger(KeccakF_RoundConstantsLong[10], 16),
			new BigInteger(KeccakF_RoundConstantsLong[11], 16), new BigInteger(KeccakF_RoundConstantsLong[12], 16),
			new BigInteger(KeccakF_RoundConstantsLong[13], 16), new BigInteger(KeccakF_RoundConstantsLong[14], 16),
			new BigInteger(KeccakF_RoundConstantsLong[15], 16), new BigInteger(KeccakF_RoundConstantsLong[16], 16),
			new BigInteger(KeccakF_RoundConstantsLong[17], 16), new BigInteger(KeccakF_RoundConstantsLong[18], 16),
			new BigInteger(KeccakF_RoundConstantsLong[19], 16), new BigInteger(KeccakF_RoundConstantsLong[20], 16),
			new BigInteger(KeccakF_RoundConstantsLong[21], 16), new BigInteger(KeccakF_RoundConstantsLong[22], 16),
			new BigInteger(KeccakF_RoundConstantsLong[23], 16) };

	/*
	 * private static final BigInteger[] KeccakF_RoundConstants = { new
	 * BigInteger("0x0000000000000001L"), new BigInteger("0x0000000000008082L"), new
	 * BigInteger("0x800000000000808AL"), new BigInteger("0x8000000080008000L"), new
	 * BigInteger("0x000000000000808BL"), new BigInteger("0x0000000080000001L"), new
	 * BigInteger("0x8000000080008081L"), new BigInteger("0x8000000000008009L"), new
	 * BigInteger("0x000000000000008AL"), new BigInteger("0x0000000000000088L"), new
	 * BigInteger("0x0000000080008009L"), new BigInteger("0x000000008000000AL"), new
	 * BigInteger("0x000000008000808BL"), new BigInteger("0x800000000000008BL"), new
	 * BigInteger("0x8000000000008089L"), new BigInteger("0x8000000000008003L"), new
	 * BigInteger("0x8000000000008002L"), new BigInteger("0x8000000000000080L"), new
	 * BigInteger("0x000000000000800AL"), new BigInteger("0x800000008000000AL"), new
	 * BigInteger("0x8000000080008081L"), new BigInteger("0x8000000000008080L"), new
	 * BigInteger("0x0000000080000001L"), new BigInteger("0x8000000080008008L") };
	 *
	 */

	/*************************************************
	 * Name: KeccakF1600_StatePermute
	 *
	 * Description: The Keccak F1600 Permutation
	 *
	 * Arguments: - uint64_t *state: pointer to input/output Keccak state
	 **************************************************/
	static void KeccakF1600_StatePermute(BigInteger[] state) {
		int round;

		BigInteger Aba, Abe, Abi, Abo, Abu;
		BigInteger Aga, Age, Agi, Ago, Agu;
		BigInteger Aka, Ake, Aki, Ako, Aku;
		BigInteger Ama, Ame, Ami, Amo, Amu;
		BigInteger Asa, Ase, Asi, Aso, Asu;
		BigInteger BCa, BCe, BCi, BCo, BCu;
		BigInteger Da, De, Di, Do, Du;
		BigInteger Eba, Ebe, Ebi, Ebo, Ebu;
		BigInteger Ega, Ege, Egi, Ego, Egu;
		BigInteger Eka, Eke, Eki, Eko, Eku;
		BigInteger Ema, Eme, Emi, Emo, Emu;
		BigInteger Esa, Ese, Esi, Eso, Esu;

		// copyFromState(A, state)
		Aba = state[0];
		Abe = state[1];
		Abi = state[2];
		Abo = state[3];
		Abu = state[4];
		Aga = state[5];
		Age = state[6];
		Agi = state[7];
		Ago = state[8];
		Agu = state[9];
		Aka = state[10];
		Ake = state[11];
		Aki = state[12];
		Ako = state[13];
		Aku = state[14];
		Ama = state[15];
		Ame = state[16];
		Ami = state[17];
		Amo = state[18];
		Amu = state[19];
		Asa = state[20];
		Ase = state[21];
		Asi = state[22];
		Aso = state[23];
		Asu = state[24];

		for (round = 0; round < NROUNDS; round += 2) {
			// prepareTheta
			BCa = Aba.xor(Aga.xor(Aka.xor(Ama.xor(Asa))));
			BCe = Abe.xor(Age.xor(Ake.xor(Ame.xor(Ase))));
			BCi = Abi.xor(Agi.xor(Aki.xor(Ami.xor(Asi))));
			BCo = Abo.xor(Ago.xor(Ako.xor(Amo.xor(Aso))));
			BCu = Abu.xor(Agu.xor(Aku.xor(Amu.xor(Asu))));

			// thetaRhoPiChiIotaPrepareTheta(round, A, E)
			Da = BCu.xor(ROL(BCe, 1));
			De = BCa.xor(ROL(BCi, 1));
			Di = BCe.xor(ROL(BCo, 1));
			Do = BCi.xor(ROL(BCu, 1));
			Du = BCo.xor(ROL(BCa, 1));

			Aba = Aba.xor(Da);
			BCa = Aba;
			Age = Age.xor(De);
			BCe = ROL(Age, 44);
			Aki = Aki.xor(Di);
			BCi = ROL(Aki, 43);
			Amo = Amo.xor(Do);
			BCo = ROL(Amo, 21);
			Asu = Asu.xor(Du);
			BCu = ROL(Asu, 14);

			Eba = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Eba = Eba.xor(KeccakF_RoundConstants[round]);
			Ebe = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Ebi = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Ebo = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Ebu = BCu.xor(allOne64.subtract(BCa).and(BCe));

			Abo = Abo.xor(Do);
			BCa = ROL(Abo, 28);
			Agu = Agu.xor(Du);
			BCe = ROL(Agu, 20);
			Aka = Aka.xor(Da);
			BCi = ROL(Aka, 3);
			Ame = Ame.xor(De);
			BCo = ROL(Ame, 45);
			Asi = Asi.xor(Di);
			BCu = ROL(Asi, 61);

			Ega = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Ege = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Egi = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Ego = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Egu = BCu.xor(allOne64.subtract(BCa).and(BCe));

			Abe = Abe.xor(De);
			BCa = ROL(Abe, 1);
			Agi = Agi.xor(Di);
			BCe = ROL(Agi, 6);
			Ako = Ako.xor(Do);
			BCi = ROL(Ako, 25);
			Amu = Amu.xor(Du);
			BCo = ROL(Amu, 8);
			Asa = Asa.xor(Da);
			BCu = ROL(Asa, 18);

			Eka = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Eke = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Eki = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Eko = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Eku = BCu.xor(allOne64.subtract(BCa).and(BCe));

			Abu = Abu.xor(Du);
			BCa = ROL(Abu, 27);
			Aga = Aga.xor(Da);
			BCe = ROL(Aga, 36);
			Ake = Ake.xor(De);
			BCi = ROL(Ake, 10);
			Ami = Ami.xor(Di);
			BCo = ROL(Ami, 15);
			Aso = Aso.xor(Do);
			BCu = ROL(Aso, 56);

			Ema = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Eme = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Emi = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Emo = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Emu = BCu.xor(allOne64.subtract(BCa).and(BCe));

			Abi = Abi.xor(Di);
			BCa = ROL(Abi, 62);
			Ago = Ago.xor(Do);
			BCe = ROL(Ago, 55);
			Aku = Aku.xor(Du);
			BCi = ROL(Aku, 39);
			Ama = Ama.xor(Da);
			BCo = ROL(Ama, 41);
			Ase = Ase.xor(De);
			BCu = ROL(Ase, 2);

			Esa = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Ese = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Esi = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Eso = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Esu = BCu.xor(allOne64.subtract(BCa).and(BCe));

			// prepareTheta

			BCa = Eba.xor(Ega.xor(Eka.xor(Ema.xor(Esa))));
			BCe = Ebe.xor(Ege.xor(Eke.xor(Eme.xor(Ese))));
			BCi = Ebi.xor(Egi.xor(Eki.xor(Emi.xor(Esi))));
			BCo = Ebo.xor(Ego.xor(Eko.xor(Emo.xor(Eso))));
			BCu = Ebu.xor(Egu.xor(Eku.xor(Emu.xor(Esu))));

			// thetaRhoPiChiIotaPrepareTheta(round+1, E, A)

			Da = BCu.xor(ROL(BCe, 1));
			De = BCa.xor(ROL(BCi, 1));
			Di = BCe.xor(ROL(BCo, 1));
			Do = BCi.xor(ROL(BCu, 1));
			Du = BCo.xor(ROL(BCa, 1));

			Eba = Eba.xor(Da);
			BCa = Eba;
			Ege = Ege.xor(De);
			BCe = ROL(Ege, 44);
			Eki = Eki.xor(Di);
			BCi = ROL(Eki, 43);
			Emo = Emo.xor(Do);
			BCo = ROL(Emo, 21);
			Esu = Esu.xor(Du);
			BCu = ROL(Esu, 14);

			Aba = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Aba = Aba.xor(KeccakF_RoundConstants[round + 1]);
			Abe = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Abi = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Abo = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Abu = BCu.xor(allOne64.subtract(BCa).and(BCe));

			Ebo = Ebo.xor(Do);
			BCa = ROL(Ebo, 28);
			Egu = Egu.xor(Du);
			BCe = ROL(Egu, 20);
			Eka = Eka.xor(Da);
			BCi = ROL(Eka, 3);
			Eme = Eme.xor(De);
			BCo = ROL(Eme, 45);
			Esi = Esi.xor(Di);
			BCu = ROL(Esi, 61);

			Aga = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Age = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Agi = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Ago = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Agu = BCu.xor(allOne64.subtract(BCa).and(BCe));

			Ebe = Ebe.xor(De);
			BCa = ROL(Ebe, 1);
			Egi = Egi.xor(Di);
			BCe = ROL(Egi, 6);
			Eko = Eko.xor(Do);
			BCi = ROL(Eko, 25);
			Emu = Emu.xor(Du);
			BCo = ROL(Emu, 8);
			Esa = Esa.xor(Da);
			BCu = ROL(Esa, 18);

			Aka = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Ake = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Aki = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Ako = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Aku = BCu.xor(allOne64.subtract(BCa).and(BCe));

			Ebu = Ebu.xor(Du);
			BCa = ROL(Ebu, 27);
			Ega = Ega.xor(Da);
			BCe = ROL(Ega, 36);
			Eke = Eke.xor(De);
			BCi = ROL(Eke, 10);
			Emi = Emi.xor(Di);
			BCo = ROL(Emi, 15);
			Eso = Eso.xor(Do);
			BCu = ROL(Eso, 56);

			Ama = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Ame = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Ami = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Amo = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Amu = BCu.xor(allOne64.subtract(BCa).and(BCe));

			Ebi = Ebi.xor(Di);
			BCa = ROL(Ebi, 62);
			Ego = Ego.xor(Do);
			BCe = ROL(Ego, 55);
			Eku = Eku.xor(Du);
			BCi = ROL(Eku, 39);
			Ema = Ema.xor(Da);
			BCo = ROL(Ema, 41);
			Ese = Ese.xor(De);
			BCu = ROL(Ese, 2);

			Asa = BCa.xor(allOne64.subtract(BCe).and(BCi));
			Ase = BCe.xor(allOne64.subtract(BCi).and(BCo));
			Asi = BCi.xor(allOne64.subtract(BCo).and(BCu));
			Aso = BCo.xor(allOne64.subtract(BCu).and(BCa));
			Asu = BCu.xor(allOne64.subtract(BCa).and(BCe));

		}

		// copyToState(state, A)
		state[0] = Aba;
		state[1] = Abe;
		state[2] = Abi;
		state[3] = Abo;
		state[4] = Abu;
		state[5] = Aga;
		state[6] = Age;
		state[7] = Agi;
		state[8] = Ago;
		state[9] = Agu;
		state[10] = Aka;
		state[11] = Ake;
		state[12] = Aki;
		state[13] = Ako;
		state[14] = Aku;
		state[15] = Ama;
		state[16] = Ame;
		state[17] = Ami;
		state[18] = Amo;
		state[19] = Amu;
		state[20] = Asa;
		state[21] = Ase;
		state[22] = Asi;
		state[23] = Aso;
		state[24] = Asu;
	}

	/*************************************************
	 * Name: keccak_absorb
	 *
	 * Description: Absorb step of Keccak; incremental.
	 *
	 * Arguments: - uint64_t *s: pointer to Keccak state - unsigned int pos:
	 * position in current block to be absorbed - unsigned int r: rate in bytes
	 * (e.g., 168 for SHAKE128) - const byte *in: pointer to input to be absorbed
	 * into s - long inlen: length of input in bytes
	 *
	 * Returns new position pos in current block
	 **************************************************/

	/*************************************************
	 * Name: shake128_init
	 *
	 * Description: Initilizes Keccak state for use as SHAKE128 XOF
	 *
	 * Arguments: - keccak_state *state: pointer to (uninitialized) Keccak state
	 **************************************************/
	void shake128_init(KeccakState state) {
		state.keccakInit();
		state.pos = 0;
	}

	/*************************************************
	 * Name: shake128_absorb
	 *
	 * Description: Absorb step of the SHAKE128 XOF; incremental.
	 *
	 * Arguments: - keccak_state *state: pointer to (initialized) output Keccak
	 * state - const byte *in: pointer to input to be absorbed into s - long inlen:
	 * length of input in bytes
	 **************************************************/
	void shake128_absorb(KeccakState state, int[] in, long inlen) {
		state.pos = state.keccak_absorb(state.s, state.pos, SHAKE128_RATE, 0, in, inlen);
	}

	/*************************************************
	 * Name: shake128_finalize
	 *
	 * Description: Finalize absorb step of the SHAKE128 XOF.
	 *
	 * Arguments: - keccak_state *state: pointer to Keccak state
	 **************************************************/
	static void shake128_finalize(KeccakState state) {
		state.keccak_finalize(state.s, state.pos, SHAKE128_RATE, (byte) 0x1F);
		state.pos = SHAKE128_RATE;
	}

	/*************************************************
	 * Name: shake128_squeeze
	 *
	 * Description: Squeeze step of SHAKE128 XOF. Squeezes arbitraily many bytes.
	 * Can be called multiple times to keep squeezing.
	 *
	 * Arguments: - byte *out: pointer to output blocks - long outlen : number of
	 * bytes to be squeezed (written to output) - keccak_state *s: pointer to
	 * input/output Keccak state
	 **************************************************/
	static void shake128_squeeze(int[] out, int beginIndex, long outlen, KeccakState state) {
		// indtead of returing pos, this method should change state.pos itself
		state.keccak_squeeze(out, beginIndex, outlen, SHAKE128_RATE);
	}

	/*************************************************
	 * Name: shake128_absorb_once
	 *
	 * Description: Initialize, absorb into and finalize SHAKE128 XOF;
	 * non-incremental.
	 *
	 * Arguments: - keccak_state *state: pointer to (uninitialized) output Keccak
	 * state - const byte *in: pointer to input to be absorbed into s - long inlen:
	 * length of input in bytes
	 **************************************************/
	static void shake128_absorb_once(KeccakState state, int[] in, long inlen) {
		state.keccak_absorb_once(SHAKE128_RATE, in, inlen, (byte) 0x1F);
		state.pos = SHAKE128_RATE;
	}

	/*************************************************
	 * Name: shake128_squeezeblocks
	 *
	 * Description: Squeeze step of SHAKE128 XOF. Squeezes full blocks of
	 * SHAKE128_RATE bytes each. Can be called multiple times to keep squeezing.
	 * Assumes new block has not yet been started (state->pos = SHAKE128_RATE).
	 *
	 * Arguments: - byte *out: pointer to output blocks - long nblocks: number of
	 * blocks to be squeezed (written to output) - keccak_state *s: pointer to
	 * input/output Keccak state
	 **************************************************/
	static void shake128_squeezeblocks(int[] out, int beginIndex, long nblocks, KeccakState state) {
		state.keccak_squeezeblocks(out, beginIndex, nblocks, SHAKE128_RATE);
	}

	/*************************************************
	 * Name: shake256_init
	 *
	 * Description: Initilizes Keccak state for use as SHAKE256 XOF
	 *
	 * Arguments: - keccak_state *state: pointer to (uninitialized) Keccak state
	 **************************************************/
	static void shake256_init(KeccakState state) {
		state.keccakInit();
		state.pos = 0;
	}

	/*************************************************
	 * Name: shake256_absorb
	 *
	 * Description: Absorb step of the SHAKE256 XOF; incremental.
	 *
	 * Arguments: - keccak_state *state: pointer to (initialized) output Keccak
	 * state - const byte *in: pointer to input to be absorbed into s - long inlen:
	 * length of input in bytes
	 **************************************************/
	static void shake256_absorb(KeccakState state,int inIndex, int[] in, long inlen) {
		state.pos = state.keccak_absorb(state.s, state.pos, SHAKE256_RATE, inIndex, in, inlen);
	}

	/*************************************************
	 * Name: shake256_finalize
	 *
	 * Description: Finalize absorb step of the SHAKE256 XOF.
	 *
	 * Arguments: - keccak_state *state: pointer to Keccak state
	 **************************************************/
	static void shake256_finalize(KeccakState state) {
		state.keccak_finalize(state.s, state.pos, SHAKE256_RATE, (byte) 0x1F);
		state.pos = SHAKE256_RATE;
	}

	/*************************************************
	 * Name: shake256_squeeze
	 *
	 * Description: Squeeze step of SHAKE256 XOF. Squeezes arbitraily many bytes.
	 * Can be called multiple times to keep squeezing.
	 *
	 * Arguments: - byte *out: pointer to output blocks - long outlen : number of
	 * bytes to be squeezed (written to output) - keccak_state *s: pointer to
	 * input/output Keccak state
	 **************************************************/
	void shake256_squeeze(int[] out, int outindex, long outlen, KeccakState state) {
		state.pos = state.keccak_squeeze(out, outindex, outlen, SHAKE256_RATE);
	}

	/*************************************************
	 * Name: shake256_absorb_once
	 *
	 * Description: Initialize, absorb into and finalize SHAKE256 XOF;
	 * non-incremental.
	 *
	 * Arguments: - keccak_state *state: pointer to (uninitialized) output Keccak
	 * state - const byte *in: pointer to input to be absorbed into s - long inlen:
	 * length of input in bytes
	 **************************************************/
	void shake256_absorb_once(KeccakState state, int[] in, long inlen) {
		state.keccak_absorb_once(SHAKE256_RATE, in, inlen, (byte) 0x1F);
		state.pos = SHAKE256_RATE;
	}

	/*************************************************
	 * Name: shake256_squeezeblocks
	 *
	 * Description: Squeeze step of SHAKE256 XOF. Squeezes full blocks of
	 * SHAKE256_RATE bytes each. Can be called multiple times to keep squeezing.
	 * Assumes next block has not yet been started (state->pos = SHAKE256_RATE).
	 *
	 * Arguments: - byte *out: pointer to output blocks - long nblocks: number of
	 * blocks to be squeezed (written to output) - keccak_state *s: pointer to
	 * input/output Keccak state
	 **************************************************/
	static void shake256_squeezeblocks(int[] out, int outindex, long nblocks, KeccakState state) {
		state.keccak_squeezeblocks(out, outindex, nblocks, SHAKE256_RATE);
	}

	/*************************************************
	 * Name: shake128
	 *
	 * Description: SHAKE128 XOF with non-incremental API
	 *
	 * Arguments: - byte *out: pointer to output - long outlen: requested output
	 * length in bytes - const byte *in: pointer to input - long inlen: length of
	 * input in bytes
	 **************************************************/
	void shake128(int[] out, int beginIndex, long outlen, int[] in, long inlen) {
		long nblocks;
		KeccakState state = new KeccakState();
		shake128_absorb_once(state, in, inlen);
		nblocks = outlen / SHAKE128_RATE;
		shake128_squeezeblocks(out, beginIndex, nblocks, state);
		outlen -= nblocks * SHAKE128_RATE;
		beginIndex += nblocks * SHAKE128_RATE;
		shake128_squeeze(out, beginIndex, outlen, state);
	}

	/*************************************************
	 * Name: shake256
	 *
	 * Description: SHAKE256 XOF with non-incremental API
	 *
	 * Arguments: - byte *out: pointer to output - long outlen: requested output
	 * length in bytes - const byte *in: pointer to input - long inlen: length of
	 * input in bytes
	 **************************************************/
	void shake256(int[] out, long outlen, int[] in, long inlen) {
		long nblocks;
		int out_Index = 0;
		KeccakState state = new KeccakState();
		shake256_absorb_once(state, in, inlen);
		nblocks = outlen / SHAKE256_RATE;
		shake256_squeezeblocks(out, out_Index, nblocks, state);
		outlen -= nblocks * SHAKE256_RATE;
		out_Index += nblocks * SHAKE256_RATE;
		shake256_squeeze(out, out_Index, outlen, state);
	}

	/*************************************************
	 * Name: sha3_256
	 *
	 * Description: SHA3-256 with non-incremental API
	 *
	 * Arguments: - byte *h: pointer to output (32 bytes) - const byte *in: pointer
	 * to input - long inlen: length of input in bytes
	 **************************************************/
	void sha3_256(int[] h, int[] in, long inlen) {
		KeccakState state = new KeccakState();
		state.keccakInit();
		state.keccak_absorb_once(SHA3_256_RATE, in, inlen, (byte) 0x06);
		KeccakF1600_StatePermute(state.s);
		int hIndex = 0;
		for (int i = 0; i < 4; i++)
			store64(h, hIndex + 8 * i, state.s[i]);
	}

	/*************************************************
	 * Name: sha3_512
	 *
	 * Description: SHA3-512 with non-incremental API
	 *
	 * Arguments: - byte *h: pointer to output (64 bytes) - const byte *in: pointer
	 * to input - long inlen: length of input in bytes
	 **************************************************/
	void sha3_512(int[] h, int[] in, long inlen) {
		KeccakState state = new KeccakState();
		state.keccakInit();
		state.keccak_absorb_once(SHA3_512_RATE, in, inlen, (byte) 0x06);
		KeccakF1600_StatePermute(state.s);
		int hIndex = 0;
		for (int i = 0; i < 8; i++)
			store64(h, hIndex + 8 * i, state.s[i]);
	}

}

//TODO what if we pass state.pos to a function and it changes the value of pos
// TODO does it change in C? when we pass state->pos to the function
// TODO we should always pass index and the reference to
// the array 'in' and 'out' functions
