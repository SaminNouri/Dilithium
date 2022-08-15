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


#define number_of_tests 10

int main(){
    
    int i;
    printf("number of tests:\n");
    printf("%d\n", number_of_tests);
    for (i=0; i < number_of_tests; i++) {
        printf("testing poly_reduce:\n");
        test_poly_reduce();
    }
    return 0;
}
