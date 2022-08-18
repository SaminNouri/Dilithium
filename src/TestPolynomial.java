import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class TestPolynomial {


    public static void main(String[] args){
        try{

            run();
            parse();


        }catch (Exception e){
            e.printStackTrace();
        }


    }




    static void run() throws FileNotFoundException {
        String path = "ref/";
        String cfile = "poly.c";
        CCompiler.compile(path, cfile);
        //PrintWriter writer = new PrintWriter("poly_test.txt");
       // writer.println(out);
       // writer.flush();
       // writer.close();
    }

    static void parse() throws FileNotFoundException, DilithiumModeException {
        File file = new File("poly_test1.txt");
        Scanner sc = new Scanner(file);
        //sc.nextLine(); // Compilation Successfull!!:
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

    private static boolean testOnce(Scanner sc) throws FileNotFoundException, DilithiumModeException {
        sc.nextLine(); // testing function:
        if (!test_poly_uniform_gamma1(sc))
            return false;

        return true;
    }



    private static boolean test_rej_eta( Scanner sc) throws FileNotFoundException, DilithiumModeException {
        int[] a=new int[100];
        int[] buf=new int[200];
        Polynomial polynomial = new Polynomial();



        sc.nextLine(); // initial a:
        int N = sc.nextInt(); //N
        System.out.println(N);
        for (int i = 0; i < N; i++)
            a[i] = sc.nextInt();
        sc.nextLine();



        sc.nextLine(); // initial buf:
        int M = sc.nextInt(); //m
        for (int i = 0; i < M; i++)
            buf[i] = sc.nextInt();
        sc.nextLine();


        int ans=polynomial.rej_eta(0,a,100,0,buf,200);



        sc.nextLine(); //ans after function:
        int anss=sc.nextInt();
        sc.nextLine();

        if(ans!=anss)
            return false;

        sc.nextLine(); // a after function:
        for (int i = 0; i < N; i++)
            if (a[i] != sc.nextInt())
                return false;
        sc.nextLine(); // going to nextline after last int number
        return true;
    }


    private static boolean test_polyt0_unpack( Scanner sc) throws FileNotFoundException, DilithiumModeException {
        poly a = new poly();
        Polynomial polynomial = new Polynomial();



        sc.nextLine(); // initial a.coeefs:
        int N = sc.nextInt(); //N
        for (int i = 0; i < N; i++)
            a.coeffs[i] = sc.nextInt();
        sc.nextLine();


       sc.nextLine(); // initial seed:
        int M = sc.nextInt(); //m
        int[] seed=new int[M];
        for (int i = 0; i < M; i++)
            seed[i] = sc.nextInt();
        sc.nextLine();





      polynomial.polyw1_pack(0,seed,0,a);



   sc.nextLine(); // a after function:
        for (int i = 0; i < N; i++) {
            int tempp = sc.nextInt();
            if (a.coeffs[i] != tempp)
                return false;
        }
        sc.nextLine();

        //sc.nextLine();





       sc.nextLine(); // seed after function:
        for (int i = 0; i < M; i++)
            if (seed[i] != sc.nextInt())
                return false;
        sc.nextLine();



        //sc.nextLine(); // going to nextline after last int number
        return true;
    }





























    private static boolean test_poly_challenge( Scanner sc) throws FileNotFoundException, DilithiumModeException {
        poly a = new poly();
        Polynomial polynomial = new Polynomial();



        System.out.println(sc.nextLine()); // initial a.coeefs:
        int N = sc.nextInt(); //N
        for (int i = 0; i < N; i++)
            a.coeffs[i] = sc.nextInt();
        sc.nextLine();


        sc.nextLine(); // initial seed:
        int M = sc.nextInt(); //m
        int[] seed=new int[M];
        for (int i = 0; i < M; i++)
            seed[i] = sc.nextInt();
        sc.nextLine();





        polynomial.poly_challenge(0,a,0,seed);




        sc.nextLine(); // a after function:
        for (int i = 0; i < N; i++) {
            int tempp = sc.nextInt();
            if (a.coeffs[i] != tempp)
                return false;
        }
        sc.nextLine();





        sc.nextLine(); // seed after function:
        for (int i = 0; i < M; i++)
            if (seed[i] != sc.nextInt())
                return false;
        sc.nextLine();



        //sc.nextLine(); // going to nextline after last int number
        return true;
    }

















    private static boolean test_poly_uniform_gamma1( Scanner sc) throws FileNotFoundException, DilithiumModeException {
        poly a = new poly();
        Polynomial polynomial = new Polynomial();



        System.out.println(sc.nextLine()); // initial a.coeefs:
        int N = sc.nextInt(); //N
        for (int i = 0; i < N; i++)
            a.coeffs[i] = sc.nextInt();
        sc.nextLine();


        sc.nextLine(); // initial seed:
        int M = sc.nextInt(); //m
        int[] seed=new int[M];
        for (int i = 0; i < M; i++)
            seed[i] = sc.nextInt();
        sc.nextLine();

        sc.nextLine(); // initial nonce:
        int nonce= sc.nextInt();
        sc.nextLine();





        polynomial.poly_uniform_gamma1(0,a,0,seed,nonce);




        System.out.println(sc.nextLine()); // a after function:
        for (int i = 0; i < N; i++) {
            int tempp = sc.nextInt();
            if (a.coeffs[i] != tempp)
               return false;
        }
        sc.nextLine();






    sc.nextLine(); // seed after function:
        for (int i = 0; i < M; i++)
            if (seed[i] != sc.nextInt())
                return false;
        sc.nextLine();



        //sc.nextLine(); // going to nextline after last int number
        return true;
    }




























    private static boolean test_rej_uniform( Scanner sc) throws FileNotFoundException, DilithiumModeException {
        int[] a=new int[100];
        int[] buf=new int[200];
        Polynomial polynomial = new Polynomial();



        sc.nextLine(); // initial a:
        int N = sc.nextInt(); //N
        for (int i = 0; i < N; i++)
            a[i] = sc.nextInt();
        sc.nextLine();



        sc.nextLine(); // initial buf:
        int M = sc.nextInt(); //m
        for (int i = 0; i < M; i++)
            buf[i] = sc.nextInt();
        sc.nextLine();


        int ans=polynomial.rej_uniform(a,0,100,buf,0,200);



        sc.nextLine(); //ans after function:
        int anss=sc.nextInt();
        sc.nextLine();

        if(ans!=anss)
            return false;

        sc.nextLine(); // a after function:
        for (int i = 0; i < N; i++)
            if (a[i] != sc.nextInt())
                return false;
        sc.nextLine(); // going to nextline after last int number
        return true;
    }





























    private static boolean test_poly_chknorm( Scanner sc) throws FileNotFoundException, DilithiumModeException {
        poly a = new poly();
        Polynomial polynomial = new Polynomial();



        sc.nextLine(); // initial a.coeefs:
        int N = sc.nextInt(); //N
        for (int i = 0; i < N; i++)
            a.coeffs[i] = sc.nextInt();
        sc.nextLine();

        sc.nextLine(); // initial B:
        int B= sc.nextInt();
        sc.nextLine();


        int ans=polynomial.poly_chknorm(a,0,B);



        sc.nextLine(); //ans after function:
        if ( ans != sc.nextInt())
                return false;
        sc.nextLine(); // going to nextline after last int number
        return true;
    }






























    private static boolean test3( Scanner sc) throws FileNotFoundException, DilithiumModeException {
        poly a = new poly();
        poly b = new poly();
        poly h = new poly();
        Polynomial polynomial = new Polynomial();



        sc.nextLine(); // initial a.coeefs:
        int N = sc.nextInt();
        for (int i = 0; i < N; i++)
            a.coeffs[i] = sc.nextInt();
        sc.nextLine();


        sc.nextLine(); // initial h.coeefs:
        for (int i = 0; i < N; i++)
            h.coeffs[i] = sc.nextInt();
        sc.nextLine();

        polynomial.poly_use_hint(b, 0,a,0,h,0);



        sc.nextLine(); // b.coeefs after function:
        for (int i = 0; i < N; i++)
            if (b.coeffs[i] != sc.nextInt())
                return false;
        sc.nextLine(); // going to nextline after last int number
        return true;
    }

    private static boolean test22( Scanner sc) throws FileNotFoundException {
        poly a0 = new poly();
        poly a = new poly();
        poly a1 = new poly();
        Polynomial polynomial = new Polynomial();



        sc.nextLine(); // initial a.coeefs:
        int N = sc.nextInt();
        for (int i = 0; i < N; i++)
            a.coeffs[i] = sc.nextInt();
        sc.nextLine(); // going to nextline after last int number


        sc.nextLine(); // initial a0.coeefs:
        for (int i = 0; i < N; i++)
            a0.coeffs[i] = sc.nextInt();
        sc.nextLine(); // going to nextline after last int number

        polynomial.poly_decompose(a1, 0,a0,0,a,0);



        sc.nextLine(); // a0.coeefs after function:
        for (int i = 0; i < N; i++)
            if (a0.coeffs[i] != sc.nextInt())
                return false;
        sc.nextLine(); // going to nextline after last int number


        sc.nextLine(); // a1.coeefs after function:
        for (int i = 0; i < N; i++)
            if (a1.coeffs[i] != sc.nextInt())
                return false;

        sc.nextLine(); // going to nextline after last int number
        return true;
    }



    private static boolean test_poly_make_hint( Scanner sc) throws FileNotFoundException {
        poly a0 = new poly();
        poly a1 = new poly();
        poly h = new poly();
        Polynomial polynomial = new Polynomial();



        sc.nextLine(); // initial a0.coeefs:
        int N = sc.nextInt();
        for (int i = 0; i < N; i++)
            a0.coeffs[i] = sc.nextInt();
        sc.nextLine(); // going to nextline after last int number


        sc.nextLine(); // initial a1.coeefs:
        for (int i = 0; i < N; i++)
            a1.coeffs[i] = sc.nextInt();
        sc.nextLine(); // going to nextline after last int number

        int s=polynomial.poly_make_hint(h,0,a0,0,a1,0);



        sc.nextLine(); // h.coeefs after function:
        for (int i = 0; i < N; i++)
            if (h.coeffs[i] != sc.nextInt())
                return false;
        sc.nextLine(); // going to nextline after last int number


        sc.nextLine(); // s after function:
        if(s != sc.nextInt())
            return false;

        sc.nextLine(); // going to nextline after last int number
        return true;
    }








}
