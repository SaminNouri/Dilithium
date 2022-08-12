

public class Rounding {

	private static Config config=new Config();


	//a0 is a pointer!
	public Pair power2round( int a) {
		int a0;
		int a1;
		a1 =(a + (1 << (config.D - 1)) - 1) >> config.D;
		a0 = a - (a1 << config.D);
		return new Pair(a0, a1);
	}

	public Pair decompose(int a) {

		int a0;
		int a1;
		a1  = (a + 127) >> 7;
	  
	if (config.GAMMA2 == (config.Q-1)/32){
		a1  = (a1*1025 + (1 << 21)) >> 22;
		a1 &= 15;
	}
	else if (config.GAMMA2 == (config.Q-1)/88)
		{
			a1  = (a1*11275 + (1 << 23)) >> 24;
			a1 ^= ((43 - a1) >> 31) & a1;
		}


	  a0  = a - a1*2*config.GAMMA2;
	  a0 -= (((config.Q-1)/2 - a0) >> 31) & config.Q;
	  return new Pair(a0,a1);
	}

	/*************************************************
	* Name:        make_hint
	*
	* Description: Compute hint bit indicating whether the low bits of the
	*              input element overflow into the high bits.
	*
	* Arguments:   - int a0: low bits of input element
	*              - int a1: high bits of input element
	*
	* Returns 1 if overflow.
	**************************************************/
	public int make_hint(int a0, int a1) {
	  if(a0 > config.GAMMA2 || a0 < -config.GAMMA2 || (a0 == -config.GAMMA2 && a1 != 0))
	    return 1;

	  return 0;
	}

	/*************************************************
	* Name:        use_hint
	*
	* Description: Correct high bits according to hint.
	*
	* Arguments:   - int32_t a: input element
	*              - unsigned int hint: hint bit
	*
	* Returns corrected high bits.
	**************************************************/
	public int use_hint(int a, int hint) throws DilithiumModeException {
	  int a0, a1;

	  Pair p = decompose(a);
	  a0=p.x;
	  a1=p.y;
	  if(hint == 0)
	    return a1;

	if (config.GAMMA2 == (config.Q-1)/32)
	{
		if(a0 > 0)
			return (a1 + 1) & 15;
		else
			return (a1 - 1) & 15;
	}
	else if (config.GAMMA2 == (config.Q-1)/88)
	{
		if(a0 > 0)
			return (a1 == 43) ?  0 : a1 + 1;
		else
			return (a1 ==  0) ? 43 : a1 - 1;
	}
	else {
		throw new DilithiumModeException("Dilithium mode is not defined.");
	}

	}
}




