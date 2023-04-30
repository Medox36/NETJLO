# NETJLO
This repository acts as the index for all the different packages under this project.   

---

This project contains code for creating connections over the java.net.Socket and deals with incoming and outgoing data.  
Mainly to use for not writing boilerplate code.  
Data is being sent via Object Serialisation and Deserialization, wich can be extended to any desirable extent.  
The Data being received will get sent to an Interpreter object/class to be processed.

## [NETJLO-base]
The base package contains classes on which all other packages as based on.  
Additionally, as you might have guessed, these classes form tha base of all NETJLO packages.  
Repository can be found [here](https://github.com/Medox36/NETJLO-base).  

## [NETJLO-core]
The core package contains everything to make Connections from a client to a server and back.  
Make your own interpreter to decide what to do with the information sent over the socket. And make your very own custom package to send whatever type of data you want over the connection.  
Repository can be found [here](https://github.com/Medox36/NETJLO-core).

## [NETJLO-default]
The default package contains default implementations for other packages.  
There is only the need to implement an Interpreter, whilst there is a default Interpreter, it only prints out the received data.  
Repository can be found [here](https://github.com/Medox36/NETJLO-default).

## [NETJLO-demanding]
The demanding package should be used when having heavy or very frequent traffic.  
The difference to other packages is that this package has a buffer for all received data-packages and its own thread for interpreting.  
Repository can be found [here](https://github.com/Medox36/NETJLO-demanding).

## [NETJLO-fos]
The file transfer package contains classes to send files from a client to a sever and back.  
```fos``` stands for ```file over socket```. And quintessentially means a way to send a file over a java.net.Socket without using libraries with FTP/FTPS/SFTP support.  
Send one file or a folder with all its subdirectories and files. Also, multiple folders at once!  
There is also zip support. Files can automatically be zipped before sending over the connection and unzipped when receiving them. Works with a directory too!  
Repository can be found [here](https://github.com/Medox36/NETJLO-fos).

## [NETJLO-timed]
The package is basically the core package but with timings.  
Decide when a specific package should be sent and when it should be interpreted by the other side of the connection.  
Repository can be found [here](https://github.com/Medox36/NETJLO-timed).

## Roadmap
- SSL implementation with the java.net.SSLSocket, for all connections
- Documentation
- Examples, explanations and quick start guide
