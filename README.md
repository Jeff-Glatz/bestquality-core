# Java OSS Template
This repository contains the scaffolding necessary to build and deploy to Maven Central, 
along with a Travis CI configuration that supports deploying snapshot builds and 
automatic releasing from a tag.

## Open Sonatype OSSRH Account
* Create an issue: Community Support - Open Source Project Repository Hosting
* Add DNS TXT Record referencing ticket, i.e. `OSSRH-54345`
* Once issue has been resolved
    * Deploy artifacts to staging repository (https://oss.sonatype.org/#stagingRepositories)
    * Comment on ticket when first release is promoted (https://central.sonatype.org/pages/releasing-the-deployment.html)  

## Project Bootstrapping
In order to continuously deploy to OSSRH from Travis CI, there a some prerequisites:

### OSSRH Code Signing Key 

Export the private key required by OSSRH for signing code artifacts:

```sh
gpg -a --export-secret-key <KEYID> > deploy/code-signing-key.asc
```

Next, use `travis` CLI to encrypt the code signing private key. This command will output the `key` and `iv`
parameters needed to encrypt multiple files, so save them in a safe location:

```sh
travis encrypt-file deploy/code-signing-key.asc deploy/code-signing-key.asc.enc --com --print-key
```

After this command completes, update the encrypted key (`$encrypted_XXXXXXXXXX_key`) and 
iv (`$encrypted_XXXXXXXXXX_key`) variable names in `.travis.yml` with the correct values from
your TravisCI settings.

:exclamation: **Ensure that `deploy/code-signing-key.asc` is moved out of the project directory.** Then add the
file:
```sh
git add deploy/code-signing-key.asc.enc
```

### GitHub Repository Deploy Key
Generate a repository-specific deploy key that can be used to push commits from Travis:

```ssh
ssh-keygen -t rsa -C your@email.com -f deploy/id_rsa
```

Then upload public key to deploy keys area of the GitHub repository. Next, encrypt the 
private key that will be used to connect to the repository using the `key` and `iv` 
parameters from the previous step:

```sh
travis encrypt-file deploy/id_rsa deploy/id_rsa.enc --com --key <key> --iv <iv>
```

:exclamation: **Ensure that `deploy/id_rsa` is moved out of the project directory and to a safe location.** Then add the file:
```sh
git add deploy/id_rsa.enc
```

### Maven Master Password
To generate a master password:
```sh
mvn --encrypt-master-password <password>
```

To encrypt a password using the master:
```sh
mvn --encrypt-password <password>
```

Create a `deploy/settings-security.xml` file:
```xml
<settingsSecurity>
  <master>{MASTER-PASSWORD}</master>
</settingsSecurity>
```

Use the `travis` utility to encrypt the file for use during the build:
```sh
travis encrypt-file deploy/settings-security.xml deploy/settings-security.xml.enc --com --key <key> --iv <iv>
```

:exclamation: **Ensure that `deploy/settings-security.xml` is moved out of the project directory.** Then add the file:
```sh
git add deploy/settings-security.xml.enc
```

### Required Environment Variables
The following environment variables need to be defined to deploy locally and will need to be exposed as 
secret environment variables in your TravisCI settings:

|Variable Name|Purpose|
|:------------|:------|
| `OSSRH_USERNAME`| Maven Central username |
| `OSSRH_PASSWORD`| Maven Central password (encoded with master password) |
| `CODE_SIGNING_KEY_FINGERPRINT`| Code signing key identifier |
| `CODE_SIGNING_KEY_PASSPHRASE`| Code signing key passphrase |
| `CODECOV_TOKEN`| Codecov.io token for code coverage reports |

# `README.md` Template
[![Build Status](https://img.shields.io/travis/ruffkat/XXX/master?color=green)](https://travis-ci.com/ruffkat/XXX)
[![codecov](https://codecov.io/gh/ruffkat/XXX/branch/master/graph/badge.svg)](https://codecov.io/gh/ruffkat/XXX)
[![Maven Central](https://img.shields.io/maven-central/v/io.bestquality/XXX.svg?color=green&label=maven%20central)](https://search.maven.org/search?q=g:io.bestquality%20AND%20a:XXX)

# Project Name
Project description

## Installation
```xml
<dependency>
  <groupId>io.bestquality</groupId>
  <artifactId>XXX</artifactId>
  <version>0.0.1</version>
</dependency>
```