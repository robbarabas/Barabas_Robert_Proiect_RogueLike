# An Aplle a Day keeps the enemies at bay
# A.D.K.E.B: A 2D Dungeon roguelite game

### Developed by Barabas Robert Ilie| Java + LibGDX | 2025

---

## Introduction ans Motivation

This project is a 2D roguelike  game where the player navigates a dungeon filled with moving enemies. The primary motivation was to create a simple but functional game using Java and LibGDX, while implementing core game design principles like AI movement, collision detection, and game state management. 

The game serves both as a learning experience and as a demonstration of theoretical knowledge in object-oriented programming and interactive graphics development.

---

## Theoretical Background

- **Game Loops**: Core engine control structure based on time deltas.
- **Entity Component Model**: Each game object (player, enemy, wall) is an independent class with encapsulated behavior.
- **Collision Detection**: Uses (`Rectangle.overlaps`) to handle walls, knockbacks, and movement constraints.
- **Sprite Manipulation**: Rotation, scaling, and animation effects add life to the characters.
- **User Input Handling**: Uses LibGDX's built-in input listeners to move the player and shoot.

---

## Problem Description

The challenge was to simulate a dungeon where enemies pursue the player, navigate around walls, and respond to player attacks (e.g., knockback, hit effects). The game needed to balance fun,performance, a scalable 2D environment.

---

##  Proposed Solution

- **Language**: Java  
- **Framework**: [LibGDX](https://libgdx.com/)  
- **Concept**: A top-down 2D game where enemies track the player, interact with the environment, and respond to being hit.  
- **Tools Used**:  
  - IntelliJ IDEA (development)  
  - Krita (drawing sprites)
---


###  Architecture & Structure
```
MainGame
├── MainMenuScreen
│    └── Game UI
└── FirstScreen
     ├── Game UI
     ├── Protagonist
     │     ├── weapons
     │     └── projectiles
     ├── Enemies
     │     └── projectiles
     ├── Pick-ups (coins/shopitems)
     └── Walls
```
## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
