
## 1. Create Bus Slots
```bash
# Create first bus slot
curl -X POST http://localhost:8080/api/v1/bus-slots/ \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "pickupPointId": 1,
    "slotName": "Morning Express",
    "pickupTime": "07:30:00",
    "dropTime": "17:30:00",
    "maxCapacity": 50,
    "status": "ACTIVE",
    "description": "Morning slot for downtown route",
    "recurring": true,
    "recurringDays": "MONDAY,WEDNESDAY,FRIDAY",
    "cutoffTime": "20:00:00",
    "bufferMinutes": 15,
    "durationMinutes": 120,
    "fareAmount": 25.50,
    "vehicleNumber": "BUS-001",
    "driverName": "John Driver",
    "driverPhone": "1234567890"
  }'
```

```bash
# Create second bus slot
curl -X POST http://localhost:8080/api/v1/bus-slots/ \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "pickupPointId": 2,
    "slotName": "Evening Special",
    "pickupTime": "16:00:00",
    "dropTime": "22:00:00",
    "maxCapacity": 40,
    "status": "ACTIVE",
    "description": "Evening slot for return trip",
    "recurring": true,
    "recurringDays": "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY",
    "cutoffTime": "15:00:00",
    "bufferMinutes": 10,
    "durationMinutes": 180,
    "fareAmount": 30.00,
    "vehicleNumber": "BUS-002",
    "driverName": "Jane Driver",
    "driverPhone": "0987654321"
  }'
```

```bash
# Create third bus slot (different route)
curl -X POST http://localhost:8080/api/v1/bus-slots/ \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 2,
    "pickupPointId": 3,
    "slotName": "Weekend Special",
    "pickupTime": "09:00:00",
    "dropTime": "19:00:00",
    "maxCapacity": 30,
    "status": "INACTIVE",
    "description": "Weekend service",
    "recurring": true,
    "recurringDays": "SATURDAY,SUNDAY",
    "cutoffTime": "08:00:00",
    "bufferMinutes": 20,
    "durationMinutes": 240,
    "fareAmount": 45.00,
    "vehicleNumber": "BUS-003",
    "driverName": "Mike Driver",
    "driverPhone": "5551234567"
  }'
```

## 2. Get All Bus Slots
```bash
curl -X GET http://localhost:8080/api/v1/bus-slots/
```

## 3. Get Bus Slot by ID
```bash
curl -X GET http://localhost:8080/api/v1/bus-slots/1
```

## 4. Get Slots by Route
```bash
curl -X GET http://localhost:8080/api/v1/bus-slots/route/1
```

## 5. Get Slots by Status
```bash
# Get ACTIVE slots
curl -X GET http://localhost:8080/api/v1/bus-slots/status/ACTIVE

# Get INACTIVE slots
curl -X GET http://localhost:8080/api/v1/bus-slots/status/INACTIVE
```

## 6. Get Available Slots
```bash
curl -X GET http://localhost:8080/api/v1/bus-slots/available
```

## 7. Get Slots by Time Range
```bash
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=07:00:00&toTime=17:00:00"
```

## 8. Get Slots by Route and Time Range
```bash
curl -X GET "http://localhost:8080/api/v1/bus-slots/route/1/time-range?fromTime=07:00:00&toTime=20:00:00"
```

## 9. Update Slot Status
```bash
# Update to FULL
curl -X PATCH http://localhost:8080/api/v1/bus-slots/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "FULL",
    "reason": "All seats booked for the week"
  }'
```

## 10. Update Booking Count
```bash
# Increment bookings by 10
curl -X PATCH http://localhost:8080/api/v1/bus-slots/1/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "action": "INCREMENT",
    "count": 10
  }'

# Decrement bookings by 2
curl -X PATCH http://localhost:8080/api/v1/bus-slots/1/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "action": "DECREMENT",
    "count": 2
  }'
```

## 11. Update Entire Slot
```bash
curl -X PUT http://localhost:8080/api/v1/bus-slots/1 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "pickupPointId": 1,
    "slotName": "Morning Express - Updated",
    "pickupTime": "07:45:00",
    "dropTime": "18:00:00",
    "maxCapacity": 60,
    "status": "ACTIVE",
    "description": "Updated morning slot with new timing",
    "recurring": true,
    "recurringDays": "MONDAY,WEDNESDAY,FRIDAY",
    "cutoffTime": "21:00:00",
    "bufferMinutes": 20,
    "durationMinutes": 135,
    "fareAmount": 28.50,
    "vehicleNumber": "BUS-001-NEW",
    "driverName": "John Updated",
    "driverPhone": "1234567890"
  }'
```

## 12. Filter Slots
```bash
# Basic filter
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "isAvailable": true
  }'

# Complex filter
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "fromTime": "07:00:00",
    "toTime": "20:00:00",
    "isRecurring": true,
    "status": "ACTIVE"
  }'
```

## 13. Get Statistics
```bash
# Overall statistics
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics

# Statistics by route
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/route/1
```

## 14. Delete a Slot
```bash
curl -X DELETE http://localhost:8080/api/v1/bus-slots/3
```

## 15. Test Duplicate Creation (Should fail)
```bash
curl -X POST http://localhost:8080/api/v1/bus-slots/ \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "pickupPointId": 1,
    "slotName": "Duplicate Morning",
    "pickupTime": "07:30:00",
    "dropTime": "17:30:00",
    "maxCapacity": 50,
    "status": "ACTIVE",
    "description": "This should fail due to duplicate time",
    "recurring": true,
    "recurringDays": "MONDAY,WEDNESDAY,FRIDAY",
    "cutoffTime": "20:00:00",
    "bufferMinutes": 15,
    "durationMinutes": 120,
    "fareAmount": 25.50,
    "vehicleNumber": "BUS-001",
    "driverName": "John Driver",
    "driverPhone": "1234567890"
  }'
```
