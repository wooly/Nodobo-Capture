#!/bin/zsh

(for x in results/sitting?.csv results/walking?.csv; do echo $x; python speed.py $x clues.sqlite3; done ) > speed.dat
(for x in results/*-*.csv; do echo $x; python speed.py $x second-clues.sqlite3; done ) >> speed.dat
sh analysis.sh speed.dat
gnuplot plotter-speed.p
