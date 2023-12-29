package housepredicter
import java.nio.file.{Files, Paths}
import org.pmml4s.model.Model
import scala.io.Source
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import akka.http.scaladsl.model.StatusCodes
import housepredicter.models._
import scala.io.StdIn

object HouseRentPrediction {

  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "SprayExample")
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext: ExecutionContext = system.executionContext

  val model = Model(
    Source.fromURL(
      "https://raw.githubusercontent.com/MrYellowSock/House-Rent-Prediction-in-India/main/linear_regression_model.pmml"
    )
  )

  val route: Route =
    concat(
      path("predict") {
        post {
          entity(as[HouseRequirement]) { houseReq =>
            houseReq
              .toInputMap()
              .map(input => model.predict(input))
              .flatMap(resultMap => resultMap.get("predicted_y"))
              .map(value => HousePredictionResult(value.asInstanceOf[Double]))
              .map(complete(_))
              .getOrElse(
                complete(StatusCodes.BadRequest, "Invalid data provided")
              )
          }
        }
      },
      path("validfields") {
        complete(HouseRequirement.validValues)
      },
      pathSingleSlash {
        getFromFile(
          "src/main/resources/static/index.html"
        )
      }
    )

  val house = HouseRequirement(
    bedroom = 2,
    size = 1200,
    bathroom = 2,
    floor_level = 1,
    total_floor = 3,
    area_type = "Carpet Area",
    area_locality = "Bangalore",
    furnishing_status = "Semi-Furnished",
    tenant_preferred = "Bachelors/Family",
    point_of_contract = "Contact Agent"
  )
  def main(args: Array[String]): Unit = {
    val result = model.predict(house.toInputMap().get)
    print(result)

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(
      s"Server now online. Please navigate to http://localhost:8080/hello\nPress RETURN to stop..."
    )
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}