
public class Reduce {

	private static Config config=new Config();


	// Tested
	int montgomery_reduce(long a) {
		long r = (long) (int) (a * (long) config.QINV);
		return (int) ((a - r * (long) config.Q) >> 32);
	}

	// Tested
	int reduce32(int a) {
		int t = (a + (1 << 22)) >> 23;
		t = a - t * config.Q;
		return t;
	}

	// Tested
	int caddq(int a) {
		a += (a >> 31) & config.Q;
		return a;
	}


	int freeze(int a) {
		a = reduce32(a);
		a = caddq(a);
		return a;
	}

}
