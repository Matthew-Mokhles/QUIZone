# Quiz Management System (QMS)

The Quiz Management System (QMS) is a robust platform designed to streamline the process of creating, managing, and conducting quizzes efficiently. With the increasing demand for digital assessment tools, QMS serves as a comprehensive solution to meet the diverse needs of educators, trainers, and organizations.

## Table of Contents

- [Introduction](#introduction)
- [Key Features](#key-features)
- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Contributors](#contributors)
- [License](#license)

## Introduction

QMS provides a user-friendly interface that simplifies the quiz creation process, allowing instructors to focus on the content rather than the logistics of assessment. By centralizing quiz management tasks, the system aims to enhance productivity and accuracy in evaluating learners' knowledge and progress.

## Key Features

- **Dynamic Quiz Generation**: Adaptive question pool and randomization of questions and answers.
- **Real-Time Quiz Customization**: On-the-fly editing and instant feedback.
- **Comprehensive Dashboard for Teachers**: Data visualization and actionable insights.
- **Attendance and Performance Tracking**: Automated attendance and detailed performance metrics.
- **AI-Enhanced Question Generation**: AI-powered algorithms for generating diverse and challenging questions.
- **User-Friendly Interface**: Intuitive navigation and responsive design.
- **Robust Backend Infrastructure**: Scalable and secure data management.

## Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   ├── yourpackage
│   │   │   │   │   ├── Main.java
│   │   │   │   │   ├── controllers
│   │   │   │   │   │   └── YourController.java
│   │   │   │   │   ├── models
│   │   │   └── resources
│   │   │       ├── fxml
│   │   │       │   └── YourView.fxml
│   │   │       └── css
│   │   │           └── style.css
│   └── test
│       └── java
│           └── com
│               └── yourpackage
│                   └── YourTest.java
├── README.md
├── LICENSE
├── Example for importing Excel.xlsx
├── AI model.pdf
├── Quiz Manangement System Presentation.pptx
└── Quiz Management System Overview.pdf
```

## Technology Stack

- **Frontend**: JavaFX
- **Backend**: Java (OOP principles)
- **Database**: MySQL
- **AI Integration**: Ollama API for intelligent question generation
- **Networking**: Socket programming in Java

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/quiz-management-system.git
   ```
2. Navigate to the project directory:
   ```sh
   cd quiz-management-system
   ```
3. Set up the database:
   - Create a MySQL database.
   - Run the provided SQL script to set up the necessary tables.

4. Configure database connection:
   - Update the database configuration in the application properties file.

5. Build the project:
   ```sh
   ./gradlew build
   ```

6. Run the application:
   ```sh
   ./gradlew run
   ```

## Usage

1. Launch the application.
2. Log in as an instructor to create and manage quizzes.
3. Log in as a student to take quizzes and view results.
4. Use the dashboard to monitor attendance and performance metrics.

## Contributors

- **Matthew Mokhles** - Project Lead
- **Beshoy Marco** - UI Design
- **Veronia Gamil** - Educational Technology Specialist

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to customize this README further based on your specific project needs and details.
