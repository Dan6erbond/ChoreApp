package ch.bbbaden.choreapp.models

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class ChoreDAO {

    private val db = FirebaseFirestore.getInstance()

    fun getChores(child: Child, callback: ((ArrayList<Chore>?) -> Unit)? = null) {
        getChores(child.parentId!!) {
            if (it != null) {
                val chores = ArrayList<Chore>()
                for (chore in it) {
                    for (assignment in chore.assignments) {
                        val assignedTo = assignment.assignedTo == child.userId
                        val nextAssignmentExists = assignment.getNextDate() != null
                        if (assignedTo && nextAssignmentExists) {
                            chore.child = child
                            chores.add(chore)
                            break
                        }
                    }
                }
                callback?.invoke(chores)
            } else {
                callback?.invoke(null)
            }
        }
    }

    fun getChores(userId: String, callback: ((ArrayList<Chore>?) -> Unit)? = null) {
        db.collection("users").document(userId).collection("chores")
            .get()
            .addOnSuccessListener {
                val chores = ArrayList<Chore>()
                for (document in it) {
                    val chore = document.toObject(Chore::class.java)
                    chores.add(chore)
                }
                callback?.invoke(chores)
            }
            .addOnFailureListener {
                callback?.invoke(null)
                Log.e(this::class.simpleName, it.message ?: it.toString())
            }
    }

    fun saveChore(parent: Parent, chore: Chore, callback: ((success: Boolean) -> Unit)? = null) {
        db.collection("users").document(parent.userId!!).collection("chores").document(chore.id!!)
            .set(chore.getData())
            .addOnSuccessListener {
                callback?.invoke(true)
            }
            .addOnFailureListener {
                callback?.invoke(false)
                Log.e(this::class.simpleName, it.message ?: it.toString())
            }
    }

    fun addChore(parent: Parent, chore: Chore, callback: ((chore: Chore?) -> Unit)? = null) {
        db.collection("users").document(parent.userId!!).collection("chores")
            .add(chore.getData())
            .addOnSuccessListener {
                chore.id = it.id
                callback?.invoke(chore)
            }
            .addOnFailureListener {
                callback?.invoke(null)
                Log.e(this::class.simpleName, it.message ?: it.toString())
            }
    }

    fun deleteChore(parent: Parent, chore: Chore, callback: ((success: Boolean) -> Unit)? = null) {
        db.collection("users").document(parent.userId!!).collection("chores").document(chore.id!!)
            .delete()
            .addOnSuccessListener {
                callback?.invoke(true)
            }
            .addOnFailureListener {
                callback?.invoke(false)
                Log.e(this::class.simpleName, it.message ?: it.toString())
            }
    }

}