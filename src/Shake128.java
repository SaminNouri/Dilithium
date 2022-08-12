import java.util.Arrays;

public class Shake128 {

	static final int SHAKE128_RATE = 168;
	
	void shake128_init(KeccakState state) {
		state.keccakInit();
		state.pos = 0;
	}
	
	void shake128_squeeze(int[] out, int beginIndex, long outlen, KeccakState state) {
		// indtead of returing pos, this method should change state.pos itself
		state.keccak_squeeze(out, beginIndex, outlen, SHAKE128_RATE);
	}
	
	void shake128_absorb_once(KeccakState state, int[] in, long inlen) {
		state.keccak_absorb_once(SHAKE128_RATE, in, inlen, (byte) 0x1F);
		state.pos = SHAKE128_RATE;
	}
	
	void shake128_squeezeblocks(int[] out, int beginIndex, long nblocks, KeccakState state) {
		state.keccak_squeezeblocks(out, beginIndex, nblocks, SHAKE128_RATE);
	}
	void shake128_finalize(KeccakState state) {
		state.keccak_finalize(state.s, state.pos, SHAKE128_RATE, (byte) 0x1F);
		state.pos = SHAKE128_RATE;
	}
	
	void shake128(int[] out, int beginIndex, int outlen, int[] in, int inlen) {
		//long nblocks;
		KeccakState state = new KeccakState();
		state.keccakInit();
		shake128_absorb_once(state, in, inlen);
		long nblocks = outlen / SHAKE128_RATE;
		System.out.println("number of blocks: " +nblocks);
		shake128_squeezeblocks(out, beginIndex, nblocks, state);
		outlen -= nblocks * SHAKE128_RATE;
		beginIndex += nblocks * SHAKE128_RATE;
		shake128_squeeze(out, beginIndex, outlen, state);
	}

	public static void main(String args[])
	{
		int[] buf=new int[168];
		int SEEDBYTES= 2;
		int[] seed ={
				(byte)1, (byte)2
		} ;


		    KeccakState state = new KeccakState();
			Shake128 shake=new Shake128();
			shake.shake128_init(state);
			shake.shake128_absorb_once(state,seed,2);
			shake.shake128_finalize(state);
			System.out.println(0xFF);
			shake.shake128_squeezeblocks(buf,0,1,state);
			System.out.println(Arrays.toString(buf));
			//shake.keccak_finalize(136,0x1F);
			//System.out.println(shake.dptr);
			//System.out.println(Arrays.toString(shake.A));
			//shake.keccak_squeeze(0,buf,2,136);

          /*  for (int i = 0; i < 136; i++) {

                System.out.println(buf[i]);

            }*/



	}
}
