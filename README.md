# behaim
Java Object Graph Explorer

Behaim uses Java reflection to explore an object graph up to a configurable recursion depth.

h1. Features
Extensible with custom visitors
Multi-threaded graph exploration
Re-use of meta data about an already explored object graph

h1. Provided Visitors
Builder: instantiate an object graph
Logger: log an object graph (pending)
Cloner: clone an object graph (pending

h1. Requirements
Java 5 or above
Maven 2.2.1 or above
Populator
The structure of the created objects is configurable and defaults to filling all object fields with random data.

h1. Use Cases
* Creation of a complex domain object hierarchy to test marshalling of all fields
* Simulation of production test load

h1. Default configuration
    Foo foo = Builder.make(Foo.class);
    Collection<Foo> foos = Builder.make(Foo.class, 100);

h1. Custom configuration
    Config config = new Config(...);
    Foo foo = Builder.make(Foo.class, config);
    Collection<Foo> foos = Builder.make(Foo.class, config, 100);

h1. Re-usable builder
When you need to make objects with the same configuration in several steps, use the instance make method.

    Builder builder = new Builder(Foo.class);
    Collection<Foo> foos1 = builder.make(100);
    Collection<Foo> foos2 = builder.make(50);

h1. FAQ
What does Behaim mean?
Martin Behaim (1459 – 1507) was a German cosmographer, astronomer, geographer and explorer. His Behaim Globe from 1492 is considered to be the oldest surviving terrestrial globe.
