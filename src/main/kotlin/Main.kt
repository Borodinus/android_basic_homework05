package org.example

fun main(){

    //region Singleton
    val coffeeMachine = CoffeeMachine
    //endregion

    //region Command
    val controlPanel = ControlPanel()
    controlPanel.setCommand(CoffeeMachineCommand(coffeeMachine))
    controlPanel.buttonOn()
    //endregion

    //region builder
    val americano = CoffeeBuilder()
        .setSize(CoffeeSize.MEDIUM)
        .setType(CoffeeType.AMERICANO)
        .build()
    //endregion

    //region Decorator
    println("Кофе: ${americano.description()}, Цена: ${americano.cost()}")
    val americanoWithSugar: Coffee = Sugar(americano)
    println("Кофе: ${americanoWithSugar.description()}, Цена: ${americanoWithSugar.cost()}")
    //endregion

    //region Command
    controlPanel.buttonStart(americano)
    controlPanel.buttonOff()
    //endregion
}

enum class CoffeeType{
    AMERICANO,
    CAPPUCCINO
}

enum class CoffeeSize{
    SMALL,
    MEDIUM,
    LARGE
}

//region Decorator
abstract class Coffee(val size: CoffeeSize) {
    abstract fun cost(): Int
    abstract fun description(): String
}

class Americano(size: CoffeeSize) : Coffee(size) {
    override fun cost(): Int {
        return 70
    }

    override fun description(): String {
        return "Американо"
    }
}

class Cappuccino(size: CoffeeSize) : Coffee(size) {
    override fun cost(): Int {
        return 100
    }

    override fun description(): String {
        return "Капучино"
    }
}

class Sugar(private val coffee: Coffee) : Coffee(coffee.size) {
    override fun cost(): Int {
        return coffee.cost() + 10
    }

    override fun description(): String {
        return coffee.description() + ", с сахаром"
    }
}
//endregion

//region Command
interface Command {
    fun on()
    fun off()
    fun start(coffee: Coffee)
    fun stop()
}

//region Singleton
object CoffeeMachine {
    fun on() {
        println("Кофемашина включена")
    }

    fun off() {
        println("Кофемашина выключена.")
    }

    fun start(coffee: Coffee) {
        println("Начато приготовление ${coffee.description()}.")
    }

    fun stop() {
        println("Отменено приготовление кофе.")
    }
}
//endregion

class CoffeeMachineCommand(private val coffeeMachine: CoffeeMachine) : Command {
    override fun on() {
        coffeeMachine.on()
    }

    override fun off() {
        coffeeMachine.off()
    }

    override fun start(coffee: Coffee) {
        coffeeMachine.start(coffee)
    }

    override fun stop() {
        coffeeMachine.stop()
    }
}

class ControlPanel {
    private lateinit var command: Command

    fun setCommand(command: Command) {
        this.command = command
    }

    fun buttonOn() {
        command.on()
    }

    fun buttonOff() {
        command.off()
    }

    fun buttonStart(coffee: Coffee) {
        command.start(coffee)
    }

    fun buttonStop() {
        command.stop()
    }
}
//endregion

//region Builder
class CoffeeBuilder {
    private var type = CoffeeType.AMERICANO
    private var size = CoffeeSize.SMALL

    fun setType(type: CoffeeType): CoffeeBuilder {
        this.type = type
        return this
    }

    fun setSize(size: CoffeeSize): CoffeeBuilder {
        this.size = size
        return this
    }

    fun build(): Coffee {
        return when (type) {
            CoffeeType.AMERICANO -> Americano(size)
            CoffeeType.CAPPUCCINO -> Cappuccino(size)
        }
    }
}
//endregion
