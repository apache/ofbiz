rem interactive DOS version of mergefromtrunk.sh.
rem to use : launch and pass the trunk version number to merge in release

echo off
rem since we have now svn:mergeinfo changing root ("."), we need to update before merging
svn up

rem version to merge
set /p version=version to merge :
set /a prevRev=%version% - 1

rem build the comment
echo "Applied fix from trunk for revision: %version%" > comment.tmp
svn log https://svn.apache.org/repos/asf/ofbiz/trunk -r %version% > log.tmp
copy comment.tmp + log.tmp = comment.tmp
del log.tmp
rem keep the comment.tmp file svn ignored. In case of trouble always happier to keep trace.  It will be overidden in next backport.

rem commit the backport to release with comment fom file
echo on
svn merge -r %prevRev%:%version% https://svn.apache.org/repos/asf/ofbiz/trunk
svn commit -F comment.tmp
pause
