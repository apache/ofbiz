#!/bin/sh

prevRev=`expr $1 - 1`
svn merge -r $prevRev:$1 https://svn.apache.org/repos/asf/ofbiz/trunk 
trunkLog=runtime/trunkLog.xml
touch ${trunkLog}
svn log --xml https://svn.apache.org/repos/asf/ofbiz/trunk -r $1> ${trunkLog}
releaseBranchMessage="Applied fix from trunk for revision: $1 \n"
trunkMessage=`grep -e '<msg>' ${trunkLog} | sed 's/<msg>//' | sed 's/<\/msg>//'` 
rm -rf ${trunkLog}
svn commit -m "`echo ${releaseBranchMessage} ${trunkMessage}`"

