ExecutionContext and Security-Aware Artifacts Notes
---------------------------------------------------

2010-01-13: The main navigation is controlled by the new
security design. I created a new class - ContextUtil.java -
to hold utility methods. Those methods can be moved to
other components when the branch is merged into the trunk.

An interesting side-effect of the new security design is
its ability to expose security holes in the trunk. Log in
as admin, go to Party Manager and click on the Find button.
The exception that is thrown exposes a flaw in the
findparty.ftl file.

I added security audit capability. This was not in the
design document, but it was simple to implement and might be
useful. An artifact can be flagged as audited. Any denied
attempts to use the artifact will be logged.

---------------------------------------------------

2010-01-11: The ExecutionContext implementation is fairly complete.

The security-aware artifacts implementation is mostly complete
(the AuthorizationManager CRUD methods are not written and the
EntityListIterator is not security-aware), but its use
in the branch is still proof-of-concept. In other words, the
design is implemented and working, but very little of the project uses it.

Example: The screen renderer doesn't catch the security exceptions,
so when a user is denied access to an artifact they get the JSP error page.

---------------------------------------------------

2010-01-05: Artifact paths now support substitution ("?")
and wildcard ("*") path elements.
This solves an issue that was discussed during the design - how
to grant access to a particular artifact regardless of the
execution path. You can see examples of their use in
framework/security/data/SecurityData.xml and
framework/example/data/ExampleSecurityData.xml.

The Example component has been converted to the new
security design.

The Execution Context seems to fulfill all needs so far, and it
works pretty well, so its API could be considered stable at
this time.

---------------------------------------------------

2009-12-31: I put this text file in the branch as a means
of keeping anyone who is interested updated on the progress
of the branch.

This branch is an implementation of the Security-Aware Artifacts
design document -

http://cwiki.apache.org/confluence/display/OFBTECH/OFBiz+Security+Redesign

and it is a work in progress.

The ExecutionContext interface is
scattered across several components due to the cross-dependency
or circular-dependency issue. Cross-dependency is when Class
A references Class B, and Class B references Class A, and both
classes are in separate components. There is no way to get them
to compile. The problem is compounded in ExecutionContext because
it references 3 or 4 components.

The workaround I came up with was to have the lowest level methods
declared in the api component, then have each component extend
the interface and add their methods. It's not pretty, but it works.

This is where you can find the interfaces:

org.ofbiz.api.context.ExecutionContext
  org.ofbiz.entity.ExecutionContext
    org.ofbiz.service.ExecutionContext

When the cross-dependency issues are solved, all of the extended
interfaces will be consolidated into one.

The interface implementations can be found in the context component. 
  
The ultimate goal of ExecutionContext is to have all client code
get the contained objects from ExecutionContext only - instead of
getting them from the various classes now in use. This initial
implementation focuses more on the ExecutionContext's role as
a means of tracking the execution path - which is needed for the
security-aware artifacts.

The AuthorizationManager and AccessController interfaces are based
on the java.security.* classes, and they are intended to be
implementation-agnostic. OFBiz will have an implementation based
on the entity engine, but the goal is to be able to swap out that
implementation with another.

If you want to see the ExecutionContext and AccessController in
action, change the settings in api.properties. You will see info
messages in the console log.

I added a security-aware Freemarker transform. Template
sections can be controlled with:

<@ofbizSecurity permission="view" artifactId="thisTemplate">Some text</@ofbizSecurity>

If the user has permission to view the artifact, then "Some text"
will be rendered.

The Authorization Manager is mostly working. Filtering
EntityListIterator values is not implemented due to architectural
problems.

