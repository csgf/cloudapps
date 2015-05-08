#!/bin/sh
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
