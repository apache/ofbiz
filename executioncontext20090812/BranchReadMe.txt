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

2009-08-26: Added security-aware Freemarker transform. Template
sections can be controlled with:

<@ofbizSecurity permission="view" artifactId="thisTemplate">Some text</@ofbizSecurity>

If the user has permission to view the artifact, then "Some text"
will be rendered.
