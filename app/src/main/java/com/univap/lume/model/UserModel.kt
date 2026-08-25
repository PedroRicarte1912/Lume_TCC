package com.univap.lume.model
import com.google.firebase.database.FirebaseDatabase

data class UserModel(
    var id: String = "",
    var nome: String = "",
    var email: String = ""
) {
    fun salvar() {
        val ref = FirebaseDatabase.getInstance().reference
        ref.child("usuarios")
            .child(id)
            .setValue(this)
    }
}