# PacketValidator
Application to validate network packets based on given rules

## Pre-Requisites
1. Java version 9 or higher.
2. Maven.

## How to run the code
1. **$JAVA_HOME/bin/jshell**
2. **jshell> /env --class-path lib/opencsv-2.4.jar:target/PacketValidator-1.0-SNAPSHOT.jar**
3. **jshell> import com.illumio.firewall.Firewall;**
4. **jshell> Firewall firewall = new Firewall(<YOUR_CSV_FILE_PATH>);**
5. **jshell> firewall.acceptPacket(<DIRECTION_STRING>, <PROTOCOL_STRING>, <PORT_STRING>, <IP_STRING>)**
Do not include *jshell>* as its a prefix added in the Java shell.

## Algorithm used to validate packets
1. A hierarchical tree-like data structure is used to store the rule combinations. The rules follow this order:
```code
direction -> protocol -> port Ranges -> IP Ranges
```
2. Hashmaps are used for both direction and protocol to check whether they exist or not.
3. TreeMap / TreeSet is used for Ports and IPs respectively.
  1. The Tree is internally implemented as a Self-balancing red-black binary search tree. 
  2. The keys of this Tree are another Data structure called *Range* as explained below.
  3. This data structure is chosen because of its **O(logN)** time complexity for all operations on it.
  4. A potential alternative solution is to use a HashMap instead of TreeMap / TreeSet. But I chose to not go that way as my planned implementation using HashMap will consume more space even though time will be **O(1)** and we have to override hashCode(), which is a tricky area. This is the trade off decision I made when implementing the algoirithm.

#### Range
1. The ports and IPs are stored in a data structure called *Range*. 
2. This *Range* is used as the key for storing the ports and IPs in the TreeSet.
3. Overlapping ranges are merged as one and stored in the TreeSet.

## Testing

### How to execute
**mvn test**

### Testing Framework
#### Test Config
1. Located at *src/test/test-dependencies/test-config.json*.
2. Format:
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

#### Rules CSV
1. The rules csv is similar to the ones we provide when we are executing the code.
2. Format (exclude headers when adding):
```csv
direction,protocol,port_range,ip_range
```
3. Example:
```csv
inbound,tcp,80,192.168.1.2
outbound,tcp,10000-20000,192.168.10.11
inbound,udp,53,192.168.1.1-192.168.2.5
outbound,udp,1000-2000,52.12.48.92
```

#### Rule Engine (Optional)

Current available rule engines - **Impl**.

#### Test Data
1. The format is similar to the rules csv. We have to add the expected result as well
```csv
direction,protocol,port_range,ip_range,expected_result
```
2. Example:
```csv
inbound,tcp,80,192.168.1.2,true
outbound,tcp,10002,192.168.10.11,true
inbound,udp,52,192.168.1.1,false
```

# For the Reviewer

# Interested Team
The first team I would love to work in is the Policy team. The kind of work done by the team seems truly challenging. I think this would be a great place for me to learn how to design efficient and scalable systems.
The second team would be the Data team. I loved the Data Mining and Data Visualization courses I have taken in graduate school. I believe that the knowledge I gained while learning these courses will be useful while working for the Data team.
