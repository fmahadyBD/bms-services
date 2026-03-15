
```bash
#!/bin/bash

# ============================================
# GREEN UNIVERSITY BUS MANAGEMENT SYSTEM
# Location: Purbachole, Kanchon
# Routes: Various locations to/from Dhaka
# ============================================

echo "============================================"
echo "GREEN UNIVERSITY BUS MANAGEMENT API TESTS"
echo "Location: Purbachole, Kanchon"
echo "============================================"

# ─────────────────────────────────────────────────
# 1. CREATE BUSES (Green University Campus Routes)
# ─────────────────────────────────────────────────

echo -e "\n--- CREATING BUSES FOR GREEN UNIVERSITY ---\n"

# Create Bus 1 - Green University AC Bus (Dhaka → Campus)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University AC Express",
    "busNumber": "GUB-AC-101",
    "status": "ACTIVE",
    "driverName": "Md. Karim Ullah",
    "helperName": "Rahim Miah",
    "driverPhone": "01712345678",
    "helperPhone": "01812345678",
    "routeId": 1
  }'

echo -e "\n"

# Create Bus 2 - Green University Non-AC Bus (Dhaka → Campus)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University Non-AC",
    "busNumber": "GUB-NAC-102",
    "status": "ACTIVE",
    "driverName": "Shahidul Islam",
    "helperName": "Jahangir Alam",
    "driverPhone": "01912345678",
    "helperPhone": "01612345678",
    "routeId": 1
  }'

echo -e "\n"

# Create Bus 3 - Green University Staff Bus (Campus → Dhaka)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University Staff Special",
    "busNumber": "GUB-STF-103",
    "status": "ON_TRIP",
    "driverName": "Faruk Hossain",
    "helperName": "Shofiqul Islam",
    "driverPhone": "01512345678",
    "helperPhone": "01412345678",
    "routeId": 2
  }'

echo -e "\n"

# Create Bus 4 - Green University Women's Bus
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University Women Special",
    "busNumber": "GUB-WMN-104",
    "status": "MAINTENANCE",
    "driverName": "Abul Kashem",
    "helperName": "Bilkis Begum",
    "driverPhone": "01312345678",
    "helperPhone": "01212345678"
  }'

echo -e "\n"

# Create Bus 5 - Green University Evening Shift Bus
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University Evening Special",
    "busNumber": "GUB-EVE-105",
    "status": "INACTIVE",
    "driverName": "Mostafa Kamal",
    "helperName": "Shahin Alam",
    "driverPhone": "01112345678",
    "helperPhone": "01012345678"
  }'

echo -e "\n"

# Create Bus 6 - Green University Double Decker (Special Route)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University Double Decker",
    "busNumber": "GUB-DBL-106",
    "status": "OUT_OF_SERVICE",
    "driverName": "Harunur Rashid",
    "helperName": "Mizanur Rahman",
    "driverPhone": "01987654321",
    "helperPhone": "01887654321"
  }'

echo -e "\n"

# ─────────────────────────────────────────────────
# 2. GET ALL BUSES
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ALL GREEN UNIVERSITY BUSES ---"
curl -X GET http://localhost:8080/api/v1/buses | json_pp

# ─────────────────────────────────────────────────
# 3. GET BUS BY ID
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING BUS DETAILS BY ID ---"
curl -X GET http://localhost:8080/api/v1/buses/1 | json_pp

# ─────────────────────────────────────────────────
# 4. GET BUS BY NUMBER
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING BUS DETAILS BY BUS NUMBER ---"
curl -X GET http://localhost:8080/api/v1/buses/number/GUB-AC-101 | json_pp

# ─────────────────────────────────────────────────
# 5. GET BUSES BY STATUS
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ACTIVE GREEN UNIVERSITY BUSES ---"
curl -X GET http://localhost:8080/api/v1/buses/status/ACTIVE | json_pp

echo -e "\n--- GETTING ON TRIP GREEN UNIVERSITY BUSES ---"
curl -X GET http://localhost:8080/api/v1/buses/status/ON_TRIP | json_pp

# ─────────────────────────────────────────────────
# 6. GET BUSES BY ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING BUSES FOR ROUTE 1 (DHAKA → PURBACHOLE) ---"
curl -X GET http://localhost:8080/api/v1/buses/route/1 | json_pp

echo -e "\n--- GETTING BUSES FOR ROUTE 2 (PURBACHOLE → DHAKA) ---"
curl -X GET http://localhost:8080/api/v1/buses/route/2 | json_pp

# ─────────────────────────────────────────────────
# 7. GET ACTIVE BUSES BY ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ACTIVE BUSES FOR ROUTE 1 (DHAKA → PURBACHOLE) ---"
curl -X GET http://localhost:8080/api/v1/buses/route/1/active | json_pp

# ─────────────────────────────────────────────────
# 8. GET AVAILABLE BUSES
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING AVAILABLE GREEN UNIVERSITY BUSES ---"
curl -X GET http://localhost:8080/api/v1/buses/available | json_pp

# ─────────────────────────────────────────────────
# 9. SEARCH BUSES
# ─────────────────────────────────────────────────
echo -e "\n--- SEARCHING BUSES BY DRIVER NAME (Karim) ---"
curl -X GET "http://localhost:8080/api/v1/buses/search/driver?driverName=Karim" | json_pp

echo -e "\n--- SEARCHING BUSES BY BUS NAME (Green) ---"
curl -X GET "http://localhost:8080/api/v1/buses/search/name?busName=Green" | json_pp

# ─────────────────────────────────────────────────
# 10. UPDATE BUS (FULL UPDATE - PUT)
# ─────────────────────────────────────────────────
echo -e "\n--- UPDATING GREEN UNIVERSITY BUS 1 WITH PREMIUM SERVICE ---"
curl -X PUT http://localhost:8080/api/v1/buses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University AC Premium Express",
    "busNumber": "GUB-AC-101",
    "status": "ACTIVE",
    "driverName": "Md. Karim Ullah (Senior Driver)",
    "helperName": "Rahim Miah (Senior Helper)",
    "driverPhone": "01712345679",
    "helperPhone": "01812345679",
    "routeId": 1
  }' | json_pp

# ─────────────────────────────────────────────────
# 11. PARTIAL UPDATE (PATCH)
# ─────────────────────────────────────────────────
echo -e "\n--- UPDATING BUS 2 DRIVER INFORMATION ---"
curl -X PATCH http://localhost:8080/api/v1/buses/2 \
  -H "Content-Type: application/json" \
  -d '{
    "driverName": "Shahidul Islam (Morning Shift)",
    "driverPhone": "01912345679"
  }' | json_pp

echo -e "\n--- CHANGING BUS 3 STATUS ---"
curl -X PATCH http://localhost:8080/api/v1/buses/3 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE"
  }' | json_pp

# ─────────────────────────────────────────────────
# 12. UPDATE BUS STATUS
# ─────────────────────────────────────────────────
echo -e "\n--- UPDATING BUS 4 STATUS TO ACTIVE (MAINTENANCE COMPLETED) ---"
curl -X PATCH http://localhost:8080/api/v1/buses/4/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE",
    "reason": "Regular maintenance completed at Kanchon garage"
  }' | json_pp

echo -e "\n--- UPDATING BUS 3 STATUS TO ON_TRIP ---"
curl -X PATCH http://localhost:8080/api/v1/buses/3/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ON_TRIP",
    "reason": "Departing from Purbachole campus to Dhaka at 5:00 PM"
  }' | json_pp

# ─────────────────────────────────────────────────
# 13. ASSIGN BUS TO ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- ASSIGNING BUS 4 TO ROUTE 1 (DHAKA → PURBACHOLE) ---"
curl -X PATCH http://localhost:8080/api/v1/buses/4/assign-route/1 | json_pp

# ─────────────────────────────────────────────────
# 14. REMOVE BUS FROM ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- REMOVING BUS 5 FROM ITS CURRENT ROUTE ---"
curl -X PATCH http://localhost:8080/api/v1/buses/5/remove-route | json_pp

# ─────────────────────────────────────────────────
# 15. FILTER BUSES
# ─────────────────────────────────────────────────
echo -e "\n--- FILTER: ACTIVE BUSES ON ROUTE 1 ---"
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "status": "ACTIVE"
  }' | json_pp

echo -e "\n--- FILTER: ALL BUSES WITH "WOMEN" IN NAME ---"
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Women"
  }' | json_pp

echo -e "\n--- FILTER: BUSES DRIVEN BY "Md. Karim" ---"
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "driverName": "Karim"
  }' | json_pp

# ─────────────────────────────────────────────────
# 16. GET BUS STATISTICS
# ─────────────────────────────────────────────────
echo -e "\n--- GREEN UNIVERSITY BUS FLEET STATISTICS ---"
curl -X GET http://localhost:8080/api/v1/buses/statistics | json_pp

# ─────────────────────────────────────────────────
# 17. GET BUS STATISTICS BY ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- ROUTE 1 (DHAKA → PURBACHOLE) STATISTICS ---"
curl -X GET http://localhost:8080/api/v1/buses/statistics/route/1 | json_pp

echo -e "\n--- ROUTE 2 (PURBACHOLE → DHAKA) STATISTICS ---"
curl -X GET http://localhost:8080/api/v1/buses/statistics/route/2 | json_pp

# ─────────────────────────────────────────────────
# 18. DELETE A BUS
# ─────────────────────────────────────────────────
echo -e "\n--- DELETING BUS 6 (GUB-DBL-106) ---"
curl -X DELETE http://localhost:8080/api/v1/buses/6

echo -e "\n--- VERIFYING DELETION ---"
curl -X GET http://localhost:8080/api/v1/buses/6

# ─────────────────────────────────────────────────
# 19. TEST ERROR CASES
# ─────────────────────────────────────────────────
echo -e "\n\n--- ERROR CASE: CREATE DUPLICATE BUS NUMBER ---"
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Duplicate Bus",
    "busNumber": "GUB-AC-101",
    "status": "ACTIVE",
    "driverName": "Test Driver",
    "helperName": "Test Helper",
    "driverPhone": "01700000000",
    "helperPhone": "01800000000"
  }'

echo -e "\n\n--- ERROR CASE: INVALID PHONE NUMBER ---"
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Invalid Phone",
    "busNumber": "GUB-ERR-999",
    "status": "ACTIVE",
    "driverName": "Test Driver",
    "helperName": "Test Helper",
    "driverPhone": "invalid",
    "helperPhone": "01800000000"
  }'

echo -e "\n\n--- ERROR CASE: GET NON-EXISTENT BUS ---"
curl -X GET http://localhost:8080/api/v1/buses/999

# ─────────────────────────────────────────────────
# 20. COMPLETE TEST FLOW (Morning Routine)
# ─────────────────────────────────────────────────
echo -e "\n\n============================================"
echo "MORNING ROUTINE TEST FLOW - DHAKA TO CAMPUS"
echo "============================================"

# Step 1: Check available buses for morning shift
echo -e "\n[06:00 AM] Checking available morning shift buses..."
curl -X GET http://localhost:8080/api/v1/buses/available | json_pp

# Step 2: Update status for morning departure
echo -e "\n[06:30 AM] Updating bus status for morning departure..."
curl -X PATCH http://localhost:8080/api/v1/buses/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ON_TRIP",
    "reason": "Morning trip: Departing from Farmgate to Purbachole campus"
  }'

# Step 3: Get buses by route
echo -e "\n[07:00 AM] Checking buses on Dhaka → Purbachole route..."
curl -X GET http://localhost:8080/api/v1/buses/route/1 | json_pp

# Step 4: Get updated statistics
echo -e "\n[08:00 AM] Updated fleet statistics..."
curl -X GET http://localhost:8080/api/v1/buses/statistics | json_pp

echo -e "\n\n============================================"
echo "EVENING ROUTINE TEST FLOW - CAMPUS TO DHAKA"
echo "============================================"

# Step 5: Check buses for evening return
echo -e "\n[04:00 PM] Checking buses for evening return trip..."
curl -X GET http://localhost:8080/api/v1/buses/route/2/active | json_pp

# Step 6: Update status for return journey
echo -e "\n[05:00 PM] Updating buses for return journey to Dhaka..."
curl -X PATCH http://localhost:8080/api/v1/buses/2/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ON_TRIP",
    "reason": "Evening trip: Departing from Purbachole campus to Motijheel"
  }'

# Step 7: Final statistics
echo -e "\n[09:00 PM] End of day statistics..."
curl -X GET http://localhost:8080/api/v1/buses/statistics | json_pp

echo -e "\n\n============================================"
echo "TESTING COMPLETE"
echo "============================================"
```

## Key Changes Made:

1. **Bus Names**: Updated to reflect Green University context
2. **Bus Numbers**: Changed to GUB-XXX format (Green University Bus)
3. **Driver Names**: Added Bengali/Muslim names common in Bangladesh
4. **Route Descriptions**: Updated comments to reference:
   - Purbachole, Kanchon (campus location)
   - Various Dhaka locations (Farmgate, Motijheel, etc.)
   - Morning/evening commute context

## Additional Testing Commands for Specific Scenarios:

```bash
# Check buses available for morning pickup from Dhaka
curl -X GET "http://localhost:8080/api/v1/buses/search/name?busName=Morning"

# Check women's special bus availability
curl -X GET "http://localhost:8080/api/v1/buses/search/name?busName=Women"

# Filter buses by specific Dhaka route
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "status": "ACTIVE"
  }'

# Get statistics for Purbachole campus route
curl -X GET http://localhost:8080/api/v1/buses/statistics/route/1
```
