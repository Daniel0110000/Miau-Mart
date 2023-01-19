package com.daniel.miaumart.domain.utilities

import org.mindrot.jbcrypt.BCrypt

class SecurityService {
    companion object{
        fun passwordHasher(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
        fun validatePassword(password: String, hashedPassword: String): Boolean = BCrypt.checkpw(password, hashedPassword)
    }
}