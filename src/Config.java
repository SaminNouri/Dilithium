import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.security.PublicKey;
import java.util.Properties;

public class Config {

    public static void main(String[] args) throws Exception
    {
       Config c=new Config();

    }

    public Config(){
        try {
            setProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setProperties() throws IOException {

        FileReader reader = null;
        reader = new FileReader("src/values.properties");
        Properties p = new Properties();
        p.load(reader);
        int dilithiumMode=Integer.parseInt(p.getProperty("DilithiumMode"));

        isAESUsed=Integer.parseInt(p.getProperty("AESUSE"));

        if(isAESUsed==1){

            STREAM128_BLOCKBYTES=AES256CTR_BLOCKBYTES;
            STREAM256_BLOCKBYTES=AES256CTR_BLOCKBYTES;

        }else {
            STREAM128_BLOCKBYTES=SHAKE128_RATE;
            STREAM256_BLOCKBYTES=SHAKE256_RATE;

        }

        if(dilithiumMode==2){
            return;
        }
        else if(dilithiumMode==3){

            K= Integer.parseInt(p.getProperty("K_3"));
            L= Integer.parseInt(p.getProperty("L_3"));
            ETA=Integer.parseInt(p.getProperty("ETA_3"));
            TAU= Integer.parseInt(p.getProperty("TAU_3"));
            BETA=Integer.parseInt(p.getProperty("BETA_3"));
            GAMMA1=1 << Integer.parseInt(p.getProperty("GAMMA1_3"));
            GAMMA2= ((Q-1)/Integer.parseInt(p.getProperty("GAMMA2_3")));
            OMEGA= Integer.parseInt(p.getProperty("OMEGA_3"));
            POLYVECH_PACKEDBYTES =(OMEGA + K);
            POLYZ_PACKEDBYTES=  640;
            POLYW1_PACKEDBYTES=  128;
            POLYETA_PACKEDBYTES=  128;
            CRYPTO_PUBLICKEYBYTES= (SEEDBYTES + K*POLYT1_PACKEDBYTES);
            CRYPTO_SECRETKEYBYTES= (3*SEEDBYTES + L*POLYETA_PACKEDBYTES + K*POLYETA_PACKEDBYTES + K*POLYT0_PACKEDBYTES);
            CRYPTO_BYTES = (SEEDBYTES + L*POLYZ_PACKEDBYTES + POLYVECH_PACKEDBYTES);

        }else if (dilithiumMode==5){

            K= Integer.parseInt(p.getProperty("K_5"));
            L= Integer.parseInt(p.getProperty("L_5"));
            ETA=Integer.parseInt(p.getProperty("ETA_5"));
            TAU= Integer.parseInt(p.getProperty("TAU_5"));
            BETA=Integer.parseInt(p.getProperty("BETA_5"));
            GAMMA1=1 << Integer.parseInt(p.getProperty("GAMMA1_5"));
            GAMMA2= ((Q-1)/Integer.parseInt(p.getProperty("GAMMA2_5")));
            OMEGA= Integer.parseInt(p.getProperty("OMEGA_5"));
            POLYVECH_PACKEDBYTES =(OMEGA + K);
            POLYZ_PACKEDBYTES=   640;
            POLYW1_PACKEDBYTES=  128;
            POLYETA_PACKEDBYTES=  96;
            CRYPTO_PUBLICKEYBYTES= (SEEDBYTES + K*POLYT1_PACKEDBYTES);
            CRYPTO_SECRETKEYBYTES= (3*SEEDBYTES + L*POLYETA_PACKEDBYTES + K*POLYETA_PACKEDBYTES + K*POLYT0_PACKEDBYTES);
            CRYPTO_BYTES = (SEEDBYTES + L*POLYZ_PACKEDBYTES + POLYVECH_PACKEDBYTES);

        }



    }


    public static int  isAESUsed= 1;
    public static int STREAM256_BLOCKBYTES=64;
    public static int STREAM128_BLOCKBYTES=64;

    public static final int SHAKE128_RATE = 168;
    public static final int SHAKE256_RATE = 136;
    public static final int SHA3_256_RATE = 136;
    public static final int SHA3_512_RATE = 72;


    public static final int  SEEDBYTES= 32;
    public static final int  CRHBYTES= 64;
    public static final int  N= 256;
    public static final int  Q= 8380417;
    public static final int  QINV= 58728449; // q^(-1) mod 2^32
    public static final int  D =13;
    public static final int  ROOT_OF_UNITY= 1753;
    public static final int  MONT= -4186625; // 2^32 % Q

    //for dilithium mode 2
    public static int K= 4;
    public static int L=  4;
    public static int ETA=  2;
    public static int TAU= 39;
    public static int BETA=  78;
    public static int GAMMA1=  (1 << 17);
    public static int  GAMMA2= ((Q-1)/88);
    public static int  OMEGA= 80;


    public static final int  POLYT1_PACKEDBYTES=  320;
    public static final int  POLYT0_PACKEDBYTES=  416;
    //for dilithium mode 2
    public static int  POLYVECH_PACKEDBYTES =(OMEGA + K);

    //for dilithium mode 2
    public static int POLYZ_PACKEDBYTES=   576;
    public static int  POLYW1_PACKEDBYTES=  192;
    public static int  POLYETA_PACKEDBYTES=  96;

    //for dilithium mode 2
    public static int  CRYPTO_PUBLICKEYBYTES= (SEEDBYTES + K*POLYT1_PACKEDBYTES);
    public static int  CRYPTO_SECRETKEYBYTES= (3*SEEDBYTES + L*POLYETA_PACKEDBYTES + K*POLYETA_PACKEDBYTES + K*POLYT0_PACKEDBYTES);
    public static int  CRYPTO_BYTES = (SEEDBYTES + L*POLYZ_PACKEDBYTES + POLYVECH_PACKEDBYTES);


    public static final int AES256CTR_BLOCKBYTES= 64;















}
