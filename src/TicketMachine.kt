class TicketMachine(
    val origin: Destination,
    val destinations: MutableList<Destination>
) {

    val specialOffers: MutableList<SpecialOffer> = mutableListOf()
    private var nextOfferId = 1

    fun listDestinations(): List<Destination> = destinations.toList()

    fun addSpecialOffer(offer: SpecialOffer) {
        specialOffers.add(offer)
    }

    fun removeSpecialOffer(offerId: Int): Boolean {
        return specialOffers.removeIf { it.id == offerId }
    }

    fun searchSpecialOffers(destinationName: String): List<SpecialOffer> {
        return specialOffers.filter {
            it.destination.name.contains(destinationName, ignoreCase = true)
        }
    }

    fun getNextOfferId(): Int = nextOfferId++
}
