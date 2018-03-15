package com.supercilex.robotscouter.server.utils

import com.supercilex.robotscouter.server.utils.types.CollectionReference
import com.supercilex.robotscouter.server.utils.types.DocumentSnapshot
import com.supercilex.robotscouter.server.utils.types.Firestore
import com.supercilex.robotscouter.server.utils.types.Query
import com.supercilex.robotscouter.server.utils.types.WriteBatch
import kotlin.js.Json
import kotlin.js.Promise

fun <T> Json.toMap(): Map<String, T> {
    val map: MutableMap<String, T> = mutableMapOf()
    for (key: String in js("Object").keys(this)) {
        @Suppress("UNCHECKED_CAST") // Trust the client
        map[key] = this[key] as T
    }
    return map
}

fun DocumentSnapshot.toTeamString() =
        "${data()[FIRESTORE_NUMBER]} - ${data()[FIRESTORE_NAME]}: $id"

fun DocumentSnapshot.toTemplateString() = "${data()[FIRESTORE_NAME]}: $id"

fun Firestore.batch(transaction: WriteBatch.() -> Unit) = batch().run {
    transaction()
    commit()
}

fun CollectionReference.delete(
        batchSize: Int = 100,
        middleMan: (DocumentSnapshot) -> Promise<*>? = { null }
) = deleteQueryBatch(
        firestore,
        orderBy(FieldPath.documentId()).limit(batchSize),
        batchSize,
        middleMan
)

private fun deleteQueryBatch(
        db: Firestore,
        query: Query,
        batchSize: Int,
        middleMan: (DocumentSnapshot) -> Promise<*>?
): Promise<Unit> = query.get().then { snapshot ->
    if (snapshot.size == 0) {
        return@then Promise.resolve(0)
    }

    Promise.all(snapshot.docs.map(middleMan).map {
        it ?: Promise.resolve(Unit)
    }.toTypedArray()).then {
        db.batch {
            snapshot.docs.forEach { delete(it.ref) }
        }
    }.then {
        it.size
    }
}.then { numDeleted ->
    if (numDeleted == 0) {
        return@then Promise.resolve(Unit)
    }

    deleteQueryBatch(db, query, batchSize, middleMan)
}.then { Unit }

class FieldValue {
    //language=JavaScript
    companion object {
        fun serverTimestamp(): dynamic = js("require(\"firebase-admin\").firestore.FieldValue.serverTimestamp()")
        fun delete(): dynamic = js("require(\"firebase-admin\").firestore.FieldValue.delete()")
    }
}

class FieldPath {
    //language=JavaScript
    companion object {
        fun documentId(): dynamic = js("require(\"firebase-admin\").firestore.FieldPath.documentId()")
    }
}
