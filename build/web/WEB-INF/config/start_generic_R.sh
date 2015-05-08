#!/bin/bash
#####################################################################
# Script        : start_generic_R.sh (bash)                         #
# Release       : 1.0                                               #
# Last Update   : September 09, 2013                                #
# ================================================================  #
# Author        : Giuseppe La Rocca (giuseppe.larocca@ct.infn.it)   #
# Organization  : Physics Institute for Nuclear Research            #
#               : INFN - Univ. of Catania                           #
# Address       : Via S. Sofia, 64                                  #
#               : 95123 Catania (CT) - ITALY                        #
# Phone         : (+39) 095.378.55.19                               #
#####################################################################

OUTPUT_FILE="Rplots.pdf"
STDOUTPUT_FILE="std.out"
SOFTWARE_PATH=/usr/local/bin

echo "- Running on "`hostname -f`

echo "- Checking input file"
if [ "X$1" == "X" ] ; then
        echo "Macro file is missing!"
        exit
else
        INPUT_FILE=`basename $1`
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
~ .pdf:		the graphical file produced during the computation.
EOF
