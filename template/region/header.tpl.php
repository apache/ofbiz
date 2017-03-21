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
                <li><a href="https://twitter.com/apacheofbiz" target="_blank">Twitter</a></li>
                <li><a href="https://blogs.apache.org/ofbiz/" target="_blank">Blog</a></li>
                <li><a href="user-stories.html">User Stories and References</a></li>
                <li><a href="" class="last">Press</a></li>
              </ul>
            </li>
            <li><a href="" class="firstLevel<?php if (basename($page_name) == 'documentation.tpl.php'):?> active<?php endif;?>">Documentation</a>
              <ul>
                <li><a href="" class="">User Documentation</a></li>
                <li><a href="https://cwiki.apache.org/confluence/display/OFBIZ/OFBiz+Technical+Documentation+-+Home+Page" target="_blank" class="">Technical Documentation</a></li>
                <li><a href="https://cwiki.apache.org/confluence/display/OFBIZ/Home" target="_blank" class="">Wiki</a></li>
                <li><a href="https://ci.apache.org/projects/ofbiz/site/javadocs/" target="_blank" class="last">API Reference</a></li>
              </ul>
            </li>
            <li><a href="" class="firstLevel<?php if (basename($page_name) == 'community.tpl.php'):?> active<?php endif;?>">Community</a>
              <ul>
                <li><a href="getting-involved.html">Getting Involved</a></li>
                <li><a href="https://cwiki.apache.org/confluence/display/OFBIZ/Events" target="_blank">Meetups / Events</a></li>
                <li><a href="mailinglists.html">Mailing Lists</a></li>
                <li><a href="">Source Repository</a></li>
                <li><a href="https://issues.apache.org/jira/browse/OFBIZ/?selectedTab=com.atlassian.jira.jira-projects-plugin:summary-panel" target="_blank" >Issue Tracker</a></li>
                <li><a href="service-providers.html">Service Providers</a></li>
                <li><a href="faqs.html" class="last">FAQ</a></li>
              </ul>
            </li>
            <li><a href="" class="firstLevel<?php if (basename($page_name) == 'users.tpl.php'):?> active<?php endif;?>">Users</a>
              <ul>
                <li><a href="our-users.html">e-Commerce / e-business</a></li>
                <li><a href="our-users.html">Manufacturing</a></li>
                <li><a href="our-users.html">Retail</a></li>
                <li><a href="our-users.html">Distribution</a></li>
                <li><a href="our-users.html">Service Industries</a></li>
                <li><a href="our-users.html" class="last">Other</a></li>
              </ul>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</header>
<!-- header -->
