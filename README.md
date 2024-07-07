# Getting Started

# Install open JDK-17
sudo apt install openjdk-17-jdk-headless

# 1. Execute Test cases and 2. Generate Jacoco Code Coverage reports (HTML) 

mvn clean surefire-report:report site -DgenerateReports=false

Note 1): Test Execution Report - RetailApplication\target\site\surefire-report.html

Note 2): Jacoco Code coverage HTML reports path - RetailApplication\target\site\jacoco\* index.html

# To run the application and expose the rest api in localhost:8080
mvn spring-boot:run

# To test API on CURL or POSTMAN -
(see below example request and response)

curl -i -X GET \
'http://localhost:8080/applyDiscounts?userType=EMPLOYEE&userAssociationInYears=1&amount=80&isProductAGrocery=true'

Response:

HTTP/1.1 200

Content-Type: text/plain;charset=UTF-8

Content-Length: 4

Date: Sun, 07 Jul 2024 11:15:21 GMT

Keep-Alive: timeout=60

Connection: keep-alive
**80.0**