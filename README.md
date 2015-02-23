# Java 8 Stream API Collectors for Guava Immutable Collections
[![Build Status](https://travis-ci.org/yanaga/guava-stream.svg?branch=master)](https://travis-ci.org/yanaga/guava-stream)

With Java 8 Stream API you can collect the elements of your ```Stream``` using one of the many methods of the ```java.util.Collectors``` class.

To collect the elements in a ```java.util.List``` you would do this:

```java
list.stream().collect(Collectors.toList());
```

But the ```List``` returned is a standard mutable JDK ```List```. Those used to [Guava](https://github.com/google/guava) that want to return an immutable collection like ```ImmutableList``` would have to do this:

```java
list.stream().collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
```

The **guava-stream** project provides ```java.util.stream.Collector``` implementations for Guava Immutable Collections, simplifying the code to just this:

```java
list.stream().collect(MoreCollectors.toImmutableList());
```

**guava-stream** is available on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22guava-stream%22).

You can add this dependency to your Maven project:

```xml
<dependency>
  <groupId>me.yanaga</groupId>
  <artifactId>guava-stream</artifactId>
  <version>1.0</version>
</dependency>
```

Or if you are using Gradle:

```groovy
compile "me.yanaga:guava-stream:1.0"
```

Currently we have ```Collector``` implementations for ```ImmutableList```, ```ImmutableSet```, ```ImmutableSortedSet```, ```ImmutableMap```, ```ImmutableSortedMap```, ```ImmutableBiMap```, ```ImmutableMultiset```, ```ImmutableSortedMultiset```, ```ImmutableMultimap```, ```ImmutableListMultimap```, ```ImmutableSetMultimap```, and ```ImmutableTable```.
