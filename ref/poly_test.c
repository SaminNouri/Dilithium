#include <stdio.h>
#include <stdio.h>
#include <stdlib.h>


void test_poly_reduce() {
    poly a;
    int i;
    printf("initial a.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a.coeffs[i] = rand();
        printf("%d\n", a.coeffs[i]);
    }
    poly_reduce(&a);
    printf("a.coeefs after calling poly_reduce:\n");
    for (i = 0; i < N; i++) {
        printf("%d\n", a.coeffs[i]);
    }
}

void test_poly_caddq() {
    poly a;
    int i;
    printf("initial a.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a.coeffs[i] = rand();
        printf("%d\n", a.coeffs[i]);
    }
    poly_caddq(&a);
    printf("a.coeefs after calling poly_reduce:\n");
    for (i = 0; i < N; i++) {
        printf("%d\n", a.coeffs[i]);
    }
}

void test_poly_shiftl() {
    poly a;
    int i;
    printf("initial a.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a.coeffs[i] = rand();
        printf("%d\n", a.coeffs[i]);
    }
    poly_shiftl(&a);
    printf("a.coeefs after calling poly_reduce:\n");
    for (i = 0; i < N; i++) {
        printf("%d\n", a.coeffs[i]);
    }
}

void test_poly_ntt() {
    poly a;
    int i;
    printf("initial a.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a.coeffs[i] = rand();
        printf("%d\n", a.coeffs[i]);
    }
    poly_ntt(&a);
    printf("a.coeefs after calling poly_reduce:\n");
    for (i = 0; i < N; i++) {
        printf("%d\n", a.coeffs[i]);
    }
}

void test1() {
    poly a;
    int i;
    printf("initial a.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a.coeffs[i] = rand();
        printf("%d\n", a.coeffs[i]);
    }
    poly_invntt_tomont(&a);
    printf("a.coeefs after calling function:\n");
    for (i = 0; i < N; i++) {
        printf("%d\n", a.coeffs[i]);
    }
}

void test3() {
    poly a;
    int i;
    //a
    printf("initial a.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a.coeffs[i] = rand();
        printf("%d\n", a.coeffs[i]);
    }
    poly h;
     //h
        printf("initial h.coeefs:\n");
        for (i = 0; i < N; i++) {
            h.coeffs[i] = rand();
            printf("%d\n", h.coeffs[i]);
        }


     //function
     poly b;
     poly_use_hint(&b,&a,&h);



    printf("b.coeefs after calling function:\n");
    for (i = 0; i < N; i++) {
        printf("%d\n", b.coeffs[i]);
    }

}

void test22() {
    poly a;
    int i;
    //a
    printf("initial a.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a.coeffs[i] = rand();
        printf("%d\n", a.coeffs[i]);
    }

       //a0
       poly a0;
      printf("initial a0.coeefs:\n");
        for (i = 0; i < N; i++) {
            printf("%d\n", a0.coeffs[i]);
        }


     //function
     poly a1;
     poly_decompose(&a1,&a0,&a);



    printf("a0.coeefs after calling function:\n");
    for (i = 0; i < N; i++) {
        printf("%d\n", a0.coeffs[i]);
    }

     printf("a1.coeefs after calling function:\n");
         for (i = 0; i < N; i++) {
             printf("%d\n", a1.coeffs[i]);
         }

}

void test_poly_make_hint() {
    poly a0;
    int i;
    //a0
    printf("initial a0.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a0.coeffs[i] = rand();
        printf("%d\n", a0.coeffs[i]);
    }

       //a0
       poly a1;
      printf("initial a1.coeefs:\n");
        for (i = 0; i < N; i++) {
            printf("%d\n", a1.coeffs[i]);
        }


     //function
     poly h;
     unsigned int s=poly_make_hint(&h,&a0,&a1);



    printf("h.coeefs after calling function:\n");
    for (i = 0; i < N; i++) {
        printf("%d\n", h.coeffs[i]);
    }

     printf("s after calling function:\n");

             printf("%d\n", s);


}























void test_poly_chknorm() {
    poly a;
    int i;
    printf("initial a.coeefs:\n");
    printf("%d\n", N);
    for (i = 0; i < N; i++) {
        a.coeffs[i] = rand();
        printf("%d\n", a.coeffs[i]);
    }
    printf("initial B:\n");
    int32_t B=rand();
    printf("%d\n", B);
    int ans=poly_chknorm(&a,B);
    printf("ans after calling function:\n");
    printf("%d\n", ans);

}








void gg(){
 printf("%d\n", CRHBYTES);
}



void test_rej_uniform() {
    int32_t a[100];
    int i;
    printf("initial a:\n");
    printf("%d\n", 100);
    for (i = 0; i < 100; i++) {
        a[i] = rand();
        printf("%d\n", a[i]);
    }


    uint8_t buf[200];
    printf("initial buf:\n");
    printf("%d\n", 200);
    for (i = 0; i < 200; i++) {
            buf[i] = rand();
            printf("%d\n", buf[i]);
        }



    unsigned int ans=rej_uniform(a,100,buf,200);


    printf("ans after calling function:\n");
     printf("%d\n", ans);


    printf("a after calling function:\n");
        //printf("%d\n", 100);
        for (i = 0; i < 100; i++) {
            printf("%d\n", a[i]);
        }

}





void test_rej_eta() {


    int32_t a[100];
    int i;
    printf("initial a:\n");
    printf("%d\n", 100);
    for (i = 0; i < 100; i++) {
        a[i] = rand();
        printf("%d\n", a[i]);
    }


    uint8_t buf[200];
    printf("initial buf:\n");
    printf("%d\n", 200);
    for (i = 0; i < 200; i++) {
            buf[i] = rand();
            printf("%d\n", buf[i]);

        }



    unsigned int ans=rej_eta(a,100,buf,200);


    printf("ans after calling function:\n");
     printf("%d\n", ans);


    printf("a after calling function:\n");
        //printf("%d\n", 100);
        for (i = 0; i < 100; i++) {
            printf("%d\n", a[i]);
        }

}


















void test_poly_uniform_gamma1() {
  poly a;
      int i;
      printf("initial a.coeefs:\n");
      printf("%d\n", N);
      for (i = 0; i < N; i++) {
          a.coeffs[i] = rand();
          printf("%d\n", a.coeffs[i]);
      }


    uint8_t seed[CRHBYTES];
    printf("initial seed:\n");
    printf("%d\n",CRHBYTES);
    for (i = 0; i < CRHBYTES; i++) {
            seed[i] = rand();
            printf("%d\n", seed[i]);
        }


    printf("initial nonce:\n");
    uint16_t nonce=rand();
    printf("%d\n", nonce);




    poly_uniform_gamma1(&a,seed, nonce);


         printf("after a.coeefs:\n");
          for (i = 0; i < N; i++) {
              printf("%d\n", a.coeffs[i]);
          }


            printf("after seed:\n");
              for (i = 0; i < CRHBYTES; i++) {
                      printf("%d\n", seed[i]);
                  }



}






void test_poly_challenge() {
  poly c;
      int i;
      printf("initial c.coeefs:\n");
      printf("%d\n", N);
      for (i = 0; i < N; i++) {
          c.coeffs[i] = rand();
          printf("%d\n", c.coeffs[i]);
      }


    uint8_t seed[SEEDBYTES];
    printf("initial seed:\n");
    printf("%d\n",SEEDBYTES);
    for (i = 0; i < SEEDBYTES; i++) {
            seed[i] = rand();
            printf("%d\n", seed[i]);
        }



    poly_challenge(&c,seed);


          printf("after c.coeefs:\n");
          for (i = 0; i < N; i++) {
              printf("%d\n", c.coeffs[i]);
          }


              printf("after seed:\n");
              for (i = 0; i < SEEDBYTES; i++) {
                      printf("%d\n", seed[i]);
                  }



}







void test_polyt0_unpack() {
  poly c;
      int i;
      printf("initial c.coeefs:\n");
      printf("%d\n", N);
      for (i = 0; i < N; i++) {
          c.coeffs[i] = rand();
          printf("%d\n", c.coeffs[i]);
      }


    uint8_t seed[600];
    printf("initial seed:\n");
    printf("%d\n",600);
    for (i = 0; i < 600; i++) {
            seed[i] = rand();
            printf("%d\n", seed[i]);
        }



   polyw1_pack(seed,&c);


          printf("after c.coeefs:\n");
         for (i = 0; i < N; i++) {
              printf("%d\n", c.coeffs[i]);
          }


         printf("after seed:\n");
              for (i = 0; i < 600; i++) {
                      printf("%d\n", seed[i]);
                  }


}










#define number_of_tests 10

int main(){
    
    int i;
    printf("number of tests:\n");
    printf("%d\n", number_of_tests);
    for (i=0; i < number_of_tests; i++) {
        printf("testing function:\n");
        test_poly_uniform_gamma1();
    }
    return 0;
}
