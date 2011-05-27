#!/bin/zsh

(for x in results/sitting?.csv results/walking?.csv; do echo $x; python taps.py $x clues.sqlite3; done ) > taps.dat
(for x in results/*-*.csv; do echo $x; python taps.py $x second-clues.sqlite3; done ) >> taps.dat
sh analysis.sh taps.dat
gnuplot plotter-taps.p
