step:
1. config:
	config.properties    //配置文件库的位置和不重要的词组
	
2. double click "run.bat" to start up the server.
	if need to change port, use below command:
	java -jar cloud-chatbot-0.0.1-SNAPSHOT.jar  --server.port=8082

3. Service API (GET and POST both supported, GET for your test):
	(1) policy query: 
	http://localhost:8082/getPolicy/?policy={policyNum}
	eg: http://localhost:8082/getPolicy/?policy=15465465

	(2) QA: 
	http://localhost:8082/getInfo/?question={question}
	eg: http://localhost:8082/getInfo/?question="什么是金融"

note: folder database is for test. If you configed the config.properties, then will use the config path as database to search.
eg: 
file.system.path=D:/database
file.system.path=D:\\database

