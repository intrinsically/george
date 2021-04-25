package constanza.reflect

import costanza.geometry.Rect
import costanza.reflect.*
import costanza.reflect.typedproperties.*
import costanza.utility._List
import costanza.utility._list
import costanza.utility.list


var entityTypes = list(
    EntityType("note") { Note() },
    EntityType("inside") { Inside() },
    EntityType("person") { Person() },
    EntityType("alien") { Alien() },
    EntityType("evolved") { EvolvedAlien() },
)

class Note: IReflect {
    var name = ""
    var details = ""
    var location = Rect(0,0,0,0)
    var inside: Inside? = null
    var another: Inside? = null
    var people: _List<Person> = _list()

    override fun reflectInfo() = reflect("note") {
        string("name", true, "", { name }, { name = it })
        string("details", false, "", { details }, { details = it })
        rect("location", false, Rect(0, 0, 0, 0), { location }, { location = it })
        entity("inside", { inside }, { inside = it as Inside? })
        entity("another", { another }, { another = it as Inside? })
        entityList("person", people)
    }
}

class Inside: IReflect {
    var age = 0
    var height = 0.0

    override fun reflectInfo() = reflect("inside") {
        int("age", true, 0, { age }, { age = it })
        double("height", false, 0.0, { height }, { height = it })
    }
}

open class Person(): IReflect {
    constructor(_name: String, _address: String) : this() {
        name = _name
        address = _address
    }

    var name = ""
    var address = ""

    override fun reflectInfo() = reflect("person") {
        string("name", true, "", { name }, { name = it })
        string("address", false, "", { address }, { address = it })
    }
}

open class Alien(): Person() {
    constructor(_name: String, _address: String, _age: Int): this() {
        name = _name
        address = _address
        age = _age
    }

    var age = 0

    override fun reflectInfo() = reflect("alien", super.reflectInfo()) {
        int("age", true, 0, { age }, { age = it })
    }
}

class EvolvedAlien(): Alien() {
    constructor(_name: String, _address: String, _age: Int, _height: Double): this() {
        name = _name
        address = _address
        age = _age
        height = _height
    }

    var height = 0.0

    override fun reflectInfo() = reflect("evolved", super.reflectInfo()) {
        double("height", true, 0.0, { height }, { height = it })
    }
}


fun makeNote(): Note {
    val note = Note()
    note.details = "This is a note"
    note.name = "Andrew's Note"
    note.inside = Inside()
    note.location = Rect(10,35,20,20)
    note.inside!!.age = 10
    note.another = Inside()
    note.another!!.age = 20
    note.another!!.height = 20.7

    note.people.add(Person("andrew", "malibu"))
    note.people.add(Alien("ET", "earth", 7))
    note.people.add(EvolvedAlien("Elon", "space", 7, 6.4))
    return note
}