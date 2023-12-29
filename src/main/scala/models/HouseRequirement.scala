package housepredicter.models

object HouseRequirement {
  val validValues: Map[String, Set[String]] = Map(
    "area_type" -> Set("Carpet Area", "Super Area"),
    "area_locality" -> Set(
      "Bangalore", // Example: Located in Bangalore (1 for true, 0 for false)
      "Chennai",
      "Delhi",
      "Hyderabad",
      "Kolkata",
      "Mumbai"
    ),
    "furnishing_status" -> Set(
      "Furnished", // Example: Not furnished
      "Semi-Furnished", // Example: Semi-furnished
      "Unfurnished"
    ),
    "tenant_preferred" -> Set(
      "Bachelors", // Example: Not for bachelors
      "Bachelors/Family", // Example: Suitable for bachelors or family
      "Family"
    ),
    "point_of_contract" -> Set(
      "Contact Agent",
      "Contact Owner"
    )
  )

}
case class HouseRequirement(
    bedroom: Int,
    size: Double,
    bathroom: Int,
    floor_level: Int,
    total_floor: Int,
    area_type: String,
    area_locality: String,
    furnishing_status: String,
    tenant_preferred: String,
    point_of_contract: String
) {
  private def oneEncode(
      name: String,
      receivedValue: String
  ): Option[Map[String, Double]] = {
    for {
      valueSet <- HouseRequirement.validValues.get(name)
      content <- valueSet.find(_ == receivedValue)
    } yield valueSet.map(v => (v -> 0.0)).toMap + (content -> 1.0)
  }

  def toInputMap(): Option[Map[String, Double]] = {
    val input = Map[String, Double](
      "bhk" -> this.bedroom,
      "size" -> this.size,
      "bathroom" -> this.bathroom, // Example: 2 bathrooms
      "floor_level" -> this.floor_level, // Example: 3rd floor
      "total_floor" -> this.total_floor // Example: Building has 10 floors
    )
    for {
      area_type <- oneEncode("area_type", this.area_type)
      area_locality <- oneEncode("area_locality", this.area_locality)
      furnishing_status <- oneEncode(
        "furnishing_status",
        this.furnishing_status
      )
      tenant_preferred <- oneEncode("tenant_preferred", this.tenant_preferred)
      point_of_contract <- oneEncode(
        "point_of_contract",
        this.point_of_contract
      )
    } yield input ++ area_type ++ area_locality ++ furnishing_status ++ tenant_preferred ++ point_of_contract
  }

}
