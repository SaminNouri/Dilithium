#ifndef NTT_H
#define NTT_H

#include <stdint.h>
#include "params.h"

#define NTT_NAMESPACE(s) pqcrystals_dilithium_ntt_ref_##s

#define ntt NTT_NAMESPACE(ntt)
void ntt(int32_t a[N]);

#define invntt_tomont NTT_NAMESPACE(invntt_tomont)
void invntt_tomont(int32_t a[N]);

#endif
