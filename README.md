# Password Generator
[![Build Status](https://travis-ci.com/cagreer18/password-generator.svg?branch=master)](https://travis-ci.com/cagreer18/password-generator) [![Coverage Status](https://coveralls.io/repos/github/cagreer18/password-generator/badge.svg?branch=master)](https://coveralls.io/github/cagreer18/password-generator?branch=master) 

This password generator is meant to help anyone come up with secure passwords/phrases. Also, it is meant to help explain the methods used if the user wishes to hand generate words/phrases.

Ultimately, this project is used to help me work on a bite-size project while being secure. That said, I hope to raise awareness that making secure passwords can be easy for anyone given the proper direction.

Note: 'Password' and 'passphrase' are used interchangeably to describe any sequence of characters used to protect an account.

## Usage

### Prerequisite
* Java 1.8.0 or higher
* Git
* Maven


### Install

In the command line:

~~~
git clone https://github.com/cagreer18/password-generator
cd password-generator
mvn -Dmaven.test.skip=true package
java -cp target/password-generator-0.0.1-SNAPSHOT.jar cagreer.password_generator.Main
~~~
