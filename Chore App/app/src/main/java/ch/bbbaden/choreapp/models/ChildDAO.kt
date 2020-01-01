package ch.bbbaden.choreapp.models

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class ChildDAO {

    private val db = FirebaseFirestore.getInstance()

    fun getChild(userId: String, callback: ((Child?) -> Unit)? = null) {
        db.collection("users")
            .whereArrayContains("children", userId)
            .get()
            .addOnSuccessListener {
                it.firstOrNull { parentDocument ->
                    getChild(parentDocument.id, userId, callback)
                    true
                }
            }
            .addOnFailureListener {
                callback?.invoke(null)
                Log.e(this::class.simpleName, it.message ?: it.toString())
            }
    }

    fun getChild(parentId: String, userId: String, callback: ((Child?) -> Unit)? = null) {
        db.collection("users").document(parentId).collection("children").document(userId)
            .get()
            .addOnSuccessListener { ds ->
                val child = ds.toObject(Child::class.java)
                child!!.parentId = parentId
                callback?.invoke(child)
            }
            .addOnFailureListener { e ->
                callback?.invoke(null)
                Log.e(this::class.simpleName, e.message ?: e.toString())
            }
    }

    fun getChildren(parentId: String, callback: ((ArrayList<Child>?) -> Unit)? = null) {
        db.collection("users").document(parentId).collection("children")
            .get()
            .addOnSuccessListener {
                val children = ArrayList<Child>()
                for (document in it.documents) {
                    val child = document.toObject(Child::class.java)
                    child!!.parentId = parentId
                    children.add(child)
                }
                callback?.invoke(children)
            }
            .addOnFailureListener {
                callback?.invoke(null)
                Log.e(this::class.simpleName, it.message ?: it.toString())
            }
    }

}