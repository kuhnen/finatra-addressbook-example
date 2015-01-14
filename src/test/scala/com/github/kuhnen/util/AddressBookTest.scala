package com.github.kuhnen.util

import com.github.kuhnen.pojo.{AddressBook, User, Address}
import org.json4s.DefaultFormats

/**
 * Created by kuhnen on 1/13/15.
 */

object AddressBookTest {

  val address = Address("country", "street", 89, 89)
  val user = User("name", address, None, 78)
  val userWithEmail = user.copy(email = Option("Email@email.email"))
  val bookUser = AddressBook("id", user)

}