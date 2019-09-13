## SILVER BARS MARKETPLACE

#### Assumptions

* BUY orders are shown before SELL orders in the summary list
* Summary for each order has to include the type in it (e.g. 'SELL: 4.6 kg for £77.2')
* Although is not explicit in the specs, I've assumed that elements with same price but different types won't be grouped.
* Canceling a non-existing order will raise an exception
* Registering an order with missing or invalid fields will raise an exception
* Because no explicit requirements has been defined for the output format, it's been decided to use an object to encapsulate
the result, hiding implementation details. Although any method could be added to this object to represent it in the desired format, 
the toString method prints the result in the way shown in the specs.


#### Run tests

Just run JUnit 5 tests using Gradle wrapper:

```bash
./gradlew clean test
```

#### Use as library

1. Run `./gradlew clean build`
1. Import jar located in `build/libs/silver-bars-1.0-SNAPSHOT-all.jar` to your project's dependencies.

Alternatively we could've published the dependency to an artefact repository.


