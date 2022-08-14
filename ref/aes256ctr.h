#ifndef AES256CTR_H
#define AES256CTR_H

#include <stddef.h>
#include <stdint.h>

#define AES256CTR_BLOCKBYTES 64

#define AES256CTR_NAMESPACE(s) pqcrystals_dilithium_aes256ctr_ref_##s

typedef struct {
  uint64_t sk_exp[120];
  uint32_t ivw[16];
} aes256ctr_ctx;

#define aes256ctr_init AES256CTR_NAMESPACE(init)
void aes256ctr_init(aes256ctr_ctx *state,
                    const uint8_t key[32],
                    const uint8_t nonce[12]);

#define aes256ctr_squeezeblocks AES256CTR_NAMESPACE(squeezeblocks)
void aes256ctr_squeezeblocks(uint8_t *out,
                             size_t nblocks,
                             aes256ctr_ctx *state);

#define dilithium_aes256ctr_init AES256CTR_NAMESPACE(squeezeblocks)
void dilithium_aes256ctr_init(aes256ctr_ctx *state,
                              const uint8_t key[32],
                              uint16_t nonce);

#endif