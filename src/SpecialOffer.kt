import java.time.LocalDate

data class SpecialOffer(
    val id: Int,
    val destination: Destination,
    val discountPercentage: Double, // e.g., 0.20 for 20% off
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    fun isActive(date: LocalDate = LocalDate.now()): Boolean {
        return date in startDate..endDate
    }

    fun getDiscountedPrice(originalPrice: Double): Double {
        return (originalPrice * (1 - discountPercentage)).roundMoney()
    }
}