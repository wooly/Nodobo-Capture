#!/bin/sh

rm -f sitting.dat walking.dat results-aggregated.txt

for file in results/*.csv
do
    echo $file >> results-aggregated.txt
    python accuracy.py $file >> results-aggregated.txt
done

echo Sitting:

grep sitting -A4 results-aggregated.txt | \
awk '/30:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "30,%.3f,%.3f,%.3f,%.3f\n", (b/n), (a/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 results-aggregated.txt | \
awk '/60:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "60,%.3f,%.3f,%.3f,%.3f\n", (b/n), (a/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 results-aggregated.txt | \
awk '/99:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "99,%.3f,%.3f,%.3f,%.3f\n", (b/n), (a/n), c/n, d/n}' | \
tee -a sitting.dat

grep sitting -A4 results-aggregated.txt | \
awk '/129:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "129,%.3f,%.3f,%.3f,%.3f\n", (b/n), (a/n), c/n, d/n}' | \
tee -a sitting.dat

echo Walking:

grep walking -A4 results-aggregated.txt | \
awk '/30:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "30,%.3f,%.3f,%.3f,%.3f\n", (b/n), (a/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 results-aggregated.txt | \
awk '/60:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "60,%.3f,%.3f,%.3f,%.3f\n", (b/n), (a/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 results-aggregated.txt | \
awk '/99:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "99,%.3f,%.3f,%.3f,%.3f\n", (b/n), (a/n), c/n, d/n}' | \
tee -a walking.dat

grep walking -A4 results-aggregated.txt | \
awk '/129:/' | \
cut -d' ' -f2 | \
awk -F'/' '{a+=$1; b+=$2; c+=$3; d+=$4; ++n} END {printf "129,%.3f,%.3f,%.3f,%.3f\n", (b/n), (a/n), c/n, d/n}' | \
tee -a walking.dat

gnuplot 'plotter.p'
