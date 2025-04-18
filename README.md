# BlockTrials

A MinecraftBut-style Spigot plugin that turns your Minecraft world into a series of wild and unpredictable scenarios. Combine challenges, hazards, and world-altering events in any combination for chaotic multiplayer sessions or single-player fun.

## 🚀 Features

BlockTrials comes with a variety of built‑in scenarios that you can enable, disable, or configure via YAML. Activate one or stack multiple for truly unpredictable gameplay:

| Name               | Description                                                            |
| ------------------ |------------------------------------------------------------------------|
| Anvil Rain         | Anvils will spawn from the sky every minute.                           |
| Bedrock Trail      | Wherever you move, you leave a bedrock trail.                          |
| Sun Burn           | You burn in the sun.                                                   |
| Speed Surge        | Every minute your speed gets faster.                                   |
| Linked Damage      | All players share damage.                                              |
| Linked Hunger      | All players share a single hunger bar.                                 |
| Linked Inventories | All players share a single inventory.                                  |
| No Jump            | Attempting to jump will kill you.                                      |
| TNT Rain           | TNT will spawn from the sky every minute.                              |
| TNT Trail          | Wherever you move, you leave a TNT trail.                              |
| World Decay        | Every block the sun touches slowly breaks.                             |
| Lava Rises         | Lava slowly rises every 30 seconds.                                    |
| Glitch             | A random glitch event will happen every 10 seconds.                    |

## 🛠️ Configuration

<details>
    <summary>config.yml</summary>

```yaml
language: "EN-US"
logging: "FULL"
```

</details>

<details>
    <summary>scenarios.yml</summary>

```yaml
anvil_rain:
  name: "Anvil Rain"
  description: "Anvils will spawn from the sky every minute."

bedrock_trail:
  name: "Bedrock Trail"
  description: "Wherever you move, you leave a bedrock trail."

sun_burn:
  name: "Sun Burn"
  description: "You burn in the sun."

speed_surge:
  name: "Speed Surge"
  description: "Every minute your speed gets faster."

linked_damage:
  name: "Linked Damage"
  description: "All players share damage."

linked_hunger:
  name: "Linked Hunger"
  description: "All players share a single hunger bars."

linked_inventories:
  name: "Linked Inventories"
  description: "All players share a single inventory."

no_jump:
  name: "No Jump"
  description: "Attempting to jump will kill you."

tnt_rain:
  name: "TNT Rain"
  description: "TNT will spawn from the sky every minute."

tnt_trail:
  name: "TNT Trail"
  description: "Wherever you move, you leave a TNT trail."

world_decay:
  name: "World Decay"
  description: "Every block the sun touches slowly breaks."

lava_rises:
  name: "Lava Rises"
  description: "Lava slowly rises every 30 seconds, consuming the world."

glitch:
  name: "Glitch"
  description: "A random glitch event will happen every 10 seconds."
```

</details>

<details>
    <summary>en-us.yml</summary>

```yaml
prefix: "&b&lBlockTrials&r"

correct-usage: "{prefix} &cUsage: /blocktrials {usage}"
no-permission: "{prefix} &cYou do not have permission to run this command."
player-command-only: "{prefix} &cThis command can only be ran by players."

scenario-not-found: "{prefix} &cThe scenario '{name}' does not exist."

reload-success: "{prefix} &aSuccessfully reloaded!"

scenario-start: "{prefix} &aThe scenario \"{name}\" has started."
scenario-end: "{prefix} &cThe scenario \"{name}\" has ended."
```

</details>

---

⭐ If you find this project useful, consider giving it a star on GitHub!

📜 This project is under the [Proprietary Non-Commercial License](LICENSE).
