package com.lds.desktop


import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.ldap.sdk.LDAPException

object LdapTest {

    @JvmStatic
    fun main(args: Array<String>) {
       try {
           val connection = LDAPConnection("office.lds.ua", 389)
           connection.bind("office" + "\\reznichenko.i", "123@Aabbqq12")
           println("222222")
       }catch (e: Exception){
           e.printStackTrace()
       }
    }
}