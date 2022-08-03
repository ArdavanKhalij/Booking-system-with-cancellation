import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

import java.text.SimpleDateFormat
import java.util.{Date, Random}
import scala.collection.mutable.ListBuffer
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
object BOOKING extends App {
  // List of properties
  var PROPERTIES = List(
    property(1, "Avenue Louise", Hotel, 4, "Belgium", "Brussels", ListBuffer(), 70),
    property(2, "Azadi", Hotel, 5, "Iran", "Tehran", ListBuffer(), 99),
    property(3, "Arora", Resort, 4, "Turkey", "Istanbul", ListBuffer(), 24),
    property(4, "Diamond", Apartment, 3, "Belgium", "Brussels", ListBuffer(), 24),
    property(5, "Hilton", Hotel, 5, "Iran", "Tehran", ListBuffer(), 24),
    property(6, "Dedeman", Resort, 5, "Turkey", "Antalyia", ListBuffer(), 24),
    property(7, "LUX", Apartment, 4, "USA", "Las Vegas", ListBuffer(), 24),
    property(8, "SIDE", Resort, 3, "UK", "London", ListBuffer(), 24),
    property(9, "Khalifa", Hotel, 5, "UAE", "Dubai", ListBuffer(), 24),
    property(10, "Airbnb", Apartment, 4, "Belgium", "Brussels", ListBuffer(), 24),
    property(11, "Airbnb", Apartment, 5, "Belgium", "Brussels", ListBuffer(), 24),
    property(12, "Ferdowsi", Resort, 4, "Iran", "Mashhad", ListBuffer(), 24),
    property(13, "Pasargad", Hotel, 5, "Iran", "Kish", ListBuffer(), 24),
    property(14, "Homa", Hotel, 5, "Iran", "Tehran", ListBuffer(), 24),
    property(15, "Vista hill", Resort, 3, "Turkey", "Antalyia", ListBuffer(), 24),
    property(16, "LUXA", Apartment, 4, "USA", "Las Vegas", ListBuffer(), 24),
    property(17, "SID", Resort, 3, "UK", "London", ListBuffer(), 24),
    property(18, "ARAM", Resort, 5, "UAE", "Dubai", ListBuffer(), 24),
    property(19, "Kroonlaan", Hotel, 4, "Belgium", "Brussels", ListBuffer(), 24),
    property(20, "Espinas", Hotel, 5, "Iran", "Tehran", ListBuffer(), 24),
    property(21, "Cornel", Resort, 4, "Turkey", "Istanbul", ListBuffer(), 24),
    property(22, "Gold", Apartment, 3, "Belgium", "Brussels", ListBuffer(), 24),
    property(23, "Taksim", Hotel, 5, "Iran", "Istanbul", ListBuffer(), 24),
    property(24, "Abbasi", Resort, 5, "Iran", "Isfahan", ListBuffer(), 24),
    property(25, "Eram", Apartment, 4, "Iran", "Shiraz", ListBuffer(), 24),
    property(26, "Smith", Apartment, 4, "Denmark", "Aarhus", ListBuffer(), 24),
    property(27, "Deluxe", Hotel, 5, "USA", "Los Angeles", ListBuffer(), 54),
  )
  val format = new SimpleDateFormat("yyyy-MM-dd")
  val date1 = format.parse("2022-09-11")
  val date2 = format.parse("2022-09-11")
  val date3 = format.parse("2022-09-12")
  val date4 = format.parse("2022-09-13")

  PROPERTIES(0).NotAvailable = ListBuffer(date1)
  PROPERTIES(1).NotAvailable = ListBuffer(date2)
  PROPERTIES(2).NotAvailable = ListBuffer(date3)
  PROPERTIES(3).NotAvailable = ListBuffer(date4)

  // List of clients
  var CLIENTS = List(
    clients("Ardavan Khalij", 23, "Y44986738"),
    clients("Harry Potter", 32, "Y44986739"),
    clients("Severus Snap", 58, "Y44986740"),
    clients("Albus Dumbledor", 143, "Y44986741"),
    clients("Tony Stark", 55, "Y44986742"),
    clients("Ted Mosby", 47, "Y44986743"),
    clients("Bella Swan", 24, "Y44986744"),
    clients("Ross Geller", 57, "Y44986745"),
    clients("Rachel Green", 56, "Y44986746"),
    clients("Petter Griffin", 68, "Y44986747"),
    clients("Bojack Horseman", 48, "Y44986748"),
    clients("Jeremy Clarkson", 60, "Y44986750"),
    clients("Richard Hammond", 50, "Y44986751"),
    clients("James May", 58, "Y44986752"),
    clients("Rick Sanchez", 60, "Y44986753"),
  )
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Define the property types
sealed trait PropertyType
case object Hotel extends PropertyType
case object Apartment extends PropertyType
case object Resort extends PropertyType
case object NON extends PropertyType
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Define the reservation status
case object ReservationIsSuccessfull
case object ReservationIsFailed
//  This part has been added for the second session
case object CancellationIsSuccessfull
case object CancellationIsFailed
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Define the case classes that used above and below
case class clients(name: String, age: Int, passport_number: String)
case class property(id: Int, name: String, PropertyType: PropertyType, category: Int, country: String, city: String,
                    var NotAvailable: ListBuffer[Date], Price: Double)
case class AvailableProperty(customerName: String, id: Int, name: String, PropertyType: PropertyType, category: Int,
                             country: String, city: String, price: Double, date: ListBuffer[Date], actorRef: ActorRef)
case class AvailableProperties(var availableProperties: ListBuffer[AvailableProperty], CancelOrReserve: String)
case class SendProperty(customerName: String, date: Date, propertyType: PropertyType, actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyName(customerName: String, date: Date, name: String, actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyCategory(customerName: String, date: Date, category: Int, actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyCountryCity(customerName: String, date: Date, country: String, city: String, actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyCountryCityName(customerName: String, date: Date, country: String, city: String, name: String,
                                       actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyCountryCityCategory(customerName: String, date: Date, country: String, city: String,
                                           category: Int, actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyNameCategory(customerName: String, date: Date, category: Int, name: String, actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyTypeName(customerName: String, date: Date, propertyType: PropertyType, name: String,
                                actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyTypeCountryCity(customerName: String, date: Date, propertyType: PropertyType, country: String,
                                       city: String, actorRef: ActorRef, CancelOrReserve: String)
case class SendPropertyTypeCountryCityName(customerName: String, date: Date, propertyType: PropertyType,
                                           country: String, city: String, name: String, actorRef: ActorRef, CancelOrReserve: String)
case class RandomChosenDate(randomChosen: AvailableProperty, date: Date, replyTo: ActorRef, CancelOrReserve: String)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Client (PASSPORT: String, DATE: Date, PT: PropertyType, NAME: String, CITY: String, COUNTRY: String,
              CATEGORY: Int, searchActor: ActorRef, CancelOrReserve: String) extends Actor with ActorLogging {
  var CUSTOMER_NAME = ""
  CLIENTS.foreach { x =>
    if (x.passport_number == PASSPORT) {
      CUSTOMER_NAME = x.name
      println(s"Welcome to the system ${x.name}.")
      println("#######################################################################################################")
    }
  }
  if (CancelOrReserve == "Reserve"){
    if (DATE != null) {
      if (PT != NON) {
        if (NAME == "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for ${PT.toString}s with availability on ${DATE.toString} for reservation ...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendProperty(CUSTOMER_NAME, DATE, PT, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME != "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for properties with name $NAME , and availability on ${DATE.toString} for reservation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyName(CUSTOMER_NAME, DATE, NAME, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME == "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY != 0) {
                println(s"Start Searching for ${CATEGORY} star properties with availability on ${DATE.toString} for reservation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyCategory(CUSTOMER_NAME, DATE, CATEGORY, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME == "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for properties in ${CITY}, $COUNTRY, with availability on ${DATE.toString} for reservation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyCountryCity(CUSTOMER_NAME, DATE, COUNTRY, CITY, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME != "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for properties in ${CITY}, $COUNTRY, with name $NAME and with availability " +
                  s"on ${DATE.toString} for reservation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyCountryCityName(CUSTOMER_NAME, DATE, COUNTRY, CITY, NAME, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME == "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY != 0) {
                println(s"Start Searching for properties in ${CITY}, $COUNTRY, and with availability on " +
                  s"${DATE.toString} for reservation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyCountryCityCategory(CUSTOMER_NAME, DATE, COUNTRY, CITY, CATEGORY, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME != "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY != 0) {
                println(s"Start Searching for $PT with name, $NAME, and with availability on ${DATE.toString} for reservation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyNameCategory(CUSTOMER_NAME, DATE, CATEGORY, NAME, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT != NON) {
        if (NAME != "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for $CATEGORY star $PT and with availability on ${DATE.toString} for reservation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyTypeName(CUSTOMER_NAME, DATE, PT, NAME, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT != NON) {
        if (NAME == "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for $PT in $CITY, $COUNTRY, and with availability on ${DATE.toString} for reservation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyTypeCountryCity(CUSTOMER_NAME, DATE, PT, COUNTRY, CITY, self, "Reserve")
              }
            }
          }
        }
      }
      if (PT != NON) {
        if (NAME != "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for $PT in $CITY, $COUNTRY, with name $NAME and with availability on " +
                  s"${DATE.toString}... for reservation")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyTypeCountryCityName(CUSTOMER_NAME, DATE, PT, COUNTRY, CITY, NAME, self, "Reserve")
              }
            }
          }
        }
      }
    }
  }
  else {
    if (DATE != null) {
      if (PT != NON) {
        if (NAME == "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for ${PT.toString}s with availability on ${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendProperty(CUSTOMER_NAME, DATE, PT, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME != "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for properties with name $NAME , and availability on ${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyName(CUSTOMER_NAME, DATE, NAME, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME == "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY != 0) {
                println(s"Start Searching for ${CATEGORY} star properties with availability on ${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyCategory(CUSTOMER_NAME, DATE, CATEGORY, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME == "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for properties in ${CITY}, $COUNTRY, with availability on ${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyCountryCity(CUSTOMER_NAME, DATE, COUNTRY, CITY, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME != "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for properties in ${CITY}, $COUNTRY, with name $NAME and with availability " +
                  s"on ${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyCountryCityName(CUSTOMER_NAME, DATE, COUNTRY, CITY, NAME, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME == "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY != 0) {
                println(s"Start Searching for properties in ${CITY}, $COUNTRY, and with availability on " +
                  s"${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyCountryCityCategory(CUSTOMER_NAME, DATE, COUNTRY, CITY, CATEGORY, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT == NON) {
        if (NAME != "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY != 0) {
                println(s"Start Searching for $PT with name, $NAME, and with availability on ${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyNameCategory(CUSTOMER_NAME, DATE, CATEGORY, NAME, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT != NON) {
        if (NAME != "null") {
          if (CITY == "null") {
            if (COUNTRY == "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for $CATEGORY star $PT and with availability on ${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyTypeName(CUSTOMER_NAME, DATE, PT, NAME, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT != NON) {
        if (NAME == "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for $PT in $CITY, $COUNTRY, and with availability on ${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyTypeCountryCity(CUSTOMER_NAME, DATE, PT, COUNTRY, CITY, self, "Cancel")
              }
            }
          }
        }
      }
      if (PT != NON) {
        if (NAME != "null") {
          if (CITY != "null") {
            if (COUNTRY != "null") {
              if (CATEGORY == 0) {
                println(s"Start Searching for $PT in $CITY, $COUNTRY, with name $NAME and with availability on " +
                  s"${DATE.toString} for cancellation...")
                println("##############################################################################################" +
                  "#########")
                searchActor ! SendPropertyTypeCountryCityName(CUSTOMER_NAME, DATE, PT, COUNTRY, CITY, NAME, self, "Cancel")
              }
            }
          }
        }
      }
    }
  }
  override def receive: Receive = {
    case AvailableProperties(availableProperties, cancelOrReserve) => {
      var i = 1
      println("Available properties:")
      availableProperties.foreach { X =>
        println(s"$i- ${X.name} ${X.category.toString} star ${X.PropertyType.toString} with id number ${X.id} in " +
          s"${X.city}, ${X.country} is available on ${DATE} for ${X.price.toString} euros.")
        i = i + 1
      }
      println("#######################################################################################################")
      val random_var = new Random
      val randomChosen = availableProperties(random_var.nextInt(availableProperties.length))
      println(s"${randomChosen.customerName} choose ${randomChosen.name} ${randomChosen.category.toString} star " +
        s"${randomChosen.PropertyType.toString} with id number ${randomChosen.id} in ${randomChosen.city}, " +
        s"${randomChosen.country} for ${randomChosen.price.toString} euros.")
      println("#######################################################################################################")
      randomChosen.actorRef ! RandomChosenDate(randomChosen, DATE, self, cancelOrReserve)
    }
    case ReservationIsFailed => {
      println(s"Reservation has been failed by $CUSTOMER_NAME.")
      println("#######################################################################################################")
      println("")
      println("")
    }
    case ReservationIsSuccessfull => {
      println(s"Reservation has been complete by $CUSTOMER_NAME.")
      println("#######################################################################################################")
      println("")
      println("")
    }
    // Second session: Cancellation added
    case CancellationIsFailed => {
      println(s"Cancellation has been failed by $CUSTOMER_NAME.")
      println("#######################################################################################################")
      println("")
      println("")
    }
    case CancellationIsSuccessfull => {
      println(s"Cancellation has been complete by $CUSTOMER_NAME.")
      println("#######################################################################################################")
      println("")
      println("")
    }
    case _ =>
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class SystemService extends Actor{
  override def receive: Receive = {
    case SendProperty(customerName, date, propertyType, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.PropertyType == propertyType){
              APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
            }
          }
        }
        println(s"Sending back the List for make a reservation by $customerName ...")
      }
      else {
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.PropertyType == propertyType){
              APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyName(customerName, date, name, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.name == name){
              APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else {
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.name == name){
              APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyCategory(customerName, date, category, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.category == category){
              APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else {
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.category == category){
              APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyCountryCity(customerName, date, country, city, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.country == country){
              if (X.city == city){
                APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                  X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
              }
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else {
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.country == country){
              if (X.city == city){
                APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                  X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
              }
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyCountryCityName(customerName, date, country, city, name, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.country == country){
              if (X.city == city){
                if (X.name == name){
                  APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                    X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
                }
              }
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else{
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.country == country){
              if (X.city == city){
                if (X.name == name){
                  APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                    X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
                }
              }
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyCountryCityCategory(customerName, date, country, city, category, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.country == country){
              if (X.city == city){
                if (X.category == category){
                  APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                    X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
                }
              }
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else {
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.country == country){
              if (X.city == city){
                if (X.category == category){
                  APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                    X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
                }
              }
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyNameCategory(customerName, date, category, name, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.category == category){
              if (X.name == name) {
                APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                  X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
              }
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else {
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.category == category){
              if (X.name == name) {
                APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                  X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
              }
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyTypeName(customerName, date, propertyType, name, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.PropertyType == propertyType){
              if (X.name == name) {
                APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                  X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
              }
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else {
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.PropertyType == propertyType){
              if (X.name == name) {
                APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                  X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
              }
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyTypeCountryCity(customerName, date, propertyType, country, city, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.PropertyType == propertyType){
              if (X.country == country){
                if (X.city == city){
                  APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                    X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
                }
              }
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else{
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.PropertyType == propertyType){
              if (X.country == country){
                if (X.city == city){
                  APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                    X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
                }
              }
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case SendPropertyTypeCountryCityName(customerName, date, propertyType, country, city, name, replyTo, cancelOrReserve) => {
      var APS = AvailableProperties(ListBuffer(), cancelOrReserve)
      if (cancelOrReserve == "Reserve"){
        PROPERTIES.foreach {  X =>
          if (!X.NotAvailable.contains(date)){
            if (X.PropertyType == propertyType){
              if (X.country == country){
                if (X.city == city){
                  if (X.name == name){
                    APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                      X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
                  }
                }
              }
            }
          }
        }
        println(s"Sending back the list for make a reservation by $customerName ...")
      }
      else {
        PROPERTIES.foreach {  X =>
          if (X.NotAvailable.contains(date)){
            if (X.PropertyType == propertyType){
              if (X.country == country){
                if (X.city == city){
                  if (X.name == name){
                    APS.availableProperties = APS.availableProperties :+ AvailableProperty(customerName, X.id, X.name,
                      X.PropertyType, X.category, X.country, X.city, X.Price, X.NotAvailable, self)
                  }
                }
              }
            }
          }
        }
        println(s"Sending back the list for make a cancellation by $customerName ...")
      }
      println("#######################################################################################################")
      replyTo ! APS
    }
    case RandomChosenDate(availableProperty, date, replyTo, cancelOrReserve) => {
      if (cancelOrReserve == "Reserve"){
        val childReservation = context.actorOf(Props(new ReservationService(date, replyTo)))
        childReservation ! availableProperty
      }
      else {
        val childCancellation = context.actorOf(Props(new CancellationService(date, replyTo)))
        childCancellation ! availableProperty
      }
    }
    case _ =>
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class ReservationService (DATE: Date, replyTo: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case AvailableProperty(customerName, id, name, propertyType, category, country, city, price, date, replyTo2) => {
      var index = PROPERTIES.indexOf(property(id, name, propertyType, category, country, city, date, price))
      if (PROPERTIES(index).NotAvailable.contains(DATE)) {
        replyTo ! ReservationIsFailed
      }
      else{
        PROPERTIES(index).NotAvailable = PROPERTIES(index).NotAvailable :+ DATE
        replyTo ! ReservationIsSuccessfull
      }
      context.stop(self)
    }
    case _ => 
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Second session cancellation service
class CancellationService (DATE: Date, replyTo: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case AvailableProperty(customerName, id, name, propertyType, category, country, city, price, date, replyTo2) => {
      var index = PROPERTIES.indexOf(property(id, name, propertyType, category, country, city, date, price))
      if (PROPERTIES(index).NotAvailable.contains(DATE)) {
        PROPERTIES(index).NotAvailable -= DATE
        replyTo ! CancellationIsSuccessfull
      }
      else{
        replyTo ! CancellationIsFailed
      }
      context.stop(self)
    }
    case _ =>
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  val system = ActorSystem("Booking")
  val searchActor = system.actorOf(Props[SystemService], "search")
  val date = format.parse("2022-09-11")
//  val searchHotel = system.actorOf(Props(new Client("Y44986738", date, Hotel, "null", "null",
//    "null", 0, searchActor, "Reserve")), "searchForHotel")
//  Thread.sleep(1000)
  val searchHotel2 = system.actorOf(Props(new Client("Y44986738", date, Hotel, "null", "null",
    "null", 0, searchActor, "Cancel")), "searchForHotel")
  Thread.sleep(1000)
//  val searchHotel = system.actorOf(Props(new Client("Y44986738", date, Hotel, "null", "null",
//    "null", 0, searchActor)), "searchForHotel")
//  Thread.sleep(1000)
//  val searchApartment = system.actorOf(Props(new Client("Y44986739", date, Apartment, "null",
//    "null", "null", 0, searchActor)), "searchForApartment")
//  Thread.sleep(1000)
//  val searchResort = system.actorOf(Props(new Client("Y44986740", date, Resort, "null",
//    "null", "null", 0, searchActor)), "searchForResort")
//  Thread.sleep(1000)
//  val searchName = system.actorOf(Props(new Client("Y44986741", date, NON, "Eram", "null",
//    "null", 0, searchActor)), "searchForName")
//  Thread.sleep(1000)
//  val search4 = system.actorOf(Props(new Client("Y44986742", date, NON, "null", "Tehran",
//    "Iran", 5, searchActor)), "search4")
//  Thread.sleep(1000)
//  val search5 = system.actorOf(Props(new Client("Y44986743", date, NON, "null", "null",
//    "null", 5, searchActor)), "search5")
//  Thread.sleep(1000)
  system.terminate()
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Notes:
// -----------------------------------------------------------------------------------
// CancelOrReserve parameter has been added to Client for the second session project.
// CancelOrReserve parameter has been added to sendProperty and others for sending
// different types of search for the second session project.
// Add cancelOrReserve part to SystemService for adding cancel part for the second
// session.
// Some reserved dates has been added for the cancellation part.
// Add cancelOrReserve to AvailableProperties.
// Add cancelOrReserve to RandomChosenDate.
// Add CancellationService child actor.