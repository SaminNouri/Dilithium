#include <stdio.h>
#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <stddef.h>
#include <stdint.h>
#include <stdlib.h>




void test() {

  int i=0;

   uint8_t sk[CRYPTO_SECRETKEYBYTES];
   uint8_t pk[CRYPTO_PUBLICKEYBYTES];
  memset(sk, 0, CRYPTO_SECRETKEYBYTES);
  memset(pk, 0, CRYPTO_PUBLICKEYBYTES);



        crypto_sign_keypair(pk,sk);


          printf("after sk:\n");
          //printf("%d\n", CRYPTO_SECRETKEYBYTES);
         for (i = 0; i < CRYPTO_SECRETKEYBYTES; i++) {
              printf("%" PRIu8 "\n", sk[i]);
          }


         printf("after pk:\n");
           // printf("%d\n", CRYPTO_PUBLICKEYBYTES);
              for (i = 0; i < CRYPTO_PUBLICKEYBYTES; i++) {
                       printf("%" PRIu8 "\n", pk[i]);
                  }


}



void test1() {

  int i=0;

   uint8_t sk[CRYPTO_SECRETKEYBYTES];
  memset(sk, 0, CRYPTO_SECRETKEYBYTES);
   uint8_t m[10];
    uint8_t sig[3000];
    size_t siglen=3000;
    memset(sig,0,3000);

           printf("before m:\n");
             printf("%d\n", 10);
             for (i = 0; i < 10; i++) {
                  m[i]=rand();
                  printf("%" PRIu8 "\n", m[i]);
              }


                     printf("before sk:\n");
                        printf("%d\n", CRYPTO_SECRETKEYBYTES);
                       for (i = 0; i < CRYPTO_SECRETKEYBYTES; i++) {
                            sk[i]=rand();
                            printf("%" PRIu8 "\n", sk[i]);
                        }





        crypto_sign(sig,&siglen,m,10,sk);


         printf("after sig:\n");
          //printf("%d\n", CRYPTO_SECRETKEYBYTES);
         for (i = 0; i < 3000; i++) {
              printf("%" PRIu8 "\n", sig[i]);
          }




}


void test2(){
int i=0;


//sk,pk
uint8_t sk[CRYPTO_SECRETKEYBYTES];
uint8_t pk[CRYPTO_PUBLICKEYBYTES];
memset(sk, 0, CRYPTO_SECRETKEYBYTES);
memset(pk, 0, CRYPTO_PUBLICKEYBYTES);
crypto_sign_keypair(pk,sk);

printf("after sk:\n");
//printf("%d\n", CRYPTO_SECRETKEYBYTES);
for (i = 0; i < CRYPTO_SECRETKEYBYTES; i++) {
              printf("%" PRIu8 "\n", sk[i]);
 }


printf("after pk:\n");
//printf("%d\n", CRYPTO_PUBLICKEYBYTES);
for (i = 0; i < CRYPTO_PUBLICKEYBYTES; i++) {
              printf("%" PRIu8 "\n", pk[i]);
}

//sign

uint8_t m[10];
uint8_t sig[10000];
size_t siglen=sizeof(sig);
size_t mlen=sizeof(m);
memset(sig,0,CRYPTO_BYTES);
printf("before m:\n");
printf("%d\n", 10);
for (i = 0; i < 10; i++) {
          m[i]=rand();
          printf("%" PRIu8 "\n", m[i]);
}
crypto_sign(sig,&siglen,m,mlen,sk);


printf("sig:\n");
printf("%zu\n", siglen);
for (i = 0; i < siglen; i++) {
              printf("%" PRIu8 "\n", sig[i]);
}




int ans=crypto_sign_verify(sig, CRYPTO_BYTES, sig + CRYPTO_BYTES, mlen, pk);
int anss=crypto_sign_open(m,&mlen,sig,siglen,pk);
printf("%d\n", ans);
printf("%d\n", anss);














}









#define number_of_tests 500

int main(){
    
    int i;
    printf("number of tests:\n");
    printf("%d\n", number_of_tests);
    for (i=0; i < number_of_tests; i++) {
        printf("testing function:\n");
        test2();
    }
    return 0;
}
