Pet Store

  How to run?
  -----------

  Please set up "PET_STORE_USERNAME" and "PET_STORE_PASSWORD" environment variables to run tests.
  You can run tests by running "./gradlew test" command.

  How to add new scenarios?
  ---------------------

  You can add new scenarios in the following way:
  1. Create new .feature file with scenarios or add new scenarios to the existing .feature file.
              Use Gherkin language to define behavior description.
  2. Add Step Definitions to the corresponding Class in com.petstore.autotests.steps package or create a new one in case of adding new .feature file.
       Use com.petstore.autotests.model package if you need to create new entities.
 