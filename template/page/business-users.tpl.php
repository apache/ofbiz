<?php //Variable declarations for region templates
  $head_title = '<title>The Apache OFBiz&reg; Project</title>';
?>
   
  <!-- page content -->
 <section id="content" class="fullWidth">
    <header class="headerPage">
      <div class="container clearfix">
        <div class="row">
          <h1 class="span8">Getting Started - Business Users</h1>
          <div class="span4" id="navTrail"> <a href="index.html" class="homeLink">home</a><span>/</span><a href="">Getting Started</a><span>/</span><span class="current">Business Users</span> </div>
        </div>
      </div>
    </header>
  <section id="content" class="features" >
    <div class="slice clearfix">
      <div class="container">
        <div class="row ">
          <aside class="span2 PortfolioStickyMenu" id="sidebar">
            <ul  id="subnav" class="nav nav-stacked sidenav scrollspyNav">
              <li> <a href="#UsrStart"> Where to Start? </a> </li>
              <li> <a href="#UsrDemo"> Online Demos </a> </li>
              <li> <a href="#UsrModules"> Modules and Features </a> </li>
 	      <li> <a href="#UsrInstall"> Download and Install </a> </li>
 	      <li> <a href="#UsrDocs"> Documentation and Help </a> </li>
	     </ul>
          </aside>
          <div class="span10">
            <section  id="UsrStart" class="slice row clearfix">
              <div class="span10">
                <h2>Where to Start?</h2>
                <div class="divider"><span></span></div>
		<p>Getting started with OFBiz is easy and you also have the choice about where you want to start. We have a range of information about OFBiz modules and features as well as an online demo that you can try. After taking a look you want to download and install OFBiz too, then you'll find details about how to do this later in this page.</p>
		<ul class="iconsList">
                  <li><i class="icon-pin"></i> To see what OFBiz looks like and how it works, please try our <a href="ofbiz-demos.html"><strong>Online Demos</strong></a></li>
  		  <li><i class="icon-pin"></i> To get details about what modules and features Apache OFBiz has, please take a look at <a href="#UsrModules"><strong>Modules and Features</strong></a> </li>
  		  <li><i class="icon-pin"></i> To download your own version of Apache OFBiz, please take a look at <a href="#UsrInstall"><strong>Download and Install OFBiz</strong></a> </li>
		</ul>
                <p>If you are still unsure or have any questions then please feel free to use our <a href="https://lists.apache.org/list.html?user@ofbiz.apache.org" target="_blank"><strong>User Mailing List</strong></a> </p>
		</div>
            </section>
 	    <section  id="UsrDemo" class="slice row clearfix">
              <div class="span10">
                <h2>Online Demos</h2>
                <div class="divider"><span></span></div>
                <p>If you would like try out OFBiz, then we have a range of demos that you can login to to see what OFBiz looks like and also to test out any of the modules. Our demos include <strong> demo data </strong> that will help you understand how to use specific modules.</p>
		<p>We have two demos,
		<ul class="iconsList">
                  <li><i class="icon-pin"></i> one demo for the backend ERP applications and;</li>
  		  <li><i class="icon-pin"></i> one demo for the e-commerce store </li>  		 
		</ul>

<p>Please take a look at <a href="ofbiz-demos.html"><strong>Online Demos</strong> </a> page for details about how to login and access the system.</p>
 	     </div>
            </section>


 	   <section  id="UsrModules" class="slice row clearfix">
              <div class="span10">
                <h2>Modules and Features</h2>
  		 <div class="divider"><span></span></div>
     		 <div class="tabbable tabs-left">
                    <ul class="nav nav-tabs " id="modulesTabV">
                      <li class="active"> <a href="#tabs-v-1" data-toggle="tab"> Accounting </a> </li>
                      <li> <a href="#tabs-v-3" data-toggle="tab"> Manufacturing </a> </li>
		      <li> <a href="#tabs-v-4" data-toggle="tab"> Human Resources </a> </li>
		      <li> <a href="#tabs-v-5" data-toggle="tab"> Inventory Management </a> </li>		     
                      <li> <a href="#tabs-v-7" data-toggle="tab"> Catalog Management </a> </li>
                      <li> <a href="#tabs-v-9" data-toggle="tab"> CRM & Order Management </a> </li>
     		      <li> <a href="#tabs-v-10" data-toggle="tab"> e-Commerce / e-Shop </a> </li>


                    </ul>
                    <div class="tab-content">
                      <div class="tab-pane active" id="tabs-v-1">                     
			<ul class="iconsList">
                  	 <li><i class="icon-pin"></i> Standard Double entry General Ledger </li>
  		  	 <li><i class="icon-pin"></i> Supports multiple organisations, account hierarchies and segmentation</li>
  		 	 <li><i class="icon-pin"></i> Accounts Receivable (AR), Accounts Payable (AP), Invoices, Payments, Statements and Aging </li>
			 <li><i class="icon-pin"></i> Agreement contracts and Credit management</li>
			 <li><i class="icon-pin"></i> Asset Management including Depreciation</li>
			 <li><i class="icon-pin"></i> Budgeting Management</li>
			 <li><i class="icon-pin"></i> Support for payment gateways and payment processing</li>
			 <li><i class="icon-pin"></i> Financial Reporting </li>
  		  	 <li><i class="icon-pin"></i> Fully integrated with Order Management, Inventory, Purchasing and Manufacturing out of the box</li>
 		       </ul>

                      </div>
                      

                      <div class="tab-pane" id="tabs-v-3">
			<ul class="iconsList">
                  	 <li><i class="icon-pin"></i> Bill of Materials </li>
  		  	 <li><i class="icon-pin"></i> Jobshop, Manufacturing Routings and Tasks</li>
  		 	 <li><i class="icon-pin"></i> Production Planning and MRP </li>
			 <li><i class="icon-pin"></i> Production and Job Costing</li>
			 <li><i class="icon-pin"></i> Equipment Billing</li>
			 <li><i class="icon-pin"></i> Raw Material Procurement</li>
			 <li><i class="icon-pin"></i> Manufacturing Reporting</li>
			</ul>
                       </div>

		      <div class="tab-pane" id="tabs-v-4">
			<ul class="iconsList">
                  	 <li><i class="icon-pin"></i> Company and Department Structure </li>
  		  	 <li><i class="icon-pin"></i> Manage Job Positions, Skills and Performance Reviews</li>
  		 	 <li><i class="icon-pin"></i> Manage Recruitment Process,  Applications, Interviews </li>
			 <li><i class="icon-pin"></i> Salaries and Payments</li>
			 <li><i class="icon-pin"></i> Employment Contracts</li>
			 <li><i class="icon-pin"></i> Employee Expenses</li>
			 <li><i class="icon-pin"></i> Training</li>
			</ul>
                       </div>

  		      <div class="tab-pane" id="tabs-v-5">
 
			<ul class="iconsList">
                  	 <li><i class="icon-pin"></i> Manage and setup single, multiple warehouses </li>
  		  	 <li><i class="icon-pin"></i> Inventory Locations </li>
  		 	 <li><i class="icon-pin"></i> Serialized on non serialized Inventory </li>
			 <li><i class="icon-pin"></i> Lot Management</li>
			 <li><i class="icon-pin"></i> Shipment Integration</li>
			 <li><i class="icon-pin"></i> Picklist and Package Management</li>
			 <li><i class="icon-pin"></i> Receiving </li>
 			 <li><i class="icon-pin"></i> Returns </li>
			</ul>
                       </div>


		      <div class="tab-pane" id="tabs-v-7">                     
 		        <ul class="iconsList">
                  	 <li><i class="icon-pin"></i> Support unlimited stores, catalogs, categories, and products</li>
  		  	 <li><i class="icon-pin"></i> Handles a range of products (physical, digital, downloadable products, variant, configurable)</li>
 			 <li><i class="icon-pin"></i> Gift Certificates and gift cards</li>
  		 	 <li><i class="icon-pin"></i> Price rules for customer or group-specific pricing </li>
			 <li><i class="icon-pin"></i> Online store promotion engine</li>
			 <li><i class="icon-pin"></i> Integration with major payment gateway providers</li>
			 <li><i class="icon-pin"></i> Fully integrated online and Point of Sales (POS) stores out-of-the-box</li>
			 <li><i class="icon-pin"></i> Keyword search capability in all the applications using hibernate search</li>
			</ul>    
                       </div>

		    
		      <div class="tab-pane" id="tabs-v-9">
                        <ul class="iconsList">
                  	 <li><i class="icon-pin"></i> Lead and Sales Opportunity Management </li>
  		  	 <li><i class="icon-pin"></i> Sales ForecastsManage sales opportunities</li>
  		 	 <li><i class="icon-pin"></i> Shared Sales Team Documents, Calendar and Tasks </li>
			 <li><i class="icon-pin"></i> Email Integration</li>
			 <li><i class="icon-pin"></i> Customer Service and Case Managment</li>
			 <li><i class="icon-pin"></i> Quotes, Order Entry and Order Management</li>
			 <li><i class="icon-pin"></i> Manage marketing campaign including tracking code reporting</li>
			 <li><i class="icon-pin"></i> Address lookup ??? </li>
  		  	 <li><i class="icon-pin"></i> Integration with Asterisk ???</li>
 		       </ul>
                      </div>

 		      <div class="tab-pane" id="tabs-v-10">
                        <ul class="iconsList">
                  	 <li><i class="icon-pin"></i> Unlimited stores, catalogs, categories and products </li>
  		  	 <li><i class="icon-pin"></i> Cross-sell and upsell products</li>
  		 	 <li><i class="icon-pin"></i> Supports physical, digital, downloadable, variant and configurable products </li>
			 <li><i class="icon-pin"></i> Gift Certificates and Gift Cards</li>
			 <li><i class="icon-pin"></i> Pricing rules and Discounts</li>
			 <li><i class="icon-pin"></i> Online store promotion engine</li>
			 <li><i class="icon-pin"></i> Integrations with payment gateways</li>
			 <li><i class="icon-pin"></i> Product searching </li>
 			 <li><i class="icon-pin"></i> Customer portal </li>
  		  	</ul>
                      </div>

                    </div>
                  </div>
            </section>




	    <section  id="UsrInstall" class="slice row clearfix">
              <div class="span10">
                <h2>Download and Install</h2>
                <div class="divider"><span></span></div>

		<h3>Downloading OFBiz</h3>
		<p>You can download Apache OFBiz from one of our <a href="download.html">download mirrors</a>. Once downloaded, extract the file to create the OFBiz directory. </p>
		<p><strong>NOTE</strong>: Please make sure that you use our latest stable release as this version has been verified by the project as suitable for our users.</p>
		<h3>Build and Running OFBiz</h3>
                <div class="divider"><span></span></div>
               	<p>Once you have downloaded OFBiz it needs to be built before you can run it. To build OFBiz and start it running, you will need to:</p>
		<ul class="iconsList">
		    <li><i class="icon-pin"></i>open a command line window and;</li>
		    <li><i class="icon-pin"></i> navigate to the OFBiz directory then:</li>
		</ul>
		<p>Run the following command for Unix-like OS</p>
		<code>./gradlew ofbiz loadDefault</code>
		<p></p>
   		<p>Run the following command for Windows</p>
		<code>gradlew ofbiz loadDefault</code>
		<p></p>
		<p><strong>NOTE</strong>: It is completely normal for the command line window to remain active while OFBiz is running.</p>	
		<p>To log into OFBiz, navigate with your browser to </p>
		<p><code>https://localhost:8443/accounting</code></p>
		<p>and login with username <strong>"admin" </strong>and password <strong>"ofbiz"</strong></p>		
		<p>NOTE: For further details and a full list of all available commands, please take a look at the readme.md file</p>		
		</div>
            </section>
            
	    <section  id="UsrDocs" class="slice row clearfix">
              <div class="span10">
                <h2>Documentation and Help</h2>
                <div class="divider"><span></span></div>
                <p>Add link to User Documentation.</p>
		<p><a href="https://cwiki.apache.org/confluence/display/OFBIZ/Home" target="_blank"><strong>OFBiz Wiki</strong></a></p>
		<p><a href="faqs.html" target="_blank"><strong>OFBiz Frequently Asksed Questions (FAQs)</strong></a></p>
		<p><a href="" target="_blank"><strong>OFBiz End User Guide</strong></a></p>		
            </div>
            </section>

	   
          </div>
        </div>
      </div>
    </div>
   </section>
 </section>
  <!-- page content -->
 
