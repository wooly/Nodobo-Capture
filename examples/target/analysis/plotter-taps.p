set term aqua font "Helvetica,22"
set datafile separator ","
set xlabel "Button size (mm)"
set ylabel "Number of taps required"
set style data histogram
set yrange [0:]
set style histogram errorbars gap 1 lw 2
set style fill solid border -1
set boxwidth 0.9
plot 'sitting.dat' using 2:3:4:xtic(1) ti "Sitting", 'walking.dat' using 2:3:4:xtic(1) ti "Walking"
