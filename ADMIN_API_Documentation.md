# Elite Resort - Admin API Documentation

> **For Frontend Developers** - Complete reference for Admin panel APIs with authentication, room management, booking management, and user profile administration.

---

## Base Configuration

| Property | Value |
|----------|-------|
| **Base URL** | `https://resort-production-6aed.up.railway.app` |
| **Content-Type** | `application/json` (except file uploads) |
| **Authentication** | Bearer Token (JWT) |
| **Required Role** | ADMIN |

---

## Authentication

### Get Admin Token

Before calling any admin API, you need an admin token.

```http
POST /api/auth/admin/login
```

**Request Body:**
```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```

**Response (200 OK):**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkFkbWluIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

**Note:** Admin login returns a plain string token (not JSON).

---

### Using the Admin Token

Include this header in ALL admin API requests:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Table of Contents

| Section | Endpoint | Description |
|---------|----------|-------------|
| [Room Management](#1-room-management) | `/api/admin/rooms` | CRUD operations for rooms |
| [Admin Bookings View](#2-admin-bookings) | `/api/admin/rooms/bookings` | View all bookings with details |
| [User Profiles](#3-user-profile-apis) | `/api/profile/*` | Manage user profiles |
| [Image Upload](#4-image-upload) | `/api/images/upload` | Upload room images |

---

## 1. Room Management

### 1.1 Add New Room
Create a new room in the system.

```http
POST /api/admin/rooms
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json
```

**Request Body:**
```json
{
  "roomNumber": "102",
  "type": "Suite",
  "pricePerNight": 250.00,
  "capacity": 4,
  "available": true
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `roomNumber` | string | Yes | Unique room number |
| `type` | string | Yes | Room type: `Deluxe`, `Suite`, or `Standard` |
| `pricePerNight` | number | Yes | Price per night in USD |
| `capacity` | number | Yes | Maximum number of guests |
| `available` | boolean | Yes | Initial availability status |

**Response (200 OK):**
```json
{
  "roomId": "65g7h8i9j0k1l2m3n4o5p6q7",
  "roomNumber": "102",
  "type": "Suite",
  "pricePerNight": 250.00,
  "capacity": 4,
  "available": true,
  "createdAt": "2024-02-21T11:00:00",
  "updatedAt": "2024-02-21T11:00:00"
}
```

| Field | Type | Description |
|-------|------|-------------|
| `roomId` | string | Auto-generated unique identifier |
| `roomNumber` | string | Room number |
| `type` | string | Room type |
| `pricePerNight` | number | Price per night |
| `capacity` | number | Guest capacity |
| `available` | boolean | Availability status |
| `createdAt` | string | Creation timestamp (ISO 8601) |
| `updatedAt` | string | Last update timestamp (ISO 8601) |

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 400 | Bad Request | Invalid data format |
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Non-admin user trying to access |

---

### 1.2 Update Room
Update existing room details.

```http
PUT /api/admin/rooms/{roomId}
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `roomId` | string | Yes | ID of room to update |

**Request Body:**
```json
{
  "roomNumber": "102",
  "type": "Suite",
  "pricePerNight": 300.00,
  "capacity": 4,
  "available": false
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `roomNumber` | string | No | Updated room number |
| `type` | string | No | Updated room type |
| `pricePerNight` | number | No | Updated price |
| `capacity` | number | No | Updated capacity |
| `available` | boolean | No | Updated availability |

**Response (200 OK):**
```json
{
  "roomId": "65g7h8i9j0k1l2m3n4o5p6q7",
  "roomNumber": "102",
  "type": "Suite",
  "pricePerNight": 300.00,
  "capacity": 4,
  "available": false,
  "createdAt": "2024-02-21T11:00:00",
  "updatedAt": "2024-02-21T12:00:00"
}
```

**Note:** Only fields provided in the request will be updated. The `updatedAt` timestamp reflects when the change was made.

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 400 | Bad Request | Invalid data format |
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Non-admin user |
| 404 | Not Found | Room not found |

---

### 1.3 Delete Room
Remove a room from the system.

```http
DELETE /api/admin/rooms/{roomId}
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `roomId` | string | Yes | ID of room to delete |

**Response (200 OK):**
```json
"Room deleted successfully"
```

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Non-admin user |
| 404 | Not Found | Room not found |

---

### 1.4 Get All Rooms (Admin View)
Retrieve all rooms with complete information.

```http
GET /api/admin/rooms
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
    "roomNumber": "101",
    "type": "Deluxe",
    "pricePerNight": 150.00,
    "capacity": 2,
    "available": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-20T14:45:00"
  },
  {
    "roomId": "65b2c3d4e5f6g7h8i9j0k1l2",
    "roomNumber": "102",
    "type": "Suite",
    "pricePerNight": 250.00,
    "capacity": 4,
    "available": false,
    "createdAt": "2024-01-16T10:30:00",
    "updatedAt": "2024-01-21T14:45:00"
  }
]
```

---

### 1.5 Get Room by ID (Admin View)
Retrieve specific room details.

```http
GET /api/admin/rooms/{roomId}
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `roomId` | string | Yes | Room unique identifier |

**Response (200 OK):**
```json
{
  "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
  "roomNumber": "101",
  "type": "Deluxe",
  "pricePerNight": 150.00,
  "capacity": 2,
  "available": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00"
}
```

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Non-admin user |
| 404 | Not Found | Room not found |

---

## 2. Admin Bookings

### 2.1 Get All Bookings
View all bookings with user, room, and payment details.

```http
GET /api/admin/rooms/bookings
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
    "checkIn": "2024-03-01",
    "checkOut": "2024-03-05",
    "totalAmount": 600.00,
    "bookingStatus": "CONFIRMED",
    "userName": "John Doe",
    "userEmail": "john@example.com",
    "phone": "+91-9876543210",
    "address": "123 Main Street, City, Country",
    "idProof": "PASSPORT-AB123456",
    "roomNumber": "101",
    "roomType": "Deluxe",
    "paymentId": "65d4e5f6g7h8i9j0k1l2m3n4",
    "paymentMethod": "UPI",
    "paymentStatus": "SUCCESS",
    "paymentDate": "2024-02-21T10:30:00"
  },
  {
    "bookingId": "65c3d4e5f6g7h8i9j0k1l2m4",
    "checkIn": "2024-03-10",
    "checkOut": "2024-03-15",
    "totalAmount": 1250.00,
    "bookingStatus": "PENDING_PAYMENT",
    "userName": "Jane Smith",
    "userEmail": "jane@example.com",
    "phone": "+91-9876543211",
    "address": "456 Park Avenue, City, Country",
    "idProof": "AADHAAR-123456789",
    "roomNumber": "102",
    "roomType": "Suite",
    "paymentId": null,
    "paymentMethod": null,
    "paymentStatus": null,
    "paymentDate": null
  }
]
```

| Field | Type | Description |
|-------|------|-------------|
| `bookingId` | string | Booking unique identifier |
| `checkIn` | string | Check-in date (YYYY-MM-DD) |
| `checkOut` | string | Check-out date (YYYY-MM-DD) |
| `totalAmount` | number | Total booking amount |
| `bookingStatus` | string | Status: `CONFIRMED`, `PENDING_PAYMENT`, `CANCELLED` |
| `userName` | string | Guest's full name |
| `userEmail` | string | Guest's email address |
| `phone` | string | Guest's phone number |
| `address` | string | Guest's full address |
| `idProof` | string | Guest's ID proof number |
| `roomNumber` | string | Assigned room number |
| `roomType` | string | Room type |
| `paymentId` | string | Payment ID (null if not paid) |
| `paymentMethod` | string | Method: `UPI`, `CARD`, `NETBANKING` (null if not paid) |
| `paymentStatus` | string | Status: `SUCCESS`, `FAILED` (null if not paid) |
| `paymentDate` | string | Payment timestamp (null if not paid) |

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Non-admin user |

---

## 3. User Profile APIs

### 3.1 Get All User Profiles
Retrieve all registered user profiles.

```http
GET /api/profile/all
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "id": "65e5f6g7h8i9j0k1l2m3n4o5",
    "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+91-9876543210",
    "address": "123 Main Street, City, Country",
    "idProof": "PASSPORT-AB123456",
    "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
  },
  {
    "id": "65e5f6g7h8i9j0k1l2m3n4o6",
    "userId": "65b2c3d4e5f6g7h8i9j0k1l3",
    "name": "Jane Smith",
    "email": "jane@example.com",
    "phone": "+91-9876543211",
    "address": "456 Park Avenue, City, Country",
    "idProof": "AADHAAR-123456789",
    "photoUrl": "https://s3.amazonaws.com/bucket/photo2.jpg"
  }
]
```

| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Profile unique identifier |
| `userId` | string | Associated user ID |
| `name` | string | User's full name |
| `email` | string | User's email address |
| `phone` | string | Contact phone number |
| `address` | string | Full address |
| `idProof` | string | Government ID proof |
| `photoUrl` | string | Profile photo URL |

---

### 3.2 Search Profiles by Name
Search user profiles by name (partial match supported).

```http
GET /api/profile/search?name={searchName}
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `name` | string | Yes | Name to search for (case-insensitive) |

**Example Request:**
```http
GET /api/profile/search?name=John
```

**Response (200 OK):**
```json
[
  {
    "id": "65e5f6g7h8i9j0k1l2m3n4o5",
    "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+91-9876543210",
    "address": "123 Main Street, City, Country",
    "idProof": "PASSPORT-AB123456",
    "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
  }
]
```

**Note:** This endpoint supports partial matching. Searching for "Joh" will return "John Doe".

---

### 3.3 Get Profile by Email
Find a specific user profile by email address.

```http
GET /api/profile/email?email={emailAddress}
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `email` | string | Yes | Email address to search |

**Example Request:**
```http
GET /api/profile/email?email=john@example.com
```

**Response (200 OK):**
```json
{
  "id": "65e5f6g7h8i9j0k1l2m3n4o5",
  "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+91-9876543210",
  "address": "123 Main Street, City, Country",
  "idProof": "PASSPORT-AB123456",
  "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
}
```

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Non-admin user |
| 404 | Not Found | Profile not found |

---

## 4. Image Upload

### 4.1 Upload Image
Upload room images to S3 storage.

```http
POST /api/images/upload
```

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: multipart/form-data
```

**Request Body (Form Data):**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `file` | File | Yes | Image file (JPG, PNG, WEBP) |

**Example using FormData (JavaScript):**
```javascript
const formData = new FormData();
formData.append('file', imageFile);

const response = await fetch('http://localhost:8080/api/images/upload', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${adminToken}`
  },
  body: formData
});
```

**Response (200 OK):**
```
https://s3.amazonaws.com/bucket/images/filename_1234567890.jpg
```

**Response (500 Error):**
```
Upload failed: [error message]
```

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 400 | Bad Request | No file provided |
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Non-admin user |
| 500 | Internal Server Error | Upload failed |

---
