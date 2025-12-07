import java.time.LocalDate

fun main() {
    // Hard-coded Admin user
    val admin = Admin(id = 1, email = "admin@example.com", fullName = "Admin User", password = "admin123")

    //Login System
    println("========== Admin Login ==========")
    print("Email: ")
    val email = readLine()?.trim().orEmpty()
    print("Password: ")
    val password = readLine()?.trim().orEmpty()

    // Check if credentials match
    if (admin.email != email || admin.password != password) {
        println("Invalid credentials. Access denied.")
        return
    }
    println("Login successful! Welcome, ${admin.fullName}.\n")

    // Hard-coded origin + destinations
    val origin = Destination(name = "Central", singlePrice = 0.0, returnPrice = 0.0) // origin's prices unused
    val machine = TicketMachine(
        origin = origin,
        destinations = mutableListOf(
            Destination("Northville", 3.50, 6.00),
            Destination("Eastford", 4.20, 7.60),
            Destination("Southgate", 5.00, 9.00)
        )
    )

    while (true) {
        println(
            """
            ========== ADMIN MENU ==========
            1) View all destinations
            2) Add a destination
            3) Modify a destination
            4) Change ALL ticket prices by factor
            5) View all special offers
            6) Add a special offer
            7) Search special offers
            8) Delete a special offer
            0) Exit
            --------------------------------
            Choose an option:
            """.trimIndent()
        )

        when (readLine()?.trim()) {
            "1" -> admin.viewDestinations(machine)

            "2" -> {
                print("Enter destination name: ")
                val name = readLine()?.trim().orEmpty()

                val single = readDouble("Enter SINGLE price (e.g. 3.50): ")
                val ret = readDouble("Enter RETURN price (e.g. 6.00): ")

                admin.addDestination(
                    machine,
                    Destination(name = name, singlePrice = single.roundMoney(), returnPrice = ret.roundMoney())
                )
            }

            "3" -> {
                admin.viewDestinations(machine)
                if (machine.destinations.isEmpty()) continue

                val idx = readInt("Enter destination number to modify (shown in the list): ") - 1

                println("Leave a field blank to keep the current value.")
                print("New name: ")
                val newName = readLine()?.takeIf { input -> input.isNullOrBlank().not() }?.trim()

                val newSingle = readOptionalDouble("New SINGLE price: ")
                val newReturn = readOptionalDouble("New RETURN price: ")

                admin.modifyDestination(machine, idx, newName, newSingle, newReturn)
            }

            "4" -> {
                val factor = readDouble(
                    "Enter factor (e.g. 1.10 = +10%, 0.90 = -10%): "
                )
                admin.changeAllTicketPrices(machine, factor)
            }

            "5" -> admin.viewSpecialOffers(machine)

            "6" -> {
                admin.viewDestinations(machine)
                if (machine.destinations.isEmpty()) {
                    println("No destinations available. Add a destination first.")
                    continue
                }

                print("Enter destination name: ")
                val destName = readLine()?.trim().orEmpty()

                val discount = readDouble("Enter discount (e.g., 0.20 for 20% off): ")

                val startDate = readDate("Enter start date (YYYY-MM-DD): ")
                val endDate = readDate("Enter end date (YYYY-MM-DD): ")

                if (startDate != null && endDate != null) {
                    admin.addSpecialOffer(machine, destName, discount, startDate, endDate)
                } else {
                    println("Invalid date format. Please use YYYY-MM-DD.")
                }
            }

            "7" -> {
                print("Enter destination name to search: ")
                val searchTerm = readLine()?.trim().orEmpty()
                admin.searchSpecialOffers(machine, searchTerm)
            }

            "8" -> {
                admin.viewSpecialOffers(machine)
                if (machine.specialOffers.isEmpty()) continue

                val offerId = readInt("Enter special offer ID to delete: ")
                admin.deleteSpecialOffer(machine, offerId)
            }

            "0" -> {
                println("Goodbye!")
                return
            }

            else -> println("Invalid option.")
        }
    }
}

//Reads and validates date input in YYYY-MM-DD format, returns null if invalid
fun readDate(prompt: String): LocalDate? {
    print(prompt)
    val input = readLine()?.trim()
    return try {
        LocalDate.parse(input)
    } catch (e: Exception) {
        null
    }
}

fun readInt(prompt: String): Int {
    while (true) {
        print(prompt)
        val input = readLine()
        val value = input?.toIntOrNull()
        if (value != null) return value
        println("Please enter a whole number.")
    }
}

fun readDouble(prompt: String): Double {
    while (true) {
        print(prompt)
        val input = readLine()
        val value = input?.toDoubleOrNull()
        if (value != null) return value
        println("Please enter a number, e.g. 3.50")
    }
}

// Returns null if user just presses Enter
fun readOptionalDouble(prompt: String): Double? {
    print(prompt)
    val input = readLine()
    if (input.isNullOrBlank()) return null
    return input.toDoubleOrNull().also {
        if (it == null) println("Invalid number, keeping old value.")
    }
}
