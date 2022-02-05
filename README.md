## Hotel Booking API Documentations

### Get Started

##### Configuring SQL

- Open ```src/main/resources/application.yml```

- In **application.yml**, configure the datasource: 

  1. Change the username and password

  2. Make database with name *hotelbooking*

     **OR**

     Change the database name to your desired database name in the url

  ```
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/hotelbooking?serverTimesone=UTC
      username: development
      password: 12345678
  ```

  

##### Running Spring Boot

Run spring boot application by using command:

```
mvn spring-boot:run
```

Voila! Your server is now active on port 8080



##### API Documentations

After the server run, go to url path https://localhost:8080/swagger-ui.html for accessing the full documented API



#### About The Project

The project is about hotel booking services which will be used for 

- Self booking by guest
- Administration by the hotel manager the hotel and room info which will be used for Room Booking

This project is using JWT for authentication, there are several role provided using Enum, which is:

- **ADMIN**
- **HOTEL_MANAGER**
- **HOTEL_EMPLOYEE**
- **GUEST**



There are some restriction in the API request based on token's role. These are the permissions:

 **WITHOUT TOKEN:**

- `GET`   : /authenticate
- `POST` : /account/register
- `GET`   : /region/**
- `GET`   : /city/**
- `GET`   : /company/**
- `GET`   : /hotel/**
- `GET`   : /room/**

**GUEST**

- ``ALL WITHOUT TOKEN ACCESS``
- `PUT`   : /account/edit-account
- `GET`   : /account/me
- `POST` : /identity/add                                       -> `automatically added to your identity foreign key`
- `GET`   : /identity/{id}                                        -> `only your identity id
- `PUT`   : /identity/{id}                                        -> `only your identity id`
- `GET`   : /identity                                                -> `only show all your identity connected to your account`
- `POST` : /booking/book
- `GET`   : /booking/{id}/cancel                             -> `can only cancel booking that you've made`
- `GET`   : /booking/{id}/check-in                          -> `can only check-in booking that you've made`
- `GET`   : /booking                                                  -> `show all booking that you've made` 

**HOTEL_EMPLOYEE**

- `ALL GUEST ACCESS`
- `GET`   : /booking/{id}/cancel                              -> `can cancel other account's booking with this role`
- `GET`   : /booking/{id}/check-in                          -> `can check-in other account's booking with this role`
- `GET`   : /booking
- `GET`   : /booking/hotel/{hotelId}
- `GET`   : /booking//hotel/{hotelId}/all-time
- `GET`   : /booking/all/room/{roomId}
- `GET`   : /booking/all/room/{roomId}/all-time
- `GET`   : /contact-person
- `GET`   : /contact-person/{id}
- `POST` : /contact-person
- `PUT`   : /contact-person/{id} 
- `POST` : /contact-person/{id}/upload
- `GET`   : /contact-person/download{id}.png

**HOTEL_MANAGER**

- `ALL HOTEL_EMPLOYEE ACCESS`
- `GET`   : /{username}/make-hotel-employee
- `POST` : /company
- `PUT`   : /company/{id}
- `POST` : /company/upload/{id}
- `POST` : /city
- `PUT`   : /city/{id}
- `POST` : /city/upload/{id}
- `POST` : /room
- `PUT`   : /room/{id}
- `POST` : /room/upload/{id}

**ADMIN**

- `ALL HOTEL_MANAGER ACCESS`                                                 -> `Inaccesible for POST and PUT method in entity company, hotel, and room` 
- `GET`   : /{username}/make-hotel-manager
- `POST` : /region
- `POST` : /city
