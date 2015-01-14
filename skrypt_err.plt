unset colorbox

set autoscale

set datafile separator ","


set term png
set output errorPlotFile


plot dataFile using 1:2 with line