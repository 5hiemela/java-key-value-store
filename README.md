# Java Key-Value Store

A key-value storage engine built from scratch in Java 21. Inspired by Redis, this engine handles read/write operations, key expiration (TTL), and append-only file (AOF) persistence.

## Features
- **In-Memory Storage:** Fast key-value lookups backed by thread-safe data structures.
- **Thread Safety:** Built to safely handle simultaneous read and write actions across multiple threads.
- **Key Expiration (TTL):** Automatically expires and removes old keys after a set time.
- **Data Persistence:** Uses an Append-Only File (AOF) log to save data to disk and restore it whenever the app restarts.

## Tech Stack
- **Language:** Java 21 LTS
- **Build Tool:** Maven

