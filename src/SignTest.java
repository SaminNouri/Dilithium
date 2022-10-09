import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class SignTest {

    private static Config config=new Config();





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
            String cfile = "sign.c";
            CCompiler.compile(path, cfile);
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
            if (!test4(sc))
                return false;

            return true;
        }






        private static boolean test( Scanner sc) throws FileNotFoundException, DilithiumModeException {


            int[] sk=new int[config.CRYPTO_SECRETKEYBYTES];
            int[] pk=new int[config.CRYPTO_PUBLICKEYBYTES];



            sc.nextLine(); // getting SEEDBYTES
            int SEEDBYTES = sc.nextInt(); // SEEDBYTES
            int[] sb=new int[SEEDBYTES];
            for (int i = 0; i < SEEDBYTES; i++)
                sb[i] = sc.nextInt();
            sc.nextLine();

            System.out.println(sc.nextLine()); // getting SEEDBYTES
            int val=sc.nextInt();
            System.out.println(val);
            int[] mew=new int[val];
            for (int i = 0; i < val; i++)
                mew[i] = sc.nextInt();
            sc.nextLine();

            System.out.println(Arrays.toString(mew));





            Sign.crypto_sign_keypair(0,pk,0,sk);

            boolean flag=false;
            System.out.println(sc.nextLine()); // sk after function:
            int[] sk1=new int[config.CRYPTO_SECRETKEYBYTES];
            for (int i = 0; i < config.CRYPTO_SECRETKEYBYTES; i++) {
                int tempp = sc.nextInt();
                sk1[i] = tempp;
                if (sk[i] != tempp)
                {
                    return false;
                    //return false;
                   /* if(!flag) {
                        System.out.println(i);
                        flag=true;
                    }*/
                }

                    //return false;
            }
            sc.nextLine(); // going to nextline after last int number

            //System.out.println(Arrays.toString(sk));
            //System.out.println(Arrays.toString(sk1));
           // System.out.println(sk.length+" "+sk1.length);


            sc.nextLine(); // pk after function:
            for (int i = 0; i < config.CRYPTO_PUBLICKEYBYTES; i++)
                if (pk[i] != sc.nextInt())
                    return false;
            sc.nextLine(); // going to nextline after last int number


            return true;
        }



    private static boolean test1( Scanner sc) throws FileNotFoundException, DilithiumModeException {


        int[] sk=new int[config.CRYPTO_SECRETKEYBYTES];
        int siglen=3000;
        int[] sig=new int[siglen];




        sc.nextLine(); // getting m
        int mlen = sc.nextInt(); // m
        int[] m=new int[mlen];
        for (int i = 0; i < mlen; i++)
            m[i] = sc.nextInt();
        sc.nextLine();

        System.out.println(sc.nextLine()); // getting sk
        int val=sc.nextInt();
        System.out.println(val);
        for (int i = 0; i < val; i++)
            sk[i] = sc.nextInt();
        sc.nextLine();





        Sign.crypto_sign(0,sig,new SIZE(siglen),0,m,mlen,0,sk);

      /*  boolean flag=false;
        System.out.println(sc.nextLine()); // sk after function:
        int[] sk1=new int[config.CRYPTO_SECRETKEYBYTES];
        for (int i = 0; i < config.CRYPTO_SECRETKEYBYTES; i++) {
            int tempp = sc.nextInt();
            sk1[i] = tempp;
            if (sk[i] != tempp)
            {
                return false;
                //return false;

            }
        }
        sc.nextLine(); // going to nextline after last int number*/


        sc.nextLine(); // pk after function:
        for (int i = 0; i < siglen; i++)
            if (sig[i] != sc.nextInt())
                return false;
        sc.nextLine(); // going to nextline after last int number


        return true;
    }



    private static boolean test3(Scanner sc) throws DilithiumModeException {


        int[] sk=new int[config.CRYPTO_SECRETKEYBYTES];
        int[] pk=new int[config.CRYPTO_PUBLICKEYBYTES];



        System.out.println(sc.nextLine()); // getting sk
        for (int i = 0; i < config.CRYPTO_SECRETKEYBYTES; i++)
            sk[i] = sc.nextInt();
        sc.nextLine();

        System.out.println(sc.nextLine()); // getting pk
        for (int i = 0; i < config.CRYPTO_PUBLICKEYBYTES; i++)
            pk[i] = sc.nextInt();
        sc.nextLine();

        System.out.println(sc.nextLine()); // getting m
        int mlen=sc.nextInt();
        int[] m=new int[mlen];
        for (int i = 0; i < mlen; i++)
            m[i] = sc.nextInt();
        sc.nextLine();

        System.out.println(sc.nextLine()); // getting sig
        int siglen=sc.nextInt();
        System.out.println("siglen:"+siglen+" "+config.CRYPTO_BYTES);
        int[] sig=new int[siglen];
        for (int i = 0; i < siglen; i++)
            sig[i] = sc.nextInt();
        sc.nextLine();

   /*     sc.nextLine();
        int mmmm=sc.nextInt();
        System.out.println("mmmm:"+mmmm+" "+mlen);
        sc.nextLine();


        System.out.println(sc.nextLine()); // getting sig
        int clen=sc.nextInt();
        sc.nextLine();
        BigInteger[] c=new BigInteger[clen];
        for (int i = 0; i < clen; i++) {
            String s=sc.nextLine();
            //System.out.println(s);
            c[i] = new BigInteger(s);

        }
        //sc.nextLine();*/


        System.out.println(sc.nextLine()); // getting sig
        int clen=sc.nextInt();
        sc.nextLine();
        BigInteger[] c=new BigInteger[clen];
        for (int i = 0; i < clen; i++)
            c[i] =  new BigInteger(sc.nextLine());

        System.out.println("h2:"+Arrays.toString(c));


        int ans=Sign.crypto_sign_open(0,m,new SIZE(10),0,sig,(siglen),0,pk);

        System.out.println(sc.nextLine());





        System.out.println(sc.nextInt());
        sc.nextLine();
        System.out.println(sc.nextLine());

        sc.nextLine();
        int anss=sc.nextInt();
        sc.nextLine();

        System.out.println(ans+" "+anss);


        return ans==anss && ans==0;

    }










    private static boolean test4(Scanner sc) throws DilithiumModeException {


        int[] sk=new int[config.CRYPTO_SECRETKEYBYTES];
        int[] pk=new int[config.CRYPTO_PUBLICKEYBYTES];

        System.out.println("mewww");



        System.out.println(sc.nextLine()); // getting sk
        for (int i = 0; i < config.CRYPTO_SECRETKEYBYTES; i++)
            sk[i] = sc.nextInt();
        sc.nextLine();

        System.out.println(sc.nextLine()); // getting pk
        for (int i = 0; i < config.CRYPTO_PUBLICKEYBYTES; i++)
            pk[i] = sc.nextInt();
        sc.nextLine();

        System.out.println(sc.nextLine()); // getting m
        int mlen=sc.nextInt();
        int[] m=new int[mlen];
        for (int i = 0; i < mlen; i++)
            m[i] = sc.nextInt();
        sc.nextLine();




        System.out.println(sc.nextLine()); // getting sig
        int siglen=sc.nextInt();
        int[] sig=new int[siglen];
        for (int i = 0; i < siglen; i++)
            sig[i] = sc.nextInt();
        sc.nextLine();

        int ans1=sc.nextInt();
        int ans2=sc.nextInt();
        sc.nextLine();

        int ans3=Sign.crypto_sign_open(0,m,new SIZE(10),0,sig,(siglen),0,pk);

        int ans4=Sign.crypto_sign_verify(0,sig, (config.CRYPTO_BYTES),0 + config.CRYPTO_BYTES,sig, mlen,0, pk);

        System.out.println(ans1+" "+ans2+" "+ans3+" "+ans4);


        return ans3==ans2 && ans2==0;





   /*     sc.nextLine();
        int mmmm=sc.nextInt();
        System.out.println("mmmm:"+mmmm+" "+mlen);
        sc.nextLine();


        System.out.println(sc.nextLine()); // getting sig
        int clen=sc.nextInt();
        sc.nextLine();
        BigInteger[] c=new BigInteger[clen];
        for (int i = 0; i < clen; i++) {
            String s=sc.nextLine();
            //System.out.println(s);
            c[i] = new BigInteger(s);

        }
        //sc.nextLine();*/


       /* System.out.println(sc.nextLine()); // getting sig
        int clen=sc.nextInt();
        sc.nextLine();
        BigInteger[] c=new BigInteger[clen];
        for (int i = 0; i < clen; i++)
            c[i] =  new BigInteger(sc.nextLine());

        System.out.println("h2:"+Arrays.toString(c));


        int ans=Sign.crypto_sign_open(0,m,new SIZE(10),0,sig,(siglen),0,pk);

        System.out.println(sc.nextLine());





        System.out.println(sc.nextInt());
        sc.nextLine();
        System.out.println(sc.nextLine());

        sc.nextLine();
        int anss=sc.nextInt();
        sc.nextLine();

        System.out.println(ans+" "+anss);


        return ans==anss && ans==0;*/

    }





}






