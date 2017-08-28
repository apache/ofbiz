<?php 
/*
 * arguments:
 *   $page_name
 */
?>
<!-- header -->
<header id="mainHeader" class="clearfix">
  <div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
      <div class="container"> <a href="index.html" class="brand"><img src="images/OFBiz-logoV3-apache.png" alt="Apache OFBiz Logo"/></a>
        <nav id="mainMenu" class="clearfix">
          <ul>
            <li><a href="index.html" class="firstLevel<?php if (basename($page_name) == 'index.tpl.php'):?> active<?php endif;?>">Home</a></li>
            <li><a href="" class="firstLevel<?php if ($page_name == 'getting-started.tpl.php'):?> active<?php endif;?>">Getting Started</a>
              <ul>
                <li><a href="developers.html" class="">Developers</a></li>
                <li><a href="business-users.html" class="last">Business Users</a></li>
              </ul>
            </li>
            <li><a href="" class="firstLevel<?php if (basename($page_name) == 'news.tpl.php'):?> active<?php endif;?>">News</a>
              <ul>
                <li><a href="https://twitter.com/apacheofbiz" target="external">Twitter</a></li>
                <li><a href="https://blogs.apache.org/ofbiz/" target="external" class="last">Blog</a></li>                
              </ul>
            </li>
            <li><a href="" class="firstLevel<?php if (basename($page_name) == 'documentation.tpl.php'):?> active<?php endif;?>">Documentation</a>
              <ul>
                <li><a href="https://cwiki.apache.org/confluence/display/OFBIZ/Documentation#Documentation-End-UserDocumentation" target="external" class="">User Documentation</a></li>
                <li><a href="https://cwiki.apache.org/confluence/display/OFBIZ/Technical+Documentation" target="external" class="">Technical Documentation</a></li>
                <li><a href="https://cwiki.apache.org/confluence/display/OFBIZ/Home" target="external" class="">Wiki</a></li>
                <li><a href="https://ci.apache.org/projects/ofbiz/site/javadocs/" target="external" class="last">API Reference</a></li>
              </ul>
            </li>
            <li><a href="" class="firstLevel<?php if (basename($page_name) == 'community.tpl.php'):?> active<?php endif;?>">Community</a>
              <ul>
                <li><a href="getting-involved.html">Getting Involved</a></li>                
                <li><a href="mailing-lists.html">Mailing Lists</a></li>
                <li><a href="source-repositories.html">Source Repository</a></li>
 		<li><a href="download.html">Downloads</a></li
                <li><a href="https://issues.apache.org/jira/browse/OFBIZ/?selectedTab=com.atlassian.jira.jira-projects-plugin:summary-panel" target="external" >Issue Tracker</a></li>
                <li><a href="faqs.html" class="last">FAQ</a></li>
              </ul>
            </li>
            <li><a href="ofbiz-demos.html" class="firstLevel<?php if (basename($page_name) == 'ofbiz-demos.tpl.php'):?> active<?php endif;?>">Demos</a></li>            
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</header>
<!-- header -->
