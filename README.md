# Muti-dictionary-server
COMP90015 A1 Muti-dictionary-server

When the server is launched, it loads the dictionary data from a file containing the initial list of words and their meanings. 
These data is maintained in memory in a structure that enables an efficient word search. When words are added or removed, the data structure is updated to reflect the changes.  
A sample command to start the server is:


> java –jar DictionaryServer.jar <port> <dictionary-file> Where <port> 
is the port number where the server will listen for incoming client connections and <dictionary-file> is the path to the file containing the initial dictionary data.   


• When the client is launched, it creates a TCP socket bound to the server address and port number. 
This socket remains open for the duration of the client-server interaction. All messages are sent/received through this socket. A sample command to start the client is: > 

java –jar DictionaryClient.jar <server-address> <server-port> 
