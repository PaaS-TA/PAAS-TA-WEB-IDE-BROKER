WEB IDE Service Broker 
==================
Web-Ide 서비스 브로커

##### 가능한 명령 목록 (로컬 환경)
- Catalog 조회 : http://localhost:8888/v2/catalog
  - Method : GET 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json
  - Body : None 
  - Parameters : None

- 서비스 인스턴스 생성 : http://localhost:8888/v2/service_instances/[new-instance-name]
  - Method : PUT 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json \
      Accept: application/json
  - Body
    > { \
        "service_id": \<service-id-string\>, \
        "plan_id": \<plan-id-string\>, \
        "organization_guid": \<organization-guid-string\>, \
        "space_guid": \<space-guid-string\>, \
        "parameters": { "userName": \<user-name-in-caas-service\> } \
      }
  - Parameters : None

- 서비스 인스턴스 삭제 : http://localhost:8080/v2/service_instances/[instance-name]
  - Method : DELETE 
  - Header
    > Authorization : Bearer type \
      X-Broker-Api-Version : 2.4 \
      Content-Type : application/json 
  - Body : None
  - Parameters : 
    > service_id : \<predefined_service_id\> \
      plan_id : \<plan_id_of_service\>