# SearchNews

This application searches a specific keyword in a set of news and returns the news in which the keyword is present as well as the number of occurrences.
It is built using a client-server architecture and is capable of handling multiple requests at the same time by using a multi-thread system, where both workers and clients connect to the server and it's responsible for turning the requests of the client into tasks for workers and then returning the results back to the client.
