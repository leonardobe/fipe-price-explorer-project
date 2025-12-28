# FIPE Price Explorer

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Backend](https://img.shields.io/badge/Backend-Java-success)
![API](https://img.shields.io/badge/REST-API-lightgrey)
![License](https://img.shields.io/badge/License-MIT-green)

A clean and robust **Java back-end console application** that consumes the **FIPE public API (v2)** to retrieve Brazilian vehicle pricing data by brand, model, and year.

The project focuses on **real-world back-end practices**, including external API consumption using **Spring WebClient**, layered architecture, DTO-based data mapping, defensive programming, and **graceful error handling**, making it well-aligned with **Java Backend Developer roles**.

---

## 🚀 Features

* 🚗 List all vehicle brands or search brand codes by name
* 🔍 Retrieve models by brand
* 📅 List available years for a model
* 🧠 Automatically handles FIPE’s internal `YYYY-M` year format
* 📊 Fetch and display vehicle price history across all available years
* 🧾 Clean and formatted console tables
* ⚠️ Graceful error handling for invalid parameters, API errors, and connectivity issues
* 🔁 Continuous menu execution (no application restart required)
* 🧱 Layered architecture (Runner / Service / Client / DTO)

---

## 🛠️ Tech Stack

* **Java 21**
* **Spring Boot 3**
* **Spring WebClient (Reactive HTTP Client)**
* **Maven**
* **Jackson**
* **REST API consumption**
* **FIPE API (Parallelum)**

---

## 📁 Project Structure

```text
br.com.fipe.fipepriceexplorer
├── runner/    # Console interaction and application flow
├── service/   # Business logic and orchestration
├── client/    # FIPE API communication (WebClient)
├── dto/       # Data Transfer Objects
├── config/    # WebClient configuration
├── util/      # Console formatting
└── Application.java
```

> The structure follows separation of concerns, improving readability, maintainability, and backend scalability.

---

## 📦 API Reference

**Base URL:**

```
https://fipe.parallelum.com.br/api/v2
```

### Example Endpoint

```
/cars/brands/{brandId}/models/{modelId}/years/{yearId}
```

> The application abstracts FIPE-specific details such as the `YYYY-M` year format, allowing users to input only the year (`YYYY`).

---

## ▶️ How to Run

### Prerequisites

* Java 17 or higher
* Maven

### Steps

#### 1. Clone the repository:

```bash
https://github.com/leonardobe/fipe-price-explorer-project.git
```

#### 2. Navigate to the project folder:

```bash
cd fipe-price-explorer-project
```

#### 3. Run the application:

##### Using Maven

```bash
mvn spring-boot:run
```

> The application starts directly in the terminal.

##### Run without Maven (IDE)

Alternatively, you can run the application directly from your IDE:

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
2. Locate the `FipePriceExplorerApplication` class
3. Run the class as a Java Application
   
---

## 📌 Example Usage

```
📊 FIPE PRICE HISTORY
────────────────────────────────────────────────────────────────────────────
| BRAND        | MODEL                     | YEAR | FUEL      | PRICE      |
────────────────────────────────────────────────────────────────────────────
| FIAT         | PALIO 1.0 MPI             | 2014 | GASOLINE  | R$ 28.450  |
| FIAT         | PALIO 1.0 MPI             | 2015 | GASOLINE  | R$ 30.120  |
| FIAT         | PALIO 1.0 MPI             | 2016 | GASOLINE  | R$ 32.890  |
────────────────────────────────────────────────────────────────────────────
```

---

## 📈 Learning Outcomes

This project reinforces key **Java back-end development concepts**, including:

* Consuming external REST APIs using **Spring WebClient**
* Designing layered architectures (Client / Service / Runner)
* DTO-based JSON mapping with **Jackson**
* Defensive programming and structured exception handling
* Building user-friendly CLI applications with backend-quality code
* Writing clean, maintainable, and readable Java code
* Managing builds and dependencies with **Maven**

---

## 📄 License

This project is licensed under the **MIT License**.

---

## 👨‍💻 Author

Developed by **Leonardo**.  
Focused on Java back-end development, clean architecture, and continuous learning.
