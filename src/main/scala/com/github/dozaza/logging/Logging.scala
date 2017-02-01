/** Copyright © 2013-2016 DataYes, All Rights Reserved. */

package com.github.dozaza.logging

trait Logging {

  protected lazy val logger = new Logger(this.getClass)

}
