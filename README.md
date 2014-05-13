# Wireless Network Simulation
Coursework CS 6390

## Introduction
1. Network simulates unidirectional channels (links). Thus, if from node x to node y there is a channel it does not imply that there is a channel also from y to x.
2. This will be simulated by having a unix process correspond to a node in the network, and files correspond to channels in the network. Thus, if I have two nodes x and y in the network, and a channel from x to y, x and y will be unix processes running in the background (concurrently) and the channel from x to y will be a unix file.
3. At most 10 nodes in the network, nodes 0 , 1, 2, . . . , 9. There will a "controller" node. This node does not represent any "real" node, but we need it due to the fact that we will use files to represent channels.

## Channels, Processes, and Files
1. If there is a channel from y to x (y --> x). In this case, y is an incoming neighbor of x and x is an outgoing neighbor of y.
2. Each node will periodically broadcast a hello message. When y sends a hello message, x will receive it, and x will add y to its list of known incoming neighbors, But y is NOT aware it has an outgoing neighbor x since it cannot receive messages from x.
3. If there are two channels, y --> x and x --> y, both x and y will learn about each other.
4. Each node x has a single output file, called output_x, where x is from 0 to 9, and similarly input_x file.
5. Each message sent by a node is heard (received) by all its outgoing neighbors, but a node is not aware of who its outgoing neighbors are.
6. This is solved by controller, which knows the topology and will copy all the messages from a node's output to all its neighbors input file. In short this simulates packet transfers.
7. Final message received at receiver node must be in x_received file.

### Hello Protocol
1. Every 5 seconds, each node will send out a hello message with the following format "hello ID". ID is the ID of the node sending the message (0 .. 9).
2. If after 30 seconds the node does not receive messages from some neighbor, it believes the node is no longer a neighbor i.e. dead.

### Routing (In-Tree) Protocol
1. Nodes will also exchange in-tree with neighbors and this build its own.
2. In-trees are spanning trees that contain a path from all other nodes to the node. Tie braking of nodes done alphabetically. Example, "intree D ( A D ) ( C D ) ( E C ) ( B A )"
3. ( A D ) signifies unidirectional link A-->D

### Routing of data messages
1. When a node A wants to send data to node B, even with A-->B link exists, node A does not know of its outgoing neighbor.
2. When A receives the in-tree of B, it learns a path to B. Hence, A learns that to reach B it must follow the path A --> C --> B, so A can send messages to B using source routing.
3. Assume that A wants to send a data message to E. According to A's in-tree, E can reach A via the path E --> B --> A. So, A will route the message "towards" B. This will take several hops using source routing, i.e., the message will have to travel A --> C --> B. and then similarly to A.
	data A E C B begin message
	data A E B begin message

### Sample Topology File
	0 1
	1 2
	2 3
	3 0
	0 4
	4 3

## Usage
	java Controller 100 &
	java Node 0 100 3 "message from 0 to 3" &
	java Node 1 100 -1 &
	java Node 2 100 -1 &
	java Node 3 100 -1 &
	java Node 4 100 -1 &
	java Node 5 100 -1 &

## License

MIT: http://vineetdhanawat.mit-license.org/