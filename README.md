## Setup

* ☕️ Please make sure Java 17 installed on your local machine and configured in your IDE in order to
  successfully compile this project and run the tests.
* ⚠️ Please make sure to enable annotation processing in your IDE for Lombok compatibility.

## Notes

* Consultation-Service has been chosen as the generic name of this MVP as it is intended to possibly deliver consultation questions beyond MedExpress. It has been designed with the intention of working in a microservices architecture that can handle the requests of any medical domain.
* The 'ConsultationQuestionsCache' service is a very simplified hardcoded cache. It would normally be implemented using a library such as Ehcache. The idea is that it returns consultation questions given a condition ID from the DB. These question are unlikely to change very often so we could improve performance and save ourselves expensive DB queries by getting these questions from cache. Of course we would still need to periodically fetch the questions from the DB in case they do change.
* The question submission endpoint is done under a POST request as we intend to save the results to a DB for a doctor to later review them and confirm a prescription. This unfortunately has not been implemented in this POC.
* If the consultation-service deems a prescription unlikely to be prescribed, it will return a set of reasons. If no reasons are returned, it is assumed that the prescription will likely be given. The presentation of this information is assumed to be delegated to a front end service.
* If a new medication were to be added, the idea is that the only changes (not including testing and DB migrations) really needed would be to add a new condition validation strategy. The correct strategy would be selected by the conditionId passed into the service.

## Improvements
* Input validation needs to be expanded. More testing added in conjuction with the input validation.
* More logging would need to be added. Include more logging for any RuntimeExceptions thrown.
* Adding question types beyond a true or false answer.
* Better way to validate fetch input answers when validating.