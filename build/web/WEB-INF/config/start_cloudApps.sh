#!/bin/bash

# Possible values are: 'normal', 'octave', 'r'
ARGUMENT=$1

# Check if the IP belongs to KTH #
#str=$(ifconfig eth0 | grep "inet")
#echo $str | grep -i 192.168.1 2>/dev/null >/dev/null
#if [ $? -eq 0 ] ; then
#ARGUMENT="normal"
#fi

if [ "X${ARGUMENT}" == "Xnormal" ] ; then
echo "General Info ...> This is a CHAIN-REDS test job. See below server details "
echo "-----------------------------------------------------------------------------------"
echo "Running host ...> " `hostname -f`
echo "IP address .....> " `/sbin/ifconfig | grep "inet addr:" | head -1 | awk '{print $2}' | awk -F':' '{print $2}'`
echo "Kernel .........> " `uname -r`
echo "Distribution ...> " `head -n1 /etc/issue`
echo "Arch ...........> " `uname -a | awk '{print $12}'`
echo "CPU  ...........> " `cat /proc/cpuinfo | grep -i "model name" | head -1 | awk -F ':' '{print $2}'`
echo "Memory .........> " `cat /proc/meminfo | grep MemTotal | awk {'print $2'}` KB
echo "Partitions .....> " `cat /proc/partitions`
echo "Uptime host ....> " `uptime | sed 's/.*up ([^,]*), .*/1/'`
echo "Timestamp ......> " `date`
echo "-----------------------------------------------------------------------------------"
echo "http://www.chain-project.eu/"
echo "Copyright © 2013"

cat <<EOF >> output.README
#
# README - generic_VM
#
# Giuseppe LA ROCCA, INFN Catania
# <mailto:giuseppe.larocca@ct.infn.it>
#

This is a generic VM i686 server.

If the job has been successfully executed, the following files will be produced:
~ std.out:      the standard output file;
~ std.err:      the standard error file;
EOF

tar zcf results.tar.gz output.README 2>/dev/null
fi

if [ "X${ARGUMENT}" == "Xoctave" ] ; then
OUTPUT_FILE="demo_output.eps"
SOFTWARE_PATH=/usr/local/bin

echo "- Running on "`hostname -f`

echo "- Checking input file"
if [ "X$2" == "X" ] ; then
        echo "Macro file is missing!"
        exit
else
        INPUT_FILE=`basename $2`
fi

echo "Checking OCTAVE-3.6.4 software"
tree -L 1 ${SOFTWARE_PATH}

echo;echo "- Starting GNU OCTAVE-3.6.4 at ";date
#dos2unix ${INPUT_FILE}
${SOFTWARE_PATH}/octave ${INPUT_FILE} 2>/dev/null >/dev/null

echo;echo "- Checking results..."
ls -al ${OUTPUT_FILE}
if [ $? -eq 0 ] ; then
        echo "The macro file HAS BEEN successfully processed."
else
        echo "Problems occured during the processing of the Octave macro file, please check log files."
fi

cat <<EOF >> output.README
#
# README - GNU OCTAVE-3.6.4
#
# Giuseppe LA ROCCA, INFN Catania
# <mailto:giuseppe.larocca@ct.infn.it>
#

This is a generic VM i686 server configured to run GNU Octave-3.6.4.
GNU Octave is a high-level interpreted language, primarily intended 
for numerical computations. 
It provides capabilities for the numerical solution of linear and 
non-linear problems, and for performing other numerical experiments. 
It also provides extensive graphics capabilities for data visualization 
and manipulation.

If the job has been successfully executed, the following files will be produced:
~ std.out:      the standard output file;
~ std.err:      the standard error file;
~ .eps:         the graphical file produced during the computation.
EOF

tar zcf results.tar.gz output.README demo_output.eps 2>/dev/null
fi

if [ "X${ARGUMENT}" == "Xr" ] ; then
OUTPUT_FILE="Rplots.pdf"
STDOUTPUT_FILE="std.out"
SOFTWARE_PATH=/usr/local/bin

echo "- Running on "`hostname -f`

echo "- Checking input file"
if [ "X$2" == "X" ] ; then
        echo "Macro file is missing!"
        exit
else
        INPUT_FILE=`basename $2`
fi

echo "- Checking R-2.15.3 software"
tree -L 1 ${SOFTWARE_PATH}

echo;echo "- Starting R-2.15.3 at ";date
#dos2unix ${INPUT_FILE}
${SOFTWARE_PATH}/R --vanilla < ${INPUT_FILE} > ${STDOUTPUT_FILE}

echo;echo "- Checking results..."
ls -al ${OUTPUT_FILE}
if [ $? -eq 0 ] ; then
        echo "The macro file HAS BEEN successfully processed."
else
        echo "Problems occured during the processing of the R macro file, please check log files."
fi

cat <<EOF >> output.README
#
# README - R-2.15.3
#
# Giuseppe LA ROCCA, INFN Catania
# <mailto:giuseppe.larocca@ct.infn.it>
#

This is a generic VM i686 server configured to run R-2.15.3.
R is a free software environment for statistical computing and graphics. 
R provides a wide variety of statistical (linear and nonlinear modelling, 
classical statistical tests, time-series analysis, classification, clustering, ...) 
and graphical techniques, and is highly extensible. 
It compiles and runs on a wide variety of UNIX platforms, Windows and MacOS.

If the job has been successfully executed, the following files will be produced:
~ std.out:      the standard output file;
~ std.err:      the standard error file;
~ .pdf:         the graphical file produced during the computation.
EOF

tar zcf results.tar.gz output.README Rplots.pdf 2>/dev/null
fi
