# PacketValidator
Application to validate network packets based on given rules

## Pre-Requisites
* Java version 11.
* Maven.

## How to run the code
* **$JAVA_HOME/bin/jshell**
* **jshell> /env --class-path lib/opencsv-2.4.jar:target/PacketValidator-1.0-SNAPSHOT.jar**
* **jshell> import com.illumio.firewall.Firewall;**
* **jshell> Firewall firewall = new Firewall(<YOUR_CSV_FILE_PATH>);**
* **jshell> firewall.acceptPacket(<DIRECTION_STRING>, <PROTOCOL_STRING>, <PORT_STRING>, <IP_STRING>)**
Do not include *jshell>* as its a prefix added in the Java shell.

## Algorithm used to validate packets
* A hierarchical tree-like data structure is used to store the rule combinations. The rules follow this order:
```code
direction -> protocol -> port Ranges -> IP Ranges
```
* Hashmaps are used for both direction and protocol to check whether they exist or not.
* TreeMap / TreeSet is used for Ports and IPs respectively.
  * The Tree is internally implemented as a Self-balancing red-black binary search tree. 
  * The keys of this Tree are another Data structure called *Range* as explained below.
  * This data structure is chosen because of its **O(logN)** time complexity for all operations on it.
  * A potential alternative solution is to use a HashMap instead of TreeMap / TreeSet. But I chose to not go that way because. 
    * As my planned implementation using HashMap will consume more space even though time will be **O(1)**
    * We have to override hashCode(), which is a tricky area. 
    * Using Tree would be a more cleaner and intuitive implementation than Hashmap as the keys (Range) will be in sorted order.
    * This is the trade off decision I made when implementing the algoirithm.

#### Range
* The ports and IPs are stored in a data structure called *Range*. 
* This *Range* is used as the key for storing the ports and IPs in the TreeSet.
* Overlapping ranges are merged as one and stored in the TreeSet.
* The *Range* is made into a generic class which can accept any data type. 
* We have overriden *compareTo* and *equals* methods, so that the Tree can use them to sort the Range keys.

## Testing

### How to execute
**mvn test**

### Testing Framework
#### Test Config
* Located at *src/test/test-dependencies/test-config.json*.
* Format:
```json
{
  "configMetaData": {
    "testNo": <TEST_NO>,
    "rulesCsv": <RULES_CSV_PATH>,
    "ruleEngine": <RULE_ENGINE>,
    "testDataFile": <TEST_DATA_FILE_PATH>
  }
}
```
* Example:
```json
{
	"configMetaData": [
		{
			"testNo": 1,
			"rulesCsv": "src/test/test-dependencies/1/rules.csv",
			"ruleEngine": "impl",
			"testDataFile": "src/test/test-dependencies/1/data.csv"
		},
		{
			"testNo": 2,
			"rulesCsv": "src/test/test-dependencies/2/rules.csv",
			"ruleEngine": "impl",
			"testDataFile": "src/test/test-dependencies/2/data.csv"
		}
	]
}
```

#### Rules CSV
* The rules csv is similar to the ones we provide when we are executing the code.
* Format (exclude headers when adding):
```csv
direction,protocol,port_range,ip_range
```
* Example:
```csv
inbound,tcp,80,192.168.1.2
outbound,tcp,23,34.54.67.89
```

#### Rule Engine (Optional)

Current available rule engines - **Impl**.

#### Test Data
* The format is similar to the rules csv. We have to add the expected result as well
```csv
direction,protocol,port_range,ip_range,expected_result
```
* Example:
```csv
inbound,tcp,80,192.168.1.2,true
outbound,tcp,80,192.168.1.2,false
inbound,udp,80,192.168.1.2,false
inbound,tcp,81,192.168.1.2,false
inbound,tcp,80,192.168.1.3,false
outbound,tcp,23,34.54.67.89,true
```

# Interested Team
I am interested in the Policy and the Platform team. It aligns with my interest of working on solving problems at scale. I have prior experience in this domain. I am also interested to work with service oriented architecture. My first priority would be Policy team and second would be Priority team.
