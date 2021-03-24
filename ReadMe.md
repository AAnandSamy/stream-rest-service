# Send & Receive Kafka message - REST API
 This application helps us to send & receive Kafka messages through REST API
 
 #### Futures
 - Send default message to Kafka
 - Send your messages to Kafka
 - Send bulk messages to Kafka, based on given message criteria's 
 
 ##### Development
  - SprigBoot
  - Kafka 2.11
  - Java 8
  
 ##### Deployment
   - Java 8
   
 ### REST API Guide
  - ##### SYS - POST - Kafka
    - Send default system generated message  
    `curl -X POST -H "Content-Type: application/json" -d '{}' http://localhost:8080/ws/msg/sender`
    - Send default system generated messages based on message size
    `curl -X POST -H "Content-Type: application/json" -d '{"numberOfMsg": 10}' http://localhost:8080/ws/msg/sender`
   
  - ##### MSG - POST - Kafka
    - Send your messages to kafka   
    `curl -X POST -H "Content-Type: application/json" -d "@req-payload.json" http://localhost:8080/ws/msg/sender`
  
  - ##### SYS - GET 
    - Get the sample message for kafka post.    
      `curl -X GET http://localhost:8080/ws/msg/sender`         
    - Get the sample message criteria to generate kafka message (request payloads) .    
        `curl -X GET http://localhost:8080/ws/msg/sender/criteria`   
  
    
  
## Application setup for Test/Automation

#### Export the data

- Login into console
- Run the export sql
```
CALL CSVWRITE ('/home/srvredi/../data-backup/test_criteria-5282020.csv', 'SELECT * FROM TEST_CRITERIA', 'charset=UTF-8 fieldSeparator=|');
```
 
#### Import the data

- Login into console
- Always truncate and load
- Run the import sql
``` 
DROP TABLE IF EXISTS TEST_CRITERIA_1;
TRUNCATE TABLE TEST_CRITERIA;
CREATE TABLE TEST_CRITERIA_1 AS SELECT * FROM CSVREAD('/home/srvredi/../data-backup/test_criteria-5282020.csv', null, 'charset=UTF-8 fieldSeparator=|');
INSERT INTO TEST_CRITERIA SELECT * FROM TEST_CRITERIA_1
DROP TABLE IF EXISTS TEST_CRITERIA_1;
```
  
  
  Support @Redi-dev-team 
 