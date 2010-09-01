set term aqua
set title "Timing calculations"
set datafile separator ","
set xlabel "Button size (mm)"
set ylabel "Number of taps required"
set style data histogram
set style histogram errorbars gap 1
set style fill solid border -1
set boxwidth 0.9
plot 'sitting.dat' using 2:3:4:xtic(1) ti "Sitting", 'walking.dat' using 2:3:4:xtic(1) ti "Walking"
