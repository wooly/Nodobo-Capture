#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/stat.h> 
#include <fcntl.h>
#include <android/log.h>

#define FIFO "/data/nodobo/fifo-sigusr/fifo-sigusr.fifo"

jint
Java_com_nodobo_capture_InteractionReceiver_notifyQuirp(JNIEnv * env, jobject thiz)
{
    int ret, value;
    int fd;
    fd = open(FIFO, O_WRONLY);
    if(fd < 0)
    {
        __android_log_print(ANDROID_LOG_INFO, "Nodobo", "JNI: Unable to open %s for writing", FIFO);
        return(1);
    }
    ret = write(fd, "A", 1);
    close(fd);
    return(0);
}