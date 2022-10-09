public class polyveck {


    public poly[] vec;

    public polyveck(){
        Config config=new Config();
        vec=new poly[config.K];

        for (int i=0;i<config.K;i++){
            vec[i]=new poly();
        }


    }
}
