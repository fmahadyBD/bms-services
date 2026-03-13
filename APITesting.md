
# BMS Services API - Postman Testing Guide

## 📌 Base URL
```
http://localhost:8080
```

---

## 🔐 Authentication APIs (No Token Required)

### 1. Register Student (Public)
**Method:** `POST`  
**URL:** `{{base_url}}/auth/register/student`  
**Auth Type:** `No Auth`

**Body (raw JSON):**
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
  "shift": "Morning",
  "password": "Pass@1234"
}
```

**Expected Response:** `202 Accepted` (No body)

---

### 2. Register Manager (Public)
**Method:** `POST`  
**URL:** `{{base_url}}/auth/register/manager`  
**Auth Type:** `No Auth`

**Body (raw JSON):**
```json
{
  "firstname": "Admin",
  "lastname": "User",
  "email": "admin@example.com",
  "password": "Admin@1234",
  "employeeId": "MGR001",
  "department": "Administration",
  "position": "Senior Manager"
}
```

**Expected Response:** `202 Accepted` (No body)

---

### 3. Login (Get JWT Token)
**Method:** `POST`  
**URL:** `{{base_url}}/auth/authenticate`  
**Auth Type:** `No Auth`

**Body (raw JSON):**
```json
{
  "email": "john.doe@example.com",
  "password": "Pass@1234"
}
```

**Success Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdHVkZW50SWQiOiJCQkEtMjAyMy0wMTIiLCJyb2xlIjoiU1RVREVOVCIsImZ1bGxOYW1lIjoiTWlrZSBKb2huc29uIiwidXNlclR5cGUiOiJTVFVERU5UIiwiZGVwYXJ0bWVudCI6IkJ1c2luZXNzIEFkbWluaXN0cmF0aW9uIiwidXNlcm5hbWUiOiJtaWtlLmpAZXhhbXBsZS5jb20iLCJzdWIiOiJtaWtlLmpAZXhhbXBsZS5jb20iLCJpYXQiOjE3NzMzNzU3NTMsImV4cCI6MTc3MzQ2MjE1M30.MfSXYsB7RE5fD5mRwX-UXZ_jwvHubshrawA7CLMRjx1cCgV8R9Op7m3X3z5lvpTf",
  "userType": "STUDENT",
  "role": "STUDENT"
}
```

**⚠️ IMPORTANT:** Copy the `token` value from the response. You'll need it for all subsequent API calls.

---

### 4. Logout
**Method:** `POST`  
**URL:** `{{base_url}}/auth/logout`  
**Auth Type:** `Bearer Token`  
**Token:** `Paste your JWT token here`

**Headers:**
| Key | Value |
|-----|-------|
| Authorization | Bearer YOUR_JWT_TOKEN |

**Expected Response:** `200 OK` (No body)

---

## 📚 Student APIs (Require JWT Token)

**For all Student APIs, set Auth Type to `Bearer Token` and paste your JWT token**

### 5. Get All Students (Manager Only)
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/students`  
**Auth Type:** `Bearer Token`

**Headers:**
| Key | Value |
|-----|-------|
| Authorization | Bearer YOUR_JWT_TOKEN |

---

### 6. Get Student by ID
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001`  
**Auth Type:** `Bearer Token`

**URL Parameters:**
| Parameter | Value |
|-----------|-------|
| studentId | CSE-2021-001 |

---

### 7. Search Student by Email
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/students/search/email?email=john.doe@example.com`  
**Auth Type:** `Bearer Token`

**Query Parameters:**
| Key | Value |
|-----|-------|
| email | john.doe@example.com |

---

### 8. Search Student by Phone
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/students/search/phone?phone=%2B8801712345678`  
**Auth Type:** `Bearer Token`

**Query Parameters:**
| Key | Value |
|-----|-------|
| phone | +8801712345678 (URL encoded: %2B8801712345678) |

---

### 9. Get Students by Department and Batch
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/students/department/Computer Science/batch/2021`  
**Auth Type:** `Bearer Token`

**URL Parameters:**
| Parameter | Value |
|-----------|-------|
| department | Computer Science |
| batch | 2021 |

---

### 10. Get Students by Route
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/students/route/1`  
**Auth Type:** `Bearer Token`

**URL Parameters:**
| Parameter | Value |
|-----------|-------|
| routeId | 1 |

---

### 11. Update Student
**Method:** `PATCH`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001`  
**Auth Type:** `Bearer Token`

**Headers:**
| Key | Value |
|-----|-------|
| Content-Type | application/json |

**Body (raw JSON):**
```json
{
  "name": "John Updated Doe",
  "email": "john.updated@example.com",
  "phoneNumber": "+8801712345679",
  "address": "House 20, Road 8, Banani, Dhaka",
  "shift": "Day"
}
```

---

### 12. Assign Route to Student (Manager Only)
**Method:** `PATCH`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001/assign-route/1`  
**Auth Type:** `Bearer Token`

**URL Parameters:**
| Parameter | Value |
|-----------|-------|
| studentId | CSE-2021-001 |
| routeId | 1 |

---

### 13. Remove Route from Student (Manager Only)
**Method:** `PATCH`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001/remove-route`  
**Auth Type:** `Bearer Token`

---

### 14. Get Student's Routines
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001/routines`  
**Auth Type:** `Bearer Token`

---

### 15. Block Student (Manager Only)
**Method:** `PATCH`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001/block`  
**Auth Type:** `Bearer Token`

---

### 16. Unblock Student (Manager Only)
**Method:** `PATCH`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001/unblock`  
**Auth Type:** `Bearer Token`

---

### 17. Change Password
**Method:** `PATCH`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001/change-password`  
**Auth Type:** `Bearer Token`

**Headers:**
| Key | Value |
|-----|-------|
| Content-Type | application/json |

**Body (raw JSON):**
```json
{
  "oldPassword": "Pass@1234",
  "newPassword": "NewPass@5678"
}
```

---

### 18. Delete Student (Manager Only)
**Method:** `DELETE`  
**URL:** `{{base_url}}/api/v1/students/CSE-2021-001`  
**Auth Type:** `Bearer Token`

---

## 🚌 Route APIs (Require JWT Token - Manager Only)

### 19. Create Route
**Method:** `POST`  
**URL:** `{{base_url}}/api/v1/routes`  
**Auth Type:** `Bearer Token`

**Headers:**
| Key | Value |
|-----|-------|
| Content-Type | application/json |

**Body (raw JSON):**
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
    }
  ],
  "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]
}
```

---

### 20. Get All Routes
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/routes`  
**Auth Type:** `Bearer Token`

---

### 21. Get Route by ID
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/routes/1`  
**Auth Type:** `Bearer Token`

---

### 22. Get Route by Bus Number
**Method:** `GET`  
**URL:** `{{base_url}}/api/v1/routes/bus/BUS-01`  
**Auth Type:** `Bearer Token`

---

### 23. Update Route Status
**Method:** `PATCH`  
**URL:** `{{base_url}}/api/v1/routes/1/status?status=INACTIVE`  
**Auth Type:** `Bearer Token`

---

### 24. Delete Route
**Method:** `DELETE`  
**URL:** `{{base_url}}/api/v1/routes/1`  
**Auth Type:** `Bearer Token`

---

## 📅 Routine APIs (Require JWT Token - Manager Only)

### 25. Create Routine
**Method:** `POST`  
**URL:** `{{base_url}}/api/v1/routines`  
**Auth Type:** `Bearer Token`

**Headers:**
| Key | Value |
|-----|-------|
| Content-Type | application/json |

**Body (raw JSON):**
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

---

### 26. Assign Students to Routine
**Method:** `POST`  
**URL:** `{{base_url}}/api/v1/routines/1/students`  
**Auth Type:** `Bearer Token`

**Headers:**
| Key | Value |
|-----|-------|
| Content-Type | application/json |

**Body (raw JSON):**
```json
[1, 2, 3, 4, 5]
```

---

## 📝 Postman Setup Tips

### 1. Create Environment Variables
Create a Postman environment with these variables:
| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| base_url | http://localhost:8080 | http://localhost:8080 |
| token | | (paste your JWT token here) |
| student_id | CSE-2021-001 | CSE-2021-001 |
| route_id | 1 | 1 |

### 2. Set Authorization Globally
In your Postman collection, go to **Authorization** tab and set:
- **Type:** `Bearer Token`
- **Token:** `{{token}}`

This will automatically add the token to all requests in the collection.

### 3. Testing Flow
1. First register a student or manager
2. Login to get token
3. Copy token to environment variable
4. Test all other endpoints

---

## ⚠️ Important Notes

- **Registration endpoints** (`/auth/register/*`) do NOT require authentication
- **Login endpoint** (`/auth/authenticate`) does NOT require authentication
- **All other endpoints** require a valid JWT token
- Students can only access/modify their own data
- Managers can access/modify all data
- Always include `Content-Type: application/json` header for POST/PUT/PATCH requests
- The token expires after 24 hours (86400000 ms)