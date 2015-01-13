package com.github.kuhnen.server.controller

import com.github.kuhnen.server.business.{AddressBookBusinessImp, AddressBookBusiness}
import com.twitter.finatra._

/**
 * Created by kuhnen on 1/12/15.
 */

class AddressBookControllerImpl extends AddressBookController {
  override val addressBookBusiness = new AddressBookBusinessImp
}

trait AddressBookController extends Controller {

  val addressBookBusiness: AddressBookBusiness

  get("/addressBooks") { request: Request =>
    addressBookBusiness.list().map(render.json(_))
  }

 //WE know that there is always some id, because of the above link
 get("/addressBooks/:id") { request: Request =>
    val id: String = request.routeParams.get("id").get
     addressBookBusiness.get(id).map {
       case Some(user) => render.json(user)
       case None => render.notFound
     }

  }

}
