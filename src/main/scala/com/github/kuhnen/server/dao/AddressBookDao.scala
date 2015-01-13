package com.github.kuhnen.server.dao

import com.github.kuhnen.pojo.AddressBook
import com.twitter.util.Future

/**
 * Created by kuhnen on 1/12/15.
 */

class AddressBookDaoImpl extends AddressBookDao {

  override protected var data: Map[String, AddressBook] = AddressBookInMemoryDb.data
}


trait AddressBookDao extends DB[String, AddressBook]


trait DB[K, V] {

  protected var data: Map[K, V]

  def select(id: K): Future[Option[V]] = Future { data.get(id) }

  def insert(id: K, value: V): Future[Unit] =  Future { data = data.updated(id, value) }

  def list(): Future[Iterable[V]] =  Future { data.values }

  def delete(id: K): Future[Unit] = Future { data = data - id }

}

object AddressBookInMemoryDb {

  var data: Map[String, AddressBook] = Map.empty

}


