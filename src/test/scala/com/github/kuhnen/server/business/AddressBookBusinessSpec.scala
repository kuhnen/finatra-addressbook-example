package com.github.kuhnen.server.business

import com.github.kuhnen.{FutureHelper, CommonTraits}
import com.github.kuhnen.pojo.AddressBook
import com.github.kuhnen.server.dao.AddressBookDao
import com.github.kuhnen.util.AddressBookTest._
import com.twitter.util.{ Future => TFuture }
import scala.concurrent.{Future => SFuture }
import scala.reflect.ClassTag

/**
 * Created by kuhnen on 1/13/15.
 */
class AddressBookBusinessSpec extends CommonTraits {

  import FutureHelper._
  val business = new AddressBookBusiness {
    override val dao: AddressBookDao = mock[AddressBookDao]
  }

  val daoMocked = business.dao

  it should "call dao to insert" in {
    val fakeAddresBook = business.fakeId(user)
    (daoMocked.insert(_:AddressBook)(_:ClassTag[String])).expects(fakeAddresBook, *).returning(TFuture("id"))
    val responseFut: SFuture[daoMocked.Id] = business.insert(user)
    whenReady(responseFut) { response =>
      response shouldBe "id"
    }
  }

  it should "call dao to update" in {
    (daoMocked.update _).expects(bookUser.id, bookUser).returning(TFuture(()))
    val responseFut: SFuture[Unit] = business.update(bookUser.id, user)
    whenReady(responseFut) { response =>
      response shouldBe (())
    }
  }

  it should "call dao to get all" in {
    val expectedResponse = bookUser :: Nil
    (daoMocked.selectAll _).expects().returning(TFuture(expectedResponse))
    val responseFut: SFuture[Iterable[AddressBook]] = business.list()
    whenReady(responseFut) { response =>
      response shouldBe expectedResponse
    }

  }

  it should "call dao to get some user" in {
    val expectedResponse = bookUser
    (daoMocked.select _).expects("id").returning(TFuture(Option(bookUser)))
    val responseFut: SFuture[Option[AddressBook]] = business.get("id")
    whenReady(responseFut) { response =>
      response shouldBe defined
    }

  }



  it should "call dao to delete" in {
    (daoMocked.delete _).expects("id").returning(TFuture(()))
    val responseFut: SFuture[Unit] = business.delete("id")
    whenReady(responseFut) { response =>
      response shouldBe (())
    }



  }

}
