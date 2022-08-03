# Booking-System-Micro-Service-Architucture

### This project is the third assignment of software architecture in Vrije Universiteit Brussel, and it is under the supervision of Dr. Coen De Roover, Mr. Camilo Velazquez, and Mr. Ahmed Zerouali.
There are three actors in this project, and also there are several objects and case classes.</br></br>
The structure of the project is that client will request a search. There are multiple types of searches that can help the client. After the search, the client can see the available properties, and the data will return to the client actor. Then an available property will choose randomly by the system, and then this property will send to the SystemService actor and then to a child actor called ReservationService actor, and after that, we check again and then make the reservation if possible and if the date was still available and send the result back directly to the client actor. Priority is with the one who reserves sooner than the others.</br></br>
The Ask pattern and Froward Flow pattern are used for different parts in this project. The Ask pattern was used for the searching part and split the responsibility for reservation and search with a child actor. Moreover, the Forward Flow pattern is used to return the reservation result directly to the client actor.</br></br>
## Objects and case classes:
1. **property:** For saving the properties information
2. **Clients:** For saving the clients information
3. **PropertyType:** a sealed trait for the three types of properties
4. **ReservationIsSuccessfull:** a case object for sending to client for the success of reservation
5. **ReservationIsFailed:** a case object for sending to client for the failure of reservation
6. **AvailableProperty:** a collection of data that must be sent to the actors.
7. **AvailableProperties:** an array of AvailableProperty
8. **SendProperty:** for sending data to SystemService for searching based on the available date
9. **SendPropertyName:** for sending data to SystemService for searching based on available date and name of the property
10. **SendPropertyCategory:** for sending data to SystemService for searching based on available date and category of the property
11. **SendPropertyCountryCity:** for sending data to SystemService for searching based on the available date and place of the property
12. **SendPropertyCountryCityName:** for sending data to SystemService for searching based on available date and name and the place of the property
13. **SendPropertyCountryCityCategory:** for sending data to SystemService for searching based on available date and place and category of the property
14. **SendPropertyNameCategory:** for sending data to SystemService for searching based on available date and name and category of the property
15. **SendPropertyTypeName:** for sending data to SystemService for searching based on available date and name and type of the property
16. **SendPropertyTypeCountryCity:** for sending data to SystemService for searching based on available date and type and place of the property
17. **SendPropertyTypeCountryCityName:** for sending data to SystemService for searching based on available date and type and place and name of the property
18. **RandomChosenDate:** for sending data to SystemService to make a reservation.

</br></br>
## Actors:
**Clients:** it defines the kind of search based on the input. It also has tried cases in the receives method, AvailableProperties, ReservationIsFailed, and ReservationIsSuccessfull. AvailableProperties is for choosing a random property after receiving a list of available properties from the SystemService. ReservationIsFailed and ReservationIsSuccessfull report the reservation failure when it receives the result from the ReservationService.</br></br>
**systemService:** in this actor, there are ten different kinds of search in the receive method, and also there is RandomChosenDate that contains the child actor for using the ReservationService class.</br></br>
**ReservationService:** ReservationService only makes a reservation and sends the result to the client actor.</br></br>
