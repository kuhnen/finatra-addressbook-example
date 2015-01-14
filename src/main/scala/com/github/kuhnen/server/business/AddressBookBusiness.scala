package com.github.kuhnen.server.business

import com.github.kuhnen.pojo.{User, AddressBook}
import com.github.kuhnen.server.dao.{AddressBookDaoImpl, AddressBookDao}
import com.twitter.util.Future

/**
 * Created by kuhnen on 1/12/15.
 */

class AddressBookBusinessImp extends AddressBookBusiness {

  override val dao = new AddressBookDaoImpl

}

trait AddressBookBusiness {

  implicit def fakeId(user: User) = AddressBook("fakeId", user)

  val dao: AddressBookDao

  def get(id: String): Future[Option[AddressBook]] = dao.select(id)

  def list(): Future[Iterable[AddressBook]] = dao.selectAll()

  //I am creating a fake id, just to be able to pass the right type to the database
  //but the DB will be responsible about the key creation. Not the best idea
  def insert(user: User): Future[dao.Id] = dao.insert(user)

  def update(id: String, user: User) = dao.update(AddressBook(id, user))

  def delete(id: String): Future[Unit] = dao.delete(id)



}
