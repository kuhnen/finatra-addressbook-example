package com.github.kuhnen.server

import com.github.kuhnen.server.controller.{AddressBookControllerImpl, AddressBookController}
import com.twitter.finatra.FinatraServer

/**
 * Created by kuhnen on 1/12/15.
 */
object Server extends FinatraServer {


  val addressBookController = new AddressBookControllerImpl()
  register(addressBookController)

}
