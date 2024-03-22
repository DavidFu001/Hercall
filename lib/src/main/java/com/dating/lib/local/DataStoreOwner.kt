package com.dating.lib.local

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface DataStoreOwner {
    val store get() = DataStoreManager
}

fun DataStoreOwner.dataInt(default: Int = 0) = DataStoreProperty({ key, value ->
    store.getInt(key, value)
}, { key, value ->
    store.put(key, value)
}, default)

fun DataStoreOwner.dataLong(default: Long = 0L) = DataStoreProperty({ key, value ->
    store.getLong(key, value)
}, { key, value ->
    store.put(key, value)
}, default)

fun DataStoreOwner.dataBool(default: Boolean = false) = DataStoreProperty({ key, value ->
    store.getBoolean(key, value)
}, { key, value ->
    store.put(key, value)
}, default)

fun DataStoreOwner.dataFloat(default: Float = 0f) = DataStoreProperty({ key, value ->
    store.getFloat(key, value)
}, { key, value ->
    store.put(key, value)
}, default)

fun DataStoreOwner.dataDouble(default: Double = 0.0) = DataStoreProperty({ key, value ->
    store.getDouble(key, value)
}, { key, value ->
    store.put(key, value)
}, default)

fun DataStoreOwner.dataString() = DataStoreNullableProperty({ key, value: String? ->
    store.getString(key, value ?: "")
}, { key, value ->
    store.put(key, value)
})

fun DataStoreOwner.dataString(default: String) =
    DataStoreNullablePropertyWithDefault({ key, value: String? ->
        store.getString(key, value ?: default)
    }, { key, value ->
        store.put(key, value)
    }, default)

class DataStoreProperty<V>(
    private val decode: DataStoreManager.(String, V) -> V,
    private val encode: DataStoreManager.(String, V) -> Unit,
    private val defaultValue: V
) : ReadWriteProperty<DataStoreOwner, V> {

    private var v: V? = null

    override fun getValue(thisRef: DataStoreOwner, property: KProperty<*>): V =
        (v ?: thisRef.store.decode(property.name, defaultValue)).apply { v = this }

    override fun setValue(thisRef: DataStoreOwner, property: KProperty<*>, value: V) {
        v = value
        thisRef.store.encode(property.name, value)
    }
}

class DataStoreNullableProperty<V>(
    private val decode: DataStoreManager.(String, V?) -> V?,
    private val encode: DataStoreManager.(String, V?) -> Unit
) : ReadWriteProperty<DataStoreOwner, V?> {

    private var v: V? = null

    override fun getValue(thisRef: DataStoreOwner, property: KProperty<*>): V? =
        v ?: thisRef.store.decode(property.name, null).apply { v = this }

    override fun setValue(thisRef: DataStoreOwner, property: KProperty<*>, value: V?) {
        v = value
        thisRef.store.encode(property.name, value)
    }
}

class DataStoreNullablePropertyWithDefault<V>(
    private val decode: DataStoreManager.(String, V?) -> V?,
    private val encode: DataStoreManager.(String, V?) -> Unit,
    private val defaultValue: V
) : ReadWriteProperty<DataStoreOwner, V> {

    private var v: V? = null

    override fun getValue(thisRef: DataStoreOwner, property: KProperty<*>): V =
        v ?: (thisRef.store.decode(property.name, null) ?: defaultValue).apply {
            v = this
        }

    override fun setValue(thisRef: DataStoreOwner, property: KProperty<*>, value: V) {
        v = value
        thisRef.store.encode(property.name, value)
    }
}