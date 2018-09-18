# NetworkDiscovery

4 modules so far

Common : common lib

DiscoveryService : to be a microservice responsible for discovery

1. use mongoDb as repository

2. get metadata from manage service and schedule discovery

ManageService : to be a microservice responsible for management

1. JPA based

2. REST API to allow CRUD for metadata

SshSimulator : simulator a SSH device

1. Apache mina sshd + Spring Shell

2. A REST interface to allow CRD operations to a SSH server instance

3. Putty is used to test those SSH server instance