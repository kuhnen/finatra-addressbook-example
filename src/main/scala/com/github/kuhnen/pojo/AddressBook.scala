package com.github.kuhnen.pojo

/**
 * Created by kuhnen on 1/12/15.
 */
case class AddressBook(id: String, contact: User)

case class Address(country: String, street: String, number: Int, zip: Int)

case class User(name: String,
                address: Address,
                email: Option[String],
                age: Int) {

  //TODO better method to validate Email if passed
  private def validEmail = email.map(_.contains("@")).getOrElse(true)
  require(validEmail, s"Email provided but not valid ${email.get}")

}
