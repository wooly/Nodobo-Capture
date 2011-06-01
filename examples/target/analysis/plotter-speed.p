set term postscript font "Helvetica,14"
set output "plotter-speed.ps"
set datafile separator ","

set style data histogram
set style histogram errorbars gap 1 lw 2
set style fill solid border -1

set border 1

set xlabel "Button size (mm)"
set ylabel "Tap time (seconds)"

set tics scale 0.0

set grid y

set yrange [0:3]
set ytics 0.5
set ytics nomirror

plot 'sitting.dat' using 2:3:4:xtic(1) ti "Sitting" fs pattern 0 lt -1, 'walking.dat' using 2:3:4:xtic(1) ti "Walking" fs pattern 1 lt -1
