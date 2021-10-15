# Changelog

## [2.0.0 build 426] (2020-05-10)

*Compatible with JMS Extender since version 2.0.0*
*Compatible with code developed with the previous version*

**Improvements**

- Renamed the library as ls-jms-java-hook-api and made it available on a Maven repository at [this address](https://www.lightstreamer.com/repo/maven).
Moreover, the SDK has been removed from the Lightstreamer JMS Extender distribution package. See the SDK.TXT file in the package root for more details. 
- Added meta-information on method argument names for interface classes, so that developer GUIs can take advantage of them.
- Increased minimum required Java version to 8.
- Revised javadoc formatting style.

## [1.3 build 100] (2016-02-25)

*Compatible with JMS Extender since version 1.6*
*Compatible with code developed with the previous version*

**Improvements**

- Extended the hook interface to provide callbacks for object message payload deserialization.

## [1.2 build 93] (2015-11-03)

*Compatible with JMS Extender since version 1.5*
*Not compatible with custom hooks developed with the previous version; see compatibility notes below*

**Improvements**

- Product rebranded as JMS Extender. As a consequence, changed the package name of the classes in ls-jms-hook-interface.jar.
<b>COMPATIBILITY NOTE: Existing hooks must be ported, by just changing the "import" statements and recompiling.</b>

## [1.1.1 build 54] (2014-11-07)

*Compatible with JMS Gateway Adapter since 1.1.1*
*Compatible with code developed with the previous version*

**Improvements**

- Split into separate adapter and hook interface jar files.
- Added support for error code in Hook exceptions
