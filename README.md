# PacketValidator
Application to validate network packets based on given rules

## Pre-Requisites
1. Java version 9 or higher.
2. Maven installed.

## How to run the code
1. Open REPL for Java. **$JAVA_HOME/bin/jshell**
2. Init the Class path within the Java shell. **/env --class-path lib/opencsv-2.4.jar:target/PacketValidator-1.0-SNAPSHOT.jar**
3. Run **import com.illumio.firewall.Firewall;** to import our Firewall class.
4. Init firewall object with rules. **Firewall firewall = new Firewall(YOUR_CSV_FILE_PATH);**.
5. Check packet acceptance. **firewall.acceptPacket(DIRECTION_STRING, PROTOCOL_STRING, PORT_STRING, IP_STRING)**.
