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
      s = softwareSystem "To-Do System" {
          cli = container "To-Do CLI Client"

          micronaut = container "To-Do Microservice" {
            domain = component "Domain objects and DTOs"
            services = component "Services"
            repos = component "Repositories"
            events = component "Kafka consumers and producers"
            currency_gateway = component "Currency API Gateway"
            resources = component "Resources"
          }

          database = container "To-Do Database" "" "MariaDB" "database"
          kafka = container "Kafka Cluster"
          kafkaui = container "Kafka-UI Webapp" "" "" webapp
          adminer = container "Adminer Webapp" "" "" webapp
      }
      currency = softwareSystem "Currency API" {
        tags external
      }

      s -> currency "Invokes API"

      u -> cli "Uses"
      admin -> kafkaui "Manages"
      admin -> adminer "Uses"

      cli -> micronaut "Interacts with HTTP API"

      micronaut -> database "Reads from and writes to"
      micronaut -> kafka "Consumes and produces events"
      micronaut -> currency "Invokes API"

      kafkaui -> kafka "Manages"
      adminer -> database "Manages"

      repos -> domain "Creates and updates"
      repos -> database "Queries and writes to"
      services -> domain "Runs business workflows on"
      services -> repos "Uses"
      resources -> repos "Uses"
      resources -> events "Uses"
      resources -> currency_gateway "Uses"
      resources -> services "Uses"
      resources -> domain "Reads and updates"
      currency_gateway -> currency "Invokes"
      cli -> resources "Invokes"
      events -> kafka "Consumes and produces events in"
    }

    views {
        theme default
        systemContext s {
            include *
        }
        container s {
            include *
        }
        component micronaut {
            include *
        }
        styles {
            element "database" {
              shape Cylinder
            }
            element "webapp" {
              shape WebBrowser
            }
            element external {
              background gray
            }
        }
    }

}