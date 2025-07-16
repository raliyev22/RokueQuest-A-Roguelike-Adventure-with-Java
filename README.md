# RokueQuest – A Java Roguelike Dungeon Adventure

**RokueQuest** is a dungeon exploration game built with Java and JavaFX, developed for the COMP 302 – Software Engineering course (Fall 2024). In this roguelike adventure, you explore elemental halls, uncover runes, escape monsters, and wield enchantments — all under a ticking clock.


---

## 🎮 Game Overview

In RokueQuest, the player controls a Hero navigating through four themed halls:
- 🌍 Hall of Earth
- 🌬️ Hall of Air
- 🌊 Hall of Water
- 🔥 Hall of Fire

The goal: **Find the hidden rune in each hall before time runs out**, using logic, enchantments, and quick reflexes while avoiding randomly spawning monsters.

---

## 🧩 Features

- 🧱 **Build Mode**: Design your own dungeon hall layouts interactively.
- 🕹️ **Play Mode**: Classic top-down movement with mouse interaction to uncover objects.
- 👾 **Monster AI**:
  - Archer: Ranged attacker
  - Fighter: Melee chaser
  - Wizard: Rune teleportation & player relocation (via **Strategy Pattern**)
- 🪄 **Enchantments**:
  - Extra Time, Extra Life, Rune Reveal, Cloak of Protection, Luring Gem
- 🧠 **Design Patterns**:
  - Strategy Pattern (wizard behavior)
  - Optional Adapter Pattern (save/load design)
- 💾 **Save/Load System**: Store and restore full game state using Java serialization.
- 🖥️ **JavaFX GUI**:
  - Interactive buttons (Start, Help, Load, Exit, Pause)
  - Real-time UI updates: lives, time, hall name, inventory
- 🎨 **Customizable Graphics**: Replace or expand assets as desired.

---

## 🛠️ Tech Stack

- **Language**: Java (Java SE 8+)
- **GUI Framework**: JavaFX
- **Development Tools**: IntelliJ IDEA / Eclipse / VS Code
- **Serialization**: Java `Serializable` interface
- **Design Principles**: OOP, Strategy Pattern, Modular Design

---

