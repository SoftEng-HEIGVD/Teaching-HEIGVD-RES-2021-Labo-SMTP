# Teaching-HEIGVD-RES-2021-Labo-SMTP

## Introduction

__Spammer__ is a prank-application. With a given victim list and a give number of groups, the application will send provided messages to victims by group with another victim email chosen randomly in each group. In other words, the goal is to mislead recipient victims into thinking that the sender victim has sent them. __Spammer__ uses the Socket API to communicate with an SMTP server with TCP connection in Java. (It is notable that the application is a **partial implementation of the SMTP protocol**.)

## Instructions

There are 3 configuration files in which you are free to set values as you want as long as these values obey their standard formats :

1. __config.properties__: contains server properties.
  * ___smtpServerAddress___ : determines SMTP server addresses.
  * ___smtpServerPort___: determines SMTP server port. If the value less or equal than 0, the port will be 25 by default.
  * ___numberOfGroups___: number of groups that you want to partition victim list.
  * ___chosenGroupsForPrank___: groups of victims that you want to do the prank on. If the value is 0, the prank will be made on all groups.
  * ___witnessesToCC___: CC email for the prank. If the value is `none`, the CC email will be considered as `null`.

2. __messages.uft8__: contains given messages which will be encoded in `utf-8` of the prank. The file can contain multiple paragraphs. If there are 10 equal symbols (`==========`) separating them. All the paragraphs between these symbols represent a message. By convention, the first line of paragraph is the message subject and the rest is the message content.
3. __victims.uft8__: contains a victim email list also provided and encoded in `utf-8`. This list will be partitioned into small groups. Note that in every group of victims, there should be 1 sender and at least 2 recipients (i.e. the minimum size for a group is 3). In the file, each line must contain exactly one victim email address.

All these files are found in `data` folder.

On the other hand, once the groups are created. They will be written in `groups.utf8` file, which can be found in `target/out/`. In this file, you'll find who the sender of each group and his recipients. Each group is separated by 10 equal symbols (`==========`).

The application will choose randomly a message and a sender for each group.

## Mock SMTP server

This section is for those who want to experiment with __Spammer__ but don't really want to send pranks immediately.

First of all, you will need to simulate a ___fake___ SMTP server, i.e., mock SMTP server.
> A [MockServer](<https://en.wikipedia.org/wiki/MockServer#:~:text=MockServer%20is%20designed%20to%20simplify,not%20complete%20or%20is%20unstable.>) is designed to simplify integration testing and promotes best practices by improving the isolation of the system under test.

In order to do that, you can use any mock SMTP server on the internet. Here, we'll use [MockMock server](<https://github.com/tweakers/MockMock>) on GitHub, which is a web-application. The executable `jar` file can be easily downloaded by following the instructions in `README.md` of [MockMock server](<https://github.com/tweakers/MockMock>) or retrieved by cloning the repository then building application. For any further information, you can checkout [MockMock server](<https://github.com/tweakers/MockMock>) repository.

Once you have extracted the executable file to any place that you like, you can start the server by running: `java -jar MockMock.jar`. Note that with this command, the port is 25 by default. To run MockMock on another port, you can start it with the following parameters: `java -jar MockMock.jar -p 25000 -h 8080`. This will run MockMock on SMTP port 25000 and http port 8080. After that, you can go to your browser and type `localhost:8282`. This will show you the web interface of MockMock.

You can now launch __Spammer__ which will read your configuration in 3 files mentioned above. After refreshing the browser you'll see the prank made on your victim list.

## Description of implementation
![Simplified UML of __Spammer__](https://github.com/mhganh/Teaching-HEIGVD-RES-2021-Labo-SMTP/tree/main/figures/schema.png?raw=true "Simplified UML of __Spammer__ without Utils and Exceptions")

The logic of the application remains in 2 classes: `ConfigurationManager` and `PrankGenerator`.

The first class is responsible for reading configuration files in __Instruction__ section. For the messages, it will call create a `MessageList` to obtain messages in the message file by reading all characters in the file (Remember that each message is separated by 10 equal symbols (`==========`)), then, split the result string by the convention separation into substrings, which are the messages. For the victims, the principle is as same as for the messages, `ConfigurationManager` creates a `VictimList`, which save the victims by reading the victim file line by line. For the server properties, `ConfigurationManager` creates a `ServerProperties` to set up the server with the help of `Properties`.

The second class is used for partitioning the victim list and generating mails/pranks based on configurations above. The partition is quite easy. It just creates groups with size of victim list divided by the given number of groups. Then, for each group, it will randomly choose a victim sender and recipient and also a message for each prank.

## References

* [MockMock server](<https://github.com/tweakers/MockMock>) on GitHub
* [MockServer](<https://en.wikipedia.org/wiki/MockServer#:~:text=MockServer%20is%20designed%20to%20simplify,not%20complete%20or%20is%20unstable.>) on Wikipedia
* [Regex for email](<https://howtodoinjava.com/java/regex/java-regex-validate-email-address/>)
