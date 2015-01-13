package com.github.kuhnen.server.business

import com.github.kuhnen.pojo.AddressBook
import com.github.kuhnen.server.dao.{AddressBookDaoImpl, AddressBookDao}
import com.twitter.util.Future

/**
 * Created by kuhnen on 1/12/15.
 */

class AddressBookBusinessImp extends AddressBookBusiness {

  override val dao = new AddressBookDaoImpl

}

trait AddressBookBusiness {

  val dao: AddressBookDao

  def get(id: String): Future[Option[AddressBook]] = dao.select(id)

  def list(): Future[Iterable[AddressBook]] = dao.list()



}
