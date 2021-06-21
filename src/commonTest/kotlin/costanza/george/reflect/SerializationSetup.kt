package constanza.george.reflect

import costanza.george.geometry.Rect
import costanza.george.reflect.*
import costanza.george.reflect.typedproperties.*
import costanza.george.utility._List
import costanza.george.utility._list
import costanza.george.utility.list


var entityTypes = list(
    ObjectType("note") { Note() },
    ObjectType("inside") { Inside() },
    ObjectType("person") { Person() },
    ObjectType("alien") { Alien() },
    ObjectType("evolved") { EvolvedAlien() },
)

class Note: IObject, ReflectInfo("note") {
    override fun reflectInfo(): ReflectInfo = this

    var name = ""
    var details = ""
    var location = Rect(0,0,0,0)
    var inside: Inside? = null
    var another: Inside? = null
    var people: _List<Person> = _list()

    init {
        StringProperty(this, "name", true, "", { name }, { name = it })
        StringProperty(this, "details", false, "", { details }, { details = it })
        RectProperty(this, "location", false, Rect(0, 0, 0, 0), { location }, { location = it })
        ObjectProperty(this, "inside", { inside }, { inside = it as Inside? })
        ObjectProperty(this, "another", { another }, { another = it as Inside? })
        ObjectListProperty(this, null, people)
    }
}

class Inside: IObject, ReflectInfo("inside") {
    override fun reflectInfo(): ReflectInfo = this

    var age = 0
    val prop_age = IntProperty(this, "age", true, 0, { age }, { age = it })
    var height = 0.0
    var prop_height = DoubleProperty(this, "height", false, 0.0, { height }, { height = it })
}

open class Person(): IObject, ReflectInfo("person") {
    override fun reflectInfo(): ReflectInfo = this

    constructor(_name: String, _address: String) : this() {
        name = _name
        address = _address
    }

    var name = ""
    var prop_name = StringProperty(this, "name", true, "", { name }, { name = it })
    var address = ""
    var prop_address = StringProperty(this, "address", false, "", { address }, { address = it })
}

open class Alien(): Person() {
    override fun reflectInfo(): ReflectInfo = this

    constructor(_name: String, _address: String, _age: Int): this() {
        name = _name
        address = _address
        age = _age
    }

    init {
        objectType = "alien"
    }

    var age = 0
    var prop_age = IntProperty(this, "age", true, 0, { age }, { age = it })
}

class EvolvedAlien(): Alien() {
    override fun reflectInfo(): ReflectInfo = this

    constructor(_name: String, _address: String, _age: Int, _height: Double): this() {
        name = _name
        address = _address
        age = _age
        height = _height
    }

    init {
        objectType = "evolvedalien"
    }

    var height = 0.0
    var prop_height = DoubleProperty(this, "height", true, 0.0, { height }, { height = it })
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