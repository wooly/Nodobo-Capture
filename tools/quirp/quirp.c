    /*  quirp.
    quiet user interaction recording program.
    it doesn't beep!
    */

#include <sys/errno.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>
#include <time.h>
#include <sys/time.h>
#include <signal.h>
#include "fastlz.h"
#include "rbuf.h"

#define DATADIR "/sdcard/nodobo/quirp-data/"
#define PIDFILE "/data/nodobo/quirp/quirp.pid"
#define LOGFILE	"/data/nodobo/quirp/quirp.log"
#define FB "/dev/graphics/fb0"
#define ROW_BYTES 480 * 4
#define FRAME_BYTES 800 * ROW_BYTES
#define COMPRESSED_BYTES (int) (800 * 480 * 1.05)
#define SECONDS 10
#define MAXFPS 5

typedef struct framecap
{
    char *data;
    int len;
    struct timeval timestamp;
    int writeme;
} framecap;

rbuf *rb;

void log_message(const char *message)
{
    char timestamp[80] = "Unknown time --- ";
    struct timeval t;

    if (gettimeofday(&t, NULL) == 0)
    {
        strftime(timestamp, sizeof(timestamp), "%Y-%m-%d %H:%M:%S --- ", localtime(&(t.tv_sec)));
    }

    FILE *lf = fopen(LOGFILE, "a");

    if (lf == NULL)
        return;

    fprintf(lf, "%s%s\n", timestamp, message);
    fclose(lf);
}

void sig_term(int sig)
{
    log_message("Caught SIGTERM, exiting.");
    exit(0);
}

void sig_usr(int signum)
{
    void **buf;
    
    log_message("Caught SIGUSR, flagging ring buffer for write");
    for (buf = rb->buf; buf <= rb->end; buf++)
    {
        if (*buf != NULL)
        {
            framecap *fc = (framecap *) *buf;
            fc->writeme = 1;
        }
    }
}

void daemonize()
{
    int i, pid, fd;
    char str[10];

    if (getppid() == 1)
        return; /* already a daemon */

    pid = fork();
    if (pid < 0)
        exit(1); /* fork error */
    if (pid > 0)
        exit(0); /* parent exits */

    /* child (daemon) continues */
    setsid(); /* obtain a new process group */

    for (i = getdtablesize(); i >= 0; --i)
        close(i); /* close all descriptors */

    fd = open("/dev/null", O_RDWR);
    dup(fd); dup(fd); /* handle standard I/O */

    fd = open(PIDFILE, O_RDWR|O_CREAT, 0644);
    if (fd < 0)
	exit(1); /* can not open */
    // if (lockf(fd, F_TLOCK, 0) < 0)
    // exit(0); /* can not lock */

    sprintf(str, "%d\n", getpid());
    write(fd, str, strlen(str)); /* record pid to lockfile */
    close(fd);
	    
    signal(SIGCHLD, SIG_IGN);
    signal(SIGTSTP, SIG_IGN);
    signal(SIGTTOU, SIG_IGN);
    signal(SIGTTIN, SIG_IGN);
    signal(SIGHUP,  SIG_IGN);
    signal(SIGTERM, sig_term);
    signal(SIGUSR1, sig_usr);
}

void framecap_write(framecap *fc)
{
    int of, len;
    ssize_t ret;
    char fn[80];
    
    if (fc == NULL)
    {
        log_message("Framecap is null: returning.");
        return;
    }
    
    len = strftime(fn, sizeof(fn), "%Y%m%d%H%M%S", localtime(&(fc->timestamp.tv_sec)));
    snprintf(fn + len, sizeof(fn) - len, ".%06ld.rgba.lz", (long int) fc->timestamp.tv_usec);
    
    of = open(fn, O_WRONLY | O_CREAT);
    if (of < 0)
    {
        log_message("Failed to open file for writing");
        return;
    }

    ret = write(of, fc->data, fc->len);
    if (ret < 0)
    {
        log_message("Failed to write frame to disk");
    }
    close(of);
}

void scaleframe(char * scaledframe, char * frame)
{
    int byte, group, offset;
    
    for (byte = 0; byte < FRAME_BYTES; byte += (ROW_BYTES * 2))
    {
        for (group = 0; group < ROW_BYTES; group+=8)
        {
            int location = (byte / 4) + (group / 2);
            int start = byte + group;
            for (offset = 0; offset < 4; offset++)
                scaledframe[location + offset] = (frame[start + offset] + frame[start + offset + 4] + frame[start + offset + ROW_BYTES] + frame[start + offset + ROW_BYTES + 4]) / 4;
        }
    }
}

int main(void)
{
    int fb;
    char *frame, *scaledframe, *compressed;
    int flen, clen;
    framecap *fc, *oldfc;
    struct timeval t;
    double start, end;
    int startreturn, endreturn;
    char err[20];
    
    daemonize();

    int errcode;
    while (chdir(DATADIR) < 0)
    {
        snprintf(err, sizeof(err), "Error: %d", errno);
        log_message(err);
        sleep(1);
    }
    
    rb = rbuf_new(MAXFPS * SECONDS);
    if (rb == NULL)
    {
        log_message("Failed to allocate ring buffer");
        exit(1);
    }
    
    frame = malloc(FRAME_BYTES);
    if (frame == NULL)
    {
        log_message("Failed to allocate frame");
        exit(1);
    }
    
    scaledframe = malloc(FRAME_BYTES / 4);
    if (scaledframe == NULL)
    {
        log_message("Failed to allocate scaled frame");
        exit(1);
    }
    
    fb = open(FB, O_RDONLY);
    if (fb < 0)
    {
        log_message("Failed to open framebuffer");
        exit(1);
    }
    
    while (1)
    {
        startreturn = gettimeofday(&t, NULL);
        start = t.tv_sec * 1000000.0 + t.tv_usec;

        if (lseek(fb, FRAME_BYTES, SEEK_SET) < 0)
        {
            log_message("Failed to seek");
            exit(1);
        }
        flen = read(fb, frame, FRAME_BYTES);
        if (flen <= 0)
        {
            log_message("Failed to read");
            exit(1);
        }
        scaleframe(scaledframe, frame);
        compressed = malloc(COMPRESSED_BYTES);
        clen = fastlz_compress_level(1, scaledframe, FRAME_BYTES / 4, compressed);
        if (clen == 0)
        {
            log_message("Failed to compress frame");
            continue;
        }
        compressed = realloc(compressed, clen);
        
        fc = malloc(sizeof(framecap));
        if (fc == NULL)
        {
            log_message("Failed to allocate framecap");
            exit(1);
        }
        
        fc->data = compressed;
        fc->len = clen;
        gettimeofday(&(fc->timestamp), NULL);
        fc->writeme = 0;
        
        oldfc = rbuf_ins(rb, fc);
        if (oldfc != NULL)
        {
            if (oldfc->writeme)
                framecap_write(oldfc);
            free(oldfc->data);
            free(oldfc);
        }

        endreturn = gettimeofday(&t, NULL);
        end = t.tv_sec * 1000000.0 + t.tv_usec;

        int sleept = (int)1000000/MAXFPS - (end - start);

        #ifdef DEBUG
            char arr[150];
            snprintf(arr, sizeof(arr), "%.0f to %.0f, delta: %dus, sleep: %dus", start, end, (int)(end - start), sleept);
            log_message(arr);
        #endif
        
        if (sleept > 0)
            usleep(sleept);
    }

    close(fb);

    return 0;
}
