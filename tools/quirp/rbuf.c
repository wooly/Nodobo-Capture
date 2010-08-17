#include <stdlib.h>
#include "rbuf.h"

rbuf * rbuf_new(unsigned int size)
{
    rbuf *rb;
    unsigned int i;
    
    if (size == 0)
        return NULL;
    
    rb = malloc(sizeof(rbuf));
    if (rb == NULL)
        return NULL;
    
    /* Using calloc to get continuous region */
    rb->buf = calloc(size, sizeof(void *));
    if (rb->buf == NULL)
    {
        free(rb);
        return NULL;
    }
    
    /* calloc initialises to 0, which is not always NULL */
    for (i = 0; i < size; i++)
        rb->buf[i] = NULL;
    
    rb->ins = rb->rem = rb->buf;
    rb->end = rb->buf + size - 1;
    
    return rb;
}

void rbuf_free(rbuf *rb)
{
    if (rb == NULL)
        return;
    
    free(rb->buf);
    free(rb);
}

void * rbuf_ins(rbuf *rb, void *new)
{
    void **ins = rb->ins;
    void *old = *ins;

    *ins = new;
    
    ins++;
    if (ins > rb->end)
        ins = rb->buf;
    if (rb->ins == rb->rem && *ins != NULL)
        rb->rem = ins;
    rb->ins = ins;

    return old;
}

void * rbuf_rem(rbuf *rb)
{
    void **rem = rb->rem;
    void *old = *rem;
    
    *rem = NULL;
    
    if (old == NULL)
        return NULL;
    
    rem++;
    if (rem > rb->end)
        rem = rb->buf;
    rb->rem = rem;
    
    return old;
}
