# ARAS (ARcan As Service)
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
