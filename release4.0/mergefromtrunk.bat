rem interactive DOS version of mergefromtrunk.sh.
rem to use : launch and pass the trunk version number to merge in release

echo off
set /p version=version to merge :
set /a prevRev=%version% - 1
echo on
svn merge -r %prevRev%:%version% https://svn.apache.org/repos/asf/ofbiz/trunk
svn commit -m "Applied fix from trunk for revision: %version%"
pause
