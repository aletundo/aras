Development
***********

The development environment is provided through Docker containers.
The ``docker-compose.yml`` file contains the common services definition between production and development.
The ``docker-compose.override.yml`` file holds the additional definitions for the development.

Subsequently, a ``docker-compose.prod.yml`` file will be added in order to provide 
production configurations for a Docker swarm mode deploy.

How to run
==========

Minimum requirements:

* Docker Engine 17.09.0+
* Docker Compose 1.8+

Since there are shared dependencies among services, it is strongly suggested (read it as *you have to*) 
start them in a specific sequence.

Execute the following steps: ::

  $ docker-compose build
  $ docker-compose up -d config-server kafka
  $ docker-compose up -d eureka-server
  $ docker-compose up -d notifications-service analyses-configurator-service analyses-executor-service projects-service reports-service gateway
