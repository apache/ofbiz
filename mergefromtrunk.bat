rem DOS version of mergefromtrunk.sh. A bit clumsy but it works. If someone is able to do better I am taker JLR 2007/05/27
set /a prevRev=%1% - 1
rem DOS eats char below %1%. It eats exactly "https:" don't know why, I tried to add them : it works (DOS ways are impenetrable)
svn merge -r %prevRev%:%1% https: https://svn.apache.org/repos/asf/ofbiz/trunk
svn commit -m "Applied fix from trunk for revision: %1%"
