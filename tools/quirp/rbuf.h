typedef struct
{
    void **buf;
    void **end;
    void **ins;
    void **rem;
} rbuf;

rbuf * rbuf_new(unsigned int size);
void rbuf_free(rbuf *rb);
void * rbuf_ins(rbuf *rb, void *data);
void * rbuf_rem(rbuf *rb);
