# Dev Local

Run the Keycloak service in a development local environment, which is a setup that allows developers to run and test their applications in a simpler and more flexible environment. This is not suitable for production use, but it can be useful for development and testing purposes.

## Features

The development local environment provides the following features:
* Easy setup and configuration for local development.
* Ability to run Keycloak in a local environment without the need for a full production setup.
* Configuration options that allow developers to customize the environment to their needs.

The environment also provides a Postgres database, as well, which can be used by other services in the development environment.

The environment also provides a Docker network available to services in other Docker Compose files, allowing developers to easily connect their applications to the Keycloak service and the Postgres database.

## To Run

It's docker! To start from the project root, run the following command:

```shell
docker compose -f dev-local/docker-compose.yml up -d
```

To stop:

```shell
docker compose -f dev-local/docker-compose.yml down
```

Add a `-v` flag to the `down` command to remove volumes, if desired. **WARNING!** This will remove the Postgres database and any data stored in it.

## Using The Default Configuration