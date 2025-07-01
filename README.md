
# A.D.K.E.B: A 2D Dungeon Roguelite Game  
*An apple a day keeps the enemies at bay*  
**Developed by Barabas Robert Ilie | Java + LibGDX | 2025**

---

## Introduction and Motivation

This project is a 2D roguelike game where the player navigates a dungeon filled with moving enemies. The primary motivation was to create a simple but functional game using Java and LibGDX, implementing core game design principles like AI movement, collision detection, and game state management.

The game serves both as a learning experience and as a demonstration of theoretical knowledge in object-oriented programming and interactive graphics development.

---

## Theoretical Background

- **Game Loops:** Core engine control structure based on time deltas.  
- **Entity Component Model:** Each game object (player, enemy, wall) is an independent class with encapsulated behavior.  
- **Collision Detection:** Uses `Rectangle.overlaps` to handle walls, knockbacks, and movement constraints.  
- **Sprite Manipulation:** Rotation, scaling, and animation effects add life to the characters.  
- **User Input Handling:** Uses LibGDX's built-in input listeners to move the player and shoot.

---

## Problem Description

The challenge was to simulate a dungeon where enemies pursue the player, navigate around walls, and respond to player attacks (e.g., knockback, hit effects). The game needed to balance fun, performance, and a scalable 2D environment.

---

## Proposed Solution

- **Language:** Java  
- **Framework:** [LibGDX](https://libgdx.com/)  
- **Concept:** A top-down 2D game where the protagonist interacts with an increasingly dangerous environment, aided by weapons and upgrades.  
- **Tools Used:**  
  - IntelliJ IDEA (development)  
  - Krita (drawing sprites)

---

## Application Presentation

### Logic Structure

```
MainGame
├── MainMenuScreen
│    └── Game UI
└── FirstScreen
     ├── Game UI
     ├── Protagonist
     │     ├── Weapons
     │     └── Projectiles
     ├── Enemies
     │     └── Projectiles
     ├── Pick-ups (coins/shop items)
     └── Walls
```

### Data Management

Important data is stored in the `saves` folder and contains information such as:  
- Stage number  
- Health  
- Coins  
- Etc.

### How to Use

```bash
- Run the Rogue.jar
- Start the game or load a previous save
- Controls:
  - **WASD** - Movement
  - **Left Click** - Shoot
  - **Q/E** - Switch weapons
```

---

## Conclusion

This project successfully demonstrates the implementation of a 2D roguelike dungeon game using Java and LibGDX, combining practical programming skills with game design concepts such as AI, collision detection, and user interaction. The modular architecture ensures scalability and maintainability, allowing future enhancements like new enemy types, weapons, or game mechanics. Overall, the project met its goals of creating an engaging and functional game while serving as an effective learning experience in game development.

---

## Bibliography

- [LibGDX Official Documentation](https://libgdx.com/wiki/)  
- [Krita Manual](https://docs.krita.org/en/)  
- Stack Overflow community for troubleshooting and development tips  
- "Game Programming Patterns" by Robert Nystrom — https://gameprogrammingpatterns.com/  
- Oracle Java Tutorials — https://docs.oracle.com/javase/tutorial/

---

## Platforms

- **core:** Main module with the application logic shared by all platforms.  
- **lwjgl3:** Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

---

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies. The Gradle wrapper is included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew`.

**Useful Gradle tasks and flags:**

- `--continue`: Continue running tasks even if errors occur.  
- `--daemon`: Use the Gradle daemon to run tasks.  
- `--offline`: Use cached dependencies only.  
- `--refresh-dependencies`: Force revalidation of dependencies (useful for snapshots).  
- `build`: Build sources and archives of all projects.  
- `cleanEclipse`: Remove Eclipse project data.  
- `cleanIdea`: Remove IntelliJ project data.  
- `clean`: Delete `build` folders with compiled classes and archives.  
- `eclipse`: Generate Eclipse project files.  
- `idea`: Generate IntelliJ project files.  
- `lwjgl3:jar`: Build runnable jar (found at `lwjgl3/build/libs`).  
- `lwjgl3:run`: Start the application.  
- `test`: Run unit tests (if any).

> Most tasks can be run for a specific project by prefixing with the project ID.  
> Example: `core:clean` cleans only the `core` project.

---
