package com.github.kuhnen.pojo

import com.github.kuhnen.CommonTraits

/**
 * Created by kuhnen on 1/13/15.
 */
class UserSpec extends CommonTraits {

  import com.github.kuhnen.util.AddressBookTest._

  it should "fail is email is not valid" in {
    intercept[IllegalArgumentException] {
      val userInvalidWithEmail = user.copy(email = Option("notgoodEmail"))
    }
  }

  it should "pass if email is" in {
      val userInvalidWithEmail = user.copy(email = Option("goodEmail@goodemail"))
    }



}
