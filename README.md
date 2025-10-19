# WhipWelcome

Personalize your server's join messages with style. WhipWelcome lets players set their own welcome prefix and suffix that render with MiniMessage, legacy (&) color codes, and hex colors — all stored persistently in SQLite.

Built for Paper 1.18+, Java 21.

## ✨ Features

- Per-player custom join message parts:
	- Prefix and suffix combined around the player's name
	- Example build: prefix + <white>PlayerName</white> + suffix
- Color support out of the box:
	- MiniMessage: <gold>, <bold>, <gradient:#ff00ff:#00ffff>, <rainbow>
	- Legacy: &a, &f, &l, &n, &r
	- Hex: &#ff00ff or #ff00ff
- Safe length limits (configurable, exclude color codes)
- Clean fallback and preview in-game
- Uses SQLite for persistence: plugins/WhipWelcome/whipwelcome.db
- Tab completion for commands and helpful usage prompts
- Suppresses join message entirely if both prefix and suffix resolve empty (default + player), so you stay in full control
- **Automatic update checker** - Get notified when new versions are available on GitHub

## 📦 Requirements

- Paper 1.18+ (compatible with 1.18, 1.19, 1.20, 1.21+)
- Java 17+

## 🔧 Installation

1. Download/Build the plugin jar.
2. Drop the jar into your server’s `plugins/` folder.
3. Start the server to generate the default config and database.
4. Tweak `plugins/WhipWelcome/config.yml` to your liking.

## 🧪 Usage (Players)

- Set prefix: `/welcome setprefix <prefix>`
- Set suffix: `/welcome setsuffix <suffix>`
- View your settings: `/welcome view`
- Reset to server defaults: `/welcome reset`

Aliases: `/wel`, `/ww`

Preview is shown in-game with your current settings using the same color parsing the server uses.

### Color Examples

- Legacy: `&aGreen &fWhite &lBold &nUnderline`
- Hex: `&#ff00ff` or `#ff00ff`
- MiniMessage: `<red>Hello</red> <gradient:#ff00ff:#00ffff>World</gradient>`
- Mix and match: `&aHey <bold>&#ff00ffthere</bold>!`

Note: If MiniMessage parsing fails due to malformed tags, the plugin falls back to legacy parsing for robustness.

## 🔐 Permissions

From `plugin.yml`:

- `whipwelcome.*` (default: true)
	- Grants all WhipWelcome permissions
	- Children: `whipwelcome.customize`
- `whipwelcome.customize` (default: true)
	- Allows players to use `/welcome setprefix`, `/welcome setsuffix`, and `/welcome reset`

## ⚙️ Configuration

File: `plugins/WhipWelcome/config.yml`

Key options (defaults shown):

```yaml
# Update Checker Settings
update-checker:
  # Check for new versions on GitHub
  enabled: true
  
  # Notify online operators when an update is available
  notify-ops: true

welcome:
	# Shown if a player has not set their own values
	default-prefix: ""
	default-suffix: ""

	# Character limits (excluding color codes). Set -1 to disable.
	max-prefix-length: 30
	max-suffix-length: 30
```

Behavior:

- On join, the message is assembled as: `prefix + <white>PlayerName</white> + suffix`.
- If both effective prefix and suffix are empty (after defaults), the join message is hidden for that event.
- Update checker runs asynchronously on plugin enable and notifies console + online ops about new releases.

## 🗄️ Storage

- Backend: SQLite
- Location: `plugins/WhipWelcome/whipwelcome.db`
- Table: `player_messages`
	- Columns: `uuid (PK)`, `player_name`, `prefix`, `suffix`, `join_prefix`, `join_suffix`, `last_updated`

Note: Only `prefix` and `suffix` are currently used for the public join message. Additional columns are reserved for potential future features.

## 🛠️ Building from source

This project uses Gradle and targets Java 17.

Windows PowerShell (from the project root):

```powershell
.\gradlew shadowJar
```

The built jar will be in `build/libs/WhipWelcome-<version>.jar`.

For a local dev server (Paper), you can use the run task:

```powershell
./gradlew runServer
```

The task will download and run the configured Paper version and load your plugin automatically.

## 🙋 FAQ

- **What Minecraft versions are supported?**
	- Paper 1.18 and above (1.18, 1.19, 1.20, 1.21+). The plugin uses api-version 1.18 for maximum compatibility.

- **How are lengths calculated?**
	- The plugin strips color codes (MiniMessage tags, legacy codes, and hex markers) before counting characters, so limits apply to visible text only.

- **Can I disable the default join message entirely?**
	- Yes. Keep both `default-prefix` and `default-suffix` empty and players who haven't set custom values will not trigger a join message. Players with custom values will still show theirs.

- **How does the update checker work?**
	- On plugin startup, it queries the GitHub API for the latest release and compares version numbers. If a newer version exists, it notifies the console and online operators. This is fully configurable in `config.yml`.

## 📝 License

MIT or your preferred license. If none yet, consider adding one so server owners know how they can use and distribute the plugin.

---

Made with ❤️ by BroFan11. Issues and contributions welcome.

