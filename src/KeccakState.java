import java.math.BigInteger;
import java.util.Arrays;

public class KeccakState {

	int pos;
	BigInteger[] s;

	/*************************************************
	 * Name: keccak_init
	 *
	 * Description: Initializes the Keccak state.
	 *
	 * Arguments: - uint64_t *s: pointer to Keccak state
	 **************************************************/
	void keccakInit() {
		s = new BigInteger[25];
		for (int i = 0; i < 25; i++)
			s[i] = new BigInteger("0");
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
	static int keccak_absorb(BigInteger[] s,
							 int pos,
							 int r,
							 int in_Index,
							 int[] in,
							 long inlen)
	{
		int i;

		while(pos+inlen >= r) {
			for(i=pos;i<r;i++) {
				BigInteger temp = new BigInteger(Integer.toString(in[in_Index++]));
				temp = temp.and(Fips202.allOne64);
				temp = temp.shiftLeft(8 * (i % 8));
				temp = temp.and(Fips202.allOne64);
				s[i / 8] = s[i / 8].xor(temp);
//	    	s[i/8] ^= (uint64_t) * in++ << 8*(i%8);
			}


			inlen -= r-pos;
			Fips202.KeccakF1600_StatePermute(s);
			pos = 0;
		}

		for(i=pos;i<pos+inlen;i++) {
			BigInteger temp = new BigInteger(Integer.toString(in[in_Index++]));
			temp = temp.and(Fips202.allOne64);
			temp = temp.shiftLeft(8 * (i % 8));
			temp = temp.and(Fips202.allOne64);
			s[i / 8] = s[i / 8].xor(temp);

//		  s[i/8] ^= (uint64_t)*in++ << 8*(i%8);
		}



		return i;
	}


	/*************************************************
	 * Name: keccak_finalize
	 *
	 * Description: Finalize absorb step.
	 *
	 * Arguments: - uint64_t *s: pointer to Keccak state - unsigned int pos:
	 * position in current block to be absorbed - unsigned int r: rate in bytes
	 * (e.g., 168 for SHAKE128) - byte p: domain separation byte
	 **************************************************/

	// doesn't create a valid BigInteger
	static void keccak_finalize(BigInteger[] s, int pos, int r, int p) {
		BigInteger temp = new BigInteger(Integer.toString(p));
		temp = temp.shiftLeft(8 * (pos % 8));
		temp = temp.and(Fips202.allOne64);
		s[(pos / 8)] = s[(pos / 8)].xor(temp);
		temp = new BigInteger("1");
		temp = temp.shiftLeft(63);
		temp = temp.and(Fips202.allOne64);
		s[(r / 8) - 1] = s[(r / 8) - 1].xor(temp);
	}

	/*************************************************
	 * Name: keccak_squeeze
	 *
	 * Description: Squeeze step of Keccak. Squeezes arbitratrily many bytes.
	 * Modifies the state. Can be called multiple times to keep squeezing, i.e., is
	 * incremental.
	 *
	 * Arguments: - byte *out: pointer to output - long outlen: number of bytes to
	 * be squeezed (written to out) - uint64_t *s: pointer to input/output Keccak
	 * state - unsigned int pos: number of bytes in current block already squeezed -
	 * unsigned int r: rate in bytes (e.g., 168 for SHAKE128)
	 *
	 * Returns new position pos in current block
	 *************************************************
	 * @return*/


	// TODO
	int keccak_squeeze(int[] out, int beginIndex, long outlen, int r) {
		int i;
		while (outlen > 0) {
			if (pos == r) {
				Fips202.KeccakF1600_StatePermute(s);
				pos = 0;
			}
			for (i = pos; i < r && i < pos + outlen; i++) {
				BigInteger temp = s[i / 8].shiftRight(8 * (i % 8));
				temp = temp.and(new BigInteger("255"));
				out[beginIndex++] = temp.intValue();
			}
			outlen -= (i - pos);
			pos = i;
		}
		return pos;
	}

	/*************************************************
	 * Name: keccak_absorb_once
	 *
	 * Description: Absorb step of Keccak; non-incremental, starts by zeroeing the
	 * state.
	 *
	 * Arguments: - uint64_t *s: pointer to (uninitialized) output Keccak state -
	 * unsigned int r: rate in bytes (e.g., 168 for SHAKE128) - const byte *in:
	 * pointer to input to be absorbed into s - long inlen: length of input in bytes
	 * - byte p: domain-separation byte for different Keccak-derived functions
	 **************************************************/
	void keccak_absorb_once(int r,int inIndex, int[] in, long inlen, int p) {
		int i;
		for (i = 0; i < 25; i++)
			s[i] = new BigInteger("0");
		while (inlen >= r) {
			for (i = 0; i < r / 8; i++)
				s[i] = s[i].xor(Fips202.load64(in, inIndex + 8 * i));
			inIndex += r;
			inlen -= r;
			Fips202.KeccakF1600_StatePermute(s);
		}

		for (i = 0; i < inlen; i++) {
			BigInteger temp = new BigInteger(Integer.toString(in[i]));
			temp = temp.shiftLeft(8 * (i % 8));
			temp = temp.and(Fips202.allOne64);
			s[i / 8] = s[i / 8].xor(temp);
		}
		BigInteger temp = new BigInteger(Integer.toString(p));
		temp = temp.shiftLeft(8 * (i % 8));
		temp = temp.and(Fips202.allOne64);
		s[i / 8] = s[i / 8].xor(temp);
		temp = new BigInteger("1");
		temp = temp.shiftLeft(63);
		temp = temp.and(Fips202.allOne64);
		s[(r - 1) / 8] = s[(r - 1) / 8].xor(temp);

	}

	/*************************************************
	 * Name: keccak_squeezeblocks
	 *
	 * Description: Squeeze step of Keccak. Squeezes full blocks of r bytes each.
	 * Modifies the state. Can be called multiple times to keep squeezing, i.e., is
	 * incremental. Assumes zero bytes of current block have already been squeezed.
	 *
	 * Arguments: - byte *out: pointer to output blocks - long nblocks: number of
	 * blocks to be squeezed (written to out) - uint64_t *s: pointer to input/output
	 * Keccak state - unsigned int r: rate in bytes (e.g., 168 for SHAKE128)
	 **************************************************/
	void keccak_squeezeblocks(int[] out, int beginIndex, long nblocks, long r) {
		while (nblocks > 0) {
			Fips202.KeccakF1600_StatePermute(s);
			for (int i = 0; i < r / 8; i++)
				Fips202.store64(out, beginIndex + 8 * i, s[i]);
			beginIndex += r;
			nblocks -= 1;
		}
	}

}
