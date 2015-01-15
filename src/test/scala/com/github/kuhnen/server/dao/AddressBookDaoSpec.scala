package com.github.kuhnen.server.dao

import com.github.kuhnen.CommonTraits
import com.github.kuhnen.pojo.AddressBook
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.{Future => SFuture}
import scala.language.postfixOps

/**
 * Created by kuhnen on 1/13/15.
 */
class AddressBookDaoSpec extends CommonTraits {

  import com.github.kuhnen.FutureHelper._
  import com.github.kuhnen.util.AddressBookTest._

  val dao = new AddressBookDaoImpl

  before {
    Await.ready(dao.eraseAll, 1 second)
  }


  it should "insert a addressBook and retrieve" in {
    val resFut: SFuture[dao.Id] = dao.insert(bookUser)
    whenReady(resFut) { res =>
      res shouldEqual dao.newId(bookUser)
    }
    val selectedFut: SFuture[Option[AddressBook]] = dao.select(dao.newId(bookUser))
    whenReady(selectedFut) { selected =>
      selected shouldBe Some(bookUser.copy(id = dao.newId(bookUser)))

    }
  }

  it should "update a addressBook and retrieve" in {
    val resFut: SFuture[dao.Id] = dao.insert(bookUser)
    var id: dao.Id = ""
    whenReady(resFut) { res =>
      res shouldEqual dao.newId(bookUser)
      id = res
    }
    val selectedFut: SFuture[Option[AddressBook]] = dao.select(dao.newId(bookUser))
    whenReady(selectedFut) { selected =>
      selected shouldBe Some(bookUser.copy(id = id))

    }

    val userModified = bookUser.contact.copy(name = "newName")
    val addresBookModified= bookUser.copy(id = id, contact = userModified)
    val resUpdateFut: SFuture[Unit] = dao.update(id, addresBookModified)
    whenReady(resUpdateFut) { res =>
      res shouldBe (())
    }

    val selectedModifiedFut: SFuture[Option[AddressBook]] = dao.select(id)
    whenReady(selectedModifiedFut) { selected =>
      selected shouldBe Some(addresBookModified)

    }


  }

  it should "return empty List" in {
    val resFut: SFuture[Iterable[AddressBook]] = dao.selectAll()
    whenReady(resFut) { res =>
      res shouldBe empty
    }
  }

  it should "return None " in {
    val selectedFut: SFuture[Option[AddressBook]] = dao.select("id")
    whenReady(selectedFut) { selected =>
      selected shouldBe empty
    }
  }

  it should "Delete" in {
    val resFut: SFuture[dao.Id] = dao.insert(bookUser)
    val id = dao.newId(bookUser)
    whenReady(resFut) { res =>
      res shouldEqual dao.newId(bookUser)
    }
    val selectedFut: SFuture[Option[AddressBook]] = dao.select(dao.newId(bookUser))
    whenReady(selectedFut) { selected =>
      selected shouldBe Some(bookUser.copy(id = id))

    }
    val deleteddFut: SFuture[Unit] = dao.delete(dao.newId(bookUser))
    whenReady(deleteddFut) { deleted =>
      deleted  shouldBe (())
    }
    val selectedFutNone: SFuture[Option[AddressBook]] = dao.select(dao.newId(bookUser))
    whenReady(selectedFutNone) { selected =>
      selected shouldBe empty

    }

  }

}