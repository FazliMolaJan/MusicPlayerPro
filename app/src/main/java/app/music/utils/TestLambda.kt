package app.music.utils

import app.music.model.BaseMusik
import kotlin.reflect.KProperty

object TestLambda {

    fun doSomethingWithNumber(number: Int, receiver: (String?, Boolean) -> Unit) {
        var result: String? = null
        receiver(result, true)
    }

    fun doSomething(number: Int, receiver: (String?) -> Unit) {
        var result: String? = null
        receiver(result)
    }

    private fun processNumber(number: Int, receiver: (String?, Boolean) -> Unit) {
        var result: String? = null
        receiver(result, true)

        doSomethingWithNumber(100, fun(data: String?, status: Boolean) {

        })

        //1
        doSomethingWithNumber(1000, { result, status ->
            // do something with result
        })

//2
        doSomethingWithNumber(1000, { result: String?, status: Boolean ->
            // do something with result
        })

//3
        doSomethingWithNumber(1000) { result, status ->
            // do something with result
        }

        doSomething(1000) {
            println("The result is $it")
        }

        showUser(user)

        var ints = listOf<Int>()

        //The second way
        ints.filter {
            val shouldFilter = it > 0
            return@filter shouldFilter
        }
    }

    open class User(private val name: String, private val surname: String, private val phone: String) {
        operator fun component1(): Any {
            return name
        }

        operator fun component2(): Any {
            return surname
        }

        operator fun component3(): Any {
            return phone
        }
    }

    class ExtendedUser(val address: String, name: String, surname: String, phone: String)
        : User(name, surname, phone)

    val showUser: (User) -> Unit = { (name, surname, phone): User ->
        println("$name $surname have phone number: $phone")
    }

    val user = User("Marcin", "Moskala", "+48 123 456 789")
    // Marcin Moskala have phone number: +48 123 456 789

    class ChangedSetterClass() {
        public var testField1: String? = null
            private set

        fun test() {
            val bar = Bar2()
            bar.count = 3
        }
    }

    open class Foo {
        open val count: Int
            get() {
                TODO()
            }
    }

    class Bar1(override val count: Int) : Foo() {

    }

    class Bar2 : Foo() {
        override var count: Int = 0

        val items = listOf(1, 2, 3, 4)

        fun testList() {
            items.first() == 1
            items.last() == 4
            items.filter {
                it % 2 == 0
            }
            var item = items.firstOrNull()
        }
    }

    class Person {
        constructor(parent: Person) {

        }
    }

    interface MyInterface {
        val number: String
        //get() = "any string"

        fun bar()
        fun foo() {
            // optional body
        }
    }

    class Test : MyInterface {
        override var number: String = ""
        //override var number="abc"

        override fun bar() {
            ProtocolState.WAITING.signal()
            var musik: BaseMusik = BaseMusik(null, null, null, null, null, null, null, null, null, null)
            musik.javaClass
            musik::class.java
        }
    }

    enum class ProtocolState {
        WAITING {
            override fun signal() = TALKING
        },

        TALKING {
            override fun signal() = WAITING
        };

        abstract fun signal(): ProtocolState
    }

    interface Base {
        fun print()
    }

    class BaseImpl(val x: Int) : Base {
        override fun print() {
            print(x)
        }
    }

    class Derived(b: Base) : Base by b

    fun main(args: Array<String>) {
        val b = BaseImpl(10)
        Derived(b).print() // prints 10
        val e = Example()
        e.p = "new string"
    }

    class Example {
        var p: String by DelegateProperty()
    }

    class DelegateProperty {
        operator fun getValue(example: TestLambda.Example, property: KProperty<*>): String {
            return property.name
        }

        operator fun setValue(example: TestLambda.Example, property: KProperty<*>, s: String) {

        }

    }
}