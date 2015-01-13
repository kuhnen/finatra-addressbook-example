package com.github.kuhnen.server.controller


import com.github.kuhnen.pojo.{Address, AddressBook, User}
import com.github.kuhnen.server.business.AddressBookBusiness
import com.twitter.finatra.FinatraServer
import com.twitter.finatra.test.SpecHelper
import com.twitter.util.Future
import org.json4s.DefaultFormats
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpecLike, ShouldMatchers}
import org.json4s.native.Serialization._

/**
 * Created by kuhnen on 1/12/15.
 */

trait ApiSpec extends SpecHelper with FlatSpecLike with ShouldMatchers with MockFactory

object AddressBookApiSpec {

  implicit val formats = DefaultFormats
  val address = Address("country", "street", 89, 89)
  val user = User("name", address, None, 78)
  val userWithEmail = user.copy(email = Option("Email@email.email"))
  val bookUser = AddressBook("id", user)


}

class AddressBookApiSpec extends ApiSpec {

  import AddressBookApiSpec._

  override val server: FinatraServer = new FinatraServer

  val addressBookController = new AddressBookController {

    override val addressBookBusiness: AddressBookBusiness = mock[AddressBookBusiness]

  }

  val businessMocked = addressBookController.addressBookBusiness
  server.register(addressBookController)

  "GET /addressBooks" should "respond with a empty list" in {
    (businessMocked.list _).expects().returning(Future(Iterable.empty[AddressBook]))
    get("/addressBooks")
    response.body should equal("[]")
    response.code should equal(200)
  }

  "GET /addressBooks/nonexistent" should "respond with 404" in {
    (businessMocked.get _).expects("nonexistent").returning(Future(None))
    get("/addressBooks/nonexistent")
    response.code should equal(404)
  }

  "GET /addressBooks/existent" should "respond with 200 and user" in {
    (businessMocked.get _).expects("nonexistent").returning(Future(Option(bookUser)))
    get("/addressBooks/nonexistent")
    response.code should equal(200)
    read[AddressBook](response.body) shouldEqual bookUser
  }

  "GET /addressBooks" should "respond with 500 if something bad happens with the server" in {
    (businessMocked.get _).expects("bumm!").returning(Future(throw new RuntimeException("We are sorry but the server might have exploded")))
    get("/addressBooks/bumm!")
    response.code should equal(500)
  }
}
