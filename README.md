# Gym Management System

A desktop-based Gym Management System developed using **Java Swing, JDBC, and MySQL**. It helps an administrator manage gym members, trainers, payments, attendance, and reports.

## Features
- Admin login and dashboard
- Member management (add, view, update, delete, search)
- Trainer management
- Payment management
- Attendance management
- Reports and analytics
- Database connection settings

## Technology Stack
- Java and Java Swing
- JDBC
- MySQL
- MySQL Connector/J
- Visual Studio Code

## Requirements
Install Java JDK and MySQL Server on the computer where you want to run the application.

Check Java:
```bash
java -version
```

Make sure MySQL Server is running before launching the app.

## Download the Project

### Download ZIP
1. Open https://github.com/kartiktale12/GymManagementSystem
2. Click **Code**.
3. Click **Download ZIP**.
4. Extract the downloaded ZIP.

### Clone with Git
```bash
git clone https://github.com/kartiktale12/GymManagementSystem.git
cd GymManagementSystem
```

## Database Setup
The application expects a MySQL database named **`gym_management`** with these existing tables:
- `membership`
- `trainer`
- `member`
- `payment`
- `attendance`

1. Start MySQL Server.
2. Open MySQL Workbench or the MySQL command-line client.
3. Run the project SQL setup script if it is included in `database/gym_management.sql`.
4. If the SQL file is not included yet, use the database setup SQL supplied with the project.

**Important:** The JAR does not contain the database. MySQL must be running and the database must be accessible.

### Check Database Connection
Open `src/com/gym/DBConnection.java` and verify the JDBC URL, MySQL username, and password match your local MySQL setup. Do not upload real passwords to GitHub.

Typical local JDBC URL:
```text
jdbc:mysql://localhost:3306/gym_management
```

Use your actual connection settings and keep your existing database schema unchanged.

## Run the Application

### Option 1: Run the JAR
1. Download `GymManagementSystem.jar` from the repository (if present).
2. Ensure Java is installed and MySQL/database setup is complete.
3. Open a terminal in the folder containing the JAR.
4. Run:
```bash
java -jar GymManagementSystem.jar
```

If you see `UnsupportedClassVersionError`, the Java runtime is older than the Java version used to compile the JAR. Use a compatible runtime or rebuild with a compatible JDK.

### Option 1A: Run Using the Java 27 Runtime in VS Code (PowerShell)
On the development computer where the Java extension-pack runtime is installed, open **PowerShell** and run:

```powershell
& "$env:APPDATA\Code\User\globalStorage\pleiades.java-extension-pack-jdk\java\latest\bin\java.exe" -jar "$env:USERPROFILE\Downloads\GymManagementSystem.jar"
```

**What this command means:**
- `&` tells PowerShell to execute the program at the quoted path.
- `$env:APPDATA` points to your Windows AppData\Roaming folder, where the Java extension-pack runtime is stored on this computer.
- `java.exe` launches the Java runtime at that location, avoiding an older Java version that might be first on PATH.
- `-jar` tells Java to run the JAR application.
- `$env:USERPROFILE\Downloads\GymManagementSystem.jar` points to the JAR in your Windows Downloads folder.

**Important:** This exact command is specific to a computer that has the Java runtime at that path and the JAR in Downloads. If either file is in a different location, update the path. MySQL Server must also be running, and `DBConnection.java` must contain valid database connection settings. Other computers may need a different Java path or a compatible Java installation.

### Option 2: Compile and Run from Source (Windows)
Run these commands from the project root. If your Connector/J file has a different name, replace `mysql-connector-j-26.7.0.jar` with the actual filename in `lib`.

Compile:
```bat
javac -cp "lib\mysql-connector-j-26.7.0.jar" -d out src\com\gym\*.java src\com\gym\ui\*.java src\com\gym\dashboard\*.java src\com\gym\members\*.java src\com\gym\trainers\*.java src\com\gym\payments\*.java src\com\gym\attendance\*.java src\com\gym\reports\*.java src\com\gym\settings\*.java
```

Run:
```bat
java -cp "out;lib\mysql-connector-j-26.7.0.jar" com.gym.Main
```

## Login
Development login credentials previously used:
- **Username:** `admin`
- **Password:** `admin123`

Change default credentials before using the app with real data.

## Troubleshooting

**MySQL connection refused / Communications link failure**
- Ensure MySQL Server is running.
- Check host and port (commonly `localhost:3306`).
- Confirm `gym_management` exists.

**Access denied for user**
- Verify MySQL username and password in `DBConnection.java`.
- Check the account's database permissions.

**JDBC driver not found**
- Confirm the Connector/J JAR is inside `lib/`.
- Use its exact filename in the commands.

**UnsupportedClassVersionError**
- Use a Java runtime compatible with the JDK used to compile the JAR, or rebuild the JAR with a compatible JDK.

## Project Structure
```text
GymManagementSystem/
├── lib/                         # JDBC driver
├── out/                         # Compiled classes
├── src/com/gym/
│   ├── Main.java
│   ├── DBConnection.java
│   ├── ui/
│   ├── dashboard/
│   ├── members/
│   ├── trainers/
│   ├── payments/
│   ├── attendance/
│   ├── reports/
│   └── settings/
├── manifest.txt
├── GymManagementSystem.jar
└── README.md
```

## Important Notes
- This is a **Java Swing desktop application**, not a website. GitHub stores the project files but does not run the application in a browser.
- Every computer needs Java and access to a configured MySQL database.
- Do not upload database passwords, private credentials, or real member data to a public repository.
- Keep the database schema consistent with the project.

## Academic Project
Created as a college DBMS project.

## License
For academic and educational use unless a separate license is added.
