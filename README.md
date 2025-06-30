# Fruit Service Engine - Distributed Systems Assignment

A distributed fruit service application using Java RMI (Remote Method Invocation) that manages fruit prices and processes customer transactions.

## Project Overview

This project implements a client-server architecture using Java RMI to create a fruit service engine that:

- Manages a fruit-price table with CRUD operations
- Calculates fruit costs based on quantity
- Generates transaction receipts
- Supports multiple client connections
- Provides both console client and servlet-based HTTP interface

## Architecture

### Components

1. **RMI Server (FruitComputeEngine)**: Executes tasks on the server side
2. **Client Registry (FruitComputeTaskRegistry)**: Manages client connections to RMI server
3. **Task Classes**: Implement specific fruit service operations
4. **Model Classes**: Data structures for fruits, purchases, and receipts
5. **Servlet Interface**: HTTP API for web-based interactions

### Seven Required Classes

1. `FruitComputeEngine.java` - Extends UnicastRemoteObject, implements compute interface
2. `FruitComputeTaskRegistry.java` - Looks for compute engine, creates and runs client tasks
3. `AddFruitPrice.java` - Task to add new fruit-price entities
4. `UpdateFruitPrice.java` - Task to update existing fruit-price entities
5. `DeleteFruitPrice.java` - Task to delete fruit-price entities
6. `CalFruitCost.java` - Task to calculate fruit cost based on quantity
7. `CalculateCost.java` - Task to generate receipts

## Prerequisites

- **Java Development Kit (JDK) 8 or higher** (NOT just JRE)
- Maven 3.6 or higher (optional - can compile with javac directly)
- Network connectivity for RMI (uses localhost:1099 by default)

### ⚠️ Important: JDK Installation Required

This project requires the **Java Development Kit (JDK)**, not just the Java Runtime Environment (JRE).

**To check if you have JDK installed:**

```bash
javac -version    # Should show compiler version
java -version     # Should show runtime version
```

**If you only see java -version working but not javac -version, you need to install JDK:**

1. **Download JDK 8 or higher:**

   - Oracle JDK: https://www.oracle.com/java/technologies/downloads/
   - OpenJDK (free): https://adoptium.net/

2. **Install JDK and add to system PATH**

3. **Verify installation:**
   ```bash
   javac -version
   java -version
   ```

## Installation & Setup

### 1. Clone and Build

```bash
# Navigate to project directory
cd distributed

# Compile the project
mvn clean compile
```

### 2. Package (Optional)

```bash
# Create JAR files
mvn package
```

## Running the Application

### Method 1: Using Provided Scripts (Recommended)

#### Step 1: Compile the Project

```powershell
.\compile-core.bat
```

#### Step 2: Start the RMI Server (Terminal 1)

```powershell
.\run-server-jdk24.bat
```

#### Step 3: Run the Client (Terminal 2)

```powershell
.\run-client-jdk24.bat
```

### Method 2: Using Maven (If Available)

#### Start the RMI Server

```bash
# Requires Maven installation
mvn compile exec:java -Dexec.mainClass="engine.FruitComputeEngine"
```

#### Run the Client

```bash
mvn exec:java -Dexec.mainClass="client.FruitServiceClient"
```

### Method 3: Manual Java Command Line

#### Compile First

```powershell
# Create target directory
mkdir target\classes

# Compile core classes (without servlet dependencies)
javac -d target\classes src\interfaces\*.java src\model\*.java src\data\*.java src\engine\*.java src\client\*.java src\tasks\*.java
```

#### Start Server

```powershell
java -cp target\classes engine.FruitComputeEngine
```

#### Run Client

```powershell
java -cp target\classes client.FruitServiceClient
```

### ⚠️ Important Notes

- **JDK Required**: This project requires JDK 8 or higher (JDK 24 recommended)
- **Version Consistency**: The provided scripts ensure compatible Java versions
- **Port 1099**: Make sure port 1099 is available for RMI registry
- **Windows PowerShell**: Use `.\script.bat` format in PowerShell

## Usage

### Console Client Interface

The client provides an interactive menu with the following options:

1. **Add New Fruit Price** - Add a fruit with its price per unit
2. **Update Fruit Price** - Modify existing fruit price
3. **Delete Fruit Price** - Remove a fruit from the database
4. **Calculate Fruit Cost** - Calculate total cost for given quantity
5. **Generate Receipt** - Create a complete transaction receipt
6. **Run All Tasks Demo** - Automated demonstration of all features
7. **Change Cashier** - Switch the logged-in cashier

### Sample Usage Flow

```
=== FRUIT SERVICE ENGINE CLIENT ===
Connecting to RMI server...
Enter cashier name: John Smith
Logged in as: John Smith

==================================================
           FRUIT SERVICE MENU
           Cashier: John Smith
==================================================
1. Add New Fruit Price
2. Update Fruit Price
3. Delete Fruit Price
4. Calculate Fruit Cost
5. Generate Receipt
6. Run All Tasks Demo
7. Change Cashier
0. Exit
==================================================
Enter your choice: 1

--- ADD NEW FRUIT PRICE ---
Enter fruit name: Watermelon
Enter price per unit: $3.99
SUCCESS: Added fruit 'Watermelon' with price $3.99 per unit
```

### Pre-loaded Data

The system comes with sample data:

- Apple: $2.50 per unit
- Banana: $1.80 per unit
- Orange: $3.20 per unit
- Mango: $4.50 per unit
- Grapes: $5.00 per unit

## HTTP Servlet API

The project includes a servlet interface for web-based interactions:

### Endpoints

- `GET /fruitservice/` - API information
- `POST /fruitservice/add` - Add fruit price
- `POST /fruitservice/update` - Update fruit price
- `POST /fruitservice/delete` - Delete fruit price
- `POST /fruitservice/calculate` - Calculate fruit cost
- `POST /fruitservice/receipt` - Generate receipt

### Sample HTTP Requests

#### Add Fruit

```json
POST /fruitservice/add
{
  "fruitName": "Pineapple",
  "price": 4.50
}
```

#### Calculate Cost

```json
POST /fruitservice/calculate
{
  "fruitName": "Apple",
  "quantity": 3.5
}
```

#### Generate Receipt

```json
POST /fruitservice/receipt
{
  "items": [
    {"fruitName": "Apple", "quantity": 3},
    {"fruitName": "Banana", "quantity": 2}
  ],
  "amountPaid": 15.00,
  "cashier": "John Smith"
}
```

## Project Structure

```
distributed/
├── src/
│   ├── interfaces/          # RMI interfaces
│   │   ├── Compute.java
│   │   └── Task.java
│   ├── model/              # Data models
│   │   ├── FruitPrice.java
│   │   ├── PurchaseItem.java
│   │   └── Receipt.java
│   ├── data/               # Data management
│   │   └── FruitPriceDatabase.java
│   ├── engine/             # Server implementation
│   │   └── FruitComputeEngine.java
│   ├── client/             # Client implementation
│   │   ├── FruitComputeTaskRegistry.java
│   │   └── FruitServiceClient.java
│   ├── tasks/              # Task implementations
│   │   ├── AddFruitPrice.java
│   │   ├── UpdateFruitPrice.java
│   │   ├── DeleteFruitPrice.java
│   │   ├── CalFruitCost.java
│   │   └── CalculateCost.java
│   └── servlets/           # Web interface
│       └── FruitServiceServlet.java
├── pom.xml                 # Maven configuration
├── README.md              # This file
└── DEVELOPMENT_LOG.md     # Development diary
```

## Features

### Five Client Tasks Implemented

1. **Add Fruit-Price Entity**: Add new fruits with prices to the system
2. **Update Fruit-Price Entity**: Modify existing fruit prices
3. **Delete Fruit-Price Entity**: Remove fruits from the system
4. **Calculate Fruit Cost**: Compute total cost based on quantity
5. **Generate Receipt**: Create detailed transaction receipts with cashier info

### Technical Features

- **Thread-Safe Operations**: Uses ConcurrentHashMap for data storage
- **Error Handling**: Comprehensive validation and error reporting
- **RMI Communication**: Robust remote method invocation
- **Serializable Objects**: All data models implement Serializable
- **Clean Architecture**: Separation of concerns with interfaces
- **Logging**: Console output for debugging and monitoring

## Testing

### Manual Testing

1. Start the server
2. Run the automated demo (option 6) to test all operations
3. Test individual operations through the menu
4. Verify error handling with invalid inputs

### Test Scenarios

- Add duplicate fruits (should fail)
- Update non-existent fruits (should fail)
- Delete non-existent fruits (should fail)
- Calculate cost with invalid quantity (should fail)
- Generate receipt with insufficient payment (should fail)

## Troubleshooting

### Common Issues

1. **Connection Refused**: Ensure RMI server is running on port 1099
2. **ClassNotFoundException**: Check classpath includes all necessary classes
3. **Port Already in Use**: RMI registry port 1099 might be occupied
4. **Permission Denied**: Check network permissions for RMI communication

### Solutions

```bash
# Check if port 1099 is in use
netstat -an | grep 1099

# Kill existing RMI registry
pkill -f rmiregistry

# Force clean build
mvn clean compile
```

## Development Notes

- RMI registry runs on default port 1099
- All remote objects extend UnicastRemoteObject
- Tasks implement Serializable for RMI transfer
- Database uses singleton pattern for thread safety
- Console client provides comprehensive testing interface

## Assignment Compliance

This implementation fulfills all assignment requirements:

✅ Built using Java RMI architecture  
✅ Implements all seven required classes  
✅ Provides five client task types  
✅ Uses localhost and port 1099  
✅ Includes servlet communication layer  
✅ Well-documented with comments  
✅ Separate client and server programs  
✅ Complete with development log

## Authors

Developed for MIT8102: Advanced Distributed Systems  
Strathmore University  
Assignment #1 - RMI Implementation
