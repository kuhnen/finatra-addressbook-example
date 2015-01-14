package com.github.kuhnen.server.dao

import java.util.UUID

import com.github.kuhnen.pojo.AddressBook
import com.twitter.util.Future

import scala.reflect.ClassTag

/**
 * Created by kuhnen on 1/12/15.
 */

class AddressBookDaoImpl extends AddressBookDao {

  override protected var data: Map[String, AddressBook] = AddressBookInMemoryDb.data


}


trait AddressBookDao extends DB[String, AddressBook] {
  implicit def dbId(addressBook: AddressBook) = UUID.nameUUIDFromBytes(addressBook.toString.getBytes()).toString
}


trait DB[K, V] {

  type Id = K
  implicit def dbId(v: V): K

  protected var data: Map[K, V]

  def select(id: K): Future[Option[V]] = Future { data.get(id) }

  def insert(value: V)(implicit tag: ClassTag[K]): Future[K] = Future {
    val key: K = value
    data = data.updated(key, value)
    key
  }

  def update(id: K, value: V): Future[Unit] =  Future { data = data.updated(id, value) }

  def selectAll(): Future[Iterable[V]] =  Future { data.values }

  def delete(id: K): Future[Unit] = Future { data = data - id }

  def eraseAll: Future[Unit] = Future { data = Map.empty}

}

object AddressBookInMemoryDb {

  var data: Map[String, AddressBook] = Map.empty

}



