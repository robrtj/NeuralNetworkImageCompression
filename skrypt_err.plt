unset colorbox

set autoscale

set datafile separator ","


set term png
set output "errorPlot.png"


plot 'error_test.csv' using 1:2 with line