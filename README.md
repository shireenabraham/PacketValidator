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
### Direction and Protocol
Both of these do not need complex data structures. A HashSet is used to store the allowed directions and protocols.

### Ports and IPs
1. A TreeSet is used to store ranges of both ports and IPs. 
2. This is chosen as a TreeSet is internally a self balancing Red-Black tree. 
3. The below data structure is used as a key to store in the TreeSet. 
4. The time complexity of operations using a TreeSet - **O(logN)**.
5. The alternative is to override **hashcode()** and use a HashMap. I chose the trade off because: 
  5.1. The time complexity in this case would be reduced to **O(1)**, but the space would be an extra **O(N)**. We might need to store a lot of data.
  5.2. Implementing your own Hashcode can be tricky. The HashMap will not work as expected if not implemented properly.
  5.3. Using a TreeSet is more cleaner to understand than a HashMap.

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
