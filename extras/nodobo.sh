#!/system/bin/sh

/system/xbin/mount -t ext2 /dev/block/mmcblk0p2 /nodobo
chown system.sdcard_rw /nodobo
chmod 771 /nodobo
/nodobo/capture/fifo/fifo-to-sig
/nodobo/capture/quirp/quirp
