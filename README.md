# Boosters

Boosters is a lightweight, scalable, and flexible Minecraft plugin that allows players to collect and activate XP, chest, and custom boosters.
Designed for 1.12.2 and 1.19.4 compatibility with minimal resource impact.

---

## Features

- Players can collect boosters and activate them at any time
- Boosters have configurable strength and duration
- Real-time rotating BossBar display for active boosters
- `/booster` GUI for browsing and activating boosters
- Support for infinite booster types and strengths
- `/tip` system to reward booster creators
- API for integration with other plugins (e.g., Skills plugin)
- Player-based bossbar visibility toggling
- Full YAML storage for boosters and toggle settings
- Admin commands for live management
- Performance-optimized for large servers

---

## Commands

| Command | Description | Permission |
|:--------|:------------|:-----------|
| `/booster` or `/boosters` | Opens the boosters main menu | `boosters.use` |
| `/boosters reload` | Reloads plugin config and active boosters | `boosters.admin` |
| `/boosters modifytime <+5m/-2h>` | Modifies active boosters' remaining time | `boosters.admin` |
| `/boosters active` | View all currently active boosters | `boosters.use` |
| `/boosters toggle` | Toggle showing or hiding the booster bossbar | `boosters.use` |
| `/givebooster <player> <type> <strength> <duration>` | Give a player a booster manually | `boosters.admin` |
| `/createboost <type>` | Create a new booster type for the server | `boosters.admin` |
| `/tip` | Tip the creator of the currently active booster | `boosters.use` |

---

## Permissions

| Node | Description |
|:-----|:------------|
| `boosters.use` | Allows access to the boosters menu, tipping, and bossbar toggling |
| `boosters.admin` | Allows admins to manage boosters and modify settings |

---

## Configuration Files

### `config.yml`

- Stores general settings, booster types, and tip rewards.
- Supports dynamic creation of booster types and configurations.

### `playerdata.yml`

- Stores UUIDs of players who have hidden the bossbar.

Example:

```yaml
hidden-players:
  - 123e4567-e89b-12d3-a456-426614174000
  - 987fbc97-4bed-5078-8f07-9141ba07c9f3
```

---

## Developer API

Boosters provides a clean API for external plugin support:

```java
// Check if a player has booster bossbars visible
boolean visible = Boosters.getInstance().getBoosterAPI().isBossbarVisible(Player player);
boolean visible = Boosters.getInstance().getBoosterAPI().isBossbarVisible(UUID uuid);

// Toggle a player's booster bossbar visibility
Boosters.getInstance().getBoosterAPI().toggleBossbarVisibility(Player player);
Boosters.getInstance().getBoosterAPI().toggleBossbarVisibility(UUID uuid);
```

---

## Requirements

- Java 8 or higher
- Spigot / Paper server (1.12.2, 1.19.4 fully supported)

---

## Notes

- Players who have no toggle setting saved are automatically treated as visible by default.
- Boosters only rotate bossbar displays if there are multiple active boosters.
- Player bossbar preferences are preserved across restarts.

---

## Roadmap

- Booster stacking multipliers
- Per-booster tip command customization
- Advanced GUI pagination
- Discord webhook booster announcements (optional feature)

---

## License

This plugin is proprietary unless otherwise stated.
For licensing inquiries, please contact the plugin owner.

