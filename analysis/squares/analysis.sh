#!/bin/sh

rm -f sitting.dat walking.dat


echo Sitting:

grep sitting -A4 results-aggregated.txt | \
awk '/30:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "30,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 results-aggregated.txt | \
awk '/60:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "60,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 results-aggregated.txt | \
awk '/99:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "99,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 results-aggregated.txt | \
awk '/129:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "129,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a sitting.dat

echo Walking:

grep walking -A4 results-aggregated.txt | \
awk '/30:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "30,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 results-aggregated.txt | \
awk '/60:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "60,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 results-aggregated.txt | \
awk '/99:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "99,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 results-aggregated.txt | \
awk '/129:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "129,%.3f,%.3f,%.3f,%.3f\n", (a/n), (b/n), c/n, d/n}' | \
tee -a walking.dat

gnuplot 'plotter.p'
