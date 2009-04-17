#!/bin/sh

prevRev=`expr $1 - 1`
svn merge -r $prevRev:$1 https://svn.apache.org/repos/asf/ofbiz/trunk 
svn commit -m "Applied fix from trunk for revision: $1"

