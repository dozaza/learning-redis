
package com.github.dozaza.test

trait TestModule {

  def assertEqual[T](o1: T, o2: T): Unit = {
    if (o1 != o2) {
      throw new RuntimeException(s"Object: $o1 does not equal to object: $o2" )
    }
  }

}
