# The Lightstreamer JMS Extender Hook Java API

The Lightstreamer JMS Extender Hook API for Java is a custom pluggable component which provides a powerful extension mechanism to integrate your own authentication and authorization functionalities into the [Lightstreamer JMS Extender](https://lightstreamer.com/download/#jms210) server.

The Lightstreamer JMS Extender Hook Java API lets you easily develop any Hook, which will be then packaged and deployed into theLightsreamer JMS Extender installation. Once loaded inside the running server process, it will be able to intercept specific events originated from the client side in order to apply fine-grained custom authorization checks as per your own security needs.

# Using the API

The library is deployed on a Maven repository.
To include the library in a custom project, using any maven-compatible build tool (e.g. Maven, Gradle, Ivy, etc.) it is necessary to proper configure the dependency to the library.

### Maven

Add the following dependency to the pom.xml file:

```xml
  <dependency>
    <groupId>com.lightstreamer</groupId>
    <artifactId>ls-jms-hook-java-api</artifactId>
    <version>2.0.0</version>
  </dependency>
```        

### Gradle

Update the dependencies section of the build.gradle file with the following declaration:

```xml
dependencies {
    compile 'com.lightstreamer:ls-jms-hook-java-api:2.0.0'
}
```    

### Ivy example

Add the following dependency to the ivy.yml file:

```xml
<dependency org="com.lightstreamer" name="ls-jms-hook-java-api" rev="2.0.0"/>
```

# External Links

- [Maven Repository](https://mvnrepository.com/artifact/com.lightstreamer/ls-jms-hook-java-api)

- [Examples](https://demos.lightstreamer.com/?p=jmsextender&t=hook&a=javahook)

- [API Reference](https://www.lightstreamer.com/api/ls-jms-hook-java-api/2.0.0/index.html)

# Compatibility Notes

Compatible with Lightstreamer JMS Extender Adapter since version 2.0.0