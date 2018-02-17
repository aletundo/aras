Synopsis
*********

A lot of software analysis tools are available to monitor the various aspects of a system. 
One interesting facet is the design quality. A good design leads to evolvability, maintainability of the system, its availability, its security and cost reduction. 
When there is a lack of design, or the system is made up by poor design choices, the architecture of the system could be subjected to architectural anomalies: ARAS offers a service to run static analyses on software and inspect the retrieved results, to support developers and mainteiners during the quest for those anomalies.
In particular, ARAS aim is to expose the existing software Arcan as a web service. Arcan, which is a static analysis tool, analyses compiled Java projects in order to detect architectural flaws, in particular the ones called Architectural Smells (AS).
At the moment Arcan offers a desktop user interface and runs locally. ARAS will not extend Arcan domain features, but it will use it as a black-box.