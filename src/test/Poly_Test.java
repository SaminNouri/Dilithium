import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

import tests.CCompiler;

public class Poly_Test {

	public static void main(String[] args) throws FileNotFoundException {
		String path = "/home/ermiya/eclipse-workspace/Dilithium/ref/";
		String cfile = "poly.c";

		String out = CCompiler.compile(path, cfile);
		PrintWriter writer = new PrintWriter("poly_test.txt");
		writer.println(out);
		writer.flush();
		writer.close();

		File file = new File("poly_test.txt");
		Scanner sc = new Scanner(file);

		sc.nextLine(); // Compilation Successfull!!
		sc.nextLine(); // number of tests:
		int numberOfTests = sc.nextInt();
		sc.nextLine();

		for (int i = 0; i < numberOfTests; i++)
			if (!testOnce(sc)) {
				System.out.println("Test failed!");
				System.exit(0);
			}
		System.out.println("Test passed Successful!");
	}

	private static boolean testOnce(Scanner sc) throws FileNotFoundException {
		poly a = new poly();
		Polynomial plnml = new Polynomial();
		sc.nextLine(); // testing poly_reduce:
		if (!poly_reduce(a, plnml, sc))
			return false;

		return true;
	}

	private static boolean poly_reduce(poly a, Polynomial polynomial, Scanner sc) throws FileNotFoundException {
		sc.nextLine(); // initial a.coeefs:
		int N = sc.nextInt();
		for (int i = 0; i < N; i++)
			a.coeffs[i] = sc.nextInt();
		sc.nextLine();
		sc.nextLine(); // a.coeefs after calling poly_reduce:

		polynomial.poly_reduce(a, 0);

		for (int i = 0; i < N; i++)
			if (a.coeffs[i] != sc.nextInt())
				return false;
		sc.nextLine(); // going to nextline after last int number
		return true;
	}

}
