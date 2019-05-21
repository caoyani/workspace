step:
1. double click "run.bat" to start up the server.

3. Service API (GET and POST both supported, GET for your test):
	(1) policy query: 
	http://localhost:8082/getPolicy/?policy={policyNum}
	eg: http://localhost:8082/getPolicy/?policy=15465465

	(2) QA: 
	http://localhost:8082/getInfo/?question={question}
	eg: http://localhost:8082/getInfo/?question="什么是金融"
