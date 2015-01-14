package com.github.kuhnen.server.controller

import com.github.kuhnen.pojo.User
import com.github.kuhnen.server.business.{AddressBookBusinessImp, AddressBookBusiness}
import com.twitter.finatra._
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.read


/**
 * Created by kuhnen on 1/12/15.
 */

object StatusCodes {

  val OK = 200
  val Created = 201
  val BadRequest = 400
  val NotFound = 404
  val InternalServerError = 500
}

trait AddressBookFormats {

  implicit val formats = DefaultFormats

}

class AddressBookControllerImpl extends AddressBookController {
  override val addressBookBusiness = new AddressBookBusinessImp
}

object AddressBookController {
  case class Id(id: String)
}

trait AddressBookController extends Controller with AddressBookFormats {

  import AddressBookController._
  import StatusCodes._

  val addressBookBusiness: AddressBookBusiness

  implicit def toEntity(entity: String) = read[User](entity)

  val rootPath = "/addressBooks"

  get(rootPath) { request: Request =>
    addressBookBusiness.list().map(render.json(_))
  }

 //WE know that there is always some id, because of the above link
 get(s"$rootPath/:id") { request: Request =>
    val id: String = request.routeParams.get("id").get
     addressBookBusiness.get(id).map {
       case Some(user) => render.json(user)
       case None => render.notFound
     }
  }

  post("/addressBooks") { request: Request =>
    val entity = request.contentString
    addressBookBusiness.insert(entity).map(id => render.status(Created).json(Id(id)))
  }

  put(s"$rootPath/:id") { request: Request =>
    val id: String = request.routeParams.get("id").get
    val entity = request.contentString
    addressBookBusiness.update(id, entity).map(id => render.status(OK))
  }

  delete(s"$rootPath/:id") { request: Request =>
    val id: String = request.routeParams.get("id").get
    addressBookBusiness.delete(id).map(id => render.status(OK))

  }

}
