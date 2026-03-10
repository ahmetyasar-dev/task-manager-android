# Task Manager Android App

A modern task management application built with **Kotlin**, **XML**, **MVVM**, and **Room Database**.

This app allows users to create, update, complete, and delete tasks with a clean and user-friendly interface.  
It was developed as a portfolio project to demonstrate Android development fundamentals and modern app architecture.

---

## Features

- Add new tasks
- List tasks with RecyclerView
- Update existing tasks
- Delete tasks with long press
- Mark tasks as completed
- Show visual feedback for completed tasks
- Show empty state when there are no tasks
- Store data locally with Room Database

---

## Tech Stack

- **Kotlin**
- **XML**
- **MVVM Architecture**
- **Room Database**
- **RecyclerView**
- **LiveData**
- **ViewModel**
- **Coroutines**

---

## Architecture

This project follows the **MVVM** architecture:

- **UI Layer**
  - `MainActivity`
  - XML layouts
  - `TaskAdapter`

- **ViewModel Layer**
  - `TaskViewModel`

- **Repository Layer**
  - `TaskRepository`

- **Data Layer**
  - `Task`
  - `TaskDao`
  - `TaskDatabase`

---

## Project Structure

```bash
com.ahmetyasar.taskmanagerapp
│
├── adapter
│   └── TaskAdapter.kt
│
├── data
│   └── local
│       ├── dao
│       │   └── TaskDao.kt
│       ├── database
│       │   └── TaskDatabase.kt
│       └── entity
│           └── Task.kt
│
├── repository
│   └── TaskRepository.kt
│
├── viewmodel
│   └── TaskViewModel.kt
│
└── MainActivity.kt