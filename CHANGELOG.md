# Changelog

All notable changes to WhipWelcome will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.1] - 2025-10-19

### 🐛 Bug Fixes
- **Fixed Java version compatibility**: Changed Java target from 21 to 17
  - Resolves `Unsupported class file major version 65` error on Minecraft 1.18.2
  - Now compatible with all Paper versions 1.18+ using Java 17+
- **Fixed missing JSON library**: Added Shadow plugin to bundle dependencies
  - Resolves `NoClassDefFoundError: org/json/JSONObject` error
  - All required libraries now included in the JAR

### 🔧 Technical Changes
- Added Gradle Shadow plugin (v8.1.1) for dependency shading
- Dependencies are now relocated to avoid conflicts with other plugins:
  - `org.json` → `me.brofan11.whipwelcome.libs.json`
  - `org.sqlite` → `me.brofan11.whipwelcome.libs.sqlite`
- JAR minimization enabled to reduce file size
- Version management now reads from `plugin.yml` (single source of truth)

### 📚 Documentation
- Updated README: Java 21 → Java 17+
- Added BUILD.md with detailed build instructions
- Added FIX_GUIDE.md for troubleshooting
- Added VERSION_MANAGEMENT.md for version update workflow

### ⚠️ Migration Notes
- If you're running 1.0.0, simply replace the JAR with 1.0.1
- No config changes or data migration needed
- All your player customizations will be preserved

---

## [1.0.0] - 2025-10-18

### 🎉 Initial Release

### ✨ Core Features
- **Customizable Join Messages**: Players can set their own prefix and suffix
  - Format: `[prefix] PlayerName [suffix]`
  - Stored persistently per-player in SQLite database
- **Rich Color Support**:
  - MiniMessage tags: `<gold>`, `<gradient:#ff00ff:#00ffff>`, `<rainbow>`, `<bold>`
  - Legacy codes: `&a`, `&f`, `&l`, `&n`, `&r`
  - Hex colors: `&#ff00ff` or `#ff00ff`
  - Mix and match all formats!
- **SQLite Storage**: Automatic database creation and management
  - Location: `plugins/WhipWelcome/whipwelcome.db`
  - Per-UUID storage (survives name changes)

### 🎮 Commands
- `/welcome setprefix <text>` - Set your join message prefix
- `/welcome setsuffix <text>` - Set your join message suffix
- `/welcome view` - Preview your current customization
- `/welcome reset` - Reset to server defaults
- **Aliases**: `/wel`, `/ww`
- Full tab completion support

### 🔐 Permissions
- `whipwelcome.*` - All permissions (default: true)
- `whipwelcome.customize` - Customize own messages (default: true)

### ⚙️ Configuration (`config.yml`)
- `update-checker.enabled` - Enable/disable update checking (default: true)
- `update-checker.notify-ops` - Notify ops about updates (default: true)
- `welcome.default-prefix` - Default prefix for new players (default: "")
- `welcome.default-suffix` - Default suffix for new players (default: "")
- `welcome.max-prefix-length` - Character limit for prefix (default: 30)
- `welcome.max-suffix-length` - Character limit for suffix (default: 30)

### 🔔 Update Checker
- Automatically checks GitHub for new versions on startup
- Notifies console with a formatted box display
- Notifies online operators in-game
- Runs asynchronously (no server lag)
- Supports multiple tag formats: `v1.0.0`, `WhipWelcome-1.0.0`, etc.
- Configurable via `config.yml`

### 🛠️ Technical Details
- **API Version**: 1.18 (compatible with 1.18+)
- **Dependencies**:
  - Paper API 1.18.2-R0.1-SNAPSHOT
  - SQLite JDBC 3.47.1.0
  - JSON 20240303
- **Build Tool**: Gradle with Shadow plugin
- **Database**: SQLite with automatic table creation
- **Color Parsing**: Supports MiniMessage, legacy, and hex with fallback

### 📋 Requirements
- Paper 1.18+ (tested on 1.18, 1.19, 1.20, 1.21)
- Java 17+

---

## Version History Overview

| Version | Date | Type | Summary |
|---------|------|------|---------|
| 1.0.1 | 2025-10-19 | Bug Fix | Fixed Java 17 compatibility & bundled dependencies |
| 1.0.0 | 2025-10-18 | Initial | First public release with core features |

---

## Planned Features

Ideas for future releases:

- [ ] Join sound effects
- [ ] Animated join messages
- [ ] Per-world custom messages
- [ ] PlaceholderAPI integration
- [ ] Import/export customizations
- [ ] Admin command to view/edit any player's messages
- [ ] Join message cooldowns
- [ ] Message templates/presets

---

## Support

- **Issues**: [GitHub Issues](https://github.com/brofan11/WhipWelcome/issues)
- **Releases**: [GitHub Releases](https://github.com/brofan11/WhipWelcome/releases)
- **Source**: [GitHub Repository](https://github.com/brofan11/WhipWelcome)

---

**Made with ❤️ by brofan11**
