package com.lds.desktop


import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.ldap.sdk.LDAPException

object LdapTest {

    @JvmStatic
    fun main(args: Array<String>) {
       try {
           val connection = LDAPConnection("office.lds.ua", 389)
           connection.bind("office" + "\\~userName~", "....")
           println("222222")
       }catch (e: Exception){
           e.printStackTrace()
       }
    }
}