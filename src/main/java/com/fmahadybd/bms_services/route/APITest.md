

## Route API Testing Commands

### 1. Create Routes

```bash
# Create Route 1: Mirpur - Motijheel (Weekday Service)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-001",
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

```bash
# Create Route 2: Uttara - Gulshan (Alternate Days)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-002",
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

```bash
# Create Route 3: Dhanmondi - Motijheel (Weekend Service)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-003",
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

```bash
# Create Route 4: Airport - Banani (Express Service)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-004",
    "routeName": "Airport - Banani Express",
    "routeLine": "Airport → Khilkhet → Banani",
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"],
    "pickupPoints": [
      {
        "placeName": "Airport",
        "placeDetails": "Hazrat Shahjalal International Airport",
        "pickupTime": "06:00",
        "stopOrder": 1
      },
      {
        "placeName": "Khilkhet",
        "placeDetails": "Khilkhet Bus Stop",
        "pickupTime": "06:20",
        "stopOrder": 2
      },
      {
        "placeName": "Banani",
        "placeDetails": "Banani Bus Stand",
        "pickupTime": "06:45",
        "stopOrder": 3
      }
    ]
  }'
```

### 2. Get All Routes
```bash
curl -X GET http://localhost:8080/api/v1/routes
```

### 3. Get Route by ID
```bash
curl -X GET http://localhost:8080/api/v1/routes/1
curl -X GET http://localhost:8080/api/v1/routes/2
curl -X GET http://localhost:8080/api/v1/routes/3
```

### 4. Get Route by Bus Number
```bash
curl -X GET http://localhost:8080/api/v1/routes/bus/BUS-001
curl -X GET http://localhost:8080/api/v1/routes/bus/BUS-002
curl -X GET http://localhost:8080/api/v1/routes/bus/BUS-003
```

### 5. Get Routes by Operating Day
```bash
# Monday routes
curl -X GET http://localhost:8080/api/v1/routes/day/MONDAY

# Friday routes
curl -X GET http://localhost:8080/api/v1/routes/day/FRIDAY

# Saturday routes
curl -X GET http://localhost:8080/api/v1/routes/day/SATURDAY

# Sunday routes
curl -X GET http://localhost:8080/api/v1/routes/day/SUNDAY
```

### 6. Get Routes by Status
```bash
# Get all ACTIVE routes
curl -X GET "http://localhost:8080/api/v1/routes/status/ACTIVE"

# Get all INACTIVE routes
curl -X GET "http://localhost:8080/api/v1/routes/status/INACTIVE"

# Get all MAINTENANCE routes
curl -X GET "http://localhost:8080/api/v1/routes/status/MAINTENANCE"
```

### 7. Update Route Status
```bash
# Set Route 1 to INACTIVE
curl -X PATCH "http://localhost:8080/api/v1/routes/1/status?status=INACTIVE"

# Set Route 2 to MAINTENANCE
curl -X PATCH "http://localhost:8080/api/v1/routes/2/status?status=MAINTENANCE"

# Set Route 3 back to ACTIVE
curl -X PATCH "http://localhost:8080/api/v1/routes/3/status?status=ACTIVE"
```

### 8. Full Update (PUT) - Replace Entire Route
```bash
curl -X PUT http://localhost:8080/api/v1/routes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-001-UPDATED",
    "routeName": "Mirpur - Motijheel (Updated)",
    "routeLine": "Mirpur 10 → Farmgate → Shahbag → Paltan → Motijheel",
    "status": "ACTIVE",
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"],
    "pickupPoints": [
      {
        "placeName": "Mirpur 10",
        "placeDetails": "In front of Mirpur 10 Metro Station",
        "pickupTime": "07:15",
        "stopOrder": 1
      },
      {
        "placeName": "Farmgate",
        "placeDetails": "Near Farmgate Bus Stand",
        "pickupTime": "07:30",
        "stopOrder": 2
      },
      {
        "placeName": "Shahbag",
        "placeDetails": "Opposite of Dhaka University",
        "pickupTime": "07:45",
        "stopOrder": 3
      },
      {
        "placeName": "Paltan",
        "placeDetails": "Paltan Crossing",
        "pickupTime": "08:00",
        "stopOrder": 4
      },
      {
        "placeName": "Motijheel",
        "placeDetails": "Near Motijheel Shapla Chattar",
        "pickupTime": "08:15",
        "stopOrder": 5
      }
    ]
  }'
```

### 9. Partial Update (PATCH)
```bash
# Update only bus number
curl -X PATCH http://localhost:8080/api/v1/routes/2 \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-002-NEW"
  }'

# Update only route name
curl -X PATCH http://localhost:8080/api/v1/routes/2 \
  -H "Content-Type: application/json" \
  -d '{
    "routeName": "Uttara - Gulshan Express"
  }'

# Update only operating days
curl -X PATCH http://localhost:8080/api/v1/routes/2 \
  -H "Content-Type: application/json" \
  -d '{
    "operatingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]
  }'

# Update multiple fields
curl -X PATCH http://localhost:8080/api/v1/routes/3 \
  -H "Content-Type: application/json" \
  -d '{
    "routeLine": "Dhanmondi 32 → Science Lab → Shahbag → Motijheel",
    "status": "ACTIVE"
  }'
```

### 10. Add Pickup Point to Route
```bash
curl -X POST http://localhost:8080/api/v1/routes/1/pickup-points \
  -H "Content-Type: application/json" \
  -d '{
    "placeName": "Paltan",
    "placeDetails": "Paltan Crossing",
    "pickupTime": "08:05",
    "stopOrder": 5
  }'
```

### 11. Update Pickup Point
```bash
curl -X PUT http://localhost:8080/api/v1/routes/1/pickup-points/5 \
  -H "Content-Type: application/json" \
  -d '{
    "placeName": "Paltan (Updated)",
    "placeDetails": "Paltan Crossing - Near Bangladesh Bank",
    "pickupTime": "08:10",
    "stopOrder": 5
  }'
```

### 12. Delete Pickup Point
```bash
curl -X DELETE http://localhost:8080/api/v1/routes/1/pickup-points/5
```

### 13. Reorder Pickup Points
```bash
curl -X POST http://localhost:8080/api/v1/routes/1/pickup-points/reorder \
  -H "Content-Type: application/json" \
  -d '[4, 1, 2, 3]'
```

### 14. Get Route Statistics
```bash
# Statistics for specific route
curl -X GET http://localhost:8080/api/v1/routes/1/statistics

# Statistics for all routes
curl -X GET http://localhost:8080/api/v1/routes/statistics/all
```

### 15. Get Buses by Route
```bash
curl -X GET http://localhost:8080/api/v1/routes/1/buses
```

### 16. Get Active Buses by Route
```bash
curl -X GET http://localhost:8080/api/v1/routes/1/buses/active
```

### 17. Get Slots by Route
```bash
curl -X GET http://localhost:8080/api/v1/routes/1/slots
```

### 18. Get Active Slots by Route
```bash
curl -X GET http://localhost:8080/api/v1/routes/1/slots/active
```

### 19. Delete Route
```bash
# Delete Route 4
curl -X DELETE http://localhost:8080/api/v1/routes/4

# Try to get deleted route (should return 404)
curl -X GET http://localhost:8080/api/v1/routes/4
```

### 20. Error Testing

```bash
# Try to create duplicate bus number (should fail)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-001",
    "routeName": "Duplicate Route",
    "routeLine": "Test → Route",
    "operatingDays": ["MONDAY"],
    "pickupPoints": [
      {
        "placeName": "Test Point",
        "pickupTime": "10:00",
        "stopOrder": 1
      }
    ]
  }'

# Try to get non-existent route
curl -X GET http://localhost:8080/api/v1/routes/999

# Try to update non-existent route
curl -X PUT http://localhost:8080/api/v1/routes/999 \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-999",
    "routeName": "Non Existent",
    "routeLine": "Nowhere",
    "status": "ACTIVE",
    "operatingDays": ["MONDAY"],
    "pickupPoints": []
  }'

# Try to add pickup point with duplicate stop order
curl -X POST http://localhost:8080/api/v1/routes/1/pickup-points \
  -H "Content-Type: application/json" \
  -d '{
    "placeName": "Duplicate Order",
    "placeDetails": "Testing duplicate",
    "pickupTime": "09:00",
    "stopOrder": 2
  }'
```

## Complete Test Flow (Run in Order)

```bash
# 1. Create routes
curl -X POST http://localhost:8080/api/v1/routes -H "Content-Type: application/json" -d '{"busNo":"BUS-T1","routeName":"Test Route 1","routeLine":"A → B","operatingDays":["MONDAY"],"pickupPoints":[{"placeName":"Point A","pickupTime":"07:00","stopOrder":1}]}'
curl -X POST http://localhost:8080/api/v1/routes -H "Content-Type: application/json" -d '{"busNo":"BUS-T2","routeName":"Test Route 2","routeLine":"C → D","operatingDays":["TUESDAY"],"pickupPoints":[{"placeName":"Point C","pickupTime":"08:00","stopOrder":1}]}'

# 2. Get all routes
curl -X GET http://localhost:8080/api/v1/routes

# 3. Get route by ID
curl -X GET http://localhost:8080/api/v1/routes/1

# 4. Update status
curl -X PATCH "http://localhost:8080/api/v1/routes/1/status?status=MAINTENANCE"

# 5. Add pickup point
curl -X POST http://localhost:8080/api/v1/routes/1/pickup-points -H "Content-Type: application/json" -d '{"placeName":"Point B","pickupTime":"07:30","stopOrder":2}'

# 6. Get route statistics
curl -X GET http://localhost:8080/api/v1/routes/1/statistics

# 7. Delete test route
curl -X DELETE http://localhost:8080/api/v1/routes/2

# 8. Verify deletion
curl -X GET http://localhost:8080/api/v1/routes
```

