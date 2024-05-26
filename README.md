# Veterinary Clinic Management System API

This project provides a RESTful API to manage daily operations of a veterinary clinic.

---

## Features

### Animal and Owner Management

#### Animal Operations:

- **Manage Animals:** Perform operations like saving, updating, viewing, and deleting animals.
- **Filter Animals:** API endpoints are provided to list animals filtered by name.
- **List Animals by Owner:** A special API endpoint exists to list all animals based on owner information.

#### Customer Operations:

- **Manage Owners:** Operations such as saving, updating, viewing, and deleting owners can be performed.
- **Filter Owners:** API endpoints are provided to list owners filtered by name.

### Vaccination Management

#### Vaccination Operations:

- **Manage Vaccinations:** Perform operations like saving, updating, viewing, and deleting vaccinations applied to animals.
- **Check Expiry Dates:** Prevents entry of new vaccinations by checking the expiry dates of the same type of vaccinations.
- **List Vaccination Records:** An API endpoint is provided to list all vaccination records for a specific animal.
- **List Vaccinations within a Specific Date Range:** A special API endpoint allows listing vaccinations within a specific date range.

### Appointment Management

#### Appointment Operations:

- **Manage Appointments:** Create, update, view, and delete appointments for animal vaccinations and examinations.
- **Filter Appointments:** API endpoints are provided to filter appointments by date and time information.
- **Doctor Availability:** Only hourly appointments are allowed for each doctor, ensuring the doctor's availability and no other appointments at the specified time.

### Veterinary Doctor Management

#### Doctor Operations:

- **Manage Doctors:** Perform operations like saving, updating, viewing, and deleting veterinary doctors.

#### Availability Management:

- **Manage Availability:** Operations to add, update, view, and delete available days for doctors are implemented.
- **List Available Days:** The days a doctor works are stored in the system as LocalDate, and API endpoints are provided for users to access this information.

---


## UML Diagram

![Mind map (2)](https://github.com/halilibrahimsaltas/veterinary-management-system/assets/82754847/506ff5fa-082f-4d19-b869-6d421c6c60be)

---

# Veterinary Management System API Endpoints Documentation

This project contains a RESTful API that provides a veterinary clinic management system. Below is a list of all the endpoints that the API provides.

## API

### Animal

| HTTP Method | HTTP Path                   | Action             |
|-------------|-----------------------------|--------------------|
| `GET`       | `/v1/animals`                     | get animals by paging  |
| `GET`       | `/v1/animals/{id}`                | get animals by id      |
| `GET`       | `/v1/animals/byOwner/{customerId}`| get animals by customerId      |
| `GET`       | `/v1/animals/name/{name}`         | search animals by name |
| `POST`      | `/v1/animals`                     | add new animals        |
| `PUT`       | `/v1/animals       `              | update animals         |
| `DELETE`    | `/v1/animals/{id}`                | delete animals         |

### Customer

| HTTP Method | HTTP Path                  | Action               |
|-------------|----------------------------|----------------------|
| `GET`       | `/v1/customers`                     | get customers by paging    |
| `GET`       | `/v1/customers/{customerId}`        | get customer by id  |
| `GET`       | `/v1/customers/animals/{customerId}`| get animals by id   |
| `GET`       | `/v1/customers/filter/{name}`       | get customer by name|
| `POST`      | `/v1/customers/`                    | add new customer    |
| `PUT`       | `/v1/customers/`                    | updatecustomer      |
| `DELETE`    | `/v1/customers/{id}`                | delete customer     |

### Doctor

| HTTP Method | HTTP Path        | Action        |
|-------------|------------------|---------------|
| `GET`       | `/v1/doctors`            | get  doctors by paging  |
| `GET`       | `/v1/doctors/{doctorId}` | get doctor by id |
| `POST`      | `/v1/doctors`           | add new doctor   |
| `PUT`       | `/v1/doctors`           | update doctor    |
| `DELETE`    | `/v1/doctors/{doctorId}` | delete doctor    |

### Vaccine

| HTTP Method | HTTP Path                                                               | Action                                   |
|-------------|-------------------------------------------------------------------------|------------------------------------------|
| `GET`       | `/v1/vaccine`                                                          | get vaccines by paging                     |
| `GET`       | `/v1/vaccine/{id}`                                                     | get vaccine by id                          |
| `GET`       | `/v1/vaccine/animals/expiring`                                         |  get vaccines ending in the given period   |
| `GET`       | `/v1/vaccine/animal/{animalId}`                                        | get all vaccines administered to the animal|
| `POST`      | `/v1/vaccine`                                                          | add new vaccination to the pet             |
| `PUT`       | `/v1/vaccine `                                                         | update vaccination                         |
| `DELETE`    | `/v1/vaccine/delete/{vaccineId}`                                       | delete vaccination                         |

### Appointments

| HTTP Method | HTTP Path                                               | Action                                      |
|-------------|---------------------------------------------------------|---------------------------------------------|
| `GET`       | `/v1/appointments/`                                    | get  appointments by paging                    |
| `GET`       | `/v1/appointments/{appointmentId}`                     | get appointment  by id                         |
| `GET`       | `/v1/appointments/byDateRangeAndDoctor`                | get appointments of doctor in the given period |
| `GET`       | `/v1/appointments/byDateRangeAndAnimal`                | get appointments of animal in the given period |
| `POST`      | `/v1/appointments`                                     | add new appointment                            |
| `PUT`       | `/v1/appointments `                                    | update appointment                             |
| `DELETE`    | `/v1/appointments/{appointmentId}`                     | delete appointment                             |

### AvailableDay

| HTTP Method | HTTP Path                      | Action            |
|-------------|--------------------------------|-------------------|
| `GET`       | `/v1/availableDates`                | get  availabledate  by paging   |
| `GET`       | `/v1/availableDates/{id}`           | get availabledate  by id        |
| `POST`      | `/v1/availableDates`                | add new availabledate           |
| `PUT`       | `/v1/availableDates`                | update availabledate            |
| `DELETE`    | `/v1/availableDates/{date_id}`      | delete availabledate            |

---
### UML
```
