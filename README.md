# ARAS (ARcan As a Service)
[![Documentation Status](https://readthedocs.org/projects/aras/badge/?version=latest)](http://aras.readthedocs.io/en/latest/?badge=latest)
[![Build Status](https://travis-ci.org/aletundo/aras.svg?branch=master)](https://travis-ci.org/aletundo/aras)

**SERVICES QUALITY**:
* _**projects-service**_: [![project-service Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=it.unimib.disco.aras%3Aprojects-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=it.unimib.disco.aras%3Aprojects-service)
[![project-service Maintainability](https://sonarcloud.io/api/project_badges/measure?project=it.unimib.disco.aras%3Aprojects-service&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=it.unimib.disco.aras%3Aprojects-service)
[![project-service Coverage](https://sonarcloud.io/api/project_badges/measure?project=it.unimib.disco.aras%3Aprojects-service&metric=coverage)](https://sonarcloud.io/dashboard?id=it.unimib.disco.aras%3Aprojects-service)
* _**reports-service**_: [![reports-service Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=it.unimib.disco.aras%3Areports-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=it.unimib.disco.aras%3Areportts-service)
[![reports-service Maintainability](https://sonarcloud.io/api/project_badges/measure?project=it.unimib.disco.aras%3Areports-service&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=it.unimib.disco.aras%3Areports-service)
[![reports-service Coverage](https://sonarcloud.io/api/project_badges/measure?project=it.unimib.disco.aras%3Areports-service&metric=coverage)](https://sonarcloud.io/dashboard?id=it.unimib.disco.aras%3Areports-service)
## Synopsis
A lot of software analysis tools are available to monitor the various aspects of a system. One interesting facet is the design quality. A good design leads to evolvability, maintainability of the system, its availability, its security and cost reduction. When there is a lack of design, or the system is made up by poor design choices, the architecture of the system could be subjected to architectural anomalies: ARAS offers a service to run static analyses on software and inspect the retrieved results, to support developers and mainteiners during the quest for those anomalies.
In particular, ARAS aim is to expose the existing software Arcan as a web service. Arcan, which is a static analysis tool, analyses compiled Java projects in order to detect architectural flaws, in particular the ones called Architectural Smells (AS). At the moment Arcan offers a desktop user interface and runs locally. ARAS will not extend Arcan domain features, but it will use it as a black-box.

## Versioning
We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/aletundo/aras/tags).

## Authors
* **Alessandro Tundo** - [aletundo](https://github.com/aletundo)
* **Matteo Vaghi** - [oet93](https://github.com/oet93)
* **Ilaria Pigazzini** - [ipiga94](https://github.com/ipiga94)
* **Cezar Sas** - [SasCezar](https://github.com/SasCezar)

See also the list of [contributors](https://github.com/aletundo/aras/contributors) who participated in this project.

## License
This project is licensed under the AGPLv3+. See the [LICENSE.md](LICENSE.md) file for details
