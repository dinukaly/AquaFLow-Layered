
# AquaFlow 🚰 — Smart Water Distribution System (JavaFX)

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-17-green.svg)
![MySQL](https://img.shields.io/badge/Database-MySQL-orange.svg)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

AquaFlow is a JavaFX-based Smart Water Distribution System designed to help local authorities monitor water usage, manage resources and optimize distribution. It supports real-time monitoring, reporting, and email notifications built with a clean layered architecture.


## ✅ Key Features

- 🔹 JavaFX dashboard with real-time usage stats
- 🔹 CRUD operations (Water Sources, Villages, Officers, Households, etc.)
- 🔹 Water allocation & distribution tracking
- 🔹 User login + role-based access control
- 🔹 JasperReports PDF generation
- 🔹 Email alerts via JavaMail API
- 🔹 Layered architecture (Controller, Model, DAO, Util, DTO)


## 🛠 Technologies Used

| Layer / Module   | Tech / Tools            |
|------------------|-------------------------|
| UI / Frontend    | JavaFX                  |
| Backend          | Java (Maven / JDBC)     |
| Database         | MySQL                   |
| Reporting        | JasperReports           |
| Email            | JavaMail API            |


## 🗂 Project Structure


AquaFlow/
├─ src/
│  ├─ controller/
│  ├─ BO/
│  ├─ dao/
│  ├─ util/
│  └─ dto/
├─ resources/
│  ├─ fxml/
│  ├─ images/
│  └─ jasper/
├─ pom.xml
└─ README.md


## 🚀 Getting Started

1. Clone the repository

   git clone https://github.com/dinukaly/AquaFlow-Layered.git

2. Open with **IntelliJ IDEA** (or Eclipse)

3. Configure JavaFX SDK in the IDE

4. Setup MySQL database with `AquaFlow` schema and update credentials in code

5. Run using the Main class or JavaFX Launcher

---

## 📸 Screenshots

| Dashboard                                                                                    | Billing And Payment                                                                            |
| -------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| ![](https://github.com/dinukaly/AquaFLow-Layered/blob/main/Screenshot%20\(24\).png?raw=true) | ![](https://github.com/dinukaly/AquaFLow-Layered/blob/main/Screenshot%20\(16\).png?raw=true) |

| Allocation View                                                                              | Water Consumption                                                                           |
| -------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| ![](https://github.com/dinukaly/AquaFLow-Layered/blob/main/Screenshot%20\(20\).png?raw=true) | ![](https://github.com/dinukaly/AquaFLow-Layered/blob/main/Screenshot%20\(28\).png?raw=true) |

---

## 🙌 Contributing

Contributions are welcome!
Fork the project, create a branch, and submit a pull request.

---

## 📄 License

This project is licensed under the **MIT License**.

---

*For questions or support, feel free to contact the project maintainer via GitHub.*

---


