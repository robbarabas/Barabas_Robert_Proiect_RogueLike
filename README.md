
# A.D.K.E.B: A 2D Dungeon roguelite game
-** An aplle a day keeps the enemies at bay**
### Developed by Barabas Robert Ilie| Java + LibGDX | 2025

---

## Introduction and Motivation

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
- **Concept**: A top-down 2D game where the protagonist interacts with a more and more dangerous eviroment helped by weapons and upgrades.  
- **Tools Used**:  
  - IntelliJ IDEA (development)  
  - Krita (drawing sprites)
---
### Aplication Prezentation
## Logic Structure

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
## Data Mangement
```
Important data is stored in the folder saves and contains information such as :
-stage number
-health
-coins
-etc...
```
## How to use ?
```
-Run the Rogue.jar
-Start the game or make a save or load a previous save 
-**KEYS**
-**ASDW** movement
-**LeftClick** shoot
-**Q E** swich weapons

```

##Conclusion
```
This project successfully demonstrates the implementation of a 2D roguelike dungeon game using Java and LibGDX, combining practical programming skills with game design concepts such as AI, collision detection, and user interaction. The modular architecture ensures scalability and maintainability, allowing future enhancements like new enemy types, weapons, or game mechanics. Overall, the project met its goals of creating an engaging and functional game while serving as an effective learning experience in game development.
``
##Bibliography
```
LibGDX Official Documentation – https://libgdx.com/wiki/

Krita Manual – https://docs.krita.org/en/

Stack Overflow community for troubleshooting and development tips
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
