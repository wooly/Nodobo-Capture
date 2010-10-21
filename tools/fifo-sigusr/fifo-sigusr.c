#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/poll.h> 
#include <fcntl.h> 
#include <signal.h>
#include <time.h>

#define FIFO "/data/nodobo/fifo-sigusr/fifo-sigusr.fifo"
#define LOGFILE "/data/nodobo/fifo-sigusr/fifo-sigusr.log"
#define FIFOPID "/data/nodobo/fifo-sigusr/fifo-sigusr.pid"
#define QUIRPID "/data/nodobo/quirp/quirp.pid"
#define SLEEP_TIMEOUT 10000

void log_message(const char *message)
{
    FILE *lf = fopen(LOGFILE, "a");

    if (lf == NULL)
        return;    
    fprintf(lf, "%s\n", message);
    fclose(lf);
}

void sig_term(int sig)
{
    log_message("Caught SIGTERM, exiting.");
    exit(0);
}

void notifyQuirp()
{
    int pid;
    FILE *pf = fopen(QUIRPID, "r");
    fscanf(pf, "%d", &pid);
    fclose(pf);
    log_message("Sending SIGUSR1");
    kill(pid, SIGUSR1);
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

    fd = open(FIFOPID, O_RDWR|O_CREAT, 0644);
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
    signal(SIGUSR1, SIG_IGN);
    signal(SIGTERM, sig_term);
}


int main(int argc, char * argv[])
{
    int ret;
    int fd;
    struct pollfd pfd[1];
    time_t lastsig;
    daemonize();
    fd = open(FIFO, O_RDONLY | O_NONBLOCK);
    pfd[0].fd = fd;
    pfd[0].events = 0;
    pfd[0].revents = 0;
    
    lastsig = time(NULL);
    
    while(1)
    {
        ret = poll(pfd, 1, -1);
        if (ret < 0)
        {
            log_message("Error reading from named pipe\n");
            exit(1);
        }
        else if (ret > 0)
        {
            time_t now;
            
            if (pfd[0].revents & POLLHUP)
            {
                close(fd);
                open(FIFO, O_RDONLY | O_NONBLOCK);
            }
            
            now = time(NULL);
            if (now - lastsig > 1)  /* Signal no more than every 2 seconds */
            {
                lastsig = now;
                notifyQuirp();
            }
        }
    }
    close(fd);

    return 0;
}