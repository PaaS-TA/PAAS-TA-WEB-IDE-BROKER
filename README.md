WEB IDE Service Broker 
==================
Web-Ide 서비스 브로커

##### 가능한 명령 목록 (로컬 환경)


◎ Request
- Catalog 조회 : http://localhost:8080/v2/catalog
  - Method : GET 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json
  - Body : None 
  - Parameters : None
  
◎ Response : 
- Status Code : 200OK

	
	{
        "services": [
            {
                "planUpdatable": false,
                "id": "af86588c-6212-11e7-907b-b6006ad3dps0",
                "name": "webide",
                "description": "A paasta source control service for application development.provision parameters : parameters {owner : owner}",
                "bindable": false,
                "plan_updateable": false,
                "plans": [
                    {
                        "id": "a5930564-6212-11e7-907b-b6006ad3dps1",
                        "name": "webide-shared",
                        "description": "This is a dedicated service plan. All services are created equally.",
                        "metadata": {
                            "costs": [
                                {
                                    "amount": {
                                        "usd": 0
                                    },
                                    "unit": "MONTHLY"
                                }
                            ],
                            "bullets": [
                                "WEB-IDE shared build server use",
                                "WEB-IDE build service using a shared server"
                            ]
                        },
                        "free": false
                    },
                    {
                        "id": "a5930564-6212-11e7-907b-b6006ad3dps2",
                        "name": "webide-dedicated",
                        "description": "This is a dedicated service plan. All services are created equally.",
                        "metadata": {
                            "costs": [
                                {
                                    "amount": {
                                        "usd": 0
                                    },
                                    "unit": "MONTHLY"
                                }
                            ],
                            "bullets": [
                                "WEB-IDE dedicated build server use",
                                "WEB-IDE build service using a dedicated server"
                            ]
                        },
                        "free": false
                    }
                ],
                "tags": [
                    "webide-shared",
                    "webide-dedicated"
                ],
                "metadata": {
                    "longDescription": "Paas-TA Web ide",
                    "documentationUrl": "https://paas-ta.kr",
                    "providerDisplayName": "PaaS-TA",
                    "displayName": "Web-Ide",
                    "imageUrl": "",
                    "supportUrl": "https://paas-ta.kr"
                },
                "requires": [],
                "dashboard_client": null
            }
        ]
    }
