# AutoSync 

AutoSync is a personal project I started to explore how backend systems handle multiple requests competing for the same resource.

The idea is simple: **what happens when several customers try to reserve the same car at the same time?**

The goal is to make sure only one reservation succeeds, while the others are handled properly, without compromising the consistency of the database.

I'm taking this a step further by experimenting with different concurrency-control strategies and comparing how they perform under the same conditions.

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Data JPA
- **Database:** Microsoft Azure SQL
- **APIs:** REST
- **Testing:** Concurrent HTTP requests

## What I'm Working On

The backend currently supports vehicle, customer, and reservation management, with a database constraint that prevents a vehicle from having more than one active reservation.

Next, I'm working on:

- Implementing optimistic and pessimistic locking.
- Testing each approach with different numbers of simultaneous requests.
- Comparing response times, throughput, and conflict handling.
- Building a small interactive interface to run these experiments and visualize the results.

## Progress

- [x] Backend and Azure SQL integration
- [x] Vehicle, customer, and reservation APIs for db contraint, optimistic, and pessimistic schemas
- [x] Database-level protection against double bookings
- [x] Handling reservation conflicts
- [x] Optimistic locking
- [x] Pessimistic locking
- [ ] Performance testing and comparison
- [ ] Interactive visualization

## Why I'm Building This

I wanted to work on something beyond a typical CRUD application and get a better understanding of concurrency, race conditions, and the trade-offs involved in designing reliable backend systems.

AutoSync is my way of experimenting with those concepts and making the results easier to see and understand.
