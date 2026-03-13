

## 1. Create a New Route
```bash
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-01",
    "routeName": "Mirpur - Motijheel",
    "routeLine": "Mirpur 10 → Farmgate → Shahbag → Motijheel",
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
    "pickupPoints": [
      {
        "placeName": "Mirpur 10",
        "placeDetails": "In front of Mirpur 10 Metro Station",
        "pickupTime": "07:30",
        "stopOrder": 1
      },
      {
        "placeName": "Farmgate",
        "placeDetails": "Near Farmgate Bus Stand",
        "pickupTime": "07:45",
        "stopOrder": 2
      },
      {
        "placeName": "Shahbag",
        "placeDetails": "Opposite of Dhaka University",
        "pickupTime": "08:00",
        "stopOrder": 3
      },
      {
        "placeName": "Motijheel",
        "placeDetails": "Near Motijheel Shapla Chattar",
        "pickupTime": "08:20",
        "stopOrder": 4
      }
    ]
  }'
```

## 2. Create Another Route (Different Bus)
```bash
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-02",
    "routeName": "Uttara - Gulshan",
    "routeLine": "Uttara → Banani → Gulshan",
    "operatingDays": ["MONDAY", "WEDNESDAY", "FRIDAY"],
    "pickupPoints": [
      {
        "placeName": "Uttara Sector 3",
        "placeDetails": "Near Uttara Metro Station",
        "pickupTime": "08:00",
        "stopOrder": 1
      },
      {
        "placeName": "Banani",
        "placeDetails": "Banani Bus Stand",
        "pickupTime": "08:30",
        "stopOrder": 2
      },
      {
        "placeName": "Gulshan 1",
        "placeDetails": "Gulshan Circle 1",
        "pickupTime": "08:45",
        "stopOrder": 3
      }
    ]
  }'
```

## 3. Create Route with Weekend Only Service
```bash
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-03",
    "routeName": "Dhanmondi - Motijheel",
    "routeLine": "Dhanmondi 32 → Science Lab → Motijheel",
    "operatingDays": ["SATURDAY", "SUNDAY"],
    "pickupPoints": [
      {
        "placeName": "Dhanmondi 32",
        "placeDetails": "Dhanmondi 32 Bus Stop",
        "pickupTime": "09:00",
        "stopOrder": 1
      },
      {
        "placeName": "Science Lab",
        "placeDetails": "Science Lab Bus Stop",
        "pickupTime": "09:15",
        "stopOrder": 2
      },
      {
        "placeName": "Motijheel",
        "placeDetails": "Motijheel Shapla Chattar",
        "pickupTime": "09:45",
        "stopOrder": 3
      }
    ]
  }'
```

## 4. Get All Routes
```bash
curl -X GET http://localhost:8080/api/v1/routes
```

## 5. Get Route by ID
```bash
curl -X GET http://localhost:8080/api/v1/routes/1
```

## 6. Get Route by Bus Number
```bash
curl -X GET http://localhost:8080/api/v1/routes/bus/BUS-01
```

## 7. Get Routes by Operating Day
```bash
# Get routes operating on MONDAY
curl -X GET http://localhost:8080/api/v1/routes/day/MONDAY

# Get routes operating on SATURDAY
curl -X GET http://localhost:8080/api/v1/routes/day/SATURDAY
```

## 8. Update Route Status
```bash
# Set to INACTIVE
curl -X PATCH "http://localhost:8080/api/v1/routes/1/status?status=INACTIVE"

# Set back to ACTIVE
curl -X PATCH "http://localhost:8080/api/v1/routes/1/status?status=ACTIVE"

# Set to MAINTENANCE
curl -X PATCH "http://localhost:8080/api/v1/routes/2/status?status=MAINTENANCE"
```

## 9. Delete a Route
```bash
curl -X DELETE http://localhost:8080/api/v1/routes/3
```

## Additional Test Scenarios

### 10. Try Creating Route with Invalid Bus Number Format
```bash
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "12345",
    "routeName": "Test Route",
    "routeLine": "Test → Route",
    "operatingDays": ["MONDAY"],
    "pickupPoints": [
      {
        "placeName": "Test Point",
        "pickupTime": "07:30",
        "stopOrder": 1
      }
    ]
  }'
```

### 11. Try Creating Route with Invalid Time Format
```bash
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-04",
    "routeName": "Invalid Time Test",
    "routeLine": "Test → Route",
    "operatingDays": ["MONDAY"],
    "pickupPoints": [
      {
        "placeName": "Test Point",
        "pickupTime": "25:30",
        "stopOrder": 1
      }
    ]
  }'
```

### 12. Try Creating Duplicate Route (Should Fail)
```bash
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-01",
    "routeName": "Duplicate Route",
    "routeLine": "Duplicate → Route",
    "operatingDays": ["MONDAY"],
    "pickupPoints": [
      {
        "placeName": "Duplicate Point",
        "pickupTime": "10:00",
        "stopOrder": 1
      }
    ]
  }'
```

### 13. Try Creating Route with Empty Pickup Points (Should Fail)
```bash
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-05",
    "routeName": "No Pickup Points",
    "routeLine": "Test → Route",
    "operatingDays": ["MONDAY"],
    "pickupPoints": []
  }'
```

## Note:
- Replace `http://localhost:8080` with your actual server URL if different
- The enums `DAY` and `ROUTE_STATUS` should have these values:
  - DAY: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
  - ROUTE_STATUS: ACTIVE, INACTIVE, MAINTENANCE
- All times should be in 24-hour format (HH:mm)
- Bus number must follow pattern: BUS-01 or BUS-001