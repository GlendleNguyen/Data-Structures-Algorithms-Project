# CITS2200Project
An interface to implement the CITS2200 DSA Project

This project implements four functions. 

The questions consider a theoretic model of a computer network. The network contains N distinct devices, each of which is assigned a unique integer id in the range [0, N). 

allDevicesConnected: 
Determines if all the devices on the network are connected to the network. Devices are considered to be connected if they can transmit (including via other devices) to every other device in the network. 

numPaths: 
Determines the number of different paths a packet can take in a network to get from a transmitting device to a receiving device. A device will only transmit a packet to a device that is closer to the destination, where the destination is the minimum number of hops between a device and the destination.

closestInSubnet:
Compute the minimum number of hops required to reach a device in each subnet query. Each device has an associated IP address. An IP address is here represented as an array of exactly four integers between 0 and 255 inclusive (for example, {192, 168, 1, 1} ). Each query specifies a subnet address. A subnet address is specified as an array of up to four integers between 0 and 255. An IP address is considered to be in a subnet if the subnet address is a prefix of the IP address (for example, {192, 168, 1, 1} is in subnet {192, 168} but not in {192, 168, 2} ).

maxDownloadSpeed:
Compute the maximum possible download speed from a transmitting device to a receiving device. The download may travel through more than one path simultaneously, and you can assume that there is no other traffic in the network. If the transmitting and receiving devices are the same, then you should return -1 .
