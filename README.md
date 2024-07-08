# Anti-Phishing Discord Bot 🤖

Welcome to the **Sentinel Discord Bot** repository! This powerful bot is designed to keep your Discord server safe by monitoring every message for phishing attempts or harmful sites. Our bot uses advanced algorithms to detect and alert you about potential threats, ensuring your community stays protected.


## Features ✨

- **Real-time Message Scanning:** The bot scans every message in real-time for phishing links or malicious sites.
- **Role-based Access:** Only users with specific roles can configure the bot settings.
- **Alert System:** Sends alerts to a designated channel or user when a potential threat is detected.
- **Customizable Response:** Choose how the bot responds to detected threats (e.g., delete the message, warn the user, etc.).
- **Whitelist/Blacklist:** Manage lists of allowed and disallowed sites.

## Getting Started 🚀

### Prerequisites

Before you begin, ensure you have the following:

- [Java JDK 20+](https://www.oracle.com/java/technologies/javase-jdk20-downloads.html) installed
- A Discord bot token from the [Discord Developer Portal](https://discord.com/developers/applications)
- Administrative access to the Discord server you want to protect

### Installation

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/yourusername/anti-phishing-discord-bot.git
    cd anti-phishing-discord-bot
    ```

2. **Build the Project:**
    - If you're using Maven, build the project using:
      ```bash
      mvn clean install
      ```
    - If you're using Gradle, build the project using:
      ```bash
      gradle build
      ```

3. **Configure the Bot:**
    - Rename `config.example.properties` to `config.properties` and add your bot token and other configuration details.

4. **Run the Bot:**
    ```bash
    java -jar target/anti-phishing-discord-bot.jar
    ```

### Usage

Once the bot is running, it will automatically start monitoring messages in your server. You can interact with the bot using the following commands:

- `/add-site <domain>`: Add a site to the whitelist.
- `/remove-site <domain>`: Remove a site from the whitelist.
- `/status`: Check the bot's status and settings.
