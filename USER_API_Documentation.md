# Elite Resort - User API Documentation

> **For Frontend Developers** - Complete reference for User/Customer-facing APIs including authentication, room browsing, booking management, payments, and profile management.

---

## Base Configuration

| Property | Value |
|----------|-------|
| **Base URL** | `https://resort-production-6aed.up.railway.app` |
| **Content-Type** | `application/json` (except file uploads) |
| **Authentication** | Bearer Token (JWT) |

---

## Quick Start

### 1. Register a New Account

```http
POST /api/auth/register
```

**Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### 2. Login (Existing User)

```http
POST /api/auth/user/login
```

**Request:**
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### 3. Use the Token

Save the token and include it in all authenticated requests:

```javascript
localStorage.setItem('userToken', response.data.token);

// In axios/fetch headers
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Table of Contents

| Section | Endpoint | Auth Required | Description |
|---------|----------|---------------|-------------|
| [Authentication](#1-authentication) | `/api/auth/*` | No | Register & Login |
| [Browse Rooms](#2-room-apis) | `/api/rooms` | No | View available rooms |
| [Create Booking](#3-booking-apis) | `/bookings` | Yes | Book a room |
| [Make Payment](#4-payment-apis) | `/api/payments` | Yes | Pay for bookings |
| [Manage Profile](#5-profile-apis) | `/api/profile` | Yes | View/update profile |
| [View Invoices](#6-invoice-apis) | `/api/invoices` | Yes | Get booking invoices |
| [Contact Support](#7-contact-apis) | `/api/contact` | No | Send messages |

---

## 1. Authentication

### 1.1 User Registration
Create a new user account.

```http
POST https://resort-production-6aed.up.railway.app/api/auth/register
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | string | Yes | Full name |
| `email` | string | Yes | Valid email address (unique) |
| `password` | string | Yes | Password (min 6 characters recommended) |

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE1MTYyMzkwMjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

| Field | Type | Description |
|-------|------|-------------|
| `token` | string | JWT authentication token |
| `name` | string | User's name |
| `email` | string | User's email |
| `role` | string | User role (always "USER") |

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 400 | Bad Request | Missing fields or invalid format |
| 400 | Email already exists | Duplicate email registration |
| 500 | Internal Server Error | Server error |

---

### 1.2 User Login
Authenticate existing user.

```http
POST https://resort-production-6aed.up.railway.app/api/auth/user/login
```

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `email` | string | Yes | Registered email |
| `password` | string | Yes | User password |

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 400 | Bad Request | Invalid request format |
| 401 | Unauthorized | Invalid email or password |
| 500 | Internal Server Error | Server error |

---

## 2. Room APIs

### 2.1 Get All Rooms
Browse all available rooms (no authentication required).

```http
GET https://resort-production-6aed.up.railway.app/api/rooms
```

**Headers:** None (Public API)

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
    "available": true,
    "createdAt": "2024-01-16T10:30:00",
    "updatedAt": "2024-01-21T14:45:00"
  },
  {
    "roomId": "65c3d4e5f6g7h8i9j0k1l2m3",
    "roomNumber": "103",
    "type": "Standard",
    "pricePerNight": 100.00,
    "capacity": 2,
    "available": false,
    "createdAt": "2024-01-17T10:30:00",
    "updatedAt": "2024-01-22T14:45:00"
  }
]
```

| Field | Type | Description |
|-------|------|-------------|
| `roomId` | string | Unique room identifier |
| `roomNumber` | string | Room number |
| `type` | string | Room type: `Deluxe`, `Suite`, `Standard` |
| `pricePerNight` | number | Price per night in USD |
| `capacity` | number | Maximum guests allowed |
| `available` | boolean | Current availability status |
| `createdAt` | string | Creation timestamp (ISO 8601) |
| `updatedAt` | string | Last update timestamp (ISO 8601) |

---

### 2.2 Get Room by ID
View details of a specific room.

```http
GET https://resort-production-6aed.up.railway.app/api/rooms/{roomId}
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `roomId` | string | Yes | Room unique identifier |

**Example:**
```
GET https://resort-production-6aed.up.railway.app/api/rooms/65a1b2c3d4e5f6g7h8i9j0k1
```

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
| 404 | Not Found | Room not found |
| 500 | Internal Server Error | Server error |

---

## 3. Booking APIs

**Authentication Required:** Bearer Token

### 3.1 Create Booking
Book a room for specific dates.

```http
POST https://resort-production-6aed.up.railway.app/bookings/rooms/{roomId}
```

**Headers:**
```
Authorization: Bearer <USER_JWT_TOKEN>
Content-Type: application/json
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `roomId` | string | Yes | Room to book |

**Request Body:**
```json
{
  "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
  "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
  "checkInDate": "2024-03-01",
  "checkOutDate": "2024-03-05"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `roomId` | string | Yes | Room ID (must match path parameter) |
| `userId` | string | Yes | User ID from JWT token |
| `checkInDate` | string | Yes | Check-in date (YYYY-MM-DD) |
| `checkOutDate` | string | Yes | Check-out date (YYYY-MM-DD) |

**Response (200 OK):**
```json
{
  "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
  "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
  "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
  "checkIn": "2024-03-01",
  "checkOut": "2024-03-05",
  "createdAt": "2024-02-21T10:00:00",
  "updatedAt": "2024-02-21T10:00:00",
  "totalAmount": 600.00,
  "status": "PENDING_PAYMENT",
  "paymentId": null,
  "paymentDoneAt": null
}
```

| Field | Type | Description |
|-------|------|-------------|
| `bookingId` | string | Unique booking identifier |
| `userId` | string | User who made the booking |
| `roomId` | string | Booked room ID |
| `checkIn` | string | Check-in date (YYYY-MM-DD) |
| `checkOut` | string | Check-out date (YYYY-MM-DD) |
| `createdAt` | string | Booking creation timestamp |
| `updatedAt` | string | Last update timestamp |
| `totalAmount` | number | Total cost (calculated automatically) |
| `status` | string | Booking status: `PENDING_PAYMENT`, `CONFIRMED`, `CANCELLED` |
| `paymentId` | string/null | Associated payment ID (null initially) |
| `paymentDoneAt` | string/null | Payment timestamp (null until paid) |

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 400 | Bad Request | Invalid dates or missing fields |
| 400 | Check-out must be after check-in | Invalid date range |
| 401 | Unauthorized | Missing or invalid token |
| 404 | Room not found | Invalid room ID |
| 400 | Room is currently unavailable | Room not available |
| 400 | Room already booked for selected dates | Date conflict |
| 500 | Internal Server Error | Server error |

---

### 3.2 Cancel Booking
Cancel an existing booking.

```http
PUT https://resort-production-6aed.up.railway.app/bookings/{bookingId}/cancel
```

**Headers:**
```
Authorization: Bearer <USER_JWT_TOKEN>
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `bookingId` | string | Yes | Booking to cancel |

**Example:**
```
PUT https://resort-production-6aed.up.railway.app/bookings/65c3d4e5f6g7h8i9j0k1l2m3/cancel
```

**Response (200 OK):**
```json
"Booking cancelled successfully"
```

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 401 | Unauthorized | Missing or invalid token |
| 404 | Not Found | Booking not found |
| 500 | Internal Server Error | Server error |

---

## 4. Payment APIs

**Authentication Required:** Bearer Token

### 4.1 Make Payment
Process payment for a booking.

```http
POST https://resort-production-6aed.up.railway.app/api/payments/pay
```

**Headers:**
```
Authorization: Bearer <USER_JWT_TOKEN>
Content-Type: application/json
```

**Request Body:**
```json
{
  "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
  "method": "UPI",
  "transactionId": "TXN123456789"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `bookingId` | string | Yes | Booking ID to pay for |
| `method` | string | Yes | Payment method: `UPI`, `CARD`, `NETBANKING` |
| `transactionId` | string | Yes | Transaction reference ID from payment gateway |

**Response (200 OK):**
```json
{
  "paymentId": "65d4e5f6g7h8i9j0k1l2m3n4",
  "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
  "amount": 600.00,
  "method": "UPI",
  "status": "SUCCESS",
  "transactionId": "TXN123456789",
  "paidAt": "2024-02-21T10:30:00"
}
```

| Field | Type | Description |
|-------|------|-------------|
| `paymentId` | string | Unique payment identifier |
| `bookingId` | string | Associated booking ID |
| `amount` | number | Payment amount |
| `method` | string | Payment method used |
| `status` | string | Payment status: `SUCCESS`, `FAILED` |
| `transactionId` | string | Transaction reference |
| `paidAt` | string | Payment timestamp (ISO 8601) |

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 400 | Bad Request | Invalid payment data |
| 401 | Unauthorized | Missing or invalid token |
| 404 | Not Found | Booking not found |
| 500 | Internal Server Error | Payment processing failed |

---

### 4.2 Cancel Payment
Cancel a payment and associated booking.

```http
PUT https://resort-production-6aed.up.railway.app/api/payments/{paymentId}/cancel
```

**Headers:**
```
Authorization: Bearer <USER_JWT_TOKEN>
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `paymentId` | string | Yes | Payment to cancel |

**Example:**
```
PUT https://resort-production-6aed.up.railway.app/api/payments/65d4e5f6g7h8i9j0k1l2m3n4/cancel
```

**Response (200 OK):**
```json
"Payment and booking cancelled successfully"
```

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 401 | Unauthorized | Missing or invalid token |
| 404 | Not Found | Payment not found |
| 500 | Internal Server Error | Server error |

---

## 5. Profile APIs

**Authentication Required:** Bearer Token

### 5.1 Create or Update Profile
Create or update your profile information.

```http
POST https://resort-production-6aed.up.railway.app/api/profile
```

**Headers:**
```
Authorization: Bearer <USER_JWT_TOKEN>
Content-Type: application/json
```

**Request Body:**
```json
{
  "phone": "+91-9876543210",
  "address": "123 Main Street, City, Country",
  "idProof": "PASSPORT-AB123456",
  "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `phone` | string | No | Contact phone number |
| `address` | string | No | Full address |
| `idProof` | string | No | Government ID proof number |
| `photoUrl` | string | No | Profile photo URL |

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

| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Profile unique identifier |
| `userId` | string | Associated user ID |
| `name` | string | User's name (auto-filled from token) |
| `email` | string | User's email (auto-filled from token) |
| `phone` | string | Contact phone |
| `address` | string | Full address |
| `idProof` | string | ID proof number |
| `photoUrl` | string | Profile photo URL |

**Notes:**
- Name and email are automatically populated from JWT token
- This endpoint creates a new profile or updates existing one

---

### 5.2 Get My Profile
Retrieve your own profile information.

```http
GET https://resort-production-6aed.up.railway.app/api/profile/me
```

**Headers:**
```
Authorization: Bearer <USER_JWT_TOKEN>
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
| 404 | Not Found | Profile not found |
| 500 | Internal Server Error | Server error |

---

## 6. Invoice APIs

**Authentication Required:** Bearer Token

### 6.1 Get Invoice by Booking ID
Retrieve invoice for a specific booking.

```http
GET https://resort-production-6aed.up.railway.app/api/invoices/{bookingId}
```

**Headers:**
```
Authorization: Bearer <USER_JWT_TOKEN>
```

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `bookingId` | string | Yes | Booking ID |

**Example:**
```
GET https://resort-production-6aed.up.railway.app/api/invoices/65c3d4e5f6g7h8i9j0k1l2m3
```

**Response (200 OK):**
```json
{
  "id": "65f6g7h8i9j0k1l2m3n4o5p6",
  "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
  "roomAmount": 600.00,
  "finalAmount": 600.00,
  "paymentStatus": "PAID",
  "createdAt": "2024-02-21T10:30:00"
}
```

| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Invoice unique identifier |
| `bookingId` | string | Associated booking ID |
| `roomAmount` | number | Room charges |
| `finalAmount` | number | Final amount (after taxes/fees if any) |
| `paymentStatus` | string | Payment status: `PAID`, `PENDING` |
| `createdAt` | string | Invoice generation timestamp |

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 401 | Unauthorized | Missing or invalid token |
| 404 | Not Found | Invoice not found |
| 500 | Internal Server Error | Server error |

---

## 7. Contact APIs

### 7.1 Submit Contact Message
Send a message to resort support.

```http
POST https://resort-production-6aed.up.railway.app/api/contact
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phoneNo": "+91-9876543210",
  "subject": "Booking Inquiry",
  "message": "I would like to know about room availability for next month."
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | string | Yes | Sender's full name |
| `email` | string | Yes | Contact email |
| `phoneNo` | string | No | Phone number |
| `subject` | string | Yes | Message subject |
| `message` | string | Yes | Message content |

**Response (200 OK):**
```json
"Message submitted successfully"
```

**Error Responses:**

| Status | Message | Scenario |
|--------|---------|----------|
| 400 | Bad Request | Missing required fields |
| 500 | Internal Server Error | Server error |

