# Boosters Plugin

A lightweight and powerful booster system for Minecraft servers (1.12.2 - 1.19.4).

---

## Features
- Infinite booster types
- Infinite booster strengths and durations
- Dynamic tipping rewards
- Bossbar rotation for active boosters
- Full Skills plugin integration (optional)

---

## Installation
1. Place Boosters.jar into your server `/plugins/` folder.
2. Start your server.
3. Edit `/plugins/Boosters/config.yml` as needed.
4. Use `/createboost`, `/givebooster`, and `/boosters` commands!

---

## Commands

| Command | Permission | Description |
|:--------|:-----------|:------------|
| `/createboost <type>` | boosters.admin | Create a new booster type. |
| `/givebooster <player> <type> <strength> <duration>` | boosters.admin | Give a booster to a player. |
| `/boosters` | none | Open your boosters GUI. |
| `/tip` | none | Tip the creator of an active booster. |

---

## Placeholders

- `%player%` - Booster activator
- `%tipper%` - Player tipping
- `%creator%` - Player who started the booster
- `%strength%` - Booster strength (e.g., 2.0x)
- `%duration%` - Booster duration (in minutes)
- `%type%` - Booster type (e.g., xp, chest)

---

## Tip System

Each booster type can define **tip rewards**:
- **console:** prefix = runs a server command
- **internal:** prefix = calls plugin APIs (like `SkillsAPI.addXP`)

Example config:

```yaml
tip:
  - "console: give %tipper% diamond 1"
  - "internal: skills.addXp %tipper% 500"
```

✅ No tipping yourself.  
✅ Only 1 tip per booster per player.

---

## Skills Plugin Integration (optional)

If `Skills` plugin is installed (`com.prismforge.skills.Skills`), tipping XP automatically hooks without needing a hard dependency.

**No Skills plugin?** Boosters works anyway.

---
