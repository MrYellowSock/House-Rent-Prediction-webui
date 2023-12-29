package housepredicter
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import spray.json._
import housepredicter.models._

implicit val houseReqFormat: RootJsonFormat[HouseRequirement] = jsonFormat10(
  HouseRequirement.apply
)

implicit val housePredFormat: RootJsonFormat[HousePredictionResult] =
  jsonFormat1(HousePredictionResult.apply)

implicit val setMapFormat: RootJsonFormat[Map[String, Set[String]]] =
  mapFormat[String, Set[String]]
