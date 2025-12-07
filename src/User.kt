open class User(
    val id: Int,
    val email: String,
    val fullName: String,
    var balance: Double = 0.0
) {
    val tickets: MutableList<Ticket> = mutableListOf()
}

//class admin that is a type of user - which means it will inherit from user
class Admin(
    id: Int,
    email: String,
    fullName: String
) : User(id, email, fullName) {
    fun viewDestinations(machine: TicketMachine) {
        println("\n--- All Destinations ---")
        if (machine.destinations.isEmpty()) {
            println("No destinations found.")
            return
        }

        machine.destinations.forEachIndexed { index, d ->
            println(
                "${index + 1}. ${d.name} | Single: £${"%.2f".format(d.singlePrice)} | " +
                        "Return: £${"%.2f".format(d.returnPrice)} | " +
                        "Sales: ${d.sales} | Takings: £${"%.2f".format(d.takings)}"
            )
        }
    }

    fun addDestination(machine: TicketMachine, destination: Destination) {
        machine.destinations.add(destination)
        println("Added destination: ${destination.name}")
    }

    fun modifyDestination(
        machine: TicketMachine,
        index: Int,
        newName: String?,
        newSingle: Double?,
        newReturn: Double?
    ) {
        if (index !in machine.destinations.indices) {
            println("Invalid destination index.")
            return
        }

        val d = machine.destinations[index]

        // ---- Here we use normal if statements ----
        if (newName != null) {
            d.name = newName
        }

        if (newSingle != null) {
            d.singlePrice = newSingle.roundMoney()
        }

        if (newReturn != null) {
            d.returnPrice = newReturn.roundMoney()
        }

        println("Destination updated: $d")
    }

    fun changeAllTicketPrices(machine: TicketMachine, factor: Double) {
        for (destination in machine.destinations) {
            destination.applyPriceFactor(factor)
        }
        println("All ticket prices updated by factor $factor.")
    }

    fun addSpecialOffer(machine: TicketMachine, destinationName: String, discount: Double, startDate: LocalDate, endDate: LocalDate) {
        val destination = machine.destinations.find { it.name.equals(destinationName, ignoreCase = true) }

        if (destination == null) {
            println("Destination '$destinationName' not found.")
            return
        }

        if (discount <= 0 || discount >= 1) {
            println("Discount must be between 0 and 1 (e.g., 0.20 for 20%).")
            return
        }

        if (endDate.isBefore(startDate)) {
            println("End date must be after start date.")
            return
        }

        val offer = SpecialOffer(
            id = machine.getNextOfferId(),
            destination = destination,
            discountPercentage = discount,
            startDate = startDate,
            endDate = endDate
        )

        machine.addSpecialOffer(offer)
        println("Special offer added: ${(discount * 100).toInt()}% off for ${destination.name} from $startDate to $endDate")
    }

    fun viewSpecialOffers(machine: TicketMachine) {
        println("\n--- All Special Offers ---")
        if (machine.specialOffers.isEmpty()) {
            println("No special offers found.")
            return
        }

        machine.specialOffers.forEach { offer ->
            val status = if (offer.isActive()) "ACTIVE" else "INACTIVE"
            println(
                "ID: ${offer.id} | ${offer.destination.name} | " +
                        "${(offer.discountPercentage * 100).toInt()}% off | " +
                        "${offer.startDate} to ${offer.endDate} | $status"
            )
        }
    }

    fun searchSpecialOffers(machine: TicketMachine, destinationName: String) {
        println("\n--- Search Results for '$destinationName' ---")
        val results = machine.searchSpecialOffers(destinationName)

        if (results.isEmpty()) {
            println("No special offers found for '$destinationName'.")
            return
        }

        results.forEach { offer ->
            val status = if (offer.isActive()) "ACTIVE" else "INACTIVE"
            println(
                "ID: ${offer.id} | ${offer.destination.name} | " +
                        "${(offer.discountPercentage * 100).toInt()}% off | " +
                        "${offer.startDate} to ${offer.endDate} | $status"
            )
        }
    }

    fun deleteSpecialOffer(machine: TicketMachine, offerId: Int) {
        val removed = machine.removeSpecialOffer(offerId)
        if (removed) {
            println("Special offer ID $offerId deleted successfully.")
        } else {
            println("Special offer ID $offerId not found.")
        }
    }
}
