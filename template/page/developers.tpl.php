<?php //Variable declarations for region templates
  $head_title = '<title>The Apache OFBiz&reg; Project</title>';
?>
   
  <!-- page content -->
 <section id="content" class="fullWidth">
    <header class="headerPage">
      <div class="container clearfix">
        <div class="row">
          <h1 class="span8">Getting Started - Developers</h1>
          <div class="span4" id="navTrail"> <a href="index.html" class="homeLink">home</a><span>/</span><a href="template-columns.html">Getting Started</a><span>/</span><span class="current">Developers</span> </div>
        </div>
      </div>
    </header>
  <section id="content" class="features" >
    <div class="slice clearfix">
      <div class="container">
        <div class="row ">
          <aside class="span2 PortfolioStickyMenu" id="sidebar">
            <ul  id="subnav" class="nav nav-stacked sidenav scrollspyNav">
              <li> <a href="#DevPreq"> Pre-Requisites </a> </li>
              <li> <a href="#DevDownld"> Download </a> </li>
              <li> <a href="#DevBldRun"> Build and Run </a> </li>
	      <li> <a href="#DevRepo"> Browse Repository </a> </li>
 	      <li> <a href="#DevTutorial"> Tutorial </a> </li>
 	      <li> <a href="#DevDocs"> Documentation and Help </a> </li>
	      <li> <a href="#DevDemo"> Demo </a> </li>
            </ul>
          </aside>
          <div class="span10">
            <section  id="DevPreq" class="slice row clearfix">
              <div class="span10">
                <h2>Pre-Requisites</h2>
                <div class="divider"><span></span></div>
 		<ul class="iconsList">                  
		  <li><i class="icon-pin"></i> For 14.12, 15.12, 16.11 and the ofbiz-framework trunk the minimum requirement you need installed is Java 1.8 SDK.</li>
                  <li><i class="icon-pin"></i> For 13.07 the minimum requirement you need installed is Java 1.7 SDK.</li> 		
                </ul>
               </div>
            </section>
 	    <section  id="DevDownld" class="slice row clearfix">
              <div class="span10">
                <h2>Download</h2>
                <div class="divider"><span></span></div>
                <p>There are two ways to download the OFBiz source code</p>
		<h3>Using a Download Mirror</h3>
		<p>Download your required version from one of our <a href="download.html">download mirrors</a> </p>
		<h3>Checkout the Source Code</h3>
		<p>Checkout the source code from the repository</p>
		<p>Anyone can checkout or browse the source code in the OFBiz public Subversion (SVN) repository.</p>
		<p>To checkout the source code, simply use the following command (if you are using a GUI client, configure it appropriately).</p>
		<ul class="iconsList">
          <li><i class="icon-pin"></i> <strong>ofbiz-framework trunk</strong> : $ svn co http://svn.apache.org/repos/asf/ofbiz/ofbiz-framework/trunk ofbiz-framework.</li>
		  <li><i class="icon-pin"></i> <strong>ofbiz-plugins trunk</strong> : $ svn co http://svn.apache.org/repos/asf/ofbiz/ofbiz-plugins/trunk ofbiz-framework.</li>
		  <li><i class="icon-pin"></i> <strong>branch release16.11 (stable)</strong>: $ svn co http://svn.apache.org/repos/asf/ofbiz/branches/release16.11 ofbiz.16.11</li>
        </ul>
		</div>
            </section>

	    <section  id="DevBldRun" class="slice row clearfix">
              <div class="span10">
                <h2>Build and Run</h2>
                <div class="divider"><span></span></div>
                <p>Once you have downloaded the source code it will need to be built. The command to built.</p>
		<h3>Building and Running 16.11 or the ofbiz-framework trunk</h3>
		<p>Release 16.11  and our ofbiz-framework trunk uses Gradle as it's build system so the command to build and run it are different to the previous releases. </p>
		<p>To build the trunk, navigate to the trunk directory and run the following command</p>
		<p><strong>./gradlew ofbiz loadDefault</strong></p>
		<p></p>
		<p>To start OFBiz running locally, navigate to the trunk directory and type the following command</p> 
		<p></p>
		<p><strong>./gradlew 'ofbiz -start'</strong></p>
		<p> NOTE: The use of quotation marks in the command. For further details and a full list of all available commands, please take a look at the readme.md file</p>
		<p>To log into OFBiz, navigate with your browser to </p>
		<p>http://localhost:8080/accounting</p>
		<p>and login with username <strong>"admin" </strong>and password <strong>"ofbiz"</strong></p>
		<h3>Building and Running the OFBiz branch releases 15.12 or earlier</h3>
 		<p>Our branch releases for 15.12 or below use Apache Ant as their build system so the command to build and run it is different to the trunk (which uses Gradle). </p>
		<p>To build the branch release, navigate to the release directory and run the following command</p>
		<p><strong>./ant load-demo start</strong></p>
		<p></p>
		<p>NOTE: This will build OFBiz and start it running.</p> 
		<p>To log into OFBiz, navigate with your browser to </p>
		<p>http://localhost:8080/accounting</p>
		<p>and login with username <strong>"admin"</strong> and password <strong>"ofbiz"</strong></p>
            </div>
            </section>
            
            <section  id="DevRepo" class="slice row clearfix">
              <div class="span10">
                <h2>Browse Repository</h2>
                <div class="divider"><span></span></div>
                <p>You can browse the repository using any of the following links.</p>
                <ul class="iconsList">
                  <li><i class="icon-pin"></i> <a href="https://fisheye6.atlassian.com/browse/ofbiz/branches"><strong>SVN - FishEye</strong></a></li>
  		  <li><i class="icon-pin"></i> <a href="http://svn.apache.org/repos/asf/ofbiz/"><strong>SVN - WebDAV</strong></a></li>
  		  <li><i class="icon-pin"></i> <a href="http://svn.apache.org/viewvc/ofbiz/"><strong>SVN - ViewVC</strong></a></li>
		</ul>		
		<p></p>		
            </div>
            </section>

	    <section  id="DevTutorial" class="slice row clearfix">
              <div class="span10">
                <h2>Development Tutorial</h2>
                <div class="divider"><span></span></div>
                <p>To help you getting started we have put together a beginners OFBiz development tutorial to get familiar with with OFBiz.</p>
		<p>It covers the fundamentals of the OFBiz application development process. The goal of this tutorial is to acquaint a developer with best practices, coding conventions. the control flow and things that the developer needs to know in order to modify OFBiz</p>
		<ul class="iconsList">
                  <li><i class="icon-pin"></i> <a href="https://cwiki.apache.org/confluence/display/OFBIZ/OFBiz+Tutorial+-+A+Beginners+Development+Guide"><strong>Developer Tutorial</strong></a></li>
		</ul>
            </div>
            </section>
	    <section  id="DevDocs" class="slice row clearfix">
              <div class="span10">
                <h2>Documentation and Help</h2>
                <div class="divider"><span></span></div>
                <p>We have a range of technical documenentation and help for developers. Please see the links below.</p>
		<ul class="iconsList">
                  <li><i class="icon-pin"></i> <a href="https://cwiki.apache.org/confluence/display/OFBIZ/Technical+Documentation"><strong>OFBiz Technical Documentation</strong></a></li>
  		  <li><i class="icon-pin"></i> <a href="https://ci.apache.org/projects/ofbiz/site/javadocs/"><strong>OFBiz API Reference</strong></a></li>
  		  <li><i class="icon-pin"></i> <a href="https://cwiki.apache.org/confluence/display/OFBIZ/FAQ+-+Tips+-+Tricks+-+Cookbook+-+HowTo"><strong>Developer and Technical FAQs</strong></a></li>
		</ul>
		<p>After consulting the documentation you still have questions,please feel free to post questions on our development mailing list.</p>
            </div>
            </section>

	    <section  id="DevDemo" class="slice row clearfix">
              <div class="span10">
                <h2>Demo</h2>
                <div class="divider"><span></span></div>
                <p>Try out our OFBiz demo - <a href="ofbiz-demos.html"><strong>OFBiz Demos</strong></a></p>
            </div>
            </section>
          </div>
        </div>
      </div>
    </div>
   </section>
 </section>
  <!-- page content -->
 
