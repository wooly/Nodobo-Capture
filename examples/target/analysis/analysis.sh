#!/bin/sh

rm -f sitting.dat walking.dat


echo Sitting:

grep sitting -A4 $1 | \
awk '/3:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "3,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 $1 | \
awk '/6:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "6,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 $1 | \
awk '/10:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "10,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 $1 | \
awk '/13:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "13,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a sitting.dat

echo Walking:

grep walking -A4 $1 | \
awk '/3:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "3,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 $1 | \
awk '/6:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "6,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 $1 | \
awk '/10:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "10,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 $1 | \
awk '/13:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "13,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a walking.dat
