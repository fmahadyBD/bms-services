#!/bin/bash

# ============================================
# GREEN UNIVERSITY ROUTE MANAGEMENT API TESTS
# Location: Purbachole, Kanchon
# Routes: Campus to/from Various Dhaka Locations
# ============================================

echo "============================================"
echo "GREEN UNIVERSITY ROUTE MANAGEMENT API TESTS"
echo "Campus Location: Purbachole, Kanchon"
echo "============================================"

# ─────────────────────────────────────────────────
# 1. CREATE ROUTES (Green University Campus Routes)
# ─────────────────────────────────────────────────

echo -e "\n--- CREATING GREEN UNIVERSITY CAMPUS ROUTES ---\n"

# Route 1: Dhaka (Farmgate) → Purbachole Campus (Morning Route)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-101",
    "routeName": "Dhaka (Farmgate) → Purbachole Campus",
    "routeLine": "Farmgate → Shahbag → Malibagh → Rampura → Badda → Kanchon Bridge → Purbachole (GUB)",
    "operatingDays": ["SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY"],
    "pickupPoints": [
      {
        "placeName": "Farmgate",
        "placeDetails": "In front of Farmgate Metro Station (Near IDB Bhaban)",
        "pickupTime": "07:00",
        "stopOrder": 1
      },
      {
        "placeName": "Shahbag",
        "placeDetails": "Opposite of Dhaka University (Near TSC)",
        "pickupTime": "07:15",
        "stopOrder": 2
      },
      {
        "placeName": "Malibagh",
        "placeDetails": "Malibagh Railgate (Near Khilgaon Flyover)",
        "pickupTime": "07:30",
        "stopOrder": 3
      },
      {
        "placeName": "Rampura",
        "placeDetails": "Rampura Bridge (Merul Badda)",
        "pickupTime": "07:40",
        "stopOrder": 4
      },
      {
        "placeName": "Badda",
        "placeDetails": "Badda Bus Stand (Near Badda High School)",
        "pickupTime": "07:50",
        "stopOrder": 5
      },
      {
        "placeName": "Kanchon Bridge",
        "placeDetails": "After Kanchon Bridge (Before GUB Campus)",
        "pickupTime": "08:10",
        "stopOrder": 6
      },
      {
        "placeName": "Green University Campus",
        "placeDetails": "Purbachole, Kanchon (Main Gate)",
        "pickupTime": "08:30",
        "stopOrder": 7
      }
    ]
  }'

echo -e "\n"

# Route 2: Purbachole Campus → Dhaka (Motijheel) (Evening Route)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-102",
    "routeName": "Purbachole Campus → Dhaka (Motijheel)",
    "routeLine": "Purbachole (GUB) → Kanchon Bridge → Badda → Rampura → Malibagh → Shahbag → Motijheel",
    "operatingDays": ["SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY"],
    "pickupPoints": [
      {
        "placeName": "Green University Campus",
        "placeDetails": "Purbachole, Kanchon (Main Gate)",
        "pickupTime": "16:30",
        "stopOrder": 1
      },
      {
        "placeName": "Kanchon Bridge",
        "placeDetails": "Before Kanchon Bridge (Pickup Point)",
        "pickupTime": "16:40",
        "stopOrder": 2
      },
      {
        "placeName": "Badda",
        "placeDetails": "Badda Bus Stand (Near Badda High School)",
        "pickupTime": "17:00",
        "stopOrder": 3
      },
      {
        "placeName": "Rampura",
        "placeDetails": "Rampura Bridge (Merul Badda)",
        "pickupTime": "17:10",
        "stopOrder": 4
      },
      {
        "placeName": "Malibagh",
        "placeDetails": "Malibagh Railgate",
        "pickupTime": "17:20",
        "stopOrder": 5
      },
      {
        "placeName": "Shahbag",
        "placeDetails": "Near Dhaka University (TSC)",
        "pickupTime": "17:35",
        "stopOrder": 6
      },
      {
        "placeName": "Motijheel",
        "placeDetails": "Motijheel Shapla Chattar (Near Bangladesh Bank)",
        "pickupTime": "18:00",
        "stopOrder": 7
      }
    ]
  }'

echo -e "\n"

# Route 3: Uttara → Purbachole Campus (North Dhaka Route)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-103",
    "routeName": "Uttara → Purbachole Campus",
    "routeLine": "Uttara → Airport → Banani → Mohakhali → Badda → Kanchon → Purbachole",
    "operatingDays": ["SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY"],
    "pickupPoints": [
      {
        "placeName": "Uttara Sector 3",
        "placeDetails": "Near Uttara Metro Station (House Building)",
        "pickupTime": "06:45",
        "stopOrder": 1
      },
      {
        "placeName": "Airport",
        "placeDetails": "Hazrat Shahjalal International Airport (Outer Circle)",
        "pickupTime": "07:00",
        "stopOrder": 2
      },
      {
        "placeName": "Banani",
        "placeDetails": "Banani Bus Stand (Near Banani Graveyard)",
        "pickupTime": "07:20",
        "stopOrder": 3
      },
      {
        "placeName": "Mohakhali",
        "placeDetails": "Mohakhali Bus Terminal (Inter District)",
        "pickupTime": "07:30",
        "stopOrder": 4
      },
      {
        "placeName": "Badda",
        "placeDetails": "Badda Bus Stand",
        "pickupTime": "07:45",
        "stopOrder": 5
      },
      {
        "placeName": "Kanchon",
        "placeDetails": "Kanchon Bazar",
        "pickupTime": "08:00",
        "stopOrder": 6
      },
      {
        "placeName": "Green University Campus",
        "placeDetails": "Purbachole, Kanchon",
        "pickupTime": "08:20",
        "stopOrder": 7
      }
    ]
  }'

echo -e "\n"

# Route 4: Purbachole Campus → Mirpur (Staff Route)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-104",
    "routeName": "Purbachole Campus → Mirpur (Staff Special)",
    "routeLine": "Purbachole (GUB) → Kanchon → Badda → Mohakhali → Farmgate → Khamarbari → Mirpur",
    "operatingDays": ["SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY"],
    "pickupPoints": [
      {
        "placeName": "Green University Campus",
        "placeDetails": "Purbachole, Kanchon (Staff Parking)",
        "pickupTime": "17:00",
        "stopOrder": 1
      },
      {
        "placeName": "Kanchon",
        "placeDetails": "Kanchon Bazar",
        "pickupTime": "17:10",
        "stopOrder": 2
      },
      {
        "placeName": "Badda",
        "placeDetails": "Badda Bus Stand",
        "pickupTime": "17:25",
        "stopOrder": 3
      },
      {
        "placeName": "Mohakhali",
        "placeDetails": "Mohakhali Bus Terminal",
        "pickupTime": "17:40",
        "stopOrder": 4
      },
      {
        "placeName": "Farmgate",
        "placeDetails": "Farmgate Metro Station",
        "pickupTime": "17:55",
        "stopOrder": 5
      },
      {
        "placeName": "Khamarbari",
        "placeDetails": "Khamarbari Bus Stop",
        "pickupTime": "18:05",
        "stopOrder": 6
      },
      {
        "placeName": "Mirpur 10",
        "placeDetails": "Mirpur 10 Roundabout",
        "pickupTime": "18:30",
        "stopOrder": 7
      }
    ]
  }'

echo -e "\n"

# Route 5: Saturday Special (Women's Bus)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-105",
    "routeName": "Women'\''s Special: Dhaka → Purbachole",
    "routeLine": "Mirpur → Khamarbari → Farmgate → Shahbag → Malibagh → Badda → Purbachole",
    "operatingDays": ["SATURDAY"],
    "pickupPoints": [
      {
        "placeName": "Mirpur 10",
        "placeDetails": "Women'\''s Waiting Zone (Near Pink Bus Stand)",
        "pickupTime": "07:30",
        "stopOrder": 1
      },
      {
        "placeName": "Khamarbari",
        "placeDetails": "Women'\''s Special Pickup Point",
        "pickupTime": "07:45",
        "stopOrder": 2
      },
      {
        "placeName": "Farmgate",
        "placeDetails": "Women'\''s Waiting Area (Near IDB)",
        "pickupTime": "08:00",
        "stopOrder": 3
      },
      {
        "placeName": "Shahbag",
        "placeDetails": "Women'\''s Special Zone (Near DU Women'\''s Hall)",
        "pickupTime": "08:15",
        "stopOrder": 4
      },
      {
        "placeName": "Malibagh",
        "placeDetails": "Women'\''s Pickup Point",
        "pickupTime": "08:30",
        "stopOrder": 5
      },
      {
        "placeName": "Badda",
        "placeDetails": "Women'\''s Waiting Area",
        "pickupTime": "08:45",
        "stopOrder": 6
      },
      {
        "placeName": "Green University Campus",
        "placeDetails": "Purbachole, Kanchon (Women'\''s Gate)",
        "pickupTime": "09:15",
        "stopOrder": 7
      }
    ]
  }'

echo -e "\n"

# Route 6: Evening Express (Direct Service)
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-106",
    "routeName": "Evening Express: Purbachole → Motijheel",
    "routeLine": "Purbachole (GUB) → Badda → Malibagh → Motijheel (Express)",
    "operatingDays": ["SUNDAY", "TUESDAY", "THURSDAY"],
    "pickupPoints": [
      {
        "placeName": "Green University Campus",
        "placeDetails": "Purbachole, Kanchon (Express Counter)",
        "pickupTime": "18:00",
        "stopOrder": 1
      },
      {
        "placeName": "Badda Express",
        "placeDetails": "Badda (Express Stop - Near Badda High School)",
        "pickupTime": "18:20",
        "stopOrder": 2
      },
      {
        "placeName": "Malibagh Express",
        "placeDetails": "Malibagh (Express Stop - Railgate)",
        "pickupTime": "18:35",
        "stopOrder": 3
      },
      {
        "placeName": "Motijheel Express",
        "placeDetails": "Motijheel (Near Shapla Chattar)",
        "pickupTime": "19:00",
        "stopOrder": 4
      }
    ]
  }'

echo -e "\n"

# ─────────────────────────────────────────────────
# 2. GET ALL ROUTES
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ALL GREEN UNIVERSITY ROUTES ---"
curl -X GET http://localhost:8080/api/v1/routes

# ─────────────────────────────────────────────────
# 3. GET ROUTE BY ID
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ROUTE DETAILS BY ID (Route 1: Farmgate → Campus) ---"
curl -X GET http://localhost:8080/api/v1/routes/1

# ─────────────────────────────────────────────────
# 4. GET ROUTE BY BUS NUMBER
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ROUTE DETAILS BY BUS NUMBER (BUS-101) ---"
curl -X GET http://localhost:8080/api/v1/routes/bus/BUS-101

# ─────────────────────────────────────────────────
# 5. GET ROUTES BY OPERATING DAY
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ROUTES OPERATING ON SATURDAY ---"
curl -X GET http://localhost:8080/api/v1/routes/day/SATURDAY

echo -e "\n--- GETTING ROUTES OPERATING ON SUNDAY ---"
curl -X GET http://localhost:8080/api/v1/routes/day/SUNDAY

echo -e "\n--- GETTING ROUTES OPERATING ON MONDAY ---"
curl -X GET http://localhost:8080/api/v1/routes/day/MONDAY

# ─────────────────────────────────────────────────
# 6. GET ROUTES BY STATUS
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ALL ACTIVE ROUTES ---"
curl -X GET "http://localhost:8080/api/v1/routes/status/ACTIVE"

# ─────────────────────────────────────────────────
# 7. UPDATE ROUTE STATUS
# ─────────────────────────────────────────────────
echo -e "\n--- SETTING ROUTE 3 TO MAINTENANCE (Staff Route) ---"
curl -X PATCH "http://localhost:8080/api/v1/routes/3/status?status=MAINTENANCE"

echo -e "\n--- SETTING ROUTE 4 BACK TO ACTIVE (Staff Route) ---"
curl -X PATCH "http://localhost:8080/api/v1/routes/4/status?status=ACTIVE"

# ─────────────────────────────────────────────────
# 8. FILTER ROUTES BY STATUS AND DAY
# ─────────────────────────────────────────────────
echo -e "\n--- FILTERING ACTIVE ROUTES ON SATURDAY ---"
curl -X GET "http://localhost:8080/api/v1/routes/filter?status=ACTIVE&day=SATURDAY"

# ─────────────────────────────────────────────────
# 9. FULL UPDATE (PUT) - Update Route 2 with Express Service
# ─────────────────────────────────────────────────
echo -e "\n--- UPDATING ROUTE 2 WITH EXPRESS SERVICE ---"
curl -X PUT http://localhost:8080/api/v1/routes/2 \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-102",
    "routeName": "Purbachole Campus → Motijheel (Evening Express)",
    "routeLine": "Purbachole (GUB) → Badda → Malibagh → Motijheel (Limited Stops)",
    "status": "ACTIVE",
    "operatingDays": ["SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY"],
    "pickupPoints": [
      {
        "placeName": "Green University Campus",
        "placeDetails": "Purbachole, Kanchon (Express Counter)",
        "pickupTime": "17:00",
        "stopOrder": 1
      },
      {
        "placeName": "Badda Express",
        "placeDetails": "Badda Express Point",
        "pickupTime": "17:20",
        "stopOrder": 2
      },
      {
        "placeName": "Malibagh Express",
        "placeDetails": "Malibagh Express Stop",
        "pickupTime": "17:40",
        "stopOrder": 3
      },
      {
        "placeName": "Motijheel Express",
        "placeDetails": "Motijheel Shapla Chattar",
        "pickupTime": "18:10",
        "stopOrder": 4
      }
    ]
  }'

# ─────────────────────────────────────────────────
# 10. PARTIAL UPDATE (PATCH)
# ─────────────────────────────────────────────────
echo -e "\n--- UPDATING ROUTE 1 PICKUP TIMES ---"
curl -X PATCH http://localhost:8080/api/v1/routes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "pickupPoints": [
      {
        "placeName": "Farmgate",
        "placeDetails": "Farmgate Metro Station (Pickup Point)",
        "pickupTime": "06:45",
        "stopOrder": 1
      },
      {
        "placeName": "Shahbag",
        "placeDetails": "TSC (Dhaka University)",
        "pickupTime": "07:00",
        "stopOrder": 2
      },
      {
        "placeName": "Malibagh",
        "placeDetails": "Malibagh Railgate",
        "pickupTime": "07:15",
        "stopOrder": 3
      },
      {
        "placeName": "Rampura",
        "placeDetails": "Rampura Bridge",
        "pickupTime": "07:25",
        "stopOrder": 4
      },
      {
        "placeName": "Badda",
        "placeDetails": "Badda Bus Stand",
        "pickupTime": "07:35",
        "stopOrder": 5
      },
      {
        "placeName": "Kanchon Bridge",
        "placeDetails": "Kanchon Bridge",
        "pickupTime": "07:50",
        "stopOrder": 6
      },
      {
        "placeName": "Green University Campus",
        "placeDetails": "Purbachole, Kanchon",
        "pickupTime": "08:10",
        "stopOrder": 7
      }
    ]
  }'

echo -e "\n--- UPDATING ROUTE 5 OPERATING DAYS ---"
curl -X PATCH http://localhost:8080/api/v1/routes/5 \
  -H "Content-Type: application/json" \
  -d '{
    "operatingDays": ["SATURDAY", "WEDNESDAY"]
  }'

# ─────────────────────────────────────────────────
# 11. ADD PICKUP POINT TO ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- ADDING NEW PICKUP POINT TO ROUTE 1 (UAP) ---"
curl -X POST http://localhost:8080/api/v1/routes/1/pickup-points \
  -H "Content-Type: application/json" \
  -d '{
    "placeName": "UAP (University of Asia Pacific)",
    "placeDetails": "Near UAP Campus (Farmgate)",
    "pickupTime": "06:55",
    "stopOrder": 2
  }'

# ─────────────────────────────────────────────────
# 12. UPDATE PICKUP POINT
# ─────────────────────────────────────────────────
echo -e "\n--- UPDATING PICKUP POINT IN ROUTE 1 ---"
curl -X PUT http://localhost:8080/api/v1/routes/1/pickup-points/8 \
  -H "Content-Type: application/json" \
  -d '{
    "placeName": "UAP (University of Asia Pacific)",
    "placeDetails": "Opposite of UAP Main Gate",
    "pickupTime": "06:50",
    "stopOrder": 2
  }'

# ─────────────────────────────────────────────────
# 13. DELETE PICKUP POINT
# ─────────────────────────────────────────────────
echo -e "\n--- DELETING PICKUP POINT FROM ROUTE 1 ---"
curl -X DELETE http://localhost:8080/api/v1/routes/1/pickup-points/8

# ─────────────────────────────────────────────────
# 14. REORDER PICKUP POINTS
# ─────────────────────────────────────────────────
echo -e "\n--- REORDERING PICKUP POINTS FOR ROUTE 1 ---"
curl -X POST http://localhost:8080/api/v1/routes/1/pickup-points/reorder \
  -H "Content-Type: application/json" \
  -d '[1, 3, 4, 5, 6, 7, 2]'

# ─────────────────────────────────────────────────
# 15. GET ROUTE STATISTICS
# ─────────────────────────────────────────────────
echo -e "\n--- STATISTICS FOR ROUTE 1 (Farmgate → Campus) ---"
curl -X GET http://localhost:8080/api/v1/routes/1/statistics

echo -e "\n--- STATISTICS FOR ALL ROUTES ---"
curl -X GET http://localhost:8080/api/v1/routes/statistics/all

# ─────────────────────────────────────────────────
# 16. GET BUSES BY ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING BUSES ASSIGNED TO ROUTE 1 ---"
curl -X GET http://localhost:8080/api/v1/routes/1/buses

# ─────────────────────────────────────────────────
# 17. GET ACTIVE BUSES BY ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ACTIVE BUSES ON ROUTE 1 ---"
curl -X GET http://localhost:8080/api/v1/routes/1/buses/active

# ─────────────────────────────────────────────────
# 18. GET SLOTS BY ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ALL SLOTS FOR ROUTE 1 ---"
curl -X GET http://localhost:8080/api/v1/routes/1/slots

# ─────────────────────────────────────────────────
# 19. GET ACTIVE SLOTS BY ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- GETTING ACTIVE SLOTS FOR ROUTE 1 ---"
curl -X GET http://localhost:8080/api/v1/routes/1/slots/active

# ─────────────────────────────────────────────────
# 20. DELETE ROUTE
# ─────────────────────────────────────────────────
echo -e "\n--- DELETING ROUTE 6 (Evening Express) ---"
curl -X DELETE http://localhost:8080/api/v1/routes/6

echo -e "\n--- VERIFYING DELETION ---"
curl -X GET http://localhost:8080/api/v1/routes/6

# ─────────────────────────────────────────────────
# 21. ERROR TESTING
# ─────────────────────────────────────────────────
echo -e "\n\n--- ERROR CASE: CREATE ROUTE WITH DUPLICATE BUS NUMBER ---"
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-101",
    "routeName": "Duplicate Route",
    "routeLine": "Test Route",
    "operatingDays": ["SATURDAY"],
    "pickupPoints": [
      {
        "placeName": "Test Point",
        "pickupTime": "10:00",
        "stopOrder": 1
      }
    ]
  }'

echo -e "\n\n--- ERROR CASE: INVALID BUS NUMBER FORMAT ---"
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "INVALID",
    "routeName": "Invalid Route",
    "routeLine": "Test",
    "operatingDays": ["SATURDAY"],
    "pickupPoints": [
      {
        "placeName": "Test",
        "pickupTime": "10:00",
        "stopOrder": 1
      }
    ]
  }'

echo -e "\n\n--- ERROR CASE: GET NON-EXISTENT ROUTE ---"
curl -X GET http://localhost:8080/api/v1/routes/999

# ─────────────────────────────────────────────────
# 22. COMPLETE TEST FLOW - Morning to Evening
# ─────────────────────────────────────────────────
echo -e "\n\n============================================"
echo "GREEN UNIVERSITY DAILY ROUTINE TEST FLOW"
echo "============================================"

# Step 1: Morning - Check routes operating today (Saturday)
echo -e "\n[06:00 AM] Checking Saturday morning routes..."
curl -X GET "http://localhost:8080/api/v1/routes/filter?day=SATURDAY&status=ACTIVE"

# Step 2: Update bus status for morning departure
echo -e "\n[06:30 AM] Updating Route 1 status for morning pickup..."
curl -X PATCH "http://localhost:8080/api/v1/routes/1/status?status=ACTIVE"

# Step 3: Check pickup points for Route 1
echo -e "\n[07:00 AM] Checking pickup points for Route 1 (Farmgate → Campus)..."
curl -X GET http://localhost:8080/api/v1/routes/1

# Step 4: Mid-day statistics
echo -e "\n[12:00 PM] Mid-day route statistics..."
curl -X GET http://localhost:8080/api/v1/routes/statistics/all

# Step 5: Evening - Check return routes
echo -e "\n[04:00 PM] Checking evening return routes..."
curl -X GET "http://localhost:8080/api/v1/routes/filter?day=SATURDAY"

# Step 6: Update for evening departure
echo -e "\n[05:00 PM] Activating Route 2 for evening return..."
curl -X PATCH "http://localhost:8080/api/v1/routes/2/status?status=ACTIVE"

# Step 7: Get buses on Route 2
echo -e "\n[05:30 PM] Checking buses on Route 2 (Campus → Motijheel)..."
curl -X GET http://localhost:8080/api/v1/routes/2/buses/active

# Step 8: End of day statistics
echo -e "\n[09:00 PM] End of day route statistics..."
curl -X GET http://localhost:8080/api/v1/routes/statistics/all

echo -e "\n\n============================================"
echo "ROUTE API TESTING COMPLETE"
echo "============================================"