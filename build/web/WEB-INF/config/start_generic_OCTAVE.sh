#!/bin/bash
#####################################################################
# Script        : start_generic_OCTAVE.sh (bash)                    #
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

OUTPUT_FILE="demo_output.eps"
SOFTWARE_PATH=/usr/local/bin

echo "- Running on "`hostname -f`

echo "- Checking input file"
if [ "X$1" == "X" ] ; then
        echo "Macro file is missing!"
        exit
else
        INPUT_FILE=`basename $1`
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
~ .eps:		the graphical file produced during the computation.
EOF
