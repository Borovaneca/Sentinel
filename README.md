# 🛡️ Sentinel Anti-Phishing Discord Bot


Sentinel is a powerful anti-phishing bot tailored for the SoftUni Discord Community. It ensures the security of your server by monitoring messages for malicious links or phishing attempts. Equipped with advanced detection algorithms, it provides real-time protection, keeping your community safe.

## ✨ Features

- **Real-time Message Scanning:** Monitors and detects phishing links in every message.
- **Role-based Access:** Restricts bot configuration to specific user roles.
- **Customizable Responses:** Options to delete, warn, or notify admins about threats.
- **Whitelist/Blacklist Management:** Manage domains allowed or blocked.
- **Alert System:** Sends notifications to a designated channel or specific users.

## ⚙️ Getting Started

### Prerequisites

- Java JDK 20+
- A Discord Bot Token from the Discord Developer Portal
- Administrative access to your Discord server

### 🛠️ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Borovaneca/sentinel.git
   cd sentinel
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Configure the bot by editing `application.properties` to add your bot token and other settings:
   ```properties
   BOT_TOKEN=your-discord-bot-token
   ```

4. Run the bot:
   ```bash
   java -jar target/sentinel.jar
   ```

### 🔧 Commands

Interact with the bot using these commands:
- `/add-domain <domain>`: Add a domain to the whitelist.
- `/remove-domain <domain>`: Remove a domain from the whitelist.
- `/all-domains`: Show all whitelisted domains.
- `/add-subdomain`: Add a subdomain to the whitelist.
- `/all-subdomain`: Show all whitelisted sub-domains.
- `/info`: Info about the bot.

## 🛡️ Important Notes

- Sentinel is designed specifically for the SoftUni Discord Community and might require customization for other environments.
- Regularly update the whitelist and blacklist to maintain optimal security.

## 🤝 Contributing

Contributions are welcome! Follow the GitHub flow:
1. Fork the repository.
2. Create a new branch: `git checkout -b feature-name`.
3. Commit your changes: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature-name`.
5. Open a pull request.

## 📜 License

This project is licensed under the MIT License. See the LICENSE file for more details.

## 💬 Community

For support or to report issues, please contact the **SoftUni Discord Community** maintainers or open an issue on GitHub.
