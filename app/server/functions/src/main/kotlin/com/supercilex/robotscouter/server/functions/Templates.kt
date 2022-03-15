package com.supercilex.robotscouter.server.functions

import com.supercilex.robotscouter.common.FIRESTORE_METRICS
import com.supercilex.robotscouter.common.FIRESTORE_NAME
import com.supercilex.robotscouter.common.FIRESTORE_TEMPLATE_ID
import com.supercilex.robotscouter.common.FIRESTORE_TIMESTAMP
import com.supercilex.robotscouter.server.utils.checkbox
import com.supercilex.robotscouter.server.utils.counter
import com.supercilex.robotscouter.server.utils.defaultTemplates
import com.supercilex.robotscouter.server.utils.header
import com.supercilex.robotscouter.server.utils.metrics
import com.supercilex.robotscouter.server.utils.selector
import com.supercilex.robotscouter.server.utils.stopwatch
import com.supercilex.robotscouter.server.utils.text
import com.supercilex.robotscouter.server.utils.types.CollectionReference
import com.supercilex.robotscouter.server.utils.types.FieldValues
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asPromise
import kotlinx.coroutines.async
import kotlinx.coroutines.await
import kotlinx.coroutines.awaitAll
import kotlin.js.Json
import kotlin.js.Promise
import kotlin.js.json

/**
 * This function and [pitTemplateMetrics] update the default templates server-side without requiring
 * a full app update. To improve the templates or update them for a new game, follow these
 * instructions.
 *
 * ## Creating a new template for a new game
 *
 * 1. Delete the existing metrics
 * 1. Make sure to update _both_ the match and pit scouting templates (the pit template likely won't
 *    require much work)
 * 1. Increment the letter for each new metric added (e.g. 'a' -> 'b' -> 'c')
 *      - Should the template exceed 26 items, start with `aa` -> 'ab' -> 'ac"
 * 1. Always start with a header and space the metrics in code by grouping
 * 1. In terms of punctuation, use Title Case for headers and Sentence case for all other metrics
 * 1. **For examples of correct metric DSL usage**, take a look at the current template or the Git
 *    log for past templates
 *
 * ## Improving the current year's template
 *
 * 1. When moving metrics around, keep their letter IDs the same
 * 1. When inserting a metric, don't change the IDs of surrounding metrics; just pick an unused ID
 * 1. Minimize deletions at all costs since that makes data analysis harder
 */
fun matchTemplateMetrics() = metrics {
    header("a", "SCOUT")
    text("b", "Name")
    text("c", "Match")
    
    header("d", "AUTONOMOUS")
    checkbox("e", "Successfully Taxied out of Tarmac")
    counter("f", "Upper Hub Attempts")
    counter("g", "Upper Hub Shots Made")
    counter("h", "Lower Hub Attempts")
    counter("i", "Lower Hub Shots Made")

    header("j", "TELEOP")
    counter("k", "Upper Hub Attempts")
    counter("l", "Upper Hub Shots Made")
    counter("m", "Lower Hub Attempts")
    counter("n", "Lower Hub Shots Made")

    stopwatch("o", "Cargo shots cycle time (Just Emptied-to-Empty")
    
    selector("p", "Did they play defense?"") {    
        +Item("a", "1-None demonstrated")
        +Item("b", "2-Tried, but incurred penalties")
        +Item("c", "3-Okay")
        +Item("d", "4-Better than Average")
        +Item("e", "5-Did Great!")
        +Item("f", "Unknown", true)
    text("q", "Defense Comments")       
    
    stopwatch("r", "Time to Hang (Start as enter Hangar)")
    selector("s", "Which rung did they end the game?"") {    
        +Item("a", "None")
        +Item("b", "Low")
        +Item("c", "Mid")
        +Item("d", "High")
        +Item("e", "Traversal")
        +Item("f", "Unknown", true)

    header("t", "POST-GAME")
    
    checkbox("u", "Incurred penalties")    
    checkbox("v", "Robot broke")
    checkbox("w", "They had a pattern to their play.")    
    text("x", "Additional Comments")
}

/** @see [matchTemplateMetrics] */
fun pitTemplateMetrics() = metrics {
    header("a", "SCOUT")
    text("b", "Name")

    header("c", "HARDWARE")
    selector("d", "What's their drivetrain?") {
        +Item("a", "Standard 6/8 wheel")
        +Item("b", "Swerve")
        +Item("c", "Omni/Mecanum")
        +Item("d", "Other")
        +Item("e", "Unknown", true)
    }
    selector("e", "How fast are they?") {
        +Item("a", "Slow")
        +Item("b", "Average")
        +Item("c", "Fast")
        +Item("d", "Other")
        +Item("e", "Unknown", true)
    }
    text("f", "If other, please specify")
    checkbox("g", "Do they have a Cargo ground intake?")
    checkbox("h", "Do they have a Cargo Terminal intake?")

    header("i", "AUTONOMOUS STRATEGY")
    selector("j", "What is their primary working autonomous?")
        +Item("a", "No movement")
        +Item("b", "Taxi only")
        +Item("c", "Shoot preloaded cargo only")
        +Item("d", "Combination of Shoot, Taxi + Intake (2 balls max)")
        +Item("e", "Combination of Shoot, Taxi + Intake (2+ balls)")       
        +Item("f", "Other")
        +Item("g", "Unknown", true)
    }
    text("k", "If other, please specify")

    header("l", "TELEOP STRATEGY")
    selector("m", "Where can they put Cargo during Teleop?") {
        +Item("a", "Lower Hub only")
        +Item("b", "Upper Hub only")
        +Item("c", "Anywhere")
        +Item("d", "Other")
        +Item("e", "Unknown", true)
    }
    text("n", "If other, please specify")

    selector("o", "Which is the highest rung their robot can end the game?") {
        +Item("a", "None")
        +Item("b", "Low")
        +Item("c", "Mid")
        +Item("d", "High")
        +Item("e", "Traversal")
        +Item("f", "Unknown", true)
    }

    header("p", "OTHER")
    counter("q", "Subjective quality assessment (?/5)") {
        unit = "⭐"
    }
    text("r", "What is something special you want us to know about your robot?")
    text("s", "Other comments")
}

fun updateDefaultTemplates(): Promise<*>? {
    return GlobalScope.async {
        val match = async { defaultTemplates.updateMatchTemplate() }
        val pit = async { defaultTemplates.updatePitTemplate() }
        val empty = async { defaultTemplates.updateEmptyTemplate() }

        awaitAll(match, pit, empty)
    }.asPromise()
}

private suspend fun CollectionReference.updateMatchTemplate() {
    doc("0").set(json(
            FIRESTORE_TEMPLATE_ID to "0",
            FIRESTORE_NAME to "Match Scout",
            FIRESTORE_TIMESTAMP to FieldValues.serverTimestamp(),
            FIRESTORE_METRICS to matchTemplateMetrics()
    ).log("Match")).await()
}

private suspend fun CollectionReference.updatePitTemplate() {
    doc("1").set(json(
            FIRESTORE_TEMPLATE_ID to "1",
            FIRESTORE_NAME to "Pit Scout",
            FIRESTORE_TIMESTAMP to FieldValues.serverTimestamp(),
            FIRESTORE_METRICS to pitTemplateMetrics()
    ).log("Pit")).await()
}

private suspend fun CollectionReference.updateEmptyTemplate() {
    doc("2").set(json(
            FIRESTORE_TEMPLATE_ID to "2",
            FIRESTORE_NAME to "Blank Scout",
            FIRESTORE_TIMESTAMP to FieldValues.serverTimestamp()
    ).log("Blank")).await()
}

private fun Json.log(name: String) = apply { console.log("$name: ${JSON.stringify(this)}") }
