import java.util.concurrent.Callable;

public class polyvecl {

 public poly[] vec;

 public polyvecl(){
     Config config=new Config();
     vec=new poly[config.L];

     for (int i=0;i<config.K;i++){
         vec[i]=new poly();
     }


    }




}
