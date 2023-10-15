/*
 * C4 model at the system and container levels for the To-Do Microservice,
 * using the Structurizr DSL from here:
 *
 *   https://docs.structurizr.com/dsl/
 *
 * The Compose file in this repository includes a container that runs the
 * Structurizr Lite web application. Use "./compose-it.sh up -d" to bring
 * up the application, and open this webpage:
 *
 *   http://localhost:8081/
 */
workspace "To-Do" "Example to-do list system" {

    model {
      u = person "User"
      admin = person "Administrator"
      s = softwareSystem "Software System" {
          cli = container "To-Do CLI Client"
          micronaut = container "To-Do Microservice"
          database = container "To-Do Databsae" "" "MariaDB" "database"

          kafka = container "Kafka Cluster"
          kafkaui = container "Kafka-UI Webapp" "" "" webapp
          adminer = container "Adminer Webapp" "" "" webapp
      }

      u -> cli "Uses"
      admin -> kafkaui "Manages"
      admin -> adminer "Uses"
      cli -> micronaut "Interacts with HTTP API"
      micronaut -> database "Reads from and writes to"
      micronaut -> kafka "Consumes and produces events"
      kafkaui -> kafka "Manages"
      adminer -> database "Manages"
    }
    
    views {
        theme default
        systemContext s {
            include *
            autolayout lr
        }
        container s {
            include *
        }
        styles {
            element "database" {
              shape Cylinder
            }
            element "webapp" {
              shape WebBrowser
            }
        }
    }

}