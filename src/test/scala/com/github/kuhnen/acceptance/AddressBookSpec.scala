package com.github.kuhnen.acceptance

import com.github.kuhnen.pojo.AddressBook
import com.github.kuhnen.server.business.AddressBookBusiness
import com.github.kuhnen.server.controller.AddressBookController.Id
import com.github.kuhnen.server.controller.{AddressBookControllerImpl, ApiSpec, AddressBookController, AddressBookFormats}
import com.github.kuhnen.util.AddressBookTest._
import com.twitter.finatra.FinatraServer
import com.twitter.util.Future
import org.json4s.native.Serialization._
import org.json4s.native.JsonMethods._
/**
 * Created by kuhnen on 1/14/15.
 */
class AddressBookSpec extends ApiSpec with AddressBookFormats {


    import com.github.kuhnen.server.controller.StatusCodes._

    override val server: FinatraServer = new FinatraServer

    val addressBookController = new AddressBookControllerImpl

    val path = addressBookController.rootPath
    server.register(addressBookController)

    s"GET $path" should "respond with a empty list" in {
      get(path)
      response.body shouldEqual ("[]")
      response.code shouldEqual (OK)
    }

    s"GET $path/nonexistent" should "respond with 404" in {
      get("/addressBooks/nonexistent")
      response.code shouldEqual (NotFound)
    }

    s"GET $path/existent" should "respond with 200 and user" in {
      post(s"$path", body = user)
      response.code shouldBe Created
      val id = (parse(response.body) \ "id").extract[String]
      get(s"$path/$id")
      response.code shouldEqual (OK)
      read[AddressBook](response.body) shouldEqual bookUser.copy(id = id)
    }


    s"POST $path" should "should respond with 201" in {
      post(path, body = user)
      response.code shouldEqual Created

    }

    s"PUT $path/someId" should "should respond with 200" in {
      val emptyContent = ""
      put(s"$path/${bookUser.id}", body = user)
      response.code shouldEqual OK
      response.body shouldEqual emptyContent
    }

    s"DELETE $path/someId" should "should respond with 200" in {
      val emptyContent = ""
      delete(s"$path/someId")
      response.code shouldEqual OK
      response.body shouldEqual emptyContent
    }



}
