# BMS Services API Testing Guide

## Base URL
```
http://localhost:8080/api/v1
```

---

## 📚 Student APIs

### 1. Register Student
**Endpoint:** `POST http://localhost:8080/auth/register/student 
`

**Request Body:**
```json
http://localhost:8080/auth/register/student \

```

**Success Response (201 Created):**
```json
{
 
  "studentId": "CSE-2021-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+8801712345678",
  "address": "House 12, Road 5, Dhanmondi, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": false,
  "shift": "Morning",
  "routeId": null,
  "routeName": null,
  "busNo": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 2. Student Login
**Endpoint:** `POST /students/login`

**Request Body:**
```json
{
  "studentId": "CSE-2021-001",
  "password": "Pass@1234"
}
```

**Success Response (200 OK):**
```json
{
  "message": "Login successful - Implement JWT authentication"
}
```

---

### 3. Get All Students
**Endpoint:** `GET /students`

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "studentId": "CSE-2021-001",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+8801712345678",
    "address": "House 12, Road 5, Dhanmondi, Dhaka",
    "department": "Computer Science",
    "batch": "2021",
    "gender": "MALE",
    "blocked": false,
    "shift": "Morning",
    "routeId": null,
    "routeName": null,
    "busNo": null,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "studentId": "EEE-2021-045",
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phoneNumber": "+8801812345678",
    "address": "House 5, Road 2, Gulshan, Dhaka",
    "department": "Electrical Engineering",
    "batch": "2021",
    "gender": "FEMALE",
    "blocked": false,
    "shift": "Day",
    "routeId": 1,
    "routeName": "Gulshan - GUB",
    "busNo": "BUS-01",
    "createdAt": "2024-01-15T11:45:00",
    "updatedAt": "2024-01-15T11:45:00"
  }
]
```

---

### 4. Get Student by ID
**Endpoint:** `GET /students/CSE-2021-001`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "studentId": "CSE-2021-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+8801712345678",
  "address": "House 12, Road 5, Dhanmondi, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": false,
  "shift": "Morning",
  "routeId": null,
  "routeName": null,
  "busNo": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 5. Search Student by Email
**Endpoint:** `GET /students/search/email?email=john.doe@example.com`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "studentId": "CSE-2021-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+8801712345678",
  "address": "House 12, Road 5, Dhanmondi, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": false,
  "shift": "Morning",
  "routeId": null,
  "routeName": null,
  "busNo": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 6. Search Student by Phone
**Endpoint:** `GET /students/search/phone?phone=%2B8801712345678`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "studentId": "CSE-2021-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+8801712345678",
  "address": "House 12, Road 5, Dhanmondi, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": false,
  "shift": "Morning",
  "routeId": null,
  "routeName": null,
  "busNo": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 7. Get Students by Department and Batch
**Endpoint:** `GET /students/department/Computer Science/batch/2021`

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "studentId": "CSE-2021-001",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+8801712345678",
    "address": "House 12, Road 5, Dhanmondi, Dhaka",
    "department": "Computer Science",
    "batch": "2021",
    "gender": "MALE",
    "blocked": false,
    "shift": "Morning",
    "routeId": null,
    "routeName": null,
    "busNo": null,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

---

### 8. Get Students by Route
**Endpoint:** `GET /students/route/1`

**Success Response (200 OK):**
```json
[
  {
    "id": 2,
    "studentId": "EEE-2021-045",
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phoneNumber": "+8801812345678",
    "address": "House 5, Road 2, Gulshan, Dhaka",
    "department": "Electrical Engineering",
    "batch": "2021",
    "gender": "FEMALE",
    "blocked": false,
    "shift": "Day",
    "routeId": 1,
    "routeName": "Gulshan - GUB",
    "busNo": "BUS-01",
    "createdAt": "2024-01-15T11:45:00",
    "updatedAt": "2024-01-15T11:45:00"
  }
]
```

---

### 9. Update Student
**Endpoint:** `PATCH /students/CSE-2021-001`

**Request Body:**
```json
{
  "name": "John Updated Doe",
  "email": "john.updated@example.com",
  "phoneNumber": "+8801712345679",
  "address": "House 20, Road 8, Banani, Dhaka",
  "shift": "Day"
}
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "studentId": "CSE-2021-001",
  "name": "John Updated Doe",
  "email": "john.updated@example.com",
  "phoneNumber": "+8801712345679",
  "address": "House 20, Road 8, Banani, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": false,
  "shift": "Day",
  "routeId": null,
  "routeName": null,
  "busNo": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T12:15:00"
}
```

---

### 10. Assign Route to Student
**Endpoint:** `PATCH /students/CSE-2021-001/assign-route/1`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "studentId": "CSE-2021-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+8801712345678",
  "address": "House 12, Road 5, Dhanmondi, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": false,
  "shift": "Morning",
  "routeId": 1,
  "routeName": "Gulshan - GUB",
  "busNo": "BUS-01",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T13:20:00"
}
```

---

### 11. Remove Route from Student
**Endpoint:** `PATCH /students/CSE-2021-001/remove-route`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "studentId": "CSE-2021-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+8801712345678",
  "address": "House 12, Road 5, Dhanmondi, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": false,
  "shift": "Morning",
  "routeId": null,
  "routeName": null,
  "busNo": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:10:00"
}
```

---

### 12. Get Student's Routines
**Endpoint:** `GET /students/CSE-2021-001/routines`

**Success Response (200 OK):**
```json
[
  {
    "routineId": 1,
    "courseName": "Data Structures",
    "courseCode": "CSE-2201",
    "teacherName": "Prof. Rahman",
    "day": "MONDAY",
    "startTime": "09:00:00",
    "endTime": "10:30:00",
    "roomNumber": "Room 401",
    "routineType": "CLASS"
  },
  {
    "routineId": 2,
    "courseName": "Algorithms Lab",
    "courseCode": "CSE-2202",
    "teacherName": "Dr. Khan",
    "day": "WEDNESDAY",
    "startTime": "14:00:00",
    "endTime": "16:00:00",
    "roomNumber": "Lab 203",
    "routineType": "CLASS"
  }
]
```

---

### 13. Block Student
**Endpoint:** `PATCH /students/CSE-2021-001/block`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "studentId": "CSE-2021-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+8801712345678",
  "address": "House 12, Road 5, Dhanmondi, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": true,
  "shift": "Morning",
  "routeId": null,
  "routeName": null,
  "busNo": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T15:00:00"
}
```

---

### 14. Unblock Student
**Endpoint:** `PATCH /students/CSE-2021-001/unblock`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "studentId": "CSE-2021-001",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+8801712345678",
  "address": "House 12, Road 5, Dhanmondi, Dhaka",
  "department": "Computer Science",
  "batch": "2021",
  "gender": "MALE",
  "blocked": false,
  "shift": "Morning",
  "routeId": null,
  "routeName": null,
  "busNo": null,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T16:20:00"
}
```

---

### 15. Change Password
**Endpoint:** `PATCH /students/CSE-2021-001/change-password`

**Request Body:**
```json
{
  "oldPassword": "Pass@1234",
  "newPassword": "NewPass@5678"
}
```

**Success Response (200 OK):** No content

---

### 16. Delete Student
**Endpoint:** `DELETE /students/CSE-2021-001`

**Success Response (204 No Content):** No content

---

## 🚌 Route APIs

### 17. Create Route
**Endpoint:** `POST /routes`

**Request Body:**
```json
{
  "busNo": "BUS-01",
  "routeName": "Gulshan - GUB",
  "routeLine": "Gulshan 1 → Banani → Farmgate → Shahbag → GUB",
  "pickupPoints": [
    {
      "placeName": "Gulshan 1",
      "placeDetails": "In front of Gulshan Circle 1",
      "pickupTime": "07:30",
      "stopOrder": 1
    },
    {
      "placeName": "Banani",
      "placeDetails": "Banani Railway Station",
      "pickupTime": "07:45",
      "stopOrder": 2
    },
    {
      "placeName": "Farmgate",
      "placeDetails": "Farmgate Bus Stop",
      "pickupTime": "08:00",
      "stopOrder": 3
    },
    {
      "placeName": "Shahbag",
      "placeDetails": "Shahbag Metro Station",
      "pickupTime": "08:15",
      "stopOrder": 4
    },
    {
      "placeName": "GUB",
      "placeDetails": "GUB Main Gate",
      "pickupTime": "08:30",
      "stopOrder": 5
    }
  ],
  "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]
}
```

**Success Response (201 Created):**
```json
{
  "id": 1,
  "busNo": "BUS-01",
  "routeName": "Gulshan - GUB",
  "routeLine": "Gulshan 1 → Banani → Farmgate → Shahbag → GUB",
  "status": "ACTIVE",
  "pickupPoints": [
    {
      "id": 1,
      "placeName": "Gulshan 1",
      "placeDetails": "In front of Gulshan Circle 1",
      "pickupTime": "07:30:00",
      "stopOrder": 1
    },
    {
      "id": 2,
      "placeName": "Banani",
      "placeDetails": "Banani Railway Station",
      "pickupTime": "07:45:00",
      "stopOrder": 2
    },
    {
      "id": 3,
      "placeName": "Farmgate",
      "placeDetails": "Farmgate Bus Stop",
      "pickupTime": "08:00:00",
      "stopOrder": 3
    },
    {
      "id": 4,
      "placeName": "Shahbag",
      "placeDetails": "Shahbag Metro Station",
      "pickupTime": "08:15:00",
      "stopOrder": 4
    },
    {
      "id": 5,
      "placeName": "GUB",
      "placeDetails": "GUB Main Gate",
      "pickupTime": "08:30:00",
      "stopOrder": 5
    }
  ],
  "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 18. Get All Routes
**Endpoint:** `GET /routes`

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "busNo": "BUS-01",
    "routeName": "Gulshan - GUB",
    "routeLine": "Gulshan 1 → Banani → Farmgate → Shahbag → GUB",
    "status": "ACTIVE",
    "pickupPoints": [
      {
        "id": 1,
        "placeName": "Gulshan 1",
        "placeDetails": "In front of Gulshan Circle 1",
        "pickupTime": "07:30:00",
        "stopOrder": 1
      }
    ],
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "busNo": "BUS-02",
    "routeName": "Mirpur - GUB",
    "routeLine": "Mirpur 10 → Kallyanpur → Shyamoli → Asad Gate → GUB",
    "status": "ACTIVE",
    "pickupPoints": [
      {
        "id": 6,
        "placeName": "Mirpur 10",
        "placeDetails": "Mirpur 10 Bus Stop",
        "pickupTime": "07:15:00",
        "stopOrder": 1
      }
    ],
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

---

### 19. Get Route by ID
**Endpoint:** `GET /routes/1`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "busNo": "BUS-01",
  "routeName": "Gulshan - GUB",
  "routeLine": "Gulshan 1 → Banani → Farmgate → Shahbag → GUB",
  "status": "ACTIVE",
  "pickupPoints": [
    {
      "id": 1,
      "placeName": "Gulshan 1",
      "placeDetails": "In front of Gulshan Circle 1",
      "pickupTime": "07:30:00",
      "stopOrder": 1
    },
    {
      "id": 2,
      "placeName": "Banani",
      "placeDetails": "Banani Railway Station",
      "pickupTime": "07:45:00",
      "stopOrder": 2
    },
    {
      "id": 3,
      "placeName": "Farmgate",
      "placeDetails": "Farmgate Bus Stop",
      "pickupTime": "08:00:00",
      "stopOrder": 3
    },
    {
      "id": 4,
      "placeName": "Shahbag",
      "placeDetails": "Shahbag Metro Station",
      "pickupTime": "08:15:00",
      "stopOrder": 4
    },
    {
      "id": 5,
      "placeName": "GUB",
      "placeDetails": "GUB Main Gate",
      "pickupTime": "08:30:00",
      "stopOrder": 5
    }
  ],
  "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 20. Get Route by Bus Number
**Endpoint:** `GET /routes/bus/BUS-01`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "busNo": "BUS-01",
  "routeName": "Gulshan - GUB",
  "routeLine": "Gulshan 1 → Banani → Farmgate → Shahbag → GUB",
  "status": "ACTIVE",
  "pickupPoints": [
    {
      "id": 1,
      "placeName": "Gulshan 1",
      "placeDetails": "In front of Gulshan Circle 1",
      "pickupTime": "07:30:00",
      "stopOrder": 1
    }
  ],
  "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 21. Get Routes by Day
**Endpoint:** `GET /routes/day/MONDAY`

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "busNo": "BUS-01",
    "routeName": "Gulshan - GUB",
    "status": "ACTIVE",
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]
  },
  {
    "id": 2,
    "busNo": "BUS-02",
    "routeName": "Mirpur - GUB",
    "status": "ACTIVE",
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]
  }
]
```

---

### 22. Update Route Status
**Endpoint:** `PATCH /routes/1/status?status=INACTIVE`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "busNo": "BUS-01",
  "routeName": "Gulshan - GUB",
  "routeLine": "Gulshan 1 → Banani → Farmgate → Shahbag → GUB",
  "status": "INACTIVE",
  "pickupPoints": [...],
  "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T17:00:00"
}
```

---

### 23. Delete Route
**Endpoint:** `DELETE /routes/1`

**Success Response (204 No Content):** No content

---

## 📅 Routine APIs

### 24. Create Routine
**Endpoint:** `POST /routines`

**Request Body:**
```json
{
  "courseName": "Data Structures",
  "courseCode": "CSE-2201",
  "teacherName": "Prof. Rahman",
  "day": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "10:30:00",
  "roomNumber": "Room 401",
  "department": "Computer Science",
  "batch": "2021",
  "routineType": "CLASS",
  "routeId": 1
}
```

**Success Response (201 Created):**
```json
{
  "id": 1,
  "courseName": "Data Structures",
  "courseCode": "CSE-2201",
  "teacherName": "Prof. Rahman",
  "day": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "10:30:00",
  "roomNumber": "Room 401",
  "department": "Computer Science",
  "batch": "2021",
  "routineType": "CLASS",
  "active": true,
  "routeId": 1,
  "busNo": "BUS-01",
  "studentCount": 0,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 25. Assign Students to Routine
**Endpoint:** `POST /routines/1/students`

**Request Body:**
```json
[1, 2, 3, 4, 5]
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "courseName": "Data Structures",
  "courseCode": "CSE-2201",
  "teacherName": "Prof. Rahman",
  "day": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "10:30:00",
  "roomNumber": "Room 401",
  "department": "Computer Science",
  "batch": "2021",
  "routineType": "CLASS",
  "active": true,
  "routeId": 1,
  "busNo": "BUS-01",
  "studentCount": 5,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:15:00"
}
```

---

### 26. Get Routines by Department and Batch
**Endpoint:** `GET /routines/department/Computer Science/batch/2021`

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "courseName": "Data Structures",
    "courseCode": "CSE-2201",
    "teacherName": "Prof. Rahman",
    "day": "MONDAY",
    "startTime": "09:00:00",
    "endTime": "10:30:00",
    "roomNumber": "Room 401",
    "department": "Computer Science",
    "batch": "2021",
    "routineType": "CLASS",
    "active": true,
    "routeId": 1,
    "busNo": "BUS-01",
    "studentCount": 5,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:15:00"
  },
  {
    "id": 2,
    "courseName": "Algorithms",
    "courseCode": "CSE-2203",
    "teacherName": "Dr. Khan",
    "day": "WEDNESDAY",
    "startTime": "11:00:00",
    "endTime": "12:30:00",
    "roomNumber": "Room 405",
    "department": "Computer Science",
    "batch": "2021",
    "routineType": "CLASS",
    "active": true,
    "routeId": 2,
    "busNo": "BUS-02",
    "studentCount": 3,
    "createdAt": "2024-01-15T11:30:00",
    "updatedAt": "2024-01-15T11:30:00"
  }
]
```

---

### 27. Get Routines by Day
**Endpoint:** `GET /routines/day/MONDAY`

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "courseName": "Data Structures",
    "courseCode": "CSE-2201",
    "teacherName": "Prof. Rahman",
    "day": "MONDAY",
    "startTime": "09:00:00",
    "endTime": "10:30:00",
    "roomNumber": "Room 401",
    "department": "Computer Science",
    "batch": "2021"
  },
  {
    "id": 3,
    "courseName": "Digital Logic",
    "courseCode": "EEE-2101",
    "teacherName": "Prof. Islam",
    "day": "MONDAY",
    "startTime": "14:00:00",
    "endTime": "15:30:00",
    "roomNumber": "Room 302",
    "department": "Electrical Engineering",
    "batch": "2021"
  }
]
```

---

### 28. Get Routines for Student
**Endpoint:** `GET /routines/student/CSE-2021-001`

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "courseName": "Data Structures",
    "courseCode": "CSE-2201",
    "teacherName": "Prof. Rahman",
    "day": "MONDAY",
    "startTime": "09:00:00",
    "endTime": "10:30:00",
    "roomNumber": "Room 401",
    "department": "Computer Science",
    "batch": "2021",
    "routineType": "CLASS",
    "active": true,
    "routeId": 1,
    "busNo": "BUS-01",
    "studentCount": 5
  }
]
```

---

### 29. Delete Routine
**Endpoint:** `DELETE /routines/1`

**Success Response (204 No Content):** No content

---

## ❌ Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    "Student ID is required",
    "Email must be valid"
  ],
  "path": "/api/v1/students/register"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found: CSE-2021-999",
  "path": "/api/v1/students/CSE-2021-999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Student ID already exists: CSE-2021-001",
  "path": "/api/v1/students/register"
}
```

---

## 📝 Testing with cURL Commands

### Register Student
```bash
curl -X POST http://localhost:8080/api/v1/students/register \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "CSE-2021-001",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+8801712345678",
    "address": "House 12, Road 5, Dhanmondi, Dhaka",
    "department": "Computer Science",
    "batch": "2021",
    "gender": "MALE",
    "shift": "Morning",
    "password": "Pass@1234"
  }'
```

### Create Route
```bash
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-01",
    "routeName": "Gulshan - GUB",
    "routeLine": "Gulshan 1 → Banani → Farmgate → Shahbag → GUB",
    "pickupPoints": [
      {
        "placeName": "Gulshan 1",
        "placeDetails": "In front of Gulshan Circle 1",
        "pickupTime": "07:30",
        "stopOrder": 1
      }
    ],
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]
  }'
```

### Create Routine
```bash
curl -X POST http://localhost:8080/api/v1/routines \
  -H "Content-Type: application/json" \
  -d '{
    "courseName": "Data Structures",
    "courseCode": "CSE-2201",
    "teacherName": "Prof. Rahman",
    "day": "MONDAY",
    "startTime": "09:00:00",
    "endTime": "10:30:00",
    "roomNumber": "Room 401",
    "department": "Computer Science",
    "batch": "2021",
    "routineType": "CLASS",
    "routeId": 1
  }'
```

### Assign Route to Student
```bash
curl -X PATCH http://localhost:8080/api/v1/students/CSE-2021-001/assign-route/1
```

### Get Student's Routines
```bash
curl -X GET http://localhost:8080/api/v1/students/CSE-2021-001/routines
```