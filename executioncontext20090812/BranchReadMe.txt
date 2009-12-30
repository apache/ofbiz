ExecutionContext and Security-Aware Artifacts Notes
---------------------------------------------------

2009-08-26: I put this text file in the branch as a means
of keeping anyone who is interested updated on the progress
of the branch.

This branch is an implementation of the Security-Aware Artifacts
design document -

http://docs.ofbiz.org/display/OFBTECH/OFBiz+Security+Redesign

and it is a work in progress.

The ExecutionContext and AuthorizationManager interfaces are
scattered across several components due to the cross-dependency
or circular-dependency issue. Cross-dependency is when Class
A references Class B, and Class B references Class A, and both
classes are in separate components. There is no way to get them
to compile. The problem is compounded in ExecutionContext and
AuthorizationManager because they reference 3 or 4 components.
The branch David created attempts to solve this problem, but
it is not finished.

The workaround I came up with was to have the lowest level methods
declared in the api component, then have each component extend
the interfaces and add their methods. It's not pretty, but it works.

This is where you can find the interfaces:

org.ofbiz.api.authorization.AuthorizationManager
  org.ofbiz.security.AuthorizationManager

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
action, change the settings in api.properties. You'll see info
messages in the console log.

---------------------------------------------------------------------

2009-08-26: Added security-aware Freemarker transform. Template
sections can be controlled with:

<@ofbizSecurity permission="view" artifactId="thisTemplate">Some text</@ofbizSecurity>

If the user has permission to view the artifact, then "Some text"
will be rendered.

---------------------------------------------------------------------

2009-08-28: Permissions checking has been implemented. The code has
a few bugs, and there are places where the ExecutionContext isn't being
passed along, so OFBiz won't run with the AuthorizationManager enabled.
Consequently, the AuthorizationManager is disabled by default. When it
is disabled, it still "pretends" to check permissions, but it always
grants access. You can enable it with a property in api.properties.

When a user first logs in, all of their permissions are gathered from the
security entities and are used to assemble a tree-like Java structure.
The structure is cached. When an artifact requests the user's permissions,
a permission object (OFBizPermission) uses the supplied artifact ID to traverse
the tree, accumulating permissions along the way. This is how permission
inheritance is achieved. The permission object is then queried if the user
has the requested permission and the result is returned to the artifact.

---------------------------------------------------------------------

2009-12-23: This branch will not be synchronized with the trunk from now on.
I tried to do a merge from the trunk and there were too many conflicts to
resolve. When the time comes to implement the security-aware artifacts in
the trunk, the handful of affected classes can be ported over manually.

---------------------------------------------------------------------

2009-12-28: Major rewrite. I created a utility class of static methods
(ThreadContext) to make using the ExecutionContext easier. Instead of
trying to pass an ExecutionContext instance throughout the framework,
you can use the ThreadContext static methods instead. The ThreadContext
class keeps an ExecutionContext instance per thread. We just need to
make sure all OFBiz entrance vectors call the reset() method, and
then initialize the ExecutionContext to the desired values.

---------------------------------------------------------------------

2009-12-29: The Authorization Manager is mostly working. Filtering
EntityListIterator values is not implemented due to architectural
problems. The Authorization Manager is still disabled by default
because the demo data load will not work with it enabled.
