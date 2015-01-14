package com.github.kuhnen.server.controller


import com.github.kuhnen.CommonTraits
import com.github.kuhnen.pojo.AddressBook
import com.github.kuhnen.server.business.AddressBookBusiness
import com.github.kuhnen.util.AddressBookTest._
import com.twitter.finatra.FinatraServer
import com.twitter.finatra.test.SpecHelper
import com.twitter.util.Future
import org.json4s.native.Serialization._

/**
 * Created by kuhnen on 1/12/15.
 */

trait ApiSpec extends SpecHelper with CommonTraits


class AddressBookApiSpec extends ApiSpec with AddressBookFormats {


  import com.github.kuhnen.server.controller.StatusCodes._

  override val server: FinatraServer = new FinatraServer

  val addressBookController = new AddressBookController {

    override val addressBookBusiness: AddressBookBusiness = mock[AddressBookBusiness]

  }

  val path = addressBookController.rootPath
  val businessMocked = addressBookController.addressBookBusiness
  server.register(addressBookController)

  s"GET $path" should "respond with a empty list" in {
    (businessMocked.list _).expects().returning(Future(Iterable.empty[AddressBook]))
    get(path)
    response.body shouldEqual ("[]")
    response.code shouldEqual (OK)
  }

  s"GET $path/nonexistent" should "respond with 404" in {
    (businessMocked.get _).expects("nonexistent").returning(Future(None))
    get("/addressBooks/nonexistent")
    response.code shouldEqual (NotFound)
  }

  s"GET $path/existent" should "respond with 200 and user" in {
    (businessMocked.get _).expects("nonexistent").returning(Future(Option(bookUser)))
    get(s"$path/nonexistent")
    response.code shouldEqual (OK)
    read[AddressBook](response.body) shouldEqual bookUser
  }

  s"GET $path" should "respond with 500 if something bad happens with the server" in {
    (businessMocked.get _).expects("bumm!").returning(Future(throw new RuntimeException("We are sorry but the server might have exploded")))
    get(s"$path/bumm!")
    response.code shouldEqual (InternalServerError)
  }

  import com.github.kuhnen.server.controller.AddressBookController._

  s"POST $path" should "should respond with 201" in {
    (businessMocked.insert _).expects(user).returning(Future("id"))
    post(path, body = user)
    response.code shouldEqual Created
    response.body shouldEqual write(Id("id"))
  }

  s"PUT $path/someId" should "should respond with 200" in {
    val emptyContent = ""
    (businessMocked.update _).expects("someId", user).returning(Future(()))
    put(s"$path/someId", body = user)
    response.code shouldEqual OK
    response.body shouldEqual emptyContent
  }

  s"DELETE $path/someId" should "should respond with 200" in {
    val emptyContent = ""
    (businessMocked.delete _).expects("someId").returning(Future(()))
    delete(s"$path/someId")
    response.code shouldEqual OK
    response.body shouldEqual emptyContent
  }
}
